package inspectorboat.burythelight;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.*;
import java.util.List;
import java.util.Set;

public class MixinConfigPlugin implements IMixinConfigPlugin {
    public static class Config {
        public boolean discardLightPackets = true;
        public boolean nullLightData = true;
        public boolean discardBiomePackets = true;
        public boolean nullBiomeData = true;
        public boolean changeSodiumVertexFormat = true;
    }

    public static final Logger LOGGER = LogManager.getLogger("BuryTheLightConfig");

    public Config config = new Config();

    @Override
    public void onLoad(String s) {
        var file = new File("./config/sodium-mixins.properties");
        if (!file.exists()) {
            try {
                writeDefaultConfig(file);

            } catch (IOException e) {
                LOGGER.error("could not write default config");
            }
        } else {
            try (FileInputStream fin = new FileInputStream(file)) {
                JsonObject json = new JsonParser().parse(fin.toString()).getAsJsonObject();
                json.asMap().forEach((key, value) -> {
                    switch (key) {
                        case "discardLightPackets" -> config.discardLightPackets = value.getAsBoolean();
                        case "nullLightData" -> config.nullLightData = value.getAsBoolean();
                        case "discardBiomePackets" -> config.discardBiomePackets = value.getAsBoolean();
                        case "nullBiomeData" -> config.nullBiomeData = value.getAsBoolean();
                        case "changeSodiumVertexFormat" -> config.changeSodiumVertexFormat = value.getAsBoolean();
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Could not load config file", e);
            }
        }
    }

    private static void writeDefaultConfig(File file) throws IOException {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Could not create parent directories");
            }
        } else if (!dir.isDirectory()) {
            throw new IOException("The parent file is not a directory");
        }

        try (Writer writer = new FileWriter(file)) {
            writer.write("""
                    {
                        discardLightPackets: true,
                        discardBiomePackets: true,
                        sodiumMixin: true
                    }
                    """);
        }

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
