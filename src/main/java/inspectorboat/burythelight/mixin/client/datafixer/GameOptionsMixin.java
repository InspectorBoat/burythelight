package inspectorboat.burythelight.mixin.client.datafixer;

import com.mojang.datafixers.DataFixer;
import net.minecraft.client.option.GameOptions;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/datafixer/DataFixTypes;update(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/nbt/NbtCompound;I)Lnet/minecraft/nbt/NbtCompound;"
            ),
            method = "update(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"
    )
    public NbtCompound skipFixGameOptions(DataFixTypes instance, DataFixer dataFixer, NbtCompound nbt, int oldVersion) {
        return nbt;
    }
}
