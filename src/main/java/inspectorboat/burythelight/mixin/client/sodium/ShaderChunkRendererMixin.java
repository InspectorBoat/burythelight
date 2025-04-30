package inspectorboat.burythelight.mixin.client.sodium;

import net.caffeinemc.mods.sodium.client.gl.shader.*;
import net.caffeinemc.mods.sodium.client.render.chunk.ShaderChunkRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.DefaultShaderInterface;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ShaderChunkRenderer.class)
public class ShaderChunkRendererMixin {

    @Overwrite(remap = false)
    private GlProgram<ChunkShaderInterface> createShader(String path, ChunkShaderOptions options) {
        ShaderConstants constants = options.constants();
        GlShader vertShader = ShaderLoader.loadShader(ShaderType.VERTEX, Identifier.of("sodium", path + ".vsh"), constants);
        GlShader fragShader = ShaderLoader.loadShader(ShaderType.FRAGMENT, Identifier.of("sodium", path + ".fsh"), constants);

        GlProgram var6;
        try {
            var6 = GlProgram.builder(
                    Identifier.of("sodium", "chunk_shader"))
                    .attachShader(vertShader).attachShader(fragShader)
                    .bindAttribute("a_Position", 0)
//                    .bindAttribute("a_Color", 1)
                    .bindAttribute("a_TexCoord", 2)
                    .bindAttribute("a_LightAndData", 3)
                    .bindFragmentData("fragColor", 0)
                    .link((shader) -> new DefaultShaderInterface(shader, options));
        } finally {
            vertShader.delete();
            fragShader.delete();
        }

        return var6;
    }
}
