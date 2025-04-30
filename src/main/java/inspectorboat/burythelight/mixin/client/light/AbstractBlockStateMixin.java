package inspectorboat.burythelight.mixin.client.light;

import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
    /**
     * @author _
     * @reason _
     */
    @Overwrite
    public boolean hasEmissiveLighting(BlockView world, BlockPos pos) {
        return true;
    }
}
