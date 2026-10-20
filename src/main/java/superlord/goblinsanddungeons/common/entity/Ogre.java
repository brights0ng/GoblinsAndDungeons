package superlord.goblinsanddungeons.common.entity;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import superlord.goblinsanddungeons.init.EntityInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.SoundInit;

public class Ogre extends Goblin {

    private static final UniformInt ANGER_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    private int angerTime;
    private UUID angerTarget;
    private int attackTimer;
    private static final EntityDataAccessor<Boolean> ROARING = SynchedEntityData.defineId(Ogre.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BUTT_SMASH = SynchedEntityData.defineId(Ogre.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CAN_BUTT_SMASH = SynchedEntityData.defineId(Ogre.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CAN_ROAR = SynchedEntityData.defineId(Ogre.class, EntityDataSerializers.BOOLEAN);
    private int roarTicks = 1800;
    private int buttSmashTicks = 900;

    public Ogre(EntityType<? extends Ogre> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new Ogre.AttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Raider.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(1, new Ogre.RoarGoal());
        this.targetSelector.addGoal(1, new Ogre.ButtSmashGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, (double) 0.15F)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 25.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 25.0D)
                .add(Attributes.STEP_HEIGHT, 1.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundInit.OGRE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundInit.OGRE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.OGRE_DEATH.get();
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    public boolean isRoaring() {
        return this.entityData.get(ROARING);
    }

    private void setRoaring(boolean isRoaring) {
        this.entityData.set(ROARING, isRoaring);
    }

    public boolean isFallingOnButt() {
        return this.entityData.get(BUTT_SMASH);
    }

    private void setFallingOnButt(boolean buttSmash) {
        this.entityData.set(BUTT_SMASH, buttSmash);
    }

    public boolean canRoar() {
        return this.entityData.get(CAN_ROAR);
    }

    private void setCanRoar(boolean canRoar) {
        this.entityData.set(CAN_ROAR, canRoar);
    }

    public boolean canButtSmash() {
        return this.entityData.get(CAN_BUTT_SMASH);
    }

    private void setCanButtSmash(boolean canButtSmash) {
        this.entityData.set(CAN_BUTT_SMASH, canButtSmash);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ROARING, false);
        builder.define(BUTT_SMASH, false);
        builder.define(CAN_ROAR, false);
        builder.define(CAN_BUTT_SMASH, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsRoaring", this.isRoaring());
        compound.putBoolean("ButtSmash", this.isFallingOnButt());
        compound.putBoolean("CanRoar", this.canRoar());
        compound.putBoolean("CanButtSmash", this.canButtSmash());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setRoaring(compound.getBoolean("IsRoaring"));
        this.setFallingOnButt(compound.getBoolean("ButtSmash"));
        this.setCanButtSmash(compound.getBoolean("CanButtSmash"));
        this.setCanRoar(compound.getBoolean("CanRoar"));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getTarget() != null) {
            ++this.attackTimer;
        }
        if (this.attackTimer > 200 || this.getTarget() == null) {
            this.attackTimer = 0;
        }
        if (buttSmashTicks > 0) {
            buttSmashTicks--;
        }
        if (roarTicks > 0) {
            roarTicks--;
        }
    }

    public int getAttackTimer() {
        return this.attackTimer;
    }

    public void startPersistentAngerTimer() {
        this.setAngerTime(ANGER_RANGE.sample(this.random));
    }

    public void setAngerTime(int time) {
        this.angerTime = time;
    }

    public int getAngerTime() {
        return this.angerTime;
    }

    public void setAngerTarget(@Nullable UUID target) {
        this.angerTarget = target;
    }

    public UUID getAngerTarget() {
        return this.angerTarget;
    }

    private float getAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        this.level().broadcastEntityEvent(this, (byte) 4);
        float f = this.getAttackDamage();
        float f1 = (int) f > 0 ? f / 2.0F + (float) this.random.nextInt((int) f) : f;
        DamageSource damageSource = this.damageSources().mobAttack(this);
        boolean flag = entityIn.hurt(damageSource, f1);
        if (flag) {
            entityIn.setDeltaMovement(entityIn.getDeltaMovement().add(0.0D, (double) 0.4F, 0.0D));
            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffects(serverLevel, entityIn, damageSource);
            }
        }
        return flag;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, (double) (0.875F * this.getEyeHeight()), (double) (this.getBbWidth() * 0.4F));
    }

