package superlord.goblinsanddungeons.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.common.world.structures.LargeGoblinCampStructure;
import superlord.goblinsanddungeons.common.world.structures.LargeGoblinCampStructurePiece;
import superlord.goblinsanddungeons.common.world.structures.MediumGoblinCampStructure;
import superlord.goblinsanddungeons.common.world.structures.MediumGoblinCampStructurePiece;
import superlord.goblinsanddungeons.common.world.structures.RuinedKeepStructure;
import superlord.goblinsanddungeons.common.world.structures.RuinedKeepStructurePiece;
import superlord.goblinsanddungeons.common.world.structures.SmallGoblinCampStructure;
import superlord.goblinsanddungeons.common.world.structures.SmallGoblinCampStructurePiece;

public class StructureInit {

	public static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, GoblinsAndDungeons.MOD_ID);
	public static final DeferredRegister<StructurePieceType> PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, GoblinsAndDungeons.MOD_ID);

	public static final DeferredHolder<StructureType<?>, StructureType<SmallGoblinCampStructure>> SMALL_GOBLIN_CAMP = STRUCTURES.register("small_goblin_camp", () -> () -> SmallGoblinCampStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<MediumGoblinCampStructure>> MEDIUM_GOBLIN_CAMP = STRUCTURES.register("medium_goblin_camp", () -> () -> MediumGoblinCampStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<LargeGoblinCampStructure>> LARGE_GOBLIN_CAMP = STRUCTURES.register("large_goblin_camp", () -> () -> LargeGoblinCampStructure.CODEC);
	public static final DeferredHolder<StructureType<?>, StructureType<RuinedKeepStructure>> RUINED_KEEP = STRUCTURES.register("ruined_keep", () -> () -> RuinedKeepStructure.CODEC);

	public static final DeferredHolder<StructurePieceType, StructurePieceType.StructureTemplateType> SMALL_GOBLIN_CAMP_PIECE = PIECES.register("sgcsp", () -> SmallGoblinCampStructurePiece.Piece::new);
	public static final DeferredHolder<StructurePieceType, StructurePieceType.StructureTemplateType> MEDIUM_GOBLIN_CAMP_PIECE = PIECES.register("mgcsp", () -> MediumGoblinCampStructurePiece.Piece::new);
	public static final DeferredHolder<StructurePieceType, StructurePieceType.StructureTemplateType> LARGE_GOBLIN_CAMP_PIECE = PIECES.register("lgcsp", () -> LargeGoblinCampStructurePiece.Piece::new);
	public static final DeferredHolder<StructurePieceType, StructurePieceType.StructureTemplateType> RUINED_KEEP_PIECE = PIECES.register("rksp", () -> RuinedKeepStructurePiece.Piece::new);
}
