package superlord.goblinsanddungeons.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.common.entity.Goblin;
import superlord.goblinsanddungeons.common.entity.ai.FollowOgreGoal;
import superlord.goblinsanddungeons.config.GoblinsDungeonsConfig;
import superlord.goblinsanddungeons.init.BlockInit;
import superlord.goblinsanddungeons.init.GDTags;
import superlord.goblinsanddungeons.init.ItemInit;

@EventBusSubscriber(modid = GoblinsAndDungeons.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof AbstractIllager || event.getEntity() instanceof AbstractGolem) {
            ((Mob) event.getEntity()).targetSelector.addGoal(0, new NearestAttackableTargetGoal<>((Mob) event.getEntity(), Goblin.class, true));
        }
        if (event.getEntity() instanceof AbstractVillager villager) {
            villager.goalSelector.addGoal(0, new AvoidEntityGoal<>((PathfinderMob) villager, Goblin.class, 10.0F, 1.0D, 1.0D));
        }
        if (event.getEntity() instanceof Donkey donkey) {
            donkey.goalSelector.addGoal(4, new FollowOgreGoal((Animal) donkey, 1.0F));
        }
    }

    @SubscribeEvent
    public static void onLivingVisibility(LivingEvent.LivingVisibilityEvent event) {
        LivingEntity entity = event.getEntity();
        Entity lookingEntity = event.getLookingEntity();
        if (lookingEntity != null) {
            if ((lookingEntity instanceof Goblin || lookingEntity.getType().is(GDTags.GOBLINS))
                    && entity.getItemBySlot(EquipmentSlot.HEAD).is(ItemInit.GOBLIN_CROWN.get())) {
                event.modifyVisibility(0.5D);
            }
            if (lookingEntity instanceof Monster
                    && entity.isCrouching()
                    && entity.getItemBySlot(EquipmentSlot.OFFHAND).is(ItemInit.RING_OF_STEALTH.get())) {
                event.modifyVisibility(0.8D);
            }
        }
    }

    @SubscribeEvent
    public static void convertCampfire(BlockEvent.EntityPlaceEvent event) {
        if (GoblinsDungeonsConfig.magicalWorld) {
            BlockPos pos = event.getPos();
            LevelAccessor world = event.getLevel();
            Block block = event.getPlacedBlock().getBlock();
            if (block == Blocks.SOUL_SAND && world.getBlockState(pos.below()).getBlock() == Blocks.CAMPFIRE) {
                world.setBlock(pos.below(), BlockInit.SOUL_ASH_CAMPFIRE.get().defaultBlockState(), 3);
            }
            if (block == Blocks.SOUL_SAND && world.getBlockState(pos.below()).getBlock() == Blocks.SOUL_CAMPFIRE) {
                world.setBlock(pos.below(), BlockInit.SOUL_ASH_SOUL_CAMPFIRE.get().defaultBlockState(), 3);
            }
            if (block == Blocks.CAMPFIRE && world.getBlockState(pos.above()).getBlock() == Blocks.SOUL_SAND) {
                world.setBlock(pos, BlockInit.SOUL_ASH_CAMPFIRE.get().defaultBlockState(), 3);
            }
            if (block == Blocks.SOUL_CAMPFIRE && world.getBlockState(pos.above()).getBlock() == Blocks.SOUL_SAND) {
                world.setBlock(pos, BlockInit.SOUL_ASH_SOUL_CAMPFIRE.get().defaultBlockState(), 3);
            }
        }
    }

    @SubscribeEvent
    public static void convertCampfireBack(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        LevelAccessor world = event.getLevel();
        Block block = world.getBlockState(pos).getBlock();
        if ((block == Blocks.SOUL_SAND || block == BlockInit.ASHED_SOUL_SAND.get()) && world.getBlockState(pos.below()).getBlock() == BlockInit.SOUL_ASH_CAMPFIRE.get()) {
            world.setBlock(pos.below(), Blocks.CAMPFIRE.defaultBlockState(), 3);
        }
        if ((block == Blocks.SOUL_SAND || block == BlockInit.ASHED_SOUL_SAND.get()) && world.getBlockState(pos.below()).getBlock() == BlockInit.SOUL_ASH_SOUL_CAMPFIRE.get()) {
            world.setBlock(pos.below(), Blocks.SOUL_CAMPFIRE.defaultBlockState(), 3);
        }
    }
}