    @Override
    public void tick() {
        super.tick();
        if (roarTicks == 0) {
            this.setCanRoar(true);
        }
        if (buttSmashTicks == 0) {
            this.setCanButtSmash(true);
        }
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ItemInit.OGRE_SPAWN_EGG.get());
    }

    class AttackGoal extends MeleeAttackGoal {

        public AttackGoal(PathfinderMob creature, double speedIn, boolean useLongMemory) {
            super(creature, speedIn, useLongMemory);
        }

        @Override
        public boolean canUse() {
            return Ogre.this.getTarget() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return Ogre.this.getTarget() != null && !Ogre.this.canButtSmash() && !Ogre.this.canRoar();
        }
    }

    class RoarGoal extends Goal {
        int timer = 0;

        @Override
        public boolean canUse() {
            return Ogre.this.getTarget() != null && Ogre.this.canRoar();
        }

        @Override
        public void tick() {
            timer++;
            if (timer == 50) {
                this.stop();
            }
        }

        @Override
        public void start() {
            Ogre.this.setRoaring(true);
            LivingEntity entity = Ogre.this.getTarget();
            Ogre.this.level().playSound((Player) null, Ogre.this.blockPosition(), SoundInit.OGRE_ROAR.get(), SoundSource.HOSTILE, 2, 1);
            Ogre.this.playSound(SoundInit.OGRE_ROAR.get(), 1, 1);
            if (entity != null) {
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300, 1));
            }
        }

        @Override
        public boolean canContinueToUse() {
            return Ogre.this.getTarget() != null && Ogre.this.canRoar();
        }

        @Override
        public void stop() {
            super.stop();
            Ogre.this.setRoaring(false);
            Ogre.this.roarTicks = 1800;
            timer = 0;
        }
    }

    class ButtSmashGoal extends Goal {
        Boolean hasJumped = false;
        Ogre ogre;
        int timer;

        public ButtSmashGoal(Ogre ogre) {
            this.ogre = ogre;
        }

        @Override
        public boolean canUse() {
            return ogre.getTarget() != null && ogre.canButtSmash();
        }

        @Override
        public boolean canContinueToUse() {
            return ogre.getTarget() != null && ogre.canButtSmash();
        }

        @Override
        public void tick() {
            timer++;
            if (ogre.onGround() && !this.hasJumped) {
                ogre.setDeltaMovement(0, 0.5, 0);
                this.hasJumped = true;
            }
            double perpFacing = ogre.yo * (Math.PI / 180);
            double facingAngle = perpFacing + Math.PI / 2;
            int hitY = Mth.floor(ogre.getBoundingBox().minY - 0.5);
            int tick = timer;
            final int maxDistance = 6;
            ServerLevel world = (ServerLevel) ogre.level();
            if (tick > 0) {
                int distance = tick / 2 - 2;
                double spread = Math.PI * 2;
                int arcLen = Mth.ceil(distance * spread);
                double minY = ogre.getBoundingBox().minY;
                double maxY = ogre.getBoundingBox().maxY;
                for (int i = 0; i < arcLen; i++) {
                    double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
                    double vx = Math.cos(theta);
                    double vz = Math.sin(theta);
                    double px = ogre.getX() + vx * distance;
                    double pz = ogre.getZ() + vz * distance;
                    float factor = 1 - distance / (float) maxDistance;
                    AABB selection = new AABB(px - 1.5, minY, pz - 1.5, px + 1.5, maxY, pz + 1.5);
                    List<Entity> hit = world.getEntitiesOfClass(Entity.class, selection);
                    for (Entity entity : hit) {
                        if (entity == this.ogre || entity instanceof GDFallingBlock) {
                            continue;
                        }
                        float applyKnockbackResistance = 0;
                        if (entity instanceof LivingEntity living) {
                            living.hurt(this.ogre.damageSources().mobAttack(this.ogre), (float) ((factor * 5 + 1) * ogre.getAttributeValue(Attributes.ATTACK_DAMAGE)));
                            applyKnockbackResistance = (float) living.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
                        }
                        double magnitude = world.random.nextDouble() * 0.15 + 0.1;
                        float x = 0, y = 0, z = 0;
                        x += vx * factor * magnitude * (1 - applyKnockbackResistance);
                        if (entity.onGround()) {
                            y += 0.1 * (1 - applyKnockbackResistance) + factor * 0.15 * (1 - applyKnockbackResistance);
                        }
                        z += vz * factor * magnitude * (1 - applyKnockbackResistance);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(x, y, z));
                        if (entity instanceof ServerPlayer serverPlayer) {
                            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(entity));
                        }
                        if (world.random.nextBoolean()) {
                            int hitX = Mth.floor(px);
                            int hitZ = Mth.floor(pz);
                            BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                            BlockPos abovePos = pos.above();
                            BlockState block = world.getBlockState(pos);
                            BlockState blockAbove = world.getBlockState(abovePos);
                            if (!block.isAir() && block.isRedstoneConductor(world, pos) && !block.hasBlockEntity() && !blockAbove.blocksMotion()) {
                                GDFallingBlock fallingBlock = new GDFallingBlock(EntityInit.FALLING_BLOCK.get(), world, block, (float) (0.4 + factor * 0.2));
                                fallingBlock.setPos(hitX + 0.5, hitY + 1, hitZ + 0.5);
                                world.addFreshEntity(fallingBlock);
                            }
                        }
                    }
                }
            }
            if (!ogre.onGround()) {
                ogre.setFallingOnButt(true);
            } else {
                ogre.setFallingOnButt(false);
                this.stop();
            }
        }

        @Override
        public void stop() {
            super.stop();
            ogre.buttSmashTicks = 900;
            ogre.setCanButtSmash(false);
        }
    }
}
