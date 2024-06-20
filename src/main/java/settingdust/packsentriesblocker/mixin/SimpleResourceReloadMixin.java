package settingdust.packsentriesblocker.mixin;

import net.minecraft.resource.SimpleResourceReload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settingdust.packsentriesblocker.PackEntriesBlocker;

@Mixin(SimpleResourceReload.class)
public class SimpleResourceReloadMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    private void moreprofiling$startProfiling(final CallbackInfo ci) {
        PackEntriesBlocker.reload();
    }
}
