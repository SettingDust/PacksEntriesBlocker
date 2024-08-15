package settingdust.packsentriesblocker

import net.minecraft.resource.ResourcePack
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import settingdust.packsentriesblocker.PackEntriesBlockerConfig.saveChannel

object PackEntriesBlocker {
    const val ID = "pack-entries-blocker"

    @JvmField val LOGGER = LogManager.getLogger()

    @JvmStatic
    fun blocked(id: Identifier, pack: ResourcePack): Boolean {
        return synchronized(PackEntriesBlockerConfig.commonConfig.blocked) {
            PackEntriesBlockerConfig.commonConfig.blocked[pack.name].any {
                it.matches(id.toString())
            }
        }
    }


    @JvmStatic
    fun blocked(namespace: String, prefix: String, pack: ResourcePack): Boolean {
        return synchronized(PackEntriesBlockerConfig.commonConfig.blocked) {
            PackEntriesBlockerConfig.commonConfig.blocked[pack.name].any {
                it.matches("$namespace:$prefix")
            }
        }
    }


//    @JvmStatic
//    fun Identifier.captureResource(pack: ResourcePack) {
//        if (PackEntriesBlockerConfig.commonConfig.captureFailed) {
//            PackEntriesBlockerConfig.commonConfig.blocked.put(pack.name, Regex(toString()))
//            saveChannel.trySend(Unit)
//        }
//    }
//
//    @JvmStatic
//    fun ResourcePack.captureFindResource(namespace: String, prefix: String) {
//        if (PackEntriesBlockerConfig.commonConfig.captureFailed) {
//            PackEntriesBlockerConfig.commonConfig.blocked.put(name, Regex("$namespace:$prefix"))
//            saveChannel.trySend(Unit)
//        }
//    }

    @JvmStatic
    fun reload() {
        PackEntriesBlockerConfig.load()
    }
}

fun init() {
    PackEntriesBlocker.reload()
}
