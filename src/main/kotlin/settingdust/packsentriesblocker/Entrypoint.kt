package settingdust.packsentriesblocker

import net.minecraft.resource.ResourcePack
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object PackEntriesBlocker {
    const val ID = "pack-entries-blocker"

    @JvmField val LOGGER = LogManager.getLogger()

    @JvmStatic
    fun blocked(id: Identifier, pack: ResourcePack) =
        PackEntriesBlockerConfig.commonConfig.blocked.any {
            it.key.matches(id.toString()) && it.value.contains(pack.name)
        }

    @JvmStatic
    fun blocked(namespace: String, prefix: String, pack: ResourcePack) =
        PackEntriesBlockerConfig.commonConfig.blocked.any {
            it.key.matches("$namespace:$prefix") && it.value.contains(pack.name)
        }

    /** Almost needn't since it won't take too much time and ModernFix is caching */
    @JvmStatic
    fun Identifier.captureResource() {
        if (PackEntriesBlockerConfig.commonConfig.captureFailed) {
            PackEntriesBlockerConfig.commonConfig.blocked
                .getOrPut(Regex(".*")) { mutableSetOf() }
                .add(toString())
            PackEntriesBlockerConfig.save()
        }
    }

    @JvmStatic
    fun Identifier.captureResource(pack: ResourcePack) {
        if (PackEntriesBlockerConfig.commonConfig.captureFailed) {
            PackEntriesBlockerConfig.commonConfig.blocked
                .getOrPut(Regex(toString())) { mutableSetOf() }
                .add(pack.name)
            PackEntriesBlockerConfig.save()
        }
    }

    @JvmStatic
    fun ResourcePack.captureFindResource(namespace: String, prefix: String) {
        if (PackEntriesBlockerConfig.commonConfig.captureFailed) {
            PackEntriesBlockerConfig.commonConfig.blocked
                .getOrPut(Regex("$namespace:$prefix")) { mutableSetOf() }
                .add(name)
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
