package inspectorboat.yeetlighting.mixin.client.sodium;

import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.caffeinemc.mods.sodium.client.model.color.ColorProvider;
import net.caffeinemc.mods.sodium.client.model.color.ColorProviderRegistry;
import net.caffeinemc.mods.sodium.client.model.light.LightMode;
import net.caffeinemc.mods.sodium.client.model.light.data.LightDataAccess;
import net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadOrientation;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.DefaultTerrainRenderPasses;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.parameters.AlphaCutoffParameter;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.parameters.MaterialParameters;
import net.caffeinemc.mods.sodium.client.render.chunk.translucent_sorting.TranslucentGeometryCollector;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.builder.ChunkMeshBufferBuilder;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.caffeinemc.mods.sodium.client.render.frapi.mesh.MutableQuadViewImpl;
import net.caffeinemc.mods.sodium.client.render.texture.SpriteFinderCache;
import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.color.block.BlockColorProvider;
@Mixin(BlockRenderer.class)
public class BlockRendererMixin {
    @Overwrite(remap = false)
    private void tintQuad(MutableQuadViewImpl quad) {
    }

    @Shadow @Final private ChunkVertexEncoder.Vertex[] vertices = ChunkVertexEncoder.Vertex.uninitializedQuad();
    @Shadow @Final private Vector3f posOffset = new Vector3f();
    @Shadow private TranslucentGeometryCollector collector;
    @Shadow private ChunkBuildBuffers buffers;

    @Shadow
    private @Nullable TerrainRenderPass attemptPassDowngrade(Sprite sprite, TerrainRenderPass pass) {
        return null;
    }

    @Overwrite(remap = false)
    private void bufferQuad(MutableQuadViewImpl quad, float[] brightnesses, Material material) {
        ModelQuadOrientation orientation = ModelQuadOrientation.NORMAL;
        ChunkVertexEncoder.Vertex[] vertices = this.vertices;
        Vector3f offset = this.posOffset;

        for(int dstIndex = 0; dstIndex < 4; ++dstIndex) {
            int srcIndex = orientation.getVertexIndex(dstIndex);
            ChunkVertexEncoder.Vertex out = vertices[dstIndex];
            out.x = quad.x(srcIndex) + offset.x;
            out.y = quad.y(srcIndex) + offset.y;
            out.z = quad.z(srcIndex) + offset.z;
            out.u = quad.u(srcIndex);
            out.v = quad.v(srcIndex);
            out.light = (quad.cullFace() != null ||
                    ((quad.getFlags() & 4) != 0 || (quad.getFlags() & 2) != 0)) ? switch (quad.getLightFace()) {
                case DOWN -> 128;
                case UP -> 255;
                case NORTH, SOUTH -> 204;
                case WEST, EAST -> 153;
                case null -> 255;
            } : 255;
        }

        Sprite atlasSprite = quad.sprite(SpriteFinderCache.forBlockAtlas());
        int materialBits = material.bits();
        ModelQuadFacing normalFace = quad.normalFace();
        TerrainRenderPass pass = material.pass;
        TerrainRenderPass downgradedPass = this.attemptPassDowngrade(atlasSprite, pass);
        if (downgradedPass != null) {
            pass = downgradedPass;
        }

        if (pass.isTranslucent() && this.collector != null) {
            this.collector.appendQuad(quad.getFaceNormal(), vertices, normalFace);
        }

        if (downgradedPass != null && material == DefaultMaterials.TRANSLUCENT && pass == DefaultTerrainRenderPasses.CUTOUT) {
            materialBits = MaterialParameters.pack(AlphaCutoffParameter.ONE_TENTH, material.mipped);
        }

        ChunkModelBuilder builder = this.buffers.get(pass);
        ChunkMeshBufferBuilder vertexBuffer = builder.getVertexBuffer(normalFace);
        vertexBuffer.push(vertices, materialBits);
        if (atlasSprite != null) {
            builder.addSprite(atlasSprite);
        }

    }

}
