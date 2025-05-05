package inspectorboat.burythelight.mixin.client.flerovium.Item;

//import com.mojang.blaze3d.vertex.PoseStack;

import inspectorboat.burythelight.BuryTheLightClient;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.*;
import org.joml.Math;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//import static net.neoforged.neoforge.common.util.TransformationHelper.quatFromXYZ;


@Mixin(value = Transformation.class)
public abstract class TransformationMixin2 {
    @Final
    @Mutable
    @Shadow
    public Vector3f rotation;
    @Final
    @Mutable
    @Shadow
    public Vector3f translation;
    @Final
    @Mutable
    @Shadow
    public Vector3f scale;

    @Unique
    public Matrix4f matrix;

    @Inject(
            method = "<init>(Lorg/joml/Vector3f;Lorg/joml/Vector3f;Lorg/joml/Vector3f;)V",
            at = @At("TAIL")
    )
    public void init(Vector3f rotation, Vector3f translation, Vector3f scale, CallbackInfo ci) {
        Matrix4f transform = new Matrix4f();
        if ((Object) this != Transformation.IDENTITY) {
            transform.translate(this.translation.x(), this.translation.y(), this.translation.z());
            transform.rotate((new Quaternionf()).rotationXYZ(this.rotation.x() * 0.017453292F, this.rotation.y() * 0.017453292F, this.rotation.z() * 0.017453292F));
            transform.scale(this.scale.x(), this.scale.y(), this.scale.z());
        }
//        transform.translate(-0.5F, -0.5F, -0.5F);
        matrix = transform;
//        this.rotation = null;
//        this.translation = null;
//        this.scale = null;
    }

    @Inject(
            method = "apply",
            at = @At("HEAD"),
            cancellable = true
    )
    public void apply(boolean leftHanded, MatrixStack matrices, CallbackInfo ci) {
        matrices.multiplyPositionMatrix(matrix);
        ci.cancel();
    }
}
