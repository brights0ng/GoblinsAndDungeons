package superlord.goblinsanddungeons.common.entity;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import superlord.goblinsanddungeons.common.entity.ai.GoomSmokeGoal;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.SoundInit;

public class Goom extends Goblin {
    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(Goom.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(Goom.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IGNITED = SynchedEntityData.defineId(Goom.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BLOWN = SynchedEntityData.defineId(Goom.class, EntityDataSerializers.BOOLEAN);
    @SuppressWarnings("unused")
    private int lastActiveTime;
    private int timeSinceIgnited;
    private int fuseTime = 30;
    private int timeTillBomb = 0;

    public Goom(EntityType<? extends Goom> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(1, new GoomSmokeGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Raider.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, (double) 0.275F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 25.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundInit.GOOM_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundInit.GOOM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.GOOM_DEATH.get();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag) {
            float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                entityIn.igniteForSeconds(2 * (int) f);
            }
        }
        return flag;
    }

    public boolean isBlownUp() {
        return this.entityData.get(BLOWN);
    }

    public void setBlownUp(boolean isBlownUp) {
        this.entityData.set(BLOWN, isBlownUp);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIMBING, (byte) 0);
        builder.define(STATE, -1);
        builder.define(IGNITED, false);
        builder.define(BLOWN, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            this.lastActiveTime = this.timeSinceIgnited;
            if (this.hasIgnited()) {
                this.setGoomState(1);
            }

            int i = this.getGoomState();
            if (i > 0 && this.timeSinceIgnited == 0) {
                this.playSound(SoundInit.GOOM_WARNING.get(), 1.0F, 0.5F);
            }

            this.timeSinceIgnited += i;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;
                if (!this.isBlownUp()) {
                    this.explode();
                }
            }
            if (this.isBlownUp()) {
                this.timeTillBomb++;
                if (this.timeTillBomb >= 6000) {
                    this.ignite(false);
                    this.setBlownUp(false);
                    BlockPos pos = this.blockPosition();
                    this.level().playSound(null, pos, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.HOSTILE, 0.3F, 0.5F);
                }
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBlownUp(compound.getBoolean("IsBlownUp"));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        return super.finalizeSpawn(worldIn, difficulty, reason, spawnData);
    }

    public int getGoomState() {
        return this.entityData.get(STATE);
    }

    public void setGoomState(int state) {
        this.entityData.set(STATE, state);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            this.level().playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level().isClientSide) {
                this.ignite(true);
                itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    private void explode() {
        if (!this.level().isClientSide) {
            AreaEffectCloud areaeffectcloudentity = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            areaeffectcloudentity.setRadius(2.5F);
            areaeffectcloudentity.setRadiusOnUse(-0.5F);
            areaeffectcloudentity.setWaitTime(10);
            areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
            areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float) areaeffectcloudentity.getDuration());
            areaeffectcloudentity.setParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE);
            this.setBlownUp(true);
            BlockPos pos = this.blockPosition();
            this.level().playSound(null, pos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 0.3F, 0.5F);
            this.level().addFreshEntity(areaeffectcloudentity);
        }
    }

    public boolean hasIgnited() {
        return this.entityData.get(IGNITED);
    }

    public void ignite(boolean hasIgnited) {
        this.entityData.set(IGNITED, hasIgnited);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ItemInit.GOOM_SPAWN_EGG.get());
    }

    public class PostExplosionAttackGoal extends MeleeAttackGoal {

        public PostExplosionAttackGoal(PathfinderMob creature, double speedIn, boolean useLongMemory) {
            super(creature, speedIn, useLongMemory);
        }

        @Override
        public boolean canUse() {
            return Goom.this.isBlownUp() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return Goom.this.isBlownUp() && super.canContinueToUse();
        }
    }
}
