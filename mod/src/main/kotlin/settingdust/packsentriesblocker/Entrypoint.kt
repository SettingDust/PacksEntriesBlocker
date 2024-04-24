package settingdust.packsentriesblocker

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.resource.ResourcePack
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object PackEntriesBlocker {
    const val ID = "pack-entries-blocker"

    @JvmField val LOGGER = LogManager.getLogger()

    @JvmStatic
    fun blocked(id: Identifier, pack: ResourcePack) =
        PackEntriesBlockerConfig.entries.entries.any {
            it.key.matches(id.toString()) && it.value.contains(pack.name)
        }

    @JvmStatic
    fun blocked(namespace: String, prefix: String, pack: ResourcePack) =
        PackEntriesBlockerConfig.entries.entries.any {
            it.key.matches("$namespace:$prefix") && it.value.contains(pack.name)
        }

    @JvmStatic
    fun reload() {
        PackEntriesBlockerConfig.load()
    }
}

fun init() {
    ServerLifecycleEvents.START_DATA_PACK_RELOAD.register { _, _ -> PackEntriesBlocker.reload() }
}
