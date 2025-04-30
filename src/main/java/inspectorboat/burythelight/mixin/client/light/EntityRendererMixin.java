package inspectorboat.burythelight.mixin.client.light;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EntityRenderer.class)

public class EntityRendererMixin {
    /**
     * @author _
     * @reason _
     */
    @Overwrite
    public int getBlockLight(Entity entity, BlockPos pos) {
        return 15;
    }

    /**
     * @author _
     * @reason _
     */
    @Overwrite
    public int getSkyLight(Entity entity, BlockPos pos) {
        return 15;
    }

}
