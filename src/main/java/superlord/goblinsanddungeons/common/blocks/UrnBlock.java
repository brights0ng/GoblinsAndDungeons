package superlord.goblinsanddungeons.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class UrnBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty EXPLODING = BooleanProperty.create("exploding");
    public static final BooleanProperty HAS_WATER = BooleanProperty.create("water");
    public static final BooleanProperty HAS_LAVA = BooleanProperty.create("lava");
    public static final BooleanProperty HAS_POTION = BooleanProperty.create("potion");
    public static final IntegerProperty POTION_TYPE = IntegerProperty.create("type", 0, 199);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public UrnBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(EXPLODING, false)
                .setValue(HAS_WATER, false)
                .setValue(HAS_LAVA, false)
                .setValue(WATERLOGGED, false)
                .setValue(HAS_POTION, false)
                .setValue(POTION_TYPE, 0));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        return this.defaultBlockState()
                .setValue(EXPLODING, false)
                .setValue(HAS_WATER, false)
                .setValue(HAS_LAVA, false)
                .setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)
                .setValue(HAS_POTION, false)
                .setValue(POTION_TYPE, 0);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (!stateIn.canSurvive(worldIn, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if (stateIn.getValue(WATERLOGGED)) {
                worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
            }
            return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EXPLODING, HAS_WATER, HAS_LAVA, WATERLOGGED, HAS_POTION, POTION_TYPE);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (this.hasTnT(state) || this.hasLava(state) || this.hasWater(state) || this.hasPotion(state)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.is(Blocks.TNT.asItem())) {
            worldIn.setBlock(pos, state.setValue(EXPLODING, true), 3);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(worldIn.isClientSide);
        }

        if (stack.is(Items.WATER_BUCKET)) {
            worldIn.setBlock(pos, state.setValue(HAS_WATER, true), 3);
            player.playSound(SoundEvents.BUCKET_EMPTY, 1.0F, 1.0F);
            if (!player.isCreative()) {
                player.setItemInHand(handIn, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
            }
            return ItemInteractionResult.sidedSuccess(worldIn.isClientSide);
        }

        if (stack.is(Items.LAVA_BUCKET)) {
            worldIn.setBlock(pos, state.setValue(HAS_LAVA, true), 3);
            player.playSound(SoundEvents.BUCKET_EMPTY_LAVA, 1.0F, 1.0F);
            if (!player.isCreative()) {
                player.setItemInHand(handIn, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
            }
            return ItemInteractionResult.sidedSuccess(worldIn.isClientSide);
        }

        if (stack.is(Items.POTION)) {
            PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            int effectId = 0;
            for (MobEffectInstance instance : contents.getAllEffects()) {
                effectId = BuiltInRegistries.MOB_EFFECT.getId(instance.getEffect().value());
                break;
            }
            worldIn.setBlock(pos, state.setValue(HAS_POTION, true).setValue(POTION_TYPE, Math.min(effectId, 199)), 3);
            if (!player.isCreative()) {
                player.setItemInHand(handIn, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
            }
            return ItemInteractionResult.sidedSuccess(worldIn.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockState playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
        if (this.hasTnT(state)) {
            explode(worldIn, pos);
        }
        if (this.hasWater(state)) {
            worldIn.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
        }
        if (this.hasLava(state)) {
            worldIn.setBlock(pos, Blocks.LAVA.defaultBlockState(), 3);
        }
        if (this.hasPotion(state)) {
            AreaEffectCloud cloud = new AreaEffectCloud(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            cloud.setRadius(2.5F);
            cloud.setRadiusOnUse(-0.5F);
            cloud.setWaitTime(10);
            cloud.setDuration(cloud.getDuration() / 2);
            cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
            BuiltInRegistries.MOB_EFFECT.getHolder(getPotionType(state)).ifPresent(effectHolder -> {
                cloud.addEffect(new MobEffectInstance(effectHolder, 300));
                player.addEffect(new MobEffectInstance(effectHolder, 300));
            });
            worldIn.addFreshEntity(cloud);
        }
        return super.playerWillDestroy(worldIn, pos, state, player);
    }

    public boolean hasTnT(BlockState state) {
        return state.getValue(EXPLODING);
    }

    public boolean hasWater(BlockState state) {
        return state.getValue(HAS_WATER);
    }

    public boolean hasLava(BlockState state) {
        return state.getValue(HAS_LAVA);
    }

    public boolean hasPotion(BlockState state) {
        return state.getValue(HAS_POTION);
    }

    public int getPotionType(BlockState state) {
        return state.getValue(POTION_TYPE);
    }

    public static void explode(Level world, BlockPos pos) {
        explode(world, pos, null);
    }

    private static void explode(Level worldIn, BlockPos pos, @Nullable LivingEntity entityIn) {
        if (!worldIn.isClientSide()) {
            PrimedTnt tntentity = new PrimedTnt(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, entityIn);
            worldIn.addFreshEntity(tntentity);
            worldIn.playSound(null, tntentity.getBlockX(), tntentity.getBlockY(), tntentity.getBlockZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }
}
