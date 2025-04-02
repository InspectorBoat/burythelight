package inspectorboat.yeetlighting.mixin.client.light;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightingProvider.class)
public class LightingProviderMixin {
    @Inject(
            at = @At(value = "HEAD"),
            method = "getLight",
            cancellable = true
    )
    public void getLight(BlockPos pos, int ambientDarkness, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(15);
    }
}
