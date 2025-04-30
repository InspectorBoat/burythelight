package inspectorboat.burythelight.mixin.client.sodium;

import net.caffeinemc.mods.sodium.client.gl.shader.GlProgram;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GlProgram.class)
public class GlProgramMixin {
//    @Inject(
//            method = "bindUniform(Ljava/lang/String;Ljava/util/function/IntFunction;)Lnet/caffeinemc/mods/sodium/client/gl/shader/uniform/GlUniform;",
//            at = @At("HEAD"),
//            cancellable = true)
//    public void cancel(String name, IntFunction<?> factory, CallbackInfoReturnable<?> cir) {
//        if (name.equals("u_LightTex")) cir.setReturnValue(null);
//    }
}
