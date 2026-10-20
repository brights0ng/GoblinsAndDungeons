package superlord.goblinsanddungeons.common;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.init.ItemInit;

/** The effects of the off-hand rings that act on kills (the 1.18.2 {@code CommonEvents.onEntityDeath}). */
@EventBusSubscriber(modid = GoblinsAndDungeons.MOD_ID)
public class RingEvents {

    /**
     * How much the Ring of Glory heals for a kill, by the victim's max health.
     *
     * <p>1.18.2 parity: its table has a gap, so a victim with more than 60 and at most 75 max health heals nothing (the
     * ring is still worn down). Every other band is one heart per 15 max health, and anything over 300 heals 20.
     */
    public static int gloryHeal(double victimMaxHealth) {
        if (victimMaxHealth <= 60.0D) {
            return (int) Math.max(1, Math.ceil(victimMaxHealth / 15.0D));
        }
        if (victimMaxHealth <= 75.0D) {
            return 0;
        }
        if (victimMaxHealth > 300.0D) {
            return 20;
        }
        return (int) Math.ceil(victimMaxHealth / 15.0D) - 1;
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        if (source.getDirectEntity() instanceof Player player && !player.level().isClientSide()) {
            ItemStack stack = player.getItemBySlot(EquipmentSlot.OFFHAND);
            if (stack.is(ItemInit.RING_OF_GLORY.get()) && !player.isCreative()) {
                LivingEntity victim = event.getEntity();
                player.heal(gloryHeal(victim.getMaxHealth()));
                stack.hurtAndBreak(1, player, EquipmentSlot.OFFHAND);
            }
        }
    }

    /** Ring of Experience: one more experience point for each kill. */
    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        if (player != null && player.getItemBySlot(EquipmentSlot.OFFHAND).is(ItemInit.RING_OF_EXPERIENCE.get())) {
            event.setDroppedExperience(event.getDroppedExperience() + 1);
        }
    }
}
