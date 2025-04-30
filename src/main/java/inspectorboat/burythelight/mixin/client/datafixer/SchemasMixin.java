package inspectorboat.burythelight.mixin.client.datafixer;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder.Result;
import net.minecraft.datafixer.Schemas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mixin(Schemas.class)
public class SchemasMixin {

    /**
     * @author _
     * @reason _
     */
    @Overwrite
    private static Result create() {
        return null;
    }

    /**
     * @author _
     * @reason _
     */
    @Overwrite
    public static CompletableFuture<?> optimize(Set<DSL.TypeReference> requiredTypes) {
        return null;
    }

    /**
     * @author _
     * @reason _
     */
    @Overwrite
    public static DataFixer getFixer() {
        return null;
    }
}
