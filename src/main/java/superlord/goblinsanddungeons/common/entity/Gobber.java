package superlord.goblinsanddungeons.common.entity;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.HitResult;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.SoundInit;

public class Gobber extends Goblin implements CrossbowAttackMob {

    private static final EntityDataAccessor<Boolean> HAS_CROSSBOW = SynchedEntityData.defineId(Gobber.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Gobber.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_CHARGING_STATE = SynchedEntityData.defineId(Gobber.class, EntityDataSerializers.BOOLEAN);

    public Gobber(EntityType<? extends Gobber> type, Level worldIn) {
        super(type, worldIn);
        this.reassessWeaponGoal();
    }

    public boolean hasCrossbow() {
        return this.entityData.get(HAS_CROSSBOW);
    }

    public AbstractIllager.IllagerArmPose getArmPose() {
        if (this.isCharging()) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(is -> is.getItem() instanceof CrossbowItem)) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_HOLD;
        } else {
            return this.isAggressive() ? AbstractIllager.IllagerArmPose.ATTACKING : AbstractIllager.IllagerArmPose.NEUTRAL;
        }
    }

    private void setHasCrossbow(boolean hasCrossbow) {
        this.entityData.set(HAS_CROSSBOW, hasCrossbow);
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    private void setSleeping(boolean isSleeping) {
        this.entityData.set(SLEEPING, isSleeping);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new Gobber.SleepGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Raider.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CHARGING_STATE, false);
        builder.define(HAS_CROSSBOW, false);
        builder.define(SLEEPING, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, (double) 0.15F)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ARMOR, 5.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 7.5D)
                .add(Attributes.FOLLOW_RANGE, 25.0D);
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence();
    }

    protected void updateAITasks() {
        if (this.isSleeping()) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double) 0.15F);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
        this.populateDefaultEquipmentSlots(worldIn.getRandom(), difficultyIn);
        this.reassessWeaponGoal();
        return spawnDataIn;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        int i = this.random.nextInt(3);
        if (i == 0) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
            this.setHasCrossbow(true);
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }
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

    public void reassessWeaponGoal() {
        if (this.level() != null && !this.level().isClientSide) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemstack.getItem() == Items.CROSSBOW) {
                this.goalSelector.addGoal(0, new RangedCrossbowAttackGoal<>(this, 1.0D, 8.0F));
            }
            if (itemstack.getItem() != Items.CROSSBOW) {
                this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, true));
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        this.performCrossbowAttack(this, 1.6F);
    }

    protected AbstractArrow getArrow(ItemStack arrowStack, float distanceFactor, @Nullable ItemStack weapon) {
        return ProjectileUtil.getMobArrow(this, arrowStack, distanceFactor, weapon);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem weapon) {
        return weapon == Items.CROSSBOW;
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ItemInit.GOBBER_SPAWN_EGG.get());
    }

    public boolean isCharging() {
        return this.entityData.get(DATA_CHARGING_STATE);
    }

    @Override
    public void setChargingCrossbow(boolean isCharging) {
        this.entityData.set(DATA_CHARGING_STATE, isCharging);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return SoundInit.GOBBER_SNORING.get();
        } else {
            return SoundInit.GOBBER_IDLE.get();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundInit.GOBBER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.GOBBER_DEATH.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsSleeping", this.isSleeping());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.reassessWeaponGoal();
        this.setSleeping(compound.getBoolean("IsSleeping"));
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
        super.setItemSlot(slotIn, stack);
        if (!this.level().isClientSide) {
            this.reassessWeaponGoal();
        }
    }

    class SleepGoal extends Goal {
        Gobber gobber;

        public SleepGoal(Gobber gobber) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
            this.gobber = gobber;
        }

        @Override
        public boolean canUse() {
            List<Player> players = Gobber.this.level().getEntitiesOfClass(Player.class, Gobber.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            List<AbstractIllager> illagers = Gobber.this.level().getEntitiesOfClass(AbstractIllager.class, Gobber.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            List<AbstractVillager> villagers = Gobber.this.level().getEntitiesOfClass(AbstractVillager.class, Gobber.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            List<AbstractGolem> golems = Gobber.this.level().getEntitiesOfClass(AbstractGolem.class, Gobber.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            return players.isEmpty() && illagers.isEmpty() && villagers.isEmpty() && golems.isEmpty();
        }

        @Override
        public boolean canContinueToUse() {
            List<Player> players = Gobber.this.level().getEntitiesOfClass(Player.class, Gobber.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            List<AbstractIllager> illagers = Gobber.this.level().getEntitiesOfClass(AbstractIllager.class, Gobber.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            List<AbstractVillager> villagers = Gobber.this.level().getEntitiesOfClass(AbstractVillager.class, Gobber.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            List<AbstractGolem> golems = Gobber.this.level().getEntitiesOfClass(AbstractGolem.class, Gobber.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            return players.isEmpty() && illagers.isEmpty() && villagers.isEmpty() && golems.isEmpty();
        }

        @Override
        public void start() {
            gobber.setTarget(null);
            gobber.setSleeping(true);
            gobber.getNavigation().stop();
            gobber.setDeltaMovement(0.0D, 0.0D, 0.0D);
            gobber.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
        }

        @Override
        public void stop() {
            super.stop();
            gobber.setSleeping(false);
            gobber.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double) 0.2F);
        }
    }
}
