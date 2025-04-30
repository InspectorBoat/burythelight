package inspectorboat.burythelight.mixin.client.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import net.caffeinemc.mods.sodium.client.services.SodiumModelDataContainer;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.caffeinemc.mods.sodium.client.world.SodiumAuxiliaryLightManager;
import net.caffeinemc.mods.sodium.client.world.cloned.ChunkRenderContext;
import net.caffeinemc.mods.sodium.client.world.cloned.ClonedChunkSection;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.chunk.ChunkNibbleArray;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(LevelSlice.class)
public class LevelSliceMixin {
    @WrapOperation(
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/caffeinemc/mods/sodium/client/world/LevelSlice;lightArrays:[[Lnet/minecraft/world/chunk/ChunkNibbleArray;"
            ),
            method = "<init>(Lnet/minecraft/client/world/ClientWorld;)V"
    )
    public void cancelCreateLightArrays(LevelSlice instance, ChunkNibbleArray[][] value, Operation<Void> original) {
    }

    @Overwrite(remap = false)
    private void copySectionData(ChunkRenderContext context, int sectionIndex) {
        ClonedChunkSection section = context.getSections()[sectionIndex];
        Objects.requireNonNull(section, "Chunk section must be non-null");
        this.unpackBlockData(this.blockArrays[sectionIndex], context, section);
        this.blockEntityArrays[sectionIndex] = section.getBlockEntityMap();
        this.blockEntityRenderDataArrays[sectionIndex] = section.getBlockEntityRenderDataMap();
        this.modelMapArrays[sectionIndex] = section.getModelMap();
    }

    @Overwrite(remap = false)
    public void reset() {
        for(int sectionIndex = 0; sectionIndex < SECTION_ARRAY_LENGTH; ++sectionIndex) {
            this.blockEntityArrays[sectionIndex] = null;
            this.auxLightManager[sectionIndex] = null;
            this.blockEntityRenderDataArrays[sectionIndex] = null;
        }

    }

    @Shadow @Final
    private static int SECTION_ARRAY_LENGTH;

    @Final
    @Shadow(remap = false)
    private Int2ReferenceMap<BlockEntity>[] blockEntityArrays;

    @Final
    @Shadow(remap = false)
    private Int2ReferenceMap<Object>[] blockEntityRenderDataArrays;

    @Final
    @Shadow(remap = false)
    private SodiumModelDataContainer[] modelMapArrays;

    @Final
    @Shadow(remap = false)
    private BlockState[][] blockArrays;

    @Final
    @Shadow(remap = false)
    private SodiumAuxiliaryLightManager[] auxLightManager;

    @Shadow(remap = false)
    private void unpackBlockData(BlockState[] blockArray, ChunkRenderContext context, ClonedChunkSection section) {
    }
}