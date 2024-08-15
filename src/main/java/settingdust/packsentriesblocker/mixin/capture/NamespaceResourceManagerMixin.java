package settingdust.packsentriesblocker.mixin.capture;

import net.minecraft.resource.NamespaceResourceManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = NamespaceResourceManager.class)
public class NamespaceResourceManagerMixin {
    /**
     * Almost needn't since it won't take too much time and ModernFix is caching
     */
    //    @Inject(
    //        method = "getResource",
    //        at = @At(value = "INVOKE", target = "Ljava/util/Optional;empty()Ljava/util/Optional;")
    //    )
    //    private void packsentriesblocker$getResource$captureFiltered(
    //        final Identifier identifier,
    //        final CallbackInfoReturnable<Optional<Resource>> cir
    //    ) {
    //        PackEntriesBlocker.captureResource(identifier);
    //    }
    //    @ModifyExpressionValue(
    //        method = "getAllResources",
    //        at = @At(
    //            value = "INVOKE",
    //            target = "Lnet/minecraft/resource/ResourcePack;open(Lnet/minecraft/resource/ResourceType;Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/InputSupplier;"
    //        )
    //    )
    //    private @Nullable InputSupplier<InputStream> packsentriesblocker$getAllResources$captureFailed(
    //        final @Nullable InputSupplier<InputStream> original,
    //        Identifier id,
    //        @Local ResourcePack pack
    //    ) {
    //        if (original == null) {
    //            PackEntriesBlocker.captureResource(id, pack);
    //        }
    //        return original;
    //    }
    //
    //    @WrapOperation(
    //        method = "findResources",
    //        at = @At(
    //            value = "INVOKE",
    //            target = "Lnet/minecraft/resource/ResourcePack;findResources(Lnet/minecraft/resource/ResourceType;Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/resource/ResourcePack$ResultConsumer;)V"
    //        )
    //    )
    //    private void packsentriesblocker$findResources$captureFailed(
    //        final ResourcePack pack,
    //        final ResourceType resourceType,
    //        final String namespace,
    //        final String startingPath,
    //        final ResourcePack.ResultConsumer resultConsumer,
    //        final Operation<Void> original,
    //        @Local(ordinal = 0) Map map
    //    ) {
    //        var size = map.size();
    //        original.call(pack, resourceType, namespace, startingPath, resultConsumer);
    //        if (size == map.size()) {
    //            PackEntriesBlocker.captureFindResource(pack, namespace, startingPath);
    //        }
    //    }
    //
    //    @WrapOperation(
    //        method = "findAndAdd",
    //        at = @At(
    //            value = "INVOKE",
    //            target = "Lnet/minecraft/resource/ResourcePack;findResources(Lnet/minecraft/resource/ResourceType;Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/resource/ResourcePack$ResultConsumer;)V"
    //        )
    //    )
    //    private void packsentriesblocker$findAndAdd$captureFailed(
    //        final ResourcePack pack,
    //        final ResourceType resourceType,
    //        final String namespace,
    //        final String startingPath,
    //        final ResourcePack.ResultConsumer resultConsumer,
    //        final Operation<Void> original,
    //        @Local(argsOnly = true) Map idToEntryList
    //    ) {
    //        var size = idToEntryList.size();
    //        original.call(pack, resourceType, namespace, startingPath, resultConsumer);
    //        if (size == idToEntryList.size()) {
    //            PackEntriesBlocker.captureFindResource(pack, namespace, startingPath);
    //        }
    //    }
}
