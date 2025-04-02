package inspectorboat.yeetlighting;

import net.fabricmc.api.ClientModInitializer;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class YeetLightingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
//		MixinEnvironment.getCurrentEnvironment().audit();
	}
}