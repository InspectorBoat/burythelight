package inspectorboat.yeetlighting.mixin.client.biome;

import inspectorboat.yeetlighting.ByteBufUtils;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;
import net.minecraft.world.chunk.ReadableContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.chunk.ChunkStatus.BIOMES;

@Mixin(ChunkSection.class)
public class ChunkSectionMixin {
    @Shadow
    private ReadableContainer<RegistryEntry<Biome>> biomeContainer;


    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/ReadableContainer;slice()Lnet/minecraft/world/chunk/PalettedContainer;"
            ),
            method = "readDataPacket(Lnet/minecraft/network/PacketByteBuf;)V",
            cancellable = true
    )
    private void cancelReadBiomes(PacketByteBuf buf, CallbackInfo ci) {
        ci.cancel();
        // Discard correct amount of bytes
        // TODO: make more efficient
        int i = buf.readByte();

        var slice = biomeContainer.slice();
        var data = slice.getCompatibleData(slice.data, i);
        data.palette.readPacket(buf);

        ByteBufUtils.discardLongArray(buf);
    }
}
