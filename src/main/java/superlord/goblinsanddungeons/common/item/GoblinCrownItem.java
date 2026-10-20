package superlord.goblinsanddungeons.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class GoblinCrownItem extends ArmorItem {

    public GoblinCrownItem(Item.Properties properties) {
        super(ArmorMaterials.GOLD, ArmorItem.Type.HELMET, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        if (!world.isClientSide() && entity instanceof Player player) {
            if (player.getItemBySlot(EquipmentSlot.HEAD) == stack) {
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
                    // If it's a goblin mob, apply Weakness II, else Weakness I
                    int amplifier = monster.getType().toString().contains("gob") ? 1 : 0;
                    monster.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, amplifier, false, false));
                }
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
