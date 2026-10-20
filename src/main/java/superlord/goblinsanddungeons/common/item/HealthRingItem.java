package superlord.goblinsanddungeons.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import superlord.goblinsanddungeons.GoblinsAndDungeons;

import java.util.List;

public class HealthRingItem extends Item {

    public static final ResourceLocation HEALTH_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "ring_of_health_boost");

    public HealthRingItem(Properties properties) {
        super(properties.stacksTo(1).attributes(createAttributes()));
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.MAX_HEALTH,
                        new AttributeModifier(HEALTH_MODIFIER_ID, 2.0D, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.OFFHAND
                )
                .build();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("health_ring").withStyle(ChatFormatting.GRAY));
        } else {
            tooltipComponents.add(Component.translatable("shift").withStyle(ChatFormatting.GRAY));
        }
    }
}
