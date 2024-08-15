package settingdust.packsentriesblocker

import net.minecraft.resource.ResourcePack
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object PackEntriesBlocker {
    const val ID = "pack-entries-blocker"

    @JvmField val LOGGER = LogManager.getLogger()

    @JvmStatic
    fun blocked(id: Identifier, pack: ResourcePack) =
        synchronized(PackEntriesBlockerConfig.commonConfig.blocked) {
            PackEntriesBlockerConfig.commonConfig.blocked[pack.name].any {
                it.matches(id.toString())
            }
        }

    @JvmStatic
    fun blocked(namespace: String, prefix: String, pack: ResourcePack) =
        synchronized(PackEntriesBlockerConfig.commonConfig.blocked) {
            PackEntriesBlockerConfig.commonConfig.blocked[pack.name].any {
                it.matches("$namespace:$prefix")
            }
        }

    @JvmStatic
    fun Identifier.captureResource(pack: ResourcePack) {
        if (PackEntriesBlockerConfig.commonConfig.captureFailed) {
            PackEntriesBlockerConfig.commonConfig.blocked
                .asMap()
                .getOrPut(pack.name) { mutableSetOf() }
                .add(Regex(toString()))
            PackEntriesBlockerConfig.save()
        }
    }

    @JvmStatic
    fun ResourcePack.captureFindResource(namespace: String, prefix: String) {
        if (PackEntriesBlockerConfig.commonConfig.captureFailed) {
            PackEntriesBlockerConfig.commonConfig.blocked
                .asMap()
                .getOrPut(name) { mutableSetOf() }
                .add(Regex("$namespace:$prefix"))
            PackEntriesBlockerConfig.save()
        }
    }

    @JvmStatic
    fun reload() {
        PackEntriesBlockerConfig.load()
    }
}

fun init() {
    PackEntriesBlocker.reload()
}
