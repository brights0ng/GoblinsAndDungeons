package superlord.goblinsanddungeons.common.entity;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.SoundInit;

public class Mimic extends Goblin {
    private static final EntityDataAccessor<Boolean> HIDING = SynchedEntityData.defineId(Mimic.class, EntityDataSerializers.BOOLEAN);

    public Mimic(EntityType<? extends Mimic> type, Level level) {
        super(type, level);
    }

    public boolean isHiding() {
        return this.entityData.get(HIDING);
    }

    private void setHiding(boolean isHiding) {
        this.entityData.set(HIDING, isHiding);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new Mimic.HidingLookGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, true));
        this.targetSelector.addGoal(2, new Mimic.AttackNearestGoal(this));
        this.goalSelector.addGoal(0, new Mimic.HideGoal());
        this.targetSelector.addGoal(1, new Mimic.DefendGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HIDING, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, (double) 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setHiding(compound.getBoolean("IsHiding"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsHiding", this.isHiding());
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide) {
            this.setHiding(false);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void push(Entity entity) {
        if (this.isHiding()) {
            super.push(entity);
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        spawnData = super.finalizeSpawn(worldIn, difficulty, reason, spawnData);
        this.setHiding(true);
        return spawnData;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.isHiding()) {
            return SoundInit.MIMIC_IDLE.get();
        } else {
            return null;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundInit.MIMIC_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.MIMIC_DEATH.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (super.hurt(source, amount)) {
            this.setHiding(false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ItemInit.MIMIC_SPAWN_EGG.get());
    }

    class DefendGoal extends HurtByTargetGoal {

        public DefendGoal(PathfinderMob creatureIn) {
            super(creatureIn);
        }

        @Override
        public void start() {
            super.start();
            Mimic.this.setHiding(false);
        }
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.yBodyRot = 0.0F;
        this.yBodyRotO = 0.0F;
    }

    class AttackNearestGoal extends NearestAttackableTargetGoal<Player> {
        public AttackNearestGoal(Mimic mimic) {
            super(mimic, Player.class, true);
        }

        @Override
        public boolean canUse() {
            return !Mimic.this.isHiding() && super.canUse();
        }

        @Override
        public void start() {
            super.start();
            Mimic.this.setHiding(false);
        }

        @Override
        public void stop() {
            super.stop();
        }
    }

    class HidingLookGoal extends LookAtPlayerGoal {

        public HidingLookGoal(Mob entityIn, Class<? extends Player> watchTargetClass, float maxDistance) {
            super(entityIn, watchTargetClass, maxDistance);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !Mimic.this.isHiding();
        }
    }

    class HideGoal extends Goal {

        @Override
        public boolean canUse() {
            return Mimic.this.getTarget() == null;
        }

        public void startExecuting() {
            Mimic.this.setHiding(true);
        }
    }
}
