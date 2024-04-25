package settingdust.packsentriesblocker.mixin;

import java.util.concurrent.CompletableFuture;
import net.minecraft.server.DataPackContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settingdust.packsentriesblocker.PackEntriesBlocker;

@Mixin(DataPackContents.class)
public class DataPackContentsMixin {
    @Inject(method = "reload", at = @At("HEAD"))
    private static void registryblocker$reload(final CallbackInfoReturnable<CompletableFuture<DataPackContents>> cir) {
        PackEntriesBlocker.reload();
    }
}
