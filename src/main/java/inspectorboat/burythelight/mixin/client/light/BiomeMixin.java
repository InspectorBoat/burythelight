package inspectorboat.burythelight.mixin.client.light;

import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Optional;

@Mixin(Biome.class)
public class BiomeMixin {
    /**
     * @author _
     * @reason _
     */
    @Overwrite
    // We don't know the light level so it makes no sense to play the mood sound
    public Optional<BiomeMoodSound> getMoodSound() {
        return Optional.empty();
    }
}
