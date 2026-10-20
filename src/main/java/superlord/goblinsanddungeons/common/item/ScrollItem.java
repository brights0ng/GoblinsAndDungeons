package superlord.goblinsanddungeons.common.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import superlord.goblinsanddungeons.config.GoblinsDungeonsConfig;
import superlord.goblinsanddungeons.init.ParticleInit;
import superlord.goblinsanddungeons.init.SoundInit;

/** A one use scroll of a spell, cast without knowing it (and used up, unless the player is in creative). */
public class ScrollItem extends Item {

    public enum SpellType {
        NO_SPELL(0, "no_spell"),
        SOUL_BULLET(1, "soul_bullet"),
        SOUL_JUMP(2, "soul_jump");

        private final int id;
        private final String name;

        SpellType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return this.name;
        }
    }

    private final SpellType type;

    public ScrollItem(SpellType type, Properties properties) {
        super(properties);
        this.type = type;
    }

    public SpellType getSpellType() {
        return this.type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (GoblinsDungeonsConfig.magicalWorld) {
            if (this.type == SpellType.SOUL_BULLET) {
                // The Soul Bullet projectile comes back with the SoulBullet entity in phase 3; until then the scroll is
                // not used up.
                return InteractionResultHolder.pass(stack);
            }
            if (this.type == SpellType.SOUL_JUMP) {
                // The push is applied on both sides, as the client moves the player; the rest is the server's.
                Vec3 motion = player.getDeltaMovement();
                player.setDeltaMovement(motion.x, 0.7, motion.z);
                if (!world.isClientSide()) {
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundInit.SOUL_BULLET_LAUNCH.get(), SoundSource.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                    AreaEffectCloud cloud = new AreaEffectCloud(world, player.getX(), player.getY(), player.getZ());
                    cloud.setRadius(1F);
                    cloud.setRadiusOnUse(-0.5F);
                    cloud.setWaitTime(1);
                    cloud.setDuration(4);
                    cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
                    cloud.setParticle(ParticleInit.SOUL_BULLET.get());
                    world.addFreshEntity(cloud);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
    }
}
