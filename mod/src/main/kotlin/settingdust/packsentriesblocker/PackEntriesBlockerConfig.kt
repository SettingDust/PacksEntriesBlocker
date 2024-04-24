package settingdust.packsentriesblocker

import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.div
import kotlin.io.path.inputStream
import kotlin.io.path.writeText
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.fabricmc.loader.api.FabricLoader

object PackEntriesBlockerConfig {
    var entries = emptyMap<@Contextual Regex, Set<String>>()
        private set

    private val configDir = FabricLoader.getInstance().configDir
    private val commonConfigPath = configDir / "${PackEntriesBlocker.ID}.json"

    private val json = Json {
        encodeDefaults = true
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true

        serializersModule = SerializersModule { contextual(RegexSerializer) }
    }

    init {
        load()
    }

    fun load() {
        try {
            configDir.createDirectories()
        } catch (_: Exception) {}

        try {
            commonConfigPath.createFile()
        } catch (_: Exception) {}

        try {
            entries = json.decodeFromStream(commonConfigPath.inputStream())
        } catch (_: Exception) {}

        commonConfigPath.writeText(json.encodeToString(entries))
    }
}

object RegexSerializer : KSerializer<Regex> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("RegexSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Regex) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder) = Regex(decoder.decodeString())
}
