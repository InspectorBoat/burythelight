package inspectorboat.yeetlighting.mixin.client.sodium;

import net.caffeinemc.mods.sodium.client.gl.shader.ShaderConstants;
import net.caffeinemc.mods.sodium.client.gl.shader.ShaderParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShaderParser.class)
public class ShaderParserMixin {
    @Inject(
            method = "parseShader(Ljava/lang/String;Lnet/caffeinemc/mods/sodium/client/gl/shader/ShaderConstants;)Ljava/lang/String;",
            at = @At("TAIL"),
            remap = false
    )
    private static void logParsedShader(String src, ShaderConstants constants, CallbackInfoReturnable<String> cir) {
//        System.out.println(cir.getReturnValue());
    }
}