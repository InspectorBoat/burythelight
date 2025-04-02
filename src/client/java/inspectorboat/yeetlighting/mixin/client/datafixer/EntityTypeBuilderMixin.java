package inspectorboat.yeetlighting.mixin.client.datafixer;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import net.minecraft.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityType.Builder.class)
public class EntityTypeBuilderMixin {
    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Util;getChoiceType(Lcom/mojang/datafixers/DSL$TypeReference;Ljava/lang/String;)Lcom/mojang/datafixers/types/Type;"
            ),
            method = "build(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/entity/EntityType;"
    )
    private @Nullable Type<?> cancelGetDataFixer(DSL.TypeReference typeReference, String id) {
        return null;
    }
}
