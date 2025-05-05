package inspectorboat.burythelight.mixin.client.flerovium.Entity;

import com.mojang.blaze3d.systems.RenderSystem;
import inspectorboat.burythelight.BuryTheLightClient;
import net.caffeinemc.mods.sodium.api.math.MatrixHelper;
import net.caffeinemc.mods.sodium.client.render.immediate.model.EntityRenderer;
import net.caffeinemc.mods.sodium.client.render.immediate.model.ModelCuboid;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.caffeinemc.mods.sodium.client.render.immediate.model.ModelCuboid.*;

@Mixin(value = EntityRenderer.class, remap = false)
public abstract class EntityRendererMixin {
    @Shadow @Final
    private static Vector2f[][] VERTEX_TEXTURES;

    @Unique
    private static void buildVertexPosition2(Vector3f vector, float x, float y, float z) {
        vector.set(x, y, z);
    }

    @Shadow
    private static void buildVertexTexCoord(Vector2f[] uvs, float u1, float v1, float u2, float v2) {
    }

    @Shadow @Final private static Vector3f[] CUBE_CORNERS;
    @Unique
    private static final int
            VERTEX_X0_Y0_Z0 = 0,
            VERTEX_X1_Y0_Z0 = 1,
            VERTEX_X1_Y1_Z0 = 2,
            VERTEX_X0_Y1_Z0 = 3,
            VERTEX_X0_Y0_Z1 = 4,
            VERTEX_X1_Y0_Z1 = 5,
            VERTEX_X1_Y1_Z1 = 6,
            VERTEX_X0_Y1_Z1 = 7;

    @Unique
    private static int flerovium$FACE = ~0;

