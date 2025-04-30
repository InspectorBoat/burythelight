package inspectorboat.burythelight.mixin.client.sodium;

import net.caffeinemc.mods.sodium.client.gl.attribute.GlVertexFormat;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderBindingPoints;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.impl.CompactChunkVertex;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.impl.DefaultChunkMeshAttributes;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CompactChunkVertex.class, remap = false)
public class CompactChunkVertexMixin {
    @Mutable
    @Shadow @Final
    public static GlVertexFormat VERTEX_FORMAT;

    @Unique
    private static final int CHANGED_STRIDE = 16;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyFormat(CallbackInfo ci) {
        VERTEX_FORMAT =
                GlVertexFormat.builder(CHANGED_STRIDE)
                .addElement(DefaultChunkMeshAttributes.POSITION, ChunkShaderBindingPoints.ATTRIBUTE_POSITION, 0)
//                .addElement(DefaultChunkMeshAttributes.COLOR, ChunkShaderBindingPoints.ATTRIBUTE_COLOR, 8)
                .addElement(DefaultChunkMeshAttributes.TEXTURE, ChunkShaderBindingPoints.ATTRIBUTE_TEXTURE, 8)
                .addElement(DefaultChunkMeshAttributes.LIGHT_MATERIAL_INDEX, ChunkShaderBindingPoints.ATTRIBUTE_LIGHT_MATERIAL_INDEX, 12)
                .build();
    }
    @Overwrite(remap = false)
    public ChunkVertexEncoder getEncoder() {
        return (ptr, materialBits, vertices, section) -> {
            // Calculate the center point of the texture region which is mapped to the quad
            float texCentroidU = 0.0f;
            float texCentroidV = 0.0f;

            for (var vertex : vertices) {
                texCentroidU += vertex.u;
                texCentroidV += vertex.v;
            }

            texCentroidU *= (1.0f / 4.0f);
            texCentroidV *= (1.0f / 4.0f);

            for (int i = 0; i < 4; i++) {
                var vertex = vertices[i];

                int x = quantizePosition(vertex.x);
                int y = quantizePosition(vertex.y);
                int z = quantizePosition(vertex.z);

                int u = encodeTexture(texCentroidU, vertex.u);
                int v = encodeTexture(texCentroidV, vertex.v);

//                int light = encodeLight(vertex.light);

                MemoryUtil.memPutInt(ptr + 0L, packPositionHi(x, y, z));
                MemoryUtil.memPutInt(ptr + 4L, packPositionLo(x, y, z));
//                MemoryUtil.memPutInt(ptr + 8L, ColorARGB.mulRGB(vertex.color, vertex.ao));
                MemoryUtil.memPutInt(ptr + 8L, packTexture(u, v));
                MemoryUtil.memPutInt(ptr + 12L, packLightAndData(vertex.light, materialBits, section));

                ptr += CHANGED_STRIDE;
            }

            return ptr;
        };
    }

    @Shadow(remap = false)

    private static int quantizePosition(float position) {
        return 0;
    }

    @Shadow(remap = false)

    private static int encodeTexture(float center, float x) {
        return 0;
    }

    @Shadow(remap = false)
    private static int packPositionHi(int x, int y, int z) {
        return 0;
    }

    @Shadow(remap = false)

    private static int packPositionLo(int x, int y, int z) {
        return 0;
    }

    @Shadow(remap = false)

    private static int packTexture(int u, int v) {
        return 0;
    }

    @Shadow(remap = false)

    private static int packLightAndData(int light, int material, int section) {
        return 0;
    }
}
