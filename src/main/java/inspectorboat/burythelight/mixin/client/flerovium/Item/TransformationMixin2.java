package inspectorboat.burythelight.mixin.client.flerovium.Item;

//import com.mojang.blaze3d.vertex.PoseStack;

import inspectorboat.burythelight.BuryTheLightClient;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//import static net.neoforged.neoforge.common.util.TransformationHelper.quatFromXYZ;


@Mixin(value = Transformation.class, remap = false)
public abstract class TransformationMixin2 {
    @Final
    @Mutable
    @Shadow
    private Vector3fc rotation;
    @Final
    @Mutable
    @Shadow(remap = false)
    private Vector3fc translation;
    @Final
    @Mutable
    @Shadow(remap = false)
    private Vector3fc scale;

    @Unique
    public Matrix4f matrix;

    @Inject(
            method = "<init>(Lorg/joml/Vector3fc;Lorg/joml/Vector3fc;Lorg/joml/Vector3fc;)V",
            at = @At("TAIL")
    )
    public void init(Vector3fc rotation, Vector3fc translation, Vector3fc scale, CallbackInfo ci) {
        MatrixStack.Entry entry = new MatrixStack.Entry();
        if ((Object) this != Transformation.IDENTITY) {
            entry.translate(this.translation.x(), this.translation.y(), this.translation.z());
            entry.rotate((new Quaternionf()).rotationXYZ(this.rotation.x() * 0.017453292F, this.rotation.y() * 0.017453292F, this.rotation.z() * 0.017453292F));
            entry.scale(this.scale.x(), this.scale.y(), this.scale.z());
        }
        entry.translate(-0.5F, -0.5F, -0.5F);
        matrix = entry.getPositionMatrix();
        this.rotation = null;
        this.translation = null;
        this.scale = null;
    }

    @Inject(
            method = "apply",
            at = @At("HEAD"),
            cancellable = true
    )
    public void apply(boolean leftHanded, MatrixStack.Entry entry, CallbackInfo ci) {
        entry.multiplyPositionMatrix(matrix);
        ci.cancel();
    }
}