    /**
     * @author MoePus
     * @reason BackFace Culling
     */
    @Overwrite
    private static void prepareVertices(MatrixStack.Entry matrices, ModelCuboid cuboid) {
        var pose = matrices.getPositionMatrix();

        float sizeX = cuboid.x2 - cuboid.x1;
        float sizeY = cuboid.y2 - cuboid.y1;
        float sizeZ = cuboid.z2 - cuboid.z1;

        float vxx = (pose.m00() * sizeX), vxy = (pose.m01() * sizeX), vxz = (pose.m02() * sizeX);
        float vyx = (pose.m10() * sizeY), vyy = (pose.m11() * sizeY), vyz = (pose.m12() * sizeY);
        float vzx = (pose.m20() * sizeZ), vzy = (pose.m21() * sizeZ), vzz = (pose.m22() * sizeZ);

        // Compute the transformed origin point of the cuboid
        float c000x = MatrixHelper.transformPositionX(pose, cuboid.x1, cuboid.y1, cuboid.z1);
        float c000y = MatrixHelper.transformPositionY(pose, cuboid.x1, cuboid.y1, cuboid.z1);
        float c000z = MatrixHelper.transformPositionZ(pose, cuboid.x1, cuboid.y1, cuboid.z1);
        buildVertexPosition2(CUBE_CORNERS[VERTEX_X0_Y0_Z0], c000x, c000y, c000z);

        // Add the pre-multiplied vectors to find the other 7 vertices
        // This avoids needing to multiply each vertex position against the pose matrix, which eliminates many
        // floating-point operations (going from 21 flops/vert to 3 flops/vert).
        // Originally suggested by MoePus on GitHub in this pull request:
        //  https://github.com/CaffeineMC/sodium/pull/2960
        float c100x = c000x + vxx;
        float c100y = c000y + vxy;
        float c100z = c000z + vxz;
        buildVertexPosition2(CUBE_CORNERS[VERTEX_X1_Y0_Z0], c100x, c100y, c100z);

        float c110x = c100x + vyx;
        float c110y = c100y + vyy;
        float c110z = c100z + vyz;
        buildVertexPosition2(CUBE_CORNERS[VERTEX_X1_Y1_Z0], c110x, c110y, c110z);

        float c010x = c000x + vyx;
        float c010y = c000y + vyy;
        float c010z = c000z + vyz;
        buildVertexPosition2(CUBE_CORNERS[VERTEX_X0_Y1_Z0], c010x, c010y, c010z);

        float c001x = c000x + vzx;
        float c001y = c000y + vzy;
        float c001z = c000z + vzz;
        buildVertexPosition2(CUBE_CORNERS[VERTEX_X0_Y0_Z1], c001x, c001y, c001z);

        float c101x = c100x + vzx;
        float c101y = c100y + vzy;
        float c101z = c100z + vzz;
        buildVertexPosition2(CUBE_CORNERS[VERTEX_X1_Y0_Z1], c101x, c101y, c101z);

        float c111x = c110x + vzx;
        float c111y = c110y + vzy;
        float c111z = c110z + vzz;
        buildVertexPosition2(CUBE_CORNERS[VERTEX_X1_Y1_Z1], c111x, c111y, c111z);

        float c011x = c010x + vzx;
        float c011y = c010y + vzy;
        float c011z = c010z + vzz;
        buildVertexPosition2(CUBE_CORNERS[VERTEX_X0_Y1_Z1], c011x, c011y, c011z);


        buildVertexTexCoord(VERTEX_TEXTURES[0], cuboid.u1, cuboid.v0, cuboid.u2, cuboid.v1);
        buildVertexTexCoord(VERTEX_TEXTURES[1], cuboid.u2, cuboid.v1, cuboid.u3, cuboid.v0);
        buildVertexTexCoord(VERTEX_TEXTURES[3], cuboid.u1, cuboid.v1, cuboid.u2, cuboid.v2);
        buildVertexTexCoord(VERTEX_TEXTURES[5], cuboid.u4, cuboid.v1, cuboid.u5, cuboid.v2);
        buildVertexTexCoord(VERTEX_TEXTURES[2], cuboid.u2, cuboid.v1, cuboid.u4, cuboid.v2);
        buildVertexTexCoord(VERTEX_TEXTURES[4], cuboid.u0, cuboid.v1, cuboid.u1, cuboid.v2);

        flerovium$FACE = ~0;
        // This used to be `matrices.getPositionMatrix().m32() <= -16.0F && RenderSystem.getModelViewMatrix().m32() == 0
        // but that seems to prevent culling from occurring
        // Moral of the story: try random shit until it sticks
        if (RenderSystem.getModelViewMatrix().m32() == 0) {
            Matrix3f normal = matrices.getNormalMatrix();

            float posX = c000x + c011x;
            float posY = c000y + c011y;
            float posZ = c000z + c011z;
            if (posX * normal.m00 + posY * normal.m01 + posZ * normal.m02 < 0)
                flerovium$FACE &= ~(1 << (sizeX > 0 ? FACE_POS_X : FACE_NEG_X));

            posX = c100x + c111x;
            posY = c100y + c111y;
            posZ = c100z + c111z;
            if (posX * normal.m00 + posY * normal.m01 + posZ * normal.m02 > 0)
                flerovium$FACE &= ~(1 << (sizeX > 0 ? FACE_NEG_X : FACE_POS_X));

            posX = c000x + c110x;
            posY = c000y + c110y;
            posZ = c000z + c110z;
            if (posX * normal.m20 + posY * normal.m21 + posZ * normal.m22 < 0) flerovium$FACE &= ~(1 << FACE_NEG_Z);

            posX = c001x + c111x;
            posY = c001y + c111y;
            posZ = c001z + c111z;
            if (posX * normal.m20 + posY * normal.m21 + posZ * normal.m22 > 0) flerovium$FACE &= ~(1 << FACE_POS_Z);

            posX = c000x + c101x;
            posY = c000y + c101y;
            posZ = c000z + c101z;
            if (posX * normal.m10 + posY * normal.m11 + posZ * normal.m12 < 0) flerovium$FACE &= ~(1 << FACE_NEG_Y);

            posX = c010x + c111x;
            posY = c010y + c111y;
            posZ = c010z + c111z;
            if (posX * normal.m10 + posY * normal.m11 + posZ * normal.m12 > 0) flerovium$FACE &= ~(1 << FACE_POS_Y);
        }
    }

    @Redirect(method = "emitQuads", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/immediate/model/ModelCuboid;shouldDrawFace(I)Z"))
    private static boolean onShouldDrawFace(ModelCuboid cuboid, int face) {
        BuryTheLightClient.debug(flerovium$FACE, face, flerovium$FACE & (1 << face));
        return (flerovium$FACE & (1 << face)) != 0 && cuboid.shouldDrawFace(face);
    }
}
