package superlord.goblinsanddungeons.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.entity.PartEntity;

public class BeholderPart extends PartEntity<Beholder> {
    public final Beholder parentMob;
    public final String name;
    private final EntityDimensions size;

    public BeholderPart(Beholder parentMob, String name, float width, float height) {
        super(parentMob);
        this.size = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
        this.parentMob = parentMob;
        this.name = name;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !this.isInvulnerableTo(source) && this.parentMob.damagePart(this, source, amount);
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.parentMob == entity;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D);
    }
}
