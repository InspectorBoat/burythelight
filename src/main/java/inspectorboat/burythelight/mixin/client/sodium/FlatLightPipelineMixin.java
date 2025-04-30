package inspectorboat.burythelight.mixin.client.sodium;

import net.caffeinemc.mods.sodium.client.model.light.data.QuadLightData;
import net.caffeinemc.mods.sodium.client.model.light.flat.FlatLightPipeline;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FlatLightPipeline.class)
public class FlatLightPipelineMixin {
    @Overwrite
    public void calculate(ModelQuadView quad, BlockPos pos, QuadLightData out, Direction cullFace, Direction lightFace, boolean shade, boolean enhanced) {
    }

//    public float getBrightness(Direction direction, boolean shaded) {
//        boolean bl = this.getDimensionEffects().isDarkened();
//        if (!shaded) {
//            return bl ? 0.9F : 1.0F;
//        } else {
//            switch (direction) {
//                case DOWN:
//                    return bl ? 0.9F : 0.5F;
//                case UP:
//                    return bl ? 0.9F : 1.0F;
//                case NORTH:
//                case SOUTH:
//                    return 0.8F;
//                case WEST:
//                case EAST:
//                    return 0.6F;
//                default:
//                    return 1.0F;
//            }
//        }
//    }
}
