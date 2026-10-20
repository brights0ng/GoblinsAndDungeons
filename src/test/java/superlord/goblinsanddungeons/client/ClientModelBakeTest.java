package superlord.goblinsanddungeons.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import org.junit.jupiter.api.Test;
import superlord.goblinsanddungeons.client.model.BeholderModel;
import superlord.goblinsanddungeons.client.model.GarchModel;
import superlord.goblinsanddungeons.client.model.GobModel;
import superlord.goblinsanddungeons.client.model.GobberModel;
import superlord.goblinsanddungeons.client.model.GoblinKingModel;
import superlord.goblinsanddungeons.client.model.GobloModel;
import superlord.goblinsanddungeons.client.model.GoomModel;
import superlord.goblinsanddungeons.client.model.HiddenMimicModel;
import superlord.goblinsanddungeons.client.model.HobgobModel;
import superlord.goblinsanddungeons.client.model.MimicModel;
import superlord.goblinsanddungeons.client.model.OgreModel;
import superlord.goblinsanddungeons.client.model.SittingOgreModel;
import superlord.goblinsanddungeons.client.model.SleepingGobloModel;
import superlord.goblinsanddungeons.client.model.armor.GoblinCrownModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClientModelBakeTest {

    @Test
    void allFourteenEntityAndArmorModelsBakeCleanly() {
        assertDoesNotThrow(() -> new GobModel(GobModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new GobberModel(GobberModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new HobgobModel<>(HobgobModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new GobloModel<>(GobloModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new SleepingGobloModel<>(SleepingGobloModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new GarchModel<>(GarchModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new GoomModel<>(GoomModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new OgreModel<>(OgreModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new SittingOgreModel<>(SittingOgreModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new MimicModel<>(MimicModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new HiddenMimicModel<>(HiddenMimicModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new GoblinKingModel<>(GoblinKingModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new BeholderModel(BeholderModel.createBodyLayer().bakeRoot()));
        assertDoesNotThrow(() -> new GoblinCrownModel(GoblinCrownModel.createArmorLayer(new CubeDeformation(0.5F)).bakeRoot()));
    }

    @Test
    void allBlockAndItemModelsHaveValidSingularAtlasTextures() throws Exception {
        ClassLoader cl = ClientModelBakeTest.class.getClassLoader();
        var urnModelUrl = cl.getResource("assets/goblinsanddungeons/models/block/urn.json");
        assertNotNull(urnModelUrl, "urn.json should be on classpath");
        Path assetsRoot = Path.of(urnModelUrl.toURI()).getParent().getParent().getParent();
        Path modelsRoot = assetsRoot.resolve("models");
        Path blockstatesRoot = assetsRoot.resolve("blockstates");
        assertTrue(Files.isDirectory(modelsRoot), "Models directory should exist");
        assertTrue(Files.isDirectory(blockstatesRoot), "Blockstates directory should exist");

        try (Stream<Path> paths = Files.walk(modelsRoot)) {
            for (Path modelPath : paths.filter(p -> p.toString().endsWith(".json")).toList()) {
                String content = Files.readString(modelPath, StandardCharsets.UTF_8);
                JsonObject json = JsonParser.parseString(content).getAsJsonObject();
                if (json.has("parent")) {
                    String parent = json.get("parent").getAsString();
                    if (parent.startsWith("goblinsanddungeons:")) {
                        String relParent = parent.substring("goblinsanddungeons:".length());
                        String parentResource = "assets/goblinsanddungeons/models/" + relParent + ".json";
                        assertNotNull(cl.getResource(parentResource),
                                () -> "Missing parent model for " + modelPath + ": " + parentResource);
                    }
                }
                if (json.has("textures")) {
                    JsonObject textures = json.getAsJsonObject("textures");
                    for (Map.Entry<String, JsonElement> entry : textures.entrySet()) {
                        String texRef = entry.getValue().getAsString();
                        if (texRef.startsWith("goblinsanddungeons:")) {
                            String relPath = texRef.substring("goblinsanddungeons:".length());
                            assertTrue(relPath.startsWith("block/") || relPath.startsWith("item/"),
                                    () -> "Model " + modelPath + " uses non-singular atlas prefix: " + texRef);
                            String resourcePath = "assets/goblinsanddungeons/textures/" + relPath + ".png";
                            assertNotNull(cl.getResource(resourcePath),
                                    () -> "Missing texture PNG on classpath for model " + modelPath + ": " + resourcePath);
                        }
                    }
                }
            }
        }
    }
}
