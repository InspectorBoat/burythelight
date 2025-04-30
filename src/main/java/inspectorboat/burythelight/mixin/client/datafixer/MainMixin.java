package inspectorboat.burythelight.mixin.client.datafixer;

import com.mojang.datafixers.DSL;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mixin(Main.class)
public class MainMixin {
    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/datafixer/Schemas;optimize(Ljava/util/Set;)Ljava/util/concurrent/CompletableFuture;"
            ),
            method = "main([Ljava/lang/String;)V"
    )
    private static CompletableFuture<?> cancelOptimize(Set<DSL.TypeReference> requiredTypes) {
        return null;
    }

    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/CompletableFuture;join()Ljava/lang/Object;"
            ),
            method = "main([Ljava/lang/String;)V"
    )
    private static Object cancelJoin(CompletableFuture<?> completableFuture) {
        return null;
    }
}
