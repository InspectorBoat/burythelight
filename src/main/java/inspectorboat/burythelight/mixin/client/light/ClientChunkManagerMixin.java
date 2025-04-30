package inspectorboat.burythelight.mixin.client.light;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientChunkManager.class)
public class ClientChunkManagerMixin {
    @Redirect(
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/chunk/ChunkProvider;ZZ)Lnet/minecraft/world/chunk/light/LightingProvider;"
            ),
            method = "<init>"
    )
    public LightingProvider tweakLightManager(ChunkProvider chunkProvider, boolean hasBlockLight, boolean hasSkyLight) {
        return new LightingProvider(chunkProvider, false, false);
    }
}
