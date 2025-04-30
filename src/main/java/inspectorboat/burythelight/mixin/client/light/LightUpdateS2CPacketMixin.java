package inspectorboat.burythelight.mixin.client.light;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import inspectorboat.burythelight.ByteBufUtils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.LightData;
import net.minecraft.network.packet.s2c.play.LightUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightUpdateS2CPacket.class)
public class LightUpdateS2CPacketMixin {
	@WrapOperation(
			at = @At(
					value = "NEW",
					target = "(Lnet/minecraft/network/PacketByteBuf;II)Lnet/minecraft/network/packet/s2c/play/LightData;"
			),
			method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V"
	)
	// We can just discard the entire rest of the buffer, since lightData is the last
	// field to be deserialized and as such would take up the rest of the buffer anyways
	private LightData skipDeserializeLightData(PacketByteBuf buf, int x, int y, Operation<LightData> original) {
		ByteBufUtils.discardAllBytes(buf);

		return null;
	}
}