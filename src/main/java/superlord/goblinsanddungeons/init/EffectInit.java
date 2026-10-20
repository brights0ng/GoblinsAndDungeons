package superlord.goblinsanddungeons.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.common.util.MagicNullificationEffect;
import superlord.goblinsanddungeons.common.util.OgresMightEffect;
import superlord.goblinsanddungeons.common.util.ParalysisEffect;

public class EffectInit {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, GoblinsAndDungeons.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, GoblinsAndDungeons.MOD_ID);

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, path);
    }

    public static final DeferredHolder<MobEffect, MobEffect> MIGHT = EFFECTS.register("might", () ->
            new OgresMightEffect(MobEffectCategory.NEUTRAL, 0xB8B546)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, id("effect.might.attack_speed"), (double) -0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, id("effect.might.attack_damage"), 3.0D, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> MAGIC_NULLIFICATION = EFFECTS.register("magic_nullification", () ->
            new MagicNullificationEffect(MobEffectCategory.HARMFUL, 0xB71FC5));
    public static final DeferredHolder<MobEffect, MobEffect> PARALYSIS = EFFECTS.register("paralysis", () ->
            new ParalysisEffect(MobEffectCategory.HARMFUL, 0xFF8CFF)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, id("effect.paralysis.attack_speed"), (double) -1F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, id("effect.paralysis.movement_speed"), -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.JUMP_STRENGTH, id("effect.paralysis.jump_strength"), -1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final DeferredHolder<Potion, Potion> OGRES_MIGHT = POTIONS.register("ogres_might", () -> new Potion(new MobEffectInstance(MIGHT, 600)));
    public static final DeferredHolder<Potion, Potion> OGRES_MIGHT_STRONG = POTIONS.register("ogres_might_strong", () -> new Potion(new MobEffectInstance(MIGHT, 600, 1)));
    public static final DeferredHolder<Potion, Potion> OGRES_MIGHT_LONG = POTIONS.register("ogres_might_long", () -> new Potion(new MobEffectInstance(MIGHT, 900)));

    public static void brewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addMix(Potions.AWKWARD, ItemInit.OGRE_TUSK.get(), OGRES_MIGHT);
        event.getBuilder().addMix(OGRES_MIGHT, Items.GLOWSTONE_DUST, OGRES_MIGHT_STRONG);
        event.getBuilder().addMix(OGRES_MIGHT, Items.REDSTONE, OGRES_MIGHT_LONG);
    }

}
