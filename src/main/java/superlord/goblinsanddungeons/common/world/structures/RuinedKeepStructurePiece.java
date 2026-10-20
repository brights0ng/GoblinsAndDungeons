package superlord.goblinsanddungeons.common.world.structures;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.common.entity.Garch;
import superlord.goblinsanddungeons.common.entity.Gob;
import superlord.goblinsanddungeons.common.entity.GobKing;
import superlord.goblinsanddungeons.common.entity.Gobber;
import superlord.goblinsanddungeons.common.entity.Goblo;
import superlord.goblinsanddungeons.common.entity.Goom;
import superlord.goblinsanddungeons.common.entity.HobGob;
import superlord.goblinsanddungeons.common.entity.Mimic;
import superlord.goblinsanddungeons.common.entity.Ogre;
import superlord.goblinsanddungeons.init.EntityInit;
import superlord.goblinsanddungeons.init.LootTableInit;
import superlord.goblinsanddungeons.init.StructureInit;

public class RuinedKeepStructurePiece {

	private static final ResourceLocation ruinedKeep_Template = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "ruined_keep");

	private static final Map<ResourceLocation, BlockPos> structurePos = ImmutableMap.of(ruinedKeep_Template, BlockPos.ZERO);

	static final BlockPos PIVOT = new BlockPos(9, 0, 9);

	public static void addStructure(StructureTemplateManager templateManager, BlockPos pos, Rotation rotation, StructurePieceAccessor pieceAccessor, RandomSource random) {
		pieceAccessor.addPiece(new Piece(templateManager, ruinedKeep_Template, pos, rotation, 0));
	}

	public static class Piece extends TemplateStructurePiece {
		public Piece(StructureTemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation, int yOffset) {
			super(StructureInit.RUINED_KEEP_PIECE.get(), 0, templateManager, location, location.toString(), makeSettings(rotation), makePosition(location, pos, yOffset));
		}

		public Piece(StructureTemplateManager templateManager, CompoundTag tag) {
			super(StructureInit.RUINED_KEEP_PIECE.get(), tag, templateManager, (location) -> {
				return makeSettings(Rotation.valueOf(tag.getString("Rot")));
			});
		}

		private static StructurePlaceSettings makeSettings(Rotation rotation) {
			BlockIgnoreProcessor blockignoreprocessor = BlockIgnoreProcessor.STRUCTURE_BLOCK;
			return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).setRotationPivot(PIVOT).addProcessor(blockignoreprocessor).addProcessor(new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE));
		}

		private static BlockPos makePosition(ResourceLocation location, BlockPos pos, int yOffset) {
			return pos.offset(structurePos.getOrDefault(location, BlockPos.ZERO)).below(yOffset);
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
			super.addAdditionalSaveData(context, tag);
			tag.putString("Rot", this.placeSettings.getRotation().name());
		}

		@Override
		public void postProcess(WorldGenLevel worldIn, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
			BlockPos blockpos1 = this.templatePosition.offset(this.placeSettings.getRotationPivot());
			int i = worldIn.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockpos1.getX(), blockpos1.getZ());
			BlockPos blockpos2 = this.templatePosition;
			this.templatePosition = new BlockPos(this.templatePosition.getX(), i - 2, this.templatePosition.getZ());
			super.postProcess(worldIn, structureManager, chunkGenerator, random, box, chunkPos, pos);
			this.templatePosition = blockpos2;
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor world, RandomSource rand, BoundingBox sbb) {
			if ("chest".equals(function)) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				BlockEntity tileentity = world.getBlockEntity(pos.below());
				if (tileentity instanceof ChestBlockEntity chest) {
					chest.setLootTable(LootTableInit.KEEP_LOOT_TABLE, rand.nextLong());
				}
			}
			if ("gob".equals(function)) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				Gob entity = EntityInit.GOB.get().create(world.getLevel());
				if (entity != null) {
					entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
					world.addFreshEntity(entity);
				}
			}
			if ("garch".equals(function)) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				Garch entity = EntityInit.GARCH.get().create(world.getLevel());
				if (entity != null) {
					entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
					world.addFreshEntity(entity);
				}
			}
			if ("gobking".equals(function)) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				GobKing entity = EntityInit.GOB_KING.get().create(world.getLevel());
				if (entity != null) {
					entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
					world.addFreshEntity(entity);
				}
			}
			if ("gobber".equals(function)) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				Gobber entity = EntityInit.GOBBER.get().create(world.getLevel());
				if (entity != null) {
					entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
					world.addFreshEntity(entity);
				}
			}
			if ("ogre".equals(function)) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				Ogre entity = EntityInit.OGRE.get().create(world.getLevel());
				if (entity != null) {
					entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
					world.addFreshEntity(entity);
				}
			}
			if ("goom".equals(function)) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				Goom entity = EntityInit.GOOM.get().create(world.getLevel());
				if (entity != null) {
					entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
					world.addFreshEntity(entity);
				}
			}
			if ("goblo".equals(function)) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				Goblo entity = EntityInit.GOBLO.get().create(world.getLevel());
				if (entity != null) {
					entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
					world.addFreshEntity(entity);
				}
			}
			if ("hobgob".equals(function)) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				HobGob entity = EntityInit.HOBGOB.get().create(world.getLevel());
				if (entity != null) {
					entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
					world.addFreshEntity(entity);
				}
			}
			if ("chicken".equals(function)) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				Chicken entity = EntityType.CHICKEN.create(world.getLevel());
				if (entity != null) {
					entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					entity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
					world.addFreshEntity(entity);
				}
			}
			if ("mimic".equals(function)) {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				Mimic mimic = EntityInit.MIMIC.get().create(world.getLevel());
				if (mimic != null) {
					mimic.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
					mimic.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
					world.addFreshEntity(mimic);
				}
			}
			if ("chest2".equals(function)) {
				world.setBlock(pos, Blocks.EMERALD_BLOCK.defaultBlockState(), 2);
				BlockEntity tileEntity = world.getBlockEntity(pos.below());
				if (tileEntity instanceof ChestBlockEntity chest) {
					chest.setLootTable(LootTableInit.KEEP_LOOT_TABLE, rand.nextLong());
				}
			}
		}
	}
}
