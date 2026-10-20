package superlord.goblinsanddungeons.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.common.blocks.AshedSoulSandBlock;
import superlord.goblinsanddungeons.common.blocks.SoulAshCampfireBlock;
import superlord.goblinsanddungeons.common.blocks.UrnBlock;
import superlord.goblinsanddungeons.common.compat.VerticalSlabBlock;

public class BlockInit {

    public static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(GoblinsAndDungeons.MOD_ID);

    public static final DeferredBlock<Block> URN = REGISTER.register("urn", () ->
            new UrnBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).sound(SoundTypeInit.URN).strength(0.3F).noOcclusion()));

    public static final DeferredBlock<Block> SCORIA = REGISTER.register("scoria", () ->
            new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> POLISHED_SCORIA = REGISTER.register("polished_scoria", () ->
            new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> SCORIA_BRICKS = REGISTER.register("scoria_bricks", () ->
            new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> CRACKED_SCORIA_BRICKS = REGISTER.register("cracked_scoria_bricks", () ->
            new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> CHISELED_SCORIA = REGISTER.register("chiseled_scoria", () ->
            new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> SCORIA_SLAB = REGISTER.register("scoria_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> POLISHED_SCORIA_SLAB = REGISTER.register("polished_scoria_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> SCORIA_BRICK_SLAB = REGISTER.register("scoria_brick_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> SCORIA_STAIRS = REGISTER.register("scoria_stairs", () ->
            new StairBlock(SCORIA.get().defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> POLISHED_SCORIA_STAIRS = REGISTER.register("polished_scoria_stairs", () ->
            new StairBlock(POLISHED_SCORIA.get().defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> SCORIA_BRICK_STAIRS = REGISTER.register("scoria_brick_stairs", () ->
            new StairBlock(SCORIA_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> SCORIA_WALL = REGISTER.register("scoria_wall", () ->
            new WallBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> POLISHED_SCORIA_WALL = REGISTER.register("polished_scoria_wall", () ->
            new WallBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> SCORIA_BRICK_WALL = REGISTER.register("scoria_brick_wall", () ->
            new WallBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> ASHED_SOUL_SAND = REGISTER.register("ashed_soul_sand", () ->
            new AshedSoulSandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_SAND)));

    public static final DeferredBlock<Block> SOUL_ASH_CAMPFIRE = REGISTER.register("soul_ash_campfire", () ->
            new SoulAshCampfireBlock(true, 1, BlockBehaviour.Properties.ofFullCopy(Blocks.CAMPFIRE)));
    public static final DeferredBlock<Block> SOUL_ASH_SOUL_CAMPFIRE = REGISTER.register("soul_ash_soul_campfire", () ->
            new SoulAshCampfireBlock(false, 2, BlockBehaviour.Properties.ofFullCopy(Blocks.SOUL_CAMPFIRE)));

    // Additional decorative variants
    public static final DeferredBlock<Block> CHISELED_SCORIA_BRICKS = REGISTER.register("chiseled_scoria_bricks", () ->
            new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> SCORIA_PILLAR = REGISTER.register("scoria_pillar", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> SCORIA_PAVEMENT = REGISTER.register("scoria_pavement", () ->
            new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> SCORIA_VERTICAL_SLAB = REGISTER.register("scoria_vertical_slab", () ->
            new VerticalSlabBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> POLISHED_SCORIA_VERTICAL_SLAB = REGISTER.register("polished_scoria_vertical_slab", () ->
            new VerticalSlabBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> SCORIA_BRICK_VERTICAL_SLAB = REGISTER.register("scoria_brick_vertical_slab", () ->
            new VerticalSlabBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
}
