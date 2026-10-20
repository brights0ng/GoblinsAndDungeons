package superlord.goblinsanddungeons.common.world.structures;

import java.util.Optional;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import superlord.goblinsanddungeons.init.StructureInit;

public class MediumGoblinCampStructure extends Structure {
	public static final MapCodec<MediumGoblinCampStructure> CODEC = simpleCodec(MediumGoblinCampStructure::new);

	public MediumGoblinCampStructure(Structure.StructureSettings settings) {
		super(settings);
	}

	@Override
	public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> generatePieces(builder, context));
	}

	private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
		int x = context.chunkPos().getMinBlockX();
		int z = context.chunkPos().getMinBlockZ();
		int y = context.chunkGenerator().getFirstOccupiedHeight(x + 9, z + 9, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		BlockPos blockpos = new BlockPos(x, y, z);
		Rotation rotation = Rotation.getRandom(context.random());
		MediumGoblinCampStructurePiece.addStructure(context.structureTemplateManager(), blockpos, rotation, builder, context.random());
	}

	@Override
	public StructureType<?> type() {
		return StructureInit.MEDIUM_GOBLIN_CAMP.get();
	}
}
