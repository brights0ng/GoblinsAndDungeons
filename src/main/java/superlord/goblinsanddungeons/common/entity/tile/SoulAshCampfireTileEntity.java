package superlord.goblinsanddungeons.common.entity.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import superlord.goblinsanddungeons.common.blocks.SoulAshCampfireBlock;
import superlord.goblinsanddungeons.init.BlockInit;
import superlord.goblinsanddungeons.init.TileEntityInit;

import java.util.Optional;

public class SoulAshCampfireTileEntity extends BlockEntity implements Clearable {

    private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private final int[] cookingProgress = new int[4];
    private final int[] cookingTime = new int[4];

    public SoulAshCampfireTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityInit.SOUL_ASH_CAMPFIRE.get(), pos, state);
    }

    public static void cookTick(Level level, BlockPos pos, BlockState state, SoulAshCampfireTileEntity blockEntity) {
        boolean changed = false;

        for (int i = 0; i < blockEntity.items.size(); ++i) {
            ItemStack itemstack = blockEntity.items.get(i);
            if (!itemstack.isEmpty()) {
                changed = true;
                blockEntity.cookingProgress[i]++;
                if (blockEntity.cookingProgress[i] >= blockEntity.cookingTime[i]) {
                    SingleRecipeInput input = new SingleRecipeInput(itemstack);
                    ItemStack result = level.getRecipeManager()
                            .getRecipeFor(RecipeType.CAMPFIRE_COOKING, input, level)
                            .map(holder -> holder.value().assemble(input, level.registryAccess()))
                            .orElse(itemstack);
                    Containers.dropItemStack(level, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, result);
                    blockEntity.items.set(i, ItemStack.EMPTY);
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            }
        }

        if (changed) {
            setChanged(level, pos, state);
        }

        if (level.getBlockState(pos.above()).is(Blocks.SOUL_SAND)) {
            RandomSource random = level.getRandom();
            int chance = random.nextInt(199);
            if (chance == 23) {
                level.setBlock(pos.above(), BlockInit.ASHED_SOUL_SAND.get().defaultBlockState(), 3);
            }
        }
    }

    public static void cooldownTick(Level level, BlockPos pos, BlockState state, SoulAshCampfireTileEntity blockEntity) {
        boolean changed = false;
        for (int i = 0; i < blockEntity.items.size(); ++i) {
            if (blockEntity.cookingProgress[i] > 0) {
                changed = true;
                blockEntity.cookingProgress[i] = Mth.clamp(blockEntity.cookingProgress[i] - 2, 0, blockEntity.cookingTime[i]);
            }
        }
        if (changed) {
            setChanged(level, pos, state);
        }
    }

    public static void particleTick(Level level, BlockPos pos, BlockState state, SoulAshCampfireTileEntity blockEntity) {
        RandomSource random = level.getRandom();
        if (random.nextFloat() < 0.11F) {
            for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                SoulAshCampfireBlock.makeParticles(level, pos, state.getValue(SoulAshCampfireBlock.SIGNAL_FIRE), false);
            }
        }

        int rotation = state.getValue(SoulAshCampfireBlock.FACING).get2DDataValue();
        for (int j = 0; j < blockEntity.items.size(); ++j) {
            if (!blockEntity.items.get(j).isEmpty() && random.nextFloat() < 0.2F) {
                Direction direction = Direction.from2DDataValue(Math.floorMod(j + rotation, 4));
                double d0 = (double) pos.getX() + 0.5D - (double) ((float) direction.getStepX() * 0.3125F) + (double) ((float) direction.getClockWise().getStepX() * 0.3125F);
                double d1 = (double) pos.getY() + 0.5D;
                double d2 = (double) pos.getZ() + 0.5D - (double) ((float) direction.getStepZ() * 0.3125F) + (double) ((float) direction.getClockWise().getStepZ() * 0.3125F);

                for (int k = 0; k < 4; ++k) {
                    level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                }
            }
        }
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items, registries);
        if (tag.contains("CookingTimes", 11)) {
            int[] aint = tag.getIntArray("CookingTimes");
            System.arraycopy(aint, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, aint.length));
        }
        if (tag.contains("CookingTotalTimes", 11)) {
            int[] aint1 = tag.getIntArray("CookingTotalTimes");
            System.arraycopy(aint1, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, aint1.length));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, true, registries);
        tag.putIntArray("CookingTimes", this.cookingProgress);
        tag.putIntArray("CookingTotalTimes", this.cookingTime);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag compoundtag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundtag, this.items, true, registries);
        return compoundtag;
    }

    public Optional<RecipeHolder<CampfireCookingRecipe>> getCookableRecipe(ItemStack stack) {
        if (this.items.stream().noneMatch(ItemStack::isEmpty) || this.level == null) {
            return Optional.empty();
        }
        return this.level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SingleRecipeInput(stack), this.level);
    }

    public boolean placeFood(ItemStack stack, int cookTime) {
        for (int i = 0; i < this.items.size(); ++i) {
            ItemStack itemstack = this.items.get(i);
            if (itemstack.isEmpty()) {
                this.cookingTime[i] = cookTime;
                this.cookingProgress[i] = 0;
                this.items.set(i, stack.split(1));
                this.markUpdated();
                return true;
            }
        }
        return false;
    }

    private void markUpdated() {
        this.setChanged();
        if (this.getLevel() != null) {
            this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    public void dowse() {
        if (this.level != null) {
            this.markUpdated();
        }
    }
}
