package superlord.goblinsanddungeons.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import superlord.goblinsanddungeons.common.entity.SoulBullet;
import superlord.goblinsanddungeons.config.GoblinsDungeonsConfig;
import superlord.goblinsanddungeons.init.AttachmentInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.SoundInit;
import superlord.goblinsanddungeons.magic.PlayerMana;

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

        PlayerMana mana = player.getData(AttachmentInit.MANA);
        if (mana.mana() > 0 || player.isCreative()) {
            world.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundInit.SOUL_BULLET_LAUNCH.get(), SoundSource.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!world.isClientSide()) {
                SoulBullet soulBullet = new SoulBullet(world, player);
                soulBullet.getItem();
                soulBullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                world.addFreshEntity(soulBullet);
                if (!player.isCreative()) {
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                    player.setData(AttachmentInit.MANA, mana.sub(1));
                }
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
        }

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
