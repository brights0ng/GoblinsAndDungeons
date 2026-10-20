package superlord.goblinsanddungeons.client;

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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
}
