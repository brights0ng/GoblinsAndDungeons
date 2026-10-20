package superlord.goblinsanddungeons.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import superlord.goblinsanddungeons.init.AttachmentInit;

/** Drinking it restores 4 mana (and leaves an empty bottle behind), unless the player is in creative. */
public class SoulBottleItem extends Item {

    public static final int MANA_RESTORED = 4;

    public SoulBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        super.finishUsingItem(stack, level, entity);
        if (!stack.has(DataComponents.FOOD) && entity instanceof Player player) {
            if (!player.isCreative()) {
                stack.shrink(1);
                if (!level.isClientSide()) {
                    player.setData(AttachmentInit.MANA, player.getData(AttachmentInit.MANA).add(MANA_RESTORED));
                }
            }
        }
        return stack.isEmpty() ? new ItemStack(Items.GLASS_BOTTLE) : stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
