package settingdust.packsentriesblocker

import net.minecraft.resource.ResourcePack
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object PackEntriesBlocker {
    const val ID = "pack-entries-blocker"

    @JvmField val LOGGER = LogManager.getLogger()

    @JvmStatic
    fun blocked(id: Identifier, pack: ResourcePack) =
        PackEntriesBlockerConfig.entries.entries.any {
            it.key.matches(id.toString()) && it.value.contains(pack.id)
        }

    @JvmStatic
    fun blocked(namespace: String, prefix: String, pack: ResourcePack) =
        PackEntriesBlockerConfig.entries.entries.any {
            it.key.matches("$namespace:$prefix") && it.value.contains(pack.id)
        }

    @JvmStatic
    fun reload() {
        PackEntriesBlockerConfig.load()
    }
}

fun init() {}
