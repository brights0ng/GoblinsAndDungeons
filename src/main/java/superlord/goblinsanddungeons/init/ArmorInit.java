package superlord.goblinsanddungeons.init;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import superlord.goblinsanddungeons.GoblinsAndDungeons;

import java.util.EnumMap;
import java.util.List;

/** In 1.21 an armor material is a registry entry (a {@code Holder<ArmorMaterial>}) instead of an enum or interface. */
public class ArmorInit {

    public static final DeferredRegister<ArmorMaterial> REGISTER = DeferredRegister.create(Registries.ARMOR_MATERIAL, GoblinsAndDungeons.MOD_ID);

    /** The Goblin Crown wore {@code ArmorMaterials.GOLD} in 1.18.2, so this copies the gold stats, under its own texture layer. */
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GOBLIN_CROWN = REGISTER.register("goblin_crown", () ->
            new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), defense -> {
                        defense.put(ArmorItem.Type.BOOTS, 1);
                        defense.put(ArmorItem.Type.LEGGINGS, 3);
                        defense.put(ArmorItem.Type.CHESTPLATE, 5);
                        defense.put(ArmorItem.Type.HELMET, 2);
                        defense.put(ArmorItem.Type.BODY, 5);
                    }),
                    25,
                    SoundEvents.ARMOR_EQUIP_GOLD,
                    () -> Ingredient.of(Items.GOLD_INGOT),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "goblin_crown"))),
                    0.0F,
                    0.0F));

    /** The gold armor durability multiplier, which the 1.18.2 crown inherited. */
    public static final int GOBLIN_CROWN_DURABILITY_FACTOR = 7;
}
