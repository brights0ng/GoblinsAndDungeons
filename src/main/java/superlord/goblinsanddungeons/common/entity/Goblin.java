package superlord.goblinsanddungeons.common.entity;

import javax.annotation.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import superlord.goblinsanddungeons.init.GDTags;
import superlord.goblinsanddungeons.init.ItemInit;

public class Goblin extends Monster {

    protected Goblin(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
    }

    public double getVisibilityMultiplier(@Nullable Entity lookingEntity) {
        double d0 = 1.0D;

        if (lookingEntity != null) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
            Item item = itemstack.getItem();

            ItemStack itemstack2 = this.getItemBySlot(EquipmentSlot.OFFHAND);
            Item item2 = itemstack2.getItem();

            if (lookingEntity instanceof Goblin && item == ItemInit.GOBLIN_CROWN.get()) {
                d0 *= 0.5D;
            }
            if (lookingEntity instanceof Monster && lookingEntity.isCrouching() && item2 == ItemInit.RING_OF_STEALTH.get()) {
                d0 *= 0.8D;
            }
        }

        return d0;
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof Goblin || entityIn.getType().is(GDTags.GOBLINS)) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    private static boolean isAdmiringItem(Goblin goblin) {
        return goblin.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
    }

    protected static void cancelAdmiring(Goblin goblin) {
        if (isAdmiringItem(goblin) && !goblin.getOffhandItem().isEmpty()) {
            goblin.spawnAtLocation(goblin.getOffhandItem());
            goblin.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    protected void finishConversion(ServerLevel level) {
        ZombifiedPiglin zombifiedpiglinentity = this.convertTo(EntityType.ZOMBIFIED_PIGLIN, true);
        if (zombifiedpiglinentity != null) {
            zombifiedpiglinentity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
            EventHooks.onLivingConvert(this, zombifiedpiglinentity);
        }
    }
}
