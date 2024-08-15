package settingdust.packsentriesblocker

import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.div
import kotlin.io.path.inputStream
import kotlin.io.path.writeText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
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

@Serializable
data class CommonConfig(
    val blocked:
        MutableMap<@Serializable(with = RegexSerializer::class) Regex, MutableSet<String>> =
        mutableMapOf(),
    val captureFailed: Boolean = false
)

object PackEntriesBlockerConfig {

    private val configDir = FabricLoader.getInstance().configDir
    private val commonConfigPath = configDir / "${PackEntriesBlocker.ID}.json"
    var commonConfig = CommonConfig()

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
            commonConfig = json.decodeFromStream(commonConfigPath.inputStream())
        } catch (_: Exception) {}

        save()
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
