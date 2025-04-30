package inspectorboat.yeetlighting.mixin.client.sodium;

import net.caffeinemc.mods.sodium.api.util.NormI8;
import net.caffeinemc.mods.sodium.client.model.light.data.QuadLightData;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadView;
import net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.caffeinemc.mods.sodium.client.render.chunk.translucent_sorting.TranslucentGeometryCollector;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.builder.ChunkMeshBufferBuilder;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DefaultFluidRenderer.class)
public class DefaultFluidRendererMixin {
    @Shadow
    @Final private QuadLightData quadLightData = new QuadLightData();

    @Shadow @Final
    private ChunkVertexEncoder.Vertex[] vertices = ChunkVertexEncoder.Vertex.uninitializedQuad();

    @Overwrite
    private void writeQuad(ChunkModelBuilder builder, TranslucentGeometryCollector collector, Material material, BlockPos offset, ModelQuadView quad, ModelQuadFacing facing, boolean flip) {
        ChunkVertexEncoder.Vertex[] vertices = this.vertices;

        for(int i = 0; i < 4; ++i) {
            ChunkVertexEncoder.Vertex out = vertices[flip ? 3 - i + 1 & 3 : i];
            out.x = (float)offset.getX() + quad.getX(i);
            out.y = (float)offset.getY() + quad.getY(i);
            out.z = (float)offset.getZ() + quad.getZ(i);
            out.u = quad.getTexU(i);
            out.v = quad.getTexV(i);
            out.light = 255;
        }

        Sprite sprite = quad.getSprite();
        if (sprite != null) {
            builder.addSprite(sprite);
        }

        if (material.isTranslucent() && collector != null) {
            int normal;
            if (facing.isAligned()) {
                normal = facing.getPackedAlignedNormal();
            } else {
                normal = quad.getFaceNormal();
            }

            if (flip) {
                normal = NormI8.flipPacked(normal);
            }

            collector.appendQuad(normal, vertices, facing);
        }

        ChunkMeshBufferBuilder vertexBuffer = builder.getVertexBuffer(facing);
        vertexBuffer.push(vertices, material);
    }

}
