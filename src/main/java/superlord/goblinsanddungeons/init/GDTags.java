package superlord.goblinsanddungeons.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.tags.TagKey;
import superlord.goblinsanddungeons.GoblinsAndDungeons;

public class GDTags {

    /** Every Goblin subclass of 1.18.2 (everything that {@code extends Goblin}, the Ogre and the Mimic included). */
    public static final TagKey<EntityType<?>> GOBLINS = TagKey.create(Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "goblins"));

}
