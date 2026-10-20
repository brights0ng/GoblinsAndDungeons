package superlord.goblinsanddungeons.common.util;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class MagicNullificationEffect extends MobEffect {
    public MagicNullificationEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int k = 50 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        } else {
            return true;
        }
    }

    /**
     * 1.18.2 parity: the original drained 20 mana from a freshly created, throwaway {@code PlayerMana}, so it never
     * touched the affected player's mana, and this does nothing for the same reason. To make the effect really drain
     * mana, subtract from the player's attachment here:
     * {@code player.setData(AttachmentInit.MANA, player.getData(AttachmentInit.MANA).sub(20))}.
     */
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }

}
