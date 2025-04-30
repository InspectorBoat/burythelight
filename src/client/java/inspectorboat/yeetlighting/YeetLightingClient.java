package inspectorboat.yeetlighting;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class YeetLightingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
//		MixinEnvironment.getCurrentEnvironment().audit();
    }


    public static int getTintForTextureId(Identifier id) {
        var path = id.getPath();
        if (!path.startsWith("block/")) {
            return -1;
        }
        return switch (id.getPath().substring(6)) {
            case "bamboo_large_leaves",
                 "bamboo_small_leaves",
                 "wildflowers_stem",
                 "pink_petals_stem",
                 "grass_block_top",
                 "grass_block_side_overlay",
                 "lily_pad"
                    -> rgbToInt(120, 240, 110);
            case "acacia_leaves",
                 "birch_leaves",
                 "cherry_leaves",
                 "dark_oak_leaves",
                 "jungle_leaves",
                 "mangrove_leaves",
                 "oak_leaves",
                 "spruce_leaves"
                    -> rgbToInt(80, 180, 80);
            case "bamboo_stage0",
                 "bush",
                 "fern",
                 "large_fern_bottom",
                 "large_fern_top",
                 "short_grass",
                 "sugar_cane",
                 "tall_grass_bottom",
                 "tall_grass_top"
                    -> rgbToInt(175, 250, 165);
            case "water_still", "water_flow" -> rgbToInt(94, 139, 255);
            default -> -1;
        };
    }

    public static int rgbToInt(int r, int g, int b) {
        return r << 0 | g << 8 | b << 16 | 255 << 24;
    }
    /*
//     * bamboo_large_leaves
//     * bamboo_small_leaves
//     * @flowerbed_1 #stem !wildflowers_stem !pink_petals_stem
//     * grass_block_top
//     * grass_block_side_overlay
     * @leaves #all
     * redstone_dust_dot
     * redstone_dust_line0
     * lily_pad
     * @stem_fruit #fruit
     * @stem_growth0 #stem !melon_stem !pumpkin_stem
     * @cauldron #content !water_still
     * @template_leaf_litter #texture !leaf_litter
     * @tinted_cross #cross !bamboo_stage0 !bush !fern !large_fern_bottom !large_fern_top !short_grass !sugar_cane !tall_grass_bottom !tall_grass_top
     * @tinted_flower_pot_cross #plant
     * vine
     *
     */
}