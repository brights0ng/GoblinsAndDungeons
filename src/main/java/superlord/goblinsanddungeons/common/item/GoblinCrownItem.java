package superlord.goblinsanddungeons.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.init.GDTags;

import java.util.List;

public class GoblinCrownItem extends ArmorItem {

    private static final ResourceLocation CROWN_TEXTURE = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/models/armor/goblin_crown.png");

    public GoblinCrownItem(Holder<ArmorMaterial> material, Properties properties) {
        super(material, ArmorItem.Type.HELMET, properties);
    }

    @Override
    @Nullable
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return CROWN_TEXTURE;
    }

    /** The 1.18.2 {@code onArmorTick}: while worn, the nearest monster nearby is weakened, Goblins more than the rest. */
    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, world, entity, slotId, isSelected);
        if (!world.isClientSide() && entity instanceof Player player && player.getItemBySlot(EquipmentSlot.HEAD) == stack) {
            Monster monster = world.getNearestEntity(
                    Monster.class,
                    TargetingConditions.DEFAULT,
                    player,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    player.getBoundingBox().inflate(8.0D, 3.0D, 8.0D)
            );
            if (monster != null) {
                int amplifier = monster.getType().is(GDTags.GOBLINS) ? 1 : 0;
                monster.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, amplifier, false, false));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("goblin_crown").withStyle(ChatFormatting.GRAY));
        } else {
            tooltipComponents.add(Component.translatable("shift").withStyle(ChatFormatting.GRAY));
        }
    }
}
