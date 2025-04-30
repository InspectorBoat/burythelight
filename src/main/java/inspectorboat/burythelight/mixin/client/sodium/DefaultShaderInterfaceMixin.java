package inspectorboat.burythelight.mixin.client.sodium;

import net.caffeinemc.mods.sodium.client.gl.device.GLRenderDevice;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformFloat2v;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.*;
import net.caffeinemc.mods.sodium.client.util.TextureUtil;
import net.caffeinemc.mods.sodium.mixin.core.render.texture.TextureAtlasAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.SpriteAtlasTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
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

//    @Redirect(
//            method = "setupState",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/shader/DefaultShaderInterface;bindTexture(Lnet/caffeinemc/mods/sodium/client/render/chunk/shader/ChunkShaderTextureSlot;L;)V"

    /// /                    ordinal = 1
//            ),
//            remap = false
//    )
//    private void cancel(DefaultShaderInterface instance, ChunkShaderTextureSlot slot, GpuTexture textureId) {}

    // mixin target won't work and i cba to figure out why
    @Overwrite(remap = false)
    public void setupState() {
        this.bindTexture(ChunkShaderTextureSlot.BLOCK, TextureUtil.getBlockTextureId());
//        this.bindTexture(ChunkShaderTextureSlot.LIGHT, TextureUtil.getLightTextureId());
        TextureAtlasAccessor textureAtlas = (TextureAtlasAccessor) MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        double subTexelPrecision = (double) (1 << GLRenderDevice.INSTANCE.getSubTexelPrecisionBits());
        double subTexelOffset = (double) 3.0517578E-5F;
        this.uniformTexCoordShrink.set((float) (subTexelOffset - (double) 1.0F / (double) textureAtlas.getField_43113() / subTexelPrecision), (float) (subTexelOffset - (double) 1.0F / (double) textureAtlas.getField_43114() / subTexelPrecision));
        this.fogShader.setup();
    }

    @Shadow
    private void bindTexture(ChunkShaderTextureSlot slot, int textureId) {}

    @Final
    @Shadow private GlUniformFloat2v uniformTexCoordShrink;

    @Final
    @Shadow private ChunkShaderFogComponent fogShader;


}
