package inspectorboat.burythelight.mixin.client.light;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.LightData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    /**
     * @author _
     * @reason _
     */
    @Overwrite
    private void readLightData(int x, int z, LightData data, boolean bl) {
    }
}
