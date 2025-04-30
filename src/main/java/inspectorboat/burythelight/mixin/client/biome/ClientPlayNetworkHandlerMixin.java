package inspectorboat.burythelight.mixin.client.biome;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChunkBiomeDataS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    /**
     * @author _
     * @reason _
     */
    @Overwrite
    public void onChunkBiomeData(ChunkBiomeDataS2CPacket packet) {
    }
}
