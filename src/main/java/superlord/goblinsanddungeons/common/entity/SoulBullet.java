package superlord.goblinsanddungeons.common.entity;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import superlord.goblinsanddungeons.init.EntityInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.ParticleInit;
import superlord.goblinsanddungeons.init.SoundInit;

public class SoulBullet extends ThrowableItemProjectile {

    public SoulBullet(EntityType<? extends SoulBullet> type, Level level) {
        super(type, level);
    }

    public SoulBullet(Level level, LivingEntity thrower) {
        super(EntityInit.SOUL_BULLET.get(), thrower, level);
    }

    public SoulBullet(Level level, double x, double y, double z) {
        super(EntityInit.SOUL_BULLET.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.SOUL_BULLET.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.spawnParticles(8);
            if (this.tickCount >= 300) {
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    private void spawnParticles(int particleCount) {
        this.level().addParticle(ParticleInit.SOUL_BULLET.get(), true, this.getX() - 0.1D, this.getY() - 1.0D, this.getZ() - 0.1D, 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0D;
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hitEntity = result.getEntity();
        if (hitEntity instanceof LivingEntity living) {
            living.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 4.0F);
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
        }
        this.level().playSound((Player) null, this.blockPosition(), SoundInit.SOUL_BULLET_COLLISION.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        this.playSound(SoundInit.SOUL_BULLET_COLLISION.get(), 1.0F, 1.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().playSound((Player) null, this.blockPosition(), SoundInit.SOUL_BULLET_COLLISION.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            this.playSound(SoundInit.SOUL_BULLET_COLLISION.get(), 1.0F, 1.0F);
            this.remove(RemovalReason.DISCARDED);
        }
    }
}
