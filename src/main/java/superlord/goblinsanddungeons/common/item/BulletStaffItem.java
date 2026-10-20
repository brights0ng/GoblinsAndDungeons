package superlord.goblinsanddungeons.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import superlord.goblinsanddungeons.config.GoblinsDungeonsConfig;
import superlord.goblinsanddungeons.init.AttachmentInit;
import superlord.goblinsanddungeons.init.ItemInit;

import java.util.List;

/** The staff of the Soul Bullet spell. */
public class BulletStaffItem extends Item {

    public BulletStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (!world.isClientSide()) {
                if (player.isCreative() || player.getData(AttachmentInit.SPELLS).knowsSoulJump()) {
                    StaffItem.switchStaff(player, hand, ItemInit.STAFF_JUMP, "item.goblinsanddungeons.current_spell_soul_jump");
                } else {
                    StaffItem.switchStaff(player, hand, ItemInit.STAFF_AMETHYST, "item.goblinsanddungeons.current_spell_no_spell");
                }
            }
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
        }
        // Casting (a Soul Bullet projectile, 1 mana and 1 durability, needs mana above 0 unless creative) comes back with
        // the SoulBullet entity in phase 3; until then using the staff does nothing and costs nothing.
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (GoblinsDungeonsConfig.magicalWorld) {
            tooltipComponents.add(Component.translatable("active_spell").withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("soul_bullet").withStyle(ChatFormatting.BLUE));
        }
    }
}
