package inspectorboat.yeetlighting.mixin.client.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.textures.GpuTexture;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.IntFunction;

@Mixin(DefaultShaderInterface.class)
public class DefaultShaderInterfaceMixin {
    @Redirect(
            method = "<init>(Lnet/caffeinemc/mods/sodium/client/render/chunk/shader/ShaderBindingContext;Lnet/caffeinemc/mods/sodium/client/render/chunk/shader/ChunkShaderOptions;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/shader/ShaderBindingContext;bindUniform(Ljava/lang/String;Ljava/util/function/IntFunction;)Lnet/caffeinemc/mods/sodium/client/gl/shader/uniform/GlUniform;",
                    ordinal = 5
            ),

            remap = false)
    private <U extends net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniform<?>> U cancel(ShaderBindingContext instance, String s, IntFunction<U> uIntFunction) {
        return null;
    }

    @Redirect(
            method = "setupState",
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/shader/DefaultShaderInterface;bindTexture(Lnet/caffeinemc/mods/sodium/client/render/chunk/shader/ChunkShaderTextureSlot;Lcom/mojang/blaze3d/textures/GpuTexture;)V", ordinal = 1),
            remap = false
    )
    private void cancel(DefaultShaderInterface instance, ChunkShaderTextureSlot slot, GpuTexture textureId) {}
}
