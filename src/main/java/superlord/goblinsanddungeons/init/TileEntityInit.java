package superlord.goblinsanddungeons.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.common.entity.tile.SoulAshCampfireTileEntity;

public class TileEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GoblinsAndDungeons.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SoulAshCampfireTileEntity>> SOUL_ASH_CAMPFIRE = REGISTER.register(
            "soul_ash_campfire",
            () -> BlockEntityType.Builder.of(
                    SoulAshCampfireTileEntity::new,
                    BlockInit.SOUL_ASH_CAMPFIRE.get(),
                    BlockInit.SOUL_ASH_SOUL_CAMPFIRE.get()
            ).build(null)
    );
}
