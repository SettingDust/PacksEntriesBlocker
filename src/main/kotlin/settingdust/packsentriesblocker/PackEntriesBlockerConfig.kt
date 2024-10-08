package settingdust.packsentriesblocker

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimaps
import com.google.common.collect.SetMultimap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.div
import kotlin.io.path.inputStream
import kotlin.io.path.writeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.mapSerialDescriptor
import kotlinx.serialization.descriptors.setSerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.fabricmc.loader.api.FabricLoader

@Serializable
data class CommonConfig(
    val blocked:
        @Serializable(with = SetMultimapSerializer::class)
        SetMultimap<String, @Serializable(with = RegexSerializer::class) Regex> =
        Multimaps.synchronizedSetMultimap(HashMultimap.create()),
//    val captureFailed: Boolean = false
)

@Suppress("UnstableApiUsage")
@OptIn(ExperimentalSerializationApi::class)
class SetMultimapSerializer<K, V>(
    private val keySerializer: KSerializer<K>,
    private val valueSerializer: KSerializer<V>
) : KSerializer<SetMultimap<K, V>> {

    override val descriptor =
        SerialDescriptor(
            "Multimap",
            mapSerialDescriptor(
                keySerializer.descriptor, setSerialDescriptor(valueSerializer.descriptor)))

    override fun serialize(encoder: Encoder, value: SetMultimap<K, V>) {
        MapSerializer(keySerializer, SetSerializer(valueSerializer))
            .serialize(encoder, Multimaps.asMap(value))
    }

    override fun deserialize(decoder: Decoder): SetMultimap<K, V> {
        val map = Multimaps.newSetMultimap<K, V>(Object2ObjectOpenHashMap()) { ObjectOpenHashSet() }
        for (entry in
            MapSerializer(keySerializer, SetSerializer(valueSerializer)).deserialize(decoder)) {
            map.putAll(entry.key, entry.value)
        }
        return map
    }
}

object PackEntriesBlockerConfig {

    private val configDir = FabricLoader.getInstance().configDir
    private val commonConfigPath = configDir / "${PackEntriesBlocker.ID}.json"
    var commonConfig = CommonConfig()

    val saveChannel = Channel<Unit>(1)
    private @OptIn(FlowPreview::class) val saveFlow = saveChannel.consumeAsFlow().debounce(500)

    private val json = Json {
        encodeDefaults = true
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true

        serializersModule = SerializersModule { contextual(RegexSerializer) }
    }

    init {
        load()
        GlobalScope.launch(Dispatchers.IO) { saveFlow.collect { save() } }
    }

    fun load() {
        try {
            configDir.createDirectories()
        } catch (_: Exception) {}

        try {
            commonConfigPath.createFile()
        } catch (_: Exception) {}

        try {
            commonConfig = json.decodeFromStream(commonConfigPath.inputStream())
        } catch (_: Exception) {}

        saveChannel.trySend(Unit)
    }

    fun save() {
        commonConfigPath.writeText(json.encodeToString(commonConfig))
    }
}

object RegexSerializer : KSerializer<Regex> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("RegexSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Regex) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder) = Regex(decoder.decodeString())
}
