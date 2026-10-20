package superlord.goblinsanddungeons.common.entity;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.SoundInit;

public class Goblo extends Goblin implements ContainerListener {

    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Goblo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_CHICKEN = SynchedEntityData.defineId(Goblo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> STATUS = SynchedEntityData.defineId(Goblo.class, EntityDataSerializers.BYTE);
    public int eatTicks;
    private SimpleContainer inventory;
    @SuppressWarnings("unused")
    private IItemHandler itemHandler = null;

    public Goblo(EntityType<? extends Goblo> type, Level worldIn) {
        super(type, worldIn);
        this.setCanPickUpLoot(true);
        this.initInventory();
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    private void setSleeping(boolean isSleeping) {
        this.entityData.set(SLEEPING, isSleeping);
    }

    public boolean hasChicken() {
        return this.entityData.get(HAS_CHICKEN);
    }

    private void setHasChicken(boolean hasChicken) {
        this.entityData.set(HAS_CHICKEN, hasChicken);
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence();
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
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(1, new Goblo.SleepGoal(this));
        this.goalSelector.addGoal(2, new Goblo.PickUpChickenGoal(this));
        this.goalSelector.addGoal(2, new Goblo.FindItemsGoal());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Raider.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return SoundInit.GOBLO_SNORING.get();
        } else {
            return SoundInit.GOBLO_IDLE.get();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundInit.GOBLO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.GOBLO_DEATH.get();
    }

    protected void initInventory() {
        SimpleContainer oldInventory = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (oldInventory != null) {
            oldInventory.removeListener(this);
            int i = Math.min(oldInventory.getContainerSize(), this.inventory.getContainerSize());

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = oldInventory.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }

        this.inventory.addListener(this);
        this.updateInventoryStatus();
        this.itemHandler = new InvWrapper(this.inventory);
    }

    protected void updateInventoryStatus() {
        if (!this.level().isClientSide) {
            this.setWatchableBoolean(4, !this.inventory.getItem(0).isEmpty());
        }
    }

    protected boolean getWatchableBoolean(int mask) {
        return (this.entityData.get(STATUS) & mask) != 0;
    }

    protected void setWatchableBoolean(int mask, boolean value) {
        byte b0 = this.entityData.get(STATUS);
        if (value) {
            this.entityData.set(STATUS, (byte) (b0 | mask));
        } else {
            this.entityData.set(STATUS, (byte) (b0 & ~mask));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.MOVEMENT_SPEED, (double) 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 25.0D);
    }

    private void spitOutItem(ItemStack stack) {
        if (!stack.isEmpty() && !this.level().isClientSide) {
            ItemEntity itementity = new ItemEntity(this.level(), this.getX() + this.getLookAngle().x, this.getY() + 1.0D, this.getZ() + this.getLookAngle().z, stack);
            itementity.setPickUpDelay(40);
            itementity.setThrower(this);
            this.level().addFreshEntity(itementity);
        }
    }

    private void dropItemStack(ItemStack stackIn) {
        ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), stackIn);
        this.level().addFreshEntity(itementity);
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        if (this.canEquipItem(itemstack)) {
            int i = itemstack.getCount();
            if (i > 1) {
                this.dropItemStack(itemstack.split(i - 1));
            }
            this.spitOutItem(this.getItemBySlot(EquipmentSlot.MAINHAND));
            this.onItemPickup(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.split(1));
            this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
            this.take(itemEntity, itemstack.getCount());
            itemEntity.discard();
            this.eatTicks = 0;
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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STATUS, (byte) 0);
        builder.define(SLEEPING, false);
        builder.define(HAS_CHICKEN, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsSleeping", this.isSleeping());
        compound.putBoolean("HasChicken", this.hasChicken());
        ListTag listnbt = new ListTag();

        for (int i = 2; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundnbt = new CompoundTag();
                compoundnbt.putByte("Slot", (byte) i);
                listnbt.add(itemstack.save(this.registryAccess(), compoundnbt));
            }
        }

        compound.put("Items", listnbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSleeping(compound.getBoolean("IsSleeping"));
        this.setHasChicken(compound.getBoolean("HasChicken"));
        ListTag listnbt = compound.getList("Items", 10);
        this.initInventory();

        for (int i = 0; i < listnbt.size(); ++i) {
            CompoundTag compoundnbt = listnbt.getCompound(i);
            int j = compoundnbt.getByte("Slot") & 255;
            if (j >= 2 && j < this.inventory.getContainerSize()) {
                ItemStack.parse(this.registryAccess(), compoundnbt).ifPresent(stack -> this.inventory.setItem(j, stack));
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(level, source, recentlyHitIn);
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
    }

    protected ItemStack addToInventory(ItemStack stack) {
        return this.inventory.addItem(stack);
    }

    protected boolean canAddToInventory(ItemStack stack) {
        return this.inventory.canAddItem(stack);
    }

    @Override
    protected void finishConversion(ServerLevel level) {
        super.cancelAdmiring(this);
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
        super.finishConversion(level);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 45) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!itemstack.isEmpty()) {
                for (int i = 0; i < 8; ++i) {
                    Vec3 vector3d = (new Vec3(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D))
                            .xRot(-this.getXRot() * ((float) Math.PI / 180F))
                            .yRot(-this.getYRot() * ((float) Math.PI / 180F));
                    this.level().addParticle(
                            new ItemParticleOption(ParticleTypes.ITEM, itemstack),
                            this.getX() + this.getLookAngle().x / 2.0D,
                            this.getY(),
                            this.getZ() + this.getLookAngle().z / 2.0D,
                            vector3d.x,
                            vector3d.y + 0.05D,
                            vector3d.z
                    );
                }
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    protected int getInventorySize() {
        return 256;
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
    }

    @Override
    public SoundEvent getEatingSound(ItemStack itemStackIn) {
        return SoundInit.GOBLO_EATING.get();
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide && this.isAlive()) {
            ++this.eatTicks;
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
            ItemStack air = Items.AIR.getDefaultInstance();
            if (this.canEatItem(itemstack)) {
                if (this.eatTicks > 100 && itemstack.has(DataComponents.FOOD)) {
                    if (itemstack.getItem() == Items.BEETROOT) {
                        ItemStack itemstack1 = itemstack.finishUsingItem(this.level(), this);
                        if (!itemstack1.isEmpty()) {
                            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack1);
                        }
                        this.setSleeping(true);
                    }
                    this.eatTicks = 0;
                    itemstack.shrink(1);
                } else if (this.eatTicks > 100) {
                    ItemStack itemstack1 = itemstack.finishUsingItem(this.level(), this);
                    if (!itemstack1.isEmpty()) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, itemstack1);
                    }
                    this.inventory.addItem(itemstack);
                    this.setItemSlot(EquipmentSlot.MAINHAND, air);
                    this.eatTicks = 0;
                } else if (this.eatTicks > 60 && this.random.nextFloat() < 0.1F && this.hasItemInSlot(EquipmentSlot.MAINHAND)) {
                    this.playSound(this.getEatingSound(itemstack), 1.0F, 1.0F);
                    this.level().broadcastEntityEvent(this, (byte) 45);
                }
            }
        }
        if (!this.isVehicle()) {
            this.setHasChicken(false);
        }
        Vec3 vector3d = this.getDeltaMovement();

