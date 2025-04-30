package inspectorboat.burythelight;

import net.minecraft.network.PacketByteBuf;

import static net.minecraft.network.encoding.VarInts.shouldContinueRead;

public class ByteBufUtils {
    public static void discardByte(PacketByteBuf buf) {
        buf.readerIndex(buf.readerIndex() + 1);
    }

    public static void discardBytes(PacketByteBuf buf, int bytesToDiscard) {
        buf.readerIndex(buf.readerIndex() + bytesToDiscard);
    }
    public static void discardBitSet(PacketByteBuf buf) {
        discardLongArray(buf);
    }

    public static void discardVarInt(PacketByteBuf buf) {
        int j = 0;

        byte b;
        do {
            b = buf.readByte();
            j++;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while (shouldContinueRead(b));
    }

    public static void discardLongArray(PacketByteBuf buf) {
        final int length = buf.readVarInt();
        discardLongs(buf, length);
    }

    public static void discardLongs(PacketByteBuf buf, int longsToDiscard) {
        buf.readerIndex(buf.readerIndex() + longsToDiscard * 8);
    }
    public static void discardAllBytes(PacketByteBuf buf) {
        buf.readerIndex(buf.writerIndex());
    }
}
