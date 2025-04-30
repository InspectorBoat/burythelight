package inspectorboat.burythelight.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteOpener;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static inspectorboat.burythelight.BuryTheLightClient.getTintForTextureId;
import static net.caffeinemc.mods.sodium.api.util.ColorMixer.mulComponentWise;

@Mixin(SpriteOpener.class)
public interface SpriteOpenerMixin {
    @ModifyExpressionValue(
            method = "method_52851",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/NativeImage;read(Ljava/io/InputStream;)Lnet/minecraft/client/texture/NativeImage;"
            )
    )
    private static NativeImage possiblyFixNativeImage(NativeImage original, @Local(argsOnly = true) Identifier id) {
        int tint = getTintForTextureId(id);
        if (tint != -1) {
            for (int x = 0; x < original.getWidth(); x++) {
                for (int y = 0; y < original.getHeight(); y++) {
                    int originalColor = original.getColorArgb(x, y);

                    original.setColor(x, y, mulComponentWise(originalColor, tint));
                }
            }

        }
//        System.out.println(id);
        return original;
    }
}
