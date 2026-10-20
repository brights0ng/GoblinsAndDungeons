package superlord.goblinsanddungeons.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {

    public final ModConfigSpec.IntValue ogreSpawnWeight;

    public final ModConfigSpec.IntValue largeGoblinCampMinDistance;
    public final ModConfigSpec.IntValue largeGoblinCampMaxDistance;
    public final ModConfigSpec.IntValue mediumGoblinCampMinDistance;
    public final ModConfigSpec.IntValue mediumGoblinCampMaxDistance;
    public final ModConfigSpec.IntValue smallGoblinCampMinDistance;
    public final ModConfigSpec.IntValue smallGoblinCampMaxDistance;
    public final ModConfigSpec.IntValue ruinedKeepMinDistance;
    public final ModConfigSpec.IntValue ruinedKeepMaxDistance;
    public final ModConfigSpec.BooleanValue magicalWorld;

    public final ModConfigSpec.BooleanValue superSecretSettings;

    // Config keys are kept identical to 1.18.2 (including the spaces) so existing config files keep their values.
    public ServerConfig(final ModConfigSpec.Builder builder) {
        builder.push("general");
        this.ogreSpawnWeight = buildInt(builder, "Ogre Spawn Weight", 2, 1, 300, "The weight of Ogres in vanilla's spawn rate. Default is 2");
        this.largeGoblinCampMaxDistance = buildInt(builder, "Large Goblin Camp Max Distance", 100, 1, 300, "The farthest two Large Goblin Camps can spawn from eachother. Keep this value higher than the min. Default is 100");
        this.largeGoblinCampMinDistance = buildInt(builder, "Large Goblin Camp Min Distance", 50, 1, 300, "The closest two Large Goblin Camps can spawn from eachother. Keep this value lower than the max. Default is 50");
        this.mediumGoblinCampMaxDistance = buildInt(builder, "Medium Goblin Camp Max Distance", 100, 1, 300, "The farthest two Medium Goblin Camps can spawn from eachother. Keep this value higher than the min. Default is 100");
        this.mediumGoblinCampMinDistance = buildInt(builder, "Medium Goblin Camp Min Distance", 50, 1, 300, "The closest two Medium Goblin Camps can spawn from eachother. Keep this value lower than the max. Default is 50");
        this.smallGoblinCampMaxDistance = buildInt(builder, "Small Goblin Camp Max Distance", 100, 1, 300, "The farthest two Small Goblin Camps can spawn from eachother. Keep this value higher than the min. Default is 100");
        this.smallGoblinCampMinDistance = buildInt(builder, "Small Goblin Camp Min Distance", 50, 1, 300, "The closest two Small Goblin Camps can spawn from eachother. Keep this value lower than the max. Default is 50");
        this.ruinedKeepMaxDistance = buildInt(builder, "Ruined Keep Max Distance", 100, 1, 300, "The farthest two Ruined Keeps can spawn from eachother. Keep this value higher than the min. Default is 100");
        this.ruinedKeepMinDistance = buildInt(builder, "Ruined Keep Min Distance", 50, 1, 300, "The closest two Ruined Keeps can spawn from eachother. Keep this value lower than the max. Default is 50");
        this.magicalWorld = buildBoolean(builder, "Magical World", true, "Toggles whether the player has access to magic or not. Default is true");
        this.superSecretSettings = buildBoolean(builder, "Super Secret Settings", false, "Even I don't know what it does. Default is false");
        builder.pop();
    }

    private static ModConfigSpec.BooleanValue buildBoolean(ModConfigSpec.Builder builder, String name, boolean defaultValue, String comment) {
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ModConfigSpec.IntValue buildInt(ModConfigSpec.Builder builder, String name, int defaultValue, int min, int max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

}
