package superlord.goblinsanddungeons.common.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import superlord.goblinsanddungeons.init.AttachmentInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.magic.PlayerSpells;

/** A tome that teaches the player a spell, and is used up doing so (unless the player is in creative). */
public class SpellTomeItem extends Item {

    public SpellTomeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide()) {
            PlayerSpells spells = player.getData(AttachmentInit.SPELLS);
            if (stack.is(ItemInit.SOUL_BULLET_SPELL_TOME.get()) && !spells.knowsSoulBullet()) {
                player.setData(AttachmentInit.SPELLS, spells.withSoulBullet(true));
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            } else if (stack.is(ItemInit.SOUL_JUMP_SPELL_TOME.get()) && !spells.knowsSoulJump()) {
                player.setData(AttachmentInit.SPELLS, spells.withSoulJump(true));
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
}
