package superlord.goblinsanddungeons.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import superlord.goblinsanddungeons.config.GoblinsDungeonsConfig;
import superlord.goblinsanddungeons.init.AttachmentInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.ParticleInit;
import superlord.goblinsanddungeons.init.SoundInit;
import superlord.goblinsanddungeons.magic.PlayerMana;

import java.util.List;

/** The staff of the Soul Jump spell: a hop that costs 2 mana and 1 durability. */
public class JumpStaffItem extends Item {

    public static final int MANA_COST = 2;

    public JumpStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        PlayerMana mana = player.getData(AttachmentInit.MANA);
        if ((mana.mana() > 1 || player.isCreative()) && !player.isShiftKeyDown()) {
            // The push is applied on both sides, as the client moves the player; the rest is the server's.
            Vec3 motion = player.getDeltaMovement();
            player.setDeltaMovement(motion.x, motion.y + 0.5F, motion.z);
            if (!world.isClientSide()) {
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundInit.SOUL_BULLET_LAUNCH.get(), SoundSource.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                AreaEffectCloud cloud = new AreaEffectCloud(world, player.getX(), player.getY(), player.getZ());
                cloud.setRadius(1F);
                cloud.setRadiusOnUse(-0.5F);
                cloud.setWaitTime(1);
                cloud.setDuration(4);
                cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
                cloud.setParticle(ParticleInit.SOUL_BULLET.get());
                world.addFreshEntity(cloud);
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.isCreative()) {
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                    player.setData(AttachmentInit.MANA, mana.sub(MANA_COST));
                }
            }
        }
        if (player.isShiftKeyDown() && !world.isClientSide()) {
            StaffItem.switchStaff(player, hand, ItemInit.STAFF_AMETHYST, "item.goblinsanddungeons.current_spell_no_spell");
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (GoblinsDungeonsConfig.magicalWorld) {
            tooltipComponents.add(Component.translatable("active_spell").withStyle(ChatFormatting.GRAY));
            tooltipComponents.add(Component.translatable("soul_jump").withStyle(ChatFormatting.BLUE));
        }
    }
}
