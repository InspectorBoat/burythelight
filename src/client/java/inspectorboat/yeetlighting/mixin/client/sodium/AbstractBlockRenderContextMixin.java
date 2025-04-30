package inspectorboat.yeetlighting.mixin.client.sodium;

import net.caffeinemc.mods.sodium.client.model.light.LightMode;
import net.caffeinemc.mods.sodium.client.render.frapi.mesh.MutableQuadViewImpl;
import net.caffeinemc.mods.sodium.client.render.frapi.render.AbstractBlockRenderContext;
import net.fabricmc.fabric.api.renderer.v1.material.ShadeMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AbstractBlockRenderContext.class)
public class AbstractBlockRenderContextMixin {
    @Overwrite(remap = false)
    protected void shadeQuad(MutableQuadViewImpl quad, LightMode lightMode, boolean emissive, ShadeMode shadeMode) {
    }

}
