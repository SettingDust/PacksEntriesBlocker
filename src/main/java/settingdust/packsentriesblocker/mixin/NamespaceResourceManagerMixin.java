package settingdust.packsentriesblocker.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import settingdust.packsentriesblocker.PackEntriesBlocker;

import java.util.Optional;

@Mixin(NamespaceResourceManager.class)
public class NamespaceResourceManagerMixin {
    @Inject(
        method = "getResource",
        at =
        @At(
            value = "FIELD",
            shift = At.Shift.BY,
            by = 2,
            target = "Lnet/minecraft/resource/NamespaceResourceManager$FilterablePack;underlying:Lnet/minecraft/resource/ResourcePack;"
        ),
        cancellable = true
    )
    private void packsentriesblocker$getResource$blockEntry(
        final Identifier identifier,
        final CallbackInfoReturnable<Optional<Resource>> cir,
        @Local ResourcePack pack
    ) {
        if (PackEntriesBlocker.blocked(identifier, pack)) {
            PackEntriesBlocker.LOGGER.debug("Blocked `getResource` of {} from {}", identifier, pack.getName());
            cir.setReturnValue(Optional.empty());
        }
    }

    @ModifyExpressionValue(
        method = "getAllResources",
        at =
        @At(
            value = "FIELD",
            target = "Lnet/minecraft/resource/NamespaceResourceManager$FilterablePack;underlying:Lnet/minecraft/resource/ResourcePack;"
        )
    )
    private ResourcePack packsentriesblocker$getAllResources$blockEntry(
        final ResourcePack pack, Identifier identifier
    ) {
        if (PackEntriesBlocker.blocked(identifier, pack)) {
            PackEntriesBlocker.LOGGER.debug("Blocked `getAllResources` of {} from {}", identifier, pack.getName());
            return null;
        }
        return pack;
    }

    @WrapWithCondition(
        method = "findResources",
        at =
        @At(
            value = "INVOKE",
            target =
                "Lnet/minecraft/resource/ResourcePack;findResources(Lnet/minecraft/resource/ResourceType;Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/resource/ResourcePack$ResultConsumer;)V"
        )
    )
    private boolean packsentriesblocker$findResources$blockEntry(
        final ResourcePack pack,
        final ResourceType resourceType,
        final String namespace,
        final String startingPath,
        final ResourcePack.ResultConsumer resultConsumer
    ) {
        if (PackEntriesBlocker.blocked(namespace, startingPath, pack)) {
            PackEntriesBlocker.LOGGER.debug(
                "Blocked `findResources` of {}:{} from {}", namespace, startingPath, pack.getName());
            return false;
        }
        return true;
    }

    @WrapWithCondition(
        method = "findAndAdd",
        at =
        @At(
            value = "INVOKE",
            target =
                "Lnet/minecraft/resource/ResourcePack;findResources(Lnet/minecraft/resource/ResourceType;Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/resource/ResourcePack$ResultConsumer;)V"
        )
    )
    private boolean packsentriesblocker$findAndAdd$blockEntry(
        final ResourcePack pack,
        final ResourceType resourceType,
        final String namespace,
        final String startingPath,
        final ResourcePack.ResultConsumer resultConsumer
    ) {
        if (PackEntriesBlocker.blocked(namespace, startingPath, pack)) {
            PackEntriesBlocker.LOGGER.debug(
                "Blocked `findAndAdd` of {}:{} from {}", namespace, startingPath, pack.getName());
            return false;
        }
        return true;
    }
}
