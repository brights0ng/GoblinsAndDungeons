package superlord.goblinsanddungeons.common.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import superlord.goblinsanddungeons.init.EntityInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.ParticleInit;
import superlord.goblinsanddungeons.init.SoundInit;

public class TeleportationOrb extends ThrowableItemProjectile {

    public TeleportationOrb(EntityType<? extends TeleportationOrb> type, Level level) {
        super(type, level);
    }

    public TeleportationOrb(Level level, LivingEntity thrower) {
        super(EntityInit.TELEPORTATION_ORB.get(), thrower, level);
    }

    public TeleportationOrb(Level level, double x, double y, double z) {
        super(EntityInit.TELEPORTATION_ORB.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemInit.TELEPORTATION_ORB.get();
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
        this.level().addParticle(ParticleInit.TELEPORTATION_ORB.get(), true, this.getX() - 0.1D, this.getY() - 1.0D, this.getZ() - 0.1D, 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0D;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity hitEntity = result.getEntity();
        if (hitEntity instanceof LivingEntity entity) {
            entity.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 4.0F);
            Level level = entity.level();
            if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
                double d0 = entity.getX();
                double d1 = entity.getY();
                double d2 = entity.getZ();
                for (int i = 0; i < 16; ++i) {
                    double d3 = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
                    double d4 = Mth.clamp(
                            hitEntity.getY() + (double) (entity.getRandom().nextInt(16) - 8),
                            (double) level.getMinBuildHeight(),
                            (double) (level.getMinBuildHeight() + serverLevel.getLogicalHeight() - 1)
                    );
                    double d5 = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
                    if (entity.isPassenger()) {
                        entity.stopRiding();
                    }
                    EntityTeleportEvent.ChorusFruit event = EventHooks.onChorusFruitTeleport(entity, d3, d4, d5);
                    if (event.isCanceled()) {
                        break;
                    }
                    if (entity.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true)) {
                        SoundEvent soundevent = entity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                        level.playSound((Player) null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.playSound(soundevent, 1.0F, 1.0F);
                        break;
                    }
                }
            }
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
            this.remove(RemovalReason.DISCARDED);
        }
    }
}
