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

public class RuinedKeepStructure extends Structure {
	public static final MapCodec<RuinedKeepStructure> CODEC = simpleCodec(RuinedKeepStructure::new);

	public RuinedKeepStructure(Structure.StructureSettings settings) {
		super(settings);
	}

	@Override
	public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
		return onTopOfChunkCenter(context, Heightmap.Types.OCEAN_FLOOR_WG, builder -> generatePieces(builder, context));
	}

	private static void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
		int x = context.chunkPos().getMinBlockX();
		int z = context.chunkPos().getMinBlockZ();
		int y = context.chunkGenerator().getFirstOccupiedHeight(x + 9, z + 9, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
		BlockPos blockpos = new BlockPos(x, y, z);
		Rotation rotation = Rotation.getRandom(context.random());
		RuinedKeepStructurePiece.addStructure(context.structureTemplateManager(), blockpos, rotation, builder, context.random());
	}

	@Override
	public StructureType<?> type() {
		return StructureInit.RUINED_KEEP.get();
	}
}
