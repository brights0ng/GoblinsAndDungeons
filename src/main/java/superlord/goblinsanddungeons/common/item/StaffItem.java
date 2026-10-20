package superlord.goblinsanddungeons.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import superlord.goblinsanddungeons.config.GoblinsDungeonsConfig;
import superlord.goblinsanddungeons.init.AttachmentInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.magic.PlayerSpells;

import java.util.List;
import java.util.function.Supplier;

/** The staff with no spell chosen. Sneaking and using it picks the best spell the player knows. */
public class StaffItem extends Item {

    public StaffItem(Properties properties) {
        super(properties);
    }

    /** Swaps the staff in {@code hand} for another one, keeping its damage, and says which spell is now active. */
    static void switchStaff(Player player, InteractionHand hand, Supplier<? extends Item> staff, String messageKey) {
        ItemStack newStack = new ItemStack(staff.get());
        int damage = player.getItemInHand(hand).getDamageValue();
        player.displayClientMessage(Component.translatable(messageKey), true);
        player.setItemInHand(hand, newStack);
        newStack.setDamageValue(damage);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide() && player.isShiftKeyDown()) {
            PlayerSpells spells = player.getData(AttachmentInit.SPELLS);
            if (player.getAbilities().instabuild) {
                switchStaff(player, hand, ItemInit.STAFF_BULLET, "item.goblinsanddungeons.current_spell_soul_bullet");
            } else if (spells.knowsSoulBullet()) {
                switchStaff(player, hand, ItemInit.STAFF_BULLET, "item.goblinsanddungeons.current_spell_soul_bullet");
            } else if (spells.knowsSoulJump()) {
                switchStaff(player, hand, ItemInit.STAFF_JUMP, "item.goblinsanddungeons.current_spell_soul_jump");
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (GoblinsDungeonsConfig.magicalWorld) {
            tooltipComponents.add(Component.translatable("active_spell").withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("no_spell").withStyle(ChatFormatting.GRAY));
        }
    }
}
