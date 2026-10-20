package superlord.goblinsanddungeons.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import superlord.goblinsanddungeons.init.ItemInit;

import java.util.List;

public class StealthRingItem extends Item {

    public StealthRingItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (Screen.hasShiftDown()) {
            if (stack.is(ItemInit.RING_OF_STEALTH.get())) {
                tooltipComponents.add(Component.translatable("stealth_ring").withStyle(ChatFormatting.GRAY));
            } else if (stack.is(ItemInit.RING_OF_EXPERIENCE.get())) {
                tooltipComponents.add(Component.translatable("experience_ring").withStyle(ChatFormatting.GRAY));
            } else if (stack.is(ItemInit.RING_OF_GLORY.get())) {
                tooltipComponents.add(Component.translatable("glory_ring").withStyle(ChatFormatting.GRAY));
            }
        } else {
            tooltipComponents.add(Component.translatable("shift").withStyle(ChatFormatting.GRAY));
        }
    }
}
