package inspectorboat.yeetlighting.mixin.client.biome;

import inspectorboat.yeetlighting.ByteBufUtils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketDecoder;
import net.minecraft.network.packet.s2c.play.ChunkBiomeDataS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ChunkBiomeDataS2CPacket.class)
public class ChunkBiomeDataS2CPacketMixin {
    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/PacketByteBuf;readList(Lnet/minecraft/network/codec/PacketDecoder;)Ljava/util/List;"
            ),
            method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V"
    )
    private static List<?> skipDeserializeBiomeData(PacketByteBuf buf, PacketDecoder<?, ?> reader) {
        ByteBufUtils.discardAllBytes(buf);
        return null;
    }
}