        if (!this.onGround() && vector3d.y < 0.0D && this.hasChicken()) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.6D, 1.0D));
        }

        if (this.isSleeping()) {
            this.setDeltaMovement(vector3d.multiply(0.0D, 0.0D, 0.0D));
        }
        super.aiStep();
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        if (this.hasChicken()) {
            return false;
        } else {
            return super.causeFallDamage(distance, damageMultiplier, this.damageSources().fall());
        }
    }

    private boolean canEatItem(ItemStack itemStackIn) {
        return itemStackIn.getItem() != Items.DIAMOND
                && itemStackIn.getItem() != Items.DIAMOND_AXE
                && itemStackIn.getItem() != Items.DIAMOND_BLOCK
                && itemStackIn.getItem() != Items.DIAMOND_BOOTS
                && itemStackIn.getItem() != Items.DIAMOND_CHESTPLATE
                && itemStackIn.getItem() != Items.DIAMOND_HELMET
                && itemStackIn.getItem() != Items.DIAMOND_HORSE_ARMOR
                && itemStackIn.getItem() != Items.DIAMOND_LEGGINGS
                && itemStackIn.getItem() != Items.DIAMOND_ORE
                && itemStackIn.getItem() != Items.DIAMOND_PICKAXE
                && itemStackIn.getItem() != Items.DIAMOND_SHOVEL
                && itemStackIn.getItem() != Items.DIAMOND_SWORD
                && itemStackIn.getItem() != Items.WITHER_ROSE
                && itemStackIn.getItem() != Items.WITHER_SKELETON_SKULL
                && itemStackIn.getItem() != Items.NETHER_STAR
                && itemStackIn.getItem() != Items.BEACON
                && itemStackIn.getItem() != Items.DRAGON_HEAD
                && itemStackIn.getItem() != Items.DRAGON_EGG
                && itemStackIn.getItem() != Items.CREEPER_HEAD
                && itemStackIn.getItem() != Items.PLAYER_HEAD
                && itemStackIn.getItem() != Items.ZOMBIE_HEAD
                && itemStackIn.getItem() != Items.SKELETON_SKULL
                && itemStackIn.getItem() != Items.EMERALD
                && itemStackIn.getItem() != Items.EMERALD_BLOCK
                && itemStackIn.getItem() != Items.EMERALD_ORE
                && itemStackIn.getItem() != Items.ANCIENT_DEBRIS
                && itemStackIn.getItem() != Items.NETHERITE_AXE
                && itemStackIn.getItem() != Items.NETHERITE_BOOTS
                && itemStackIn.getItem() != Items.NETHERITE_BLOCK
                && itemStackIn.getItem() != Items.NETHERITE_CHESTPLATE
                && itemStackIn.getItem() != Items.NETHERITE_HELMET
                && itemStackIn.getItem() != Items.NETHERITE_HOE
                && itemStackIn.getItem() != Items.NETHERITE_INGOT
                && itemStackIn.getItem() != Items.NETHERITE_LEGGINGS
                && itemStackIn.getItem() != Items.NETHERITE_PICKAXE
                && itemStackIn.getItem() != Items.NETHERITE_SCRAP
                && itemStackIn.getItem() != Items.NETHERITE_SHOVEL
                && itemStackIn.getItem() != Items.NETHERITE_SWORD
                && itemStackIn.getItem() != Items.END_CRYSTAL
                && itemStackIn.getItem() != Items.END_PORTAL_FRAME
                && itemStackIn.getItem() != Items.BLACK_SHULKER_BOX
                && itemStackIn.getItem() != Items.BLUE_SHULKER_BOX
                && itemStackIn.getItem() != Items.BROWN_SHULKER_BOX
                && itemStackIn.getItem() != Items.CYAN_SHULKER_BOX
                && itemStackIn.getItem() != Items.GRAY_SHULKER_BOX
                && itemStackIn.getItem() != Items.GREEN_SHULKER_BOX
                && itemStackIn.getItem() != Items.LIGHT_BLUE_SHULKER_BOX
                && itemStackIn.getItem() != Items.LIGHT_GRAY_SHULKER_BOX
                && itemStackIn.getItem() != Items.LIME_SHULKER_BOX
                && itemStackIn.getItem() != Items.MAGENTA_SHULKER_BOX
                && itemStackIn.getItem() != Items.ORANGE_SHULKER_BOX
                && itemStackIn.getItem() != Items.PINK_SHULKER_BOX
                && itemStackIn.getItem() != Items.PURPLE_SHULKER_BOX
                && itemStackIn.getItem() != Items.RED_SHULKER_BOX
                && itemStackIn.getItem() != Items.SHULKER_BOX
                && itemStackIn.getItem() != Items.WHITE_SHULKER_BOX
                && itemStackIn.getItem() != Items.YELLOW_SHULKER_BOX
                && itemStackIn.getItem() != Items.ELYTRA
                && itemStackIn.getItem() != Items.HEART_OF_THE_SEA
                && itemStackIn.getItem() != Items.TOTEM_OF_UNDYING
                && this.onGround()
                && !this.isSleeping();
    }

    public boolean canEquipItem(ItemStack stack) {
        ItemStack itemstack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        return itemstack.isEmpty() && !this.hasChicken() && !this.isSleeping()
                || this.eatTicks > 0 && stack.has(DataComponents.FOOD) && !itemstack.has(DataComponents.FOOD);
    }

    class PickUpChickenGoal extends Goal {

        Goblo goblo;
        Chicken chicken;
        int ticks = 0;

        public PickUpChickenGoal(Goblo goblo) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.goblo = goblo;
        }

        @Override
        public boolean canUse() {
            return goblo.getTarget() == null && !goblo.hasChicken();
        }

        @Override
        public void tick() {
            List<Chicken> list = goblo.level().getEntitiesOfClass(Chicken.class, goblo.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            if (!list.isEmpty() && !goblo.hasChicken()) {
                goblo.getNavigation().moveTo(list.get(0), (double) 1.2F);
            }
        }

        @Override
        public void start() {
            List<Chicken> list = goblo.level().getEntitiesOfClass(Chicken.class, goblo.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            if (!list.isEmpty() && !goblo.hasChicken()) {
                goblo.getNavigation().moveTo(list.get(0), (double) 1.2F);
            }
            List<Chicken> list2 = goblo.level().getEntitiesOfClass(Chicken.class, goblo.getBoundingBox().inflate(1.0D, 1.0D, 1.0D));
            if (!list2.isEmpty() && !goblo.hasChicken()) {
                chicken = list2.get(0);
                chicken.startRiding(goblo);
                this.goblo.setHasChicken(true);
            }
            if (goblo.getTarget() != null && chicken != null) {
                chicken.stopRiding();
                this.goblo.setHasChicken(false);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return goblo.getTarget() == null;
        }
    }

    class FindItemsGoal extends Goal {
        public FindItemsGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!Goblo.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() || Goblo.this.hasChicken()) {
                return false;
            } else if (Goblo.this.getTarget() == null) {
                if (Goblo.this.getRandom().nextInt(10) != 0) {
                    return false;
                } else {
                    List<ItemEntity> list = Goblo.this.level().getEntitiesOfClass(ItemEntity.class, Goblo.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
                    return !list.isEmpty() && Goblo.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
                }
            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            List<ItemEntity> list = Goblo.this.level().getEntitiesOfClass(ItemEntity.class, Goblo.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            ItemStack itemstack = Goblo.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemstack.isEmpty() && !list.isEmpty()) {
                Goblo.this.getNavigation().moveTo(list.get(0), (double) 1.2F);
            }
        }

        @Override
        public void start() {
            List<ItemEntity> list = Goblo.this.level().getEntitiesOfClass(ItemEntity.class, Goblo.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            if (!list.isEmpty()) {
                Goblo.this.getNavigation().moveTo(list.get(0), (double) 1.2F);
            }
        }
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ItemInit.GOBLO_SPAWN_EGG.get());
    }

    class SleepGoal extends Goal {
        Goblo goblo;
        int sleepTicks = 0;

        public SleepGoal(Goblo goblo) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
            this.goblo = goblo;
        }

        @Override
        public boolean canUse() {
            return goblo.isSleeping();
        }

        @Override
        public boolean canContinueToUse() {
            return sleepTicks < 6000 && goblo.getLastHurtMob() == null;
        }

        @Override
        public void tick() {
            ++sleepTicks;
        }

        @Override
        public void start() {
            goblo.setTarget(null);
            Goblo.this.getNavigation().isDone();
            Goblo.this.setDeltaMovement(0.0D, 0.0D, 0.0D);
            Goblo.this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
        }

        @Override
        public void stop() {
            super.stop();
            sleepTicks = 0;
            goblo.setSleeping(false);
            goblo.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double) 0.2F);
        }
    }

    @Override
    public void containerChanged(Container invBasic) {
    }
}
