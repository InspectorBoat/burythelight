package inspectorboat.burythelight.mixin.client.dispenser;

import net.minecraft.block.dispenser.DispenserBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DispenserBehavior.class)
public interface DispenserBehaviorMixin {
    /**
     * @author _
     * @reason _
     */
    @Overwrite
    static void registerDefaults() {}
}
