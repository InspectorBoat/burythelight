package inspectorboat.burythelight.mixin.client.flerovium.Item;

//import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector3f;
import org.joml.Math;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//import static net.neoforged.neoforge.common.util.TransformationHelper.quatFromXYZ;
import inspectorboat.burythelight.flerovium.functions.MatrixStuff;

@Mixin(value = Transformation.class, remap = false)
public abstract class TransformationMixin {
    @Final
    @Shadow
    private Vector3fc rotation;
    @Final
    @Shadow(remap = false)
    private Vector3fc translation;
    @Final
    @Shadow(remap = false)
    private Vector3fc scale;
//    @Final
//    @Shadow(remap = false)
//    public Vector3f rightRotation;

    @Unique
    boolean flerovium$noRot = false;
    @Unique
    boolean flerovium$noTrans = false;
    @Unique
    boolean flerovium$scaleSameAndPositive = false;
    @Unique
    boolean flerovium$noRightRot = false;
    @Unique
    float flerovium$sinX = 0f;
    @Unique
    float flerovium$cosX = 0f;
    @Unique
    float flerovium$sinY = 0f;
    @Unique
    float flerovium$cosY = 0f;

    @Inject(
            method = "<init>(Lorg/joml/Vector3fc;Lorg/joml/Vector3fc;Lorg/joml/Vector3fc;)V",
            at = @At("TAIL")
    )
    public void init(Vector3fc vector3fc, Vector3fc vector3fc2, Vector3fc vector3fc3, CallbackInfo ci) {
        if (rotation.equals(0, 0, 0)) {
            flerovium$noRot = true;
        } else if (rotation.z() == 0) {
            float radX = Math.toRadians(rotation.x());
            float radY = Math.toRadians(rotation.y());
            flerovium$sinX = Math.sin(radX);
            flerovium$sinY = Math.sin(radY);
            flerovium$cosX = Math.cosFromSin(flerovium$sinX, radX);
            flerovium$cosY = Math.cosFromSin(flerovium$sinY, radY);
        }
        if (translation.equals(0, 0, 0)) {
            flerovium$noTrans = true;
        }
        if (scale.x() == scale.y() && scale.y() == scale.z() && scale.x() > 0) {
            flerovium$scaleSameAndPositive = true;
        } else if (scale.z() == 0) {
//            scale.z = 1E-5F; // Work Around for some MCR mods
        }
//        if (rightRotation.equals(0, 0, 0)) {
//            flerovium$noRightRot = true;
//        }
    }

    @Inject(
            method = "apply",
            at = @At("HEAD"),
            cancellable = true
    )
    public void apply(boolean leftHanded, MatrixStack.Entry entry, CallbackInfo ci) {
        entry.translate(0.5f, 0.5f, 0.5f);
//        if ((Object) this != Transformation.IDENTITY) {
//            final float flip = leftHanded ? -1 : 1;
//            if (!flerovium$noTrans) {
//                entry.translate(flip * translation.x(), translation.y(), translation.z());
//            }
//            if (!flerovium$noRot) {
//                if (rotation.z() == 0) {
//                    MatrixStack.Entry last = entry;
////                    MatrixStack.Entry last = entry.last();
//                    float flipY = flip * flerovium$sinY;
//                    MatrixStuff.rotateXY(last.getPositionMatrix(), flerovium$sinX, flerovium$cosX, flipY, flerovium$cosY);
//                    MatrixStuff.rotateXY(last.getNormalMatrix(), flerovium$sinX, flerovium$cosX, flipY, flerovium$cosY);
//                } else {
////                    entry.multiplyPositionMatrix(quatFromXYZ(rotation.x(), rotation.y() * flip, rotation.z() * flip, true));
//                }
//            }
//            if (flerovium$scaleSameAndPositive) {
//                entry
////                        .last()
//                        .getPositionMatrix().scale(scale.x(), scale.x(), scale.x());
//            } else {
//                entry.getPositionMatrix().scale(scale.x(), scale.y(), scale.z());
//            }
////            if (!flerovium$noRightRot) {
////                entry.multiplyPositionMatrix(quatFromXYZ(rightRotation.x(), rightRotation.y() * flip, rightRotation.z() * flip, true));
////            }
//        }
        ci.cancel();
    }
}
