package superlord.goblinsanddungeons.common.entity;

import net.minecraft.sounds.SoundSource;
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

public class ExplosiveOrb extends ThrowableItemProjectile {

    public ExplosiveOrb(EntityType<? extends ExplosiveOrb> type, Level level) {
        super(type, level);
    }

    public ExplosiveOrb(Level level, LivingEntity thrower) {
        super(EntityInit.EXPLOSIVE_ORB.get(), thrower, level);
    }

    public ExplosiveOrb(Level level, double x, double y, double z) {
        super(EntityInit.EXPLOSIVE_ORB.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.EXPLOSIVE_ORB.get();
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
        this.level().addParticle(ParticleInit.BLINDNESS_ORB.get(), true, this.getX() - 0.1D, this.getY() - 1.0D, this.getZ() - 0.1D, 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0D;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hitEntity = result.getEntity();
        if (hitEntity instanceof LivingEntity living) {
            living.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 4.0F);
            this.level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 4.0F, Level.ExplosionInteraction.NONE);
        }
        this.level().playSound((Player) null, this.blockPosition(), SoundInit.SOUL_BULLET_COLLISION.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
        this.playSound(SoundInit.SOUL_BULLET_COLLISION.get(), 1.0F, 1.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().playSound((Player) null, this.blockPosition(), SoundInit.SOUL_BULLET_COLLISION.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
            this.playSound(SoundInit.SOUL_BULLET_COLLISION.get(), 1.0F, 1.0F);
            this.level().explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 4.0F, Level.ExplosionInteraction.NONE);
            this.remove(RemovalReason.DISCARDED);
        }
    }
}
