package superlord.goblinsanddungeons;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.slf4j.Logger;
import superlord.goblinsanddungeons.common.entity.Beholder;
import superlord.goblinsanddungeons.common.entity.Garch;
import superlord.goblinsanddungeons.common.entity.Gob;
import superlord.goblinsanddungeons.common.entity.GobKing;
import superlord.goblinsanddungeons.common.entity.Gobber;
import superlord.goblinsanddungeons.common.entity.Goblo;
import superlord.goblinsanddungeons.common.entity.Goom;
import superlord.goblinsanddungeons.common.entity.HobGob;
import superlord.goblinsanddungeons.common.entity.Mimic;
import superlord.goblinsanddungeons.common.entity.Ogre;
import superlord.goblinsanddungeons.config.GDConfigHolder;
import superlord.goblinsanddungeons.config.GoblinsDungeonsConfig;
import superlord.goblinsanddungeons.init.ArmorInit;
import superlord.goblinsanddungeons.init.AttachmentInit;
import superlord.goblinsanddungeons.init.BlockInit;
import superlord.goblinsanddungeons.init.CreativeTabInit;
import superlord.goblinsanddungeons.init.EffectInit;
import superlord.goblinsanddungeons.init.EntityInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.ParticleInit;
import superlord.goblinsanddungeons.init.SoundInit;
import superlord.goblinsanddungeons.init.StructureInit;
import superlord.goblinsanddungeons.init.TileEntityInit;

@Mod(GoblinsAndDungeons.MOD_ID)
public class GoblinsAndDungeons {
    public static final String MOD_ID = "goblinsanddungeons";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GoblinsAndDungeons(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Goblins & Dungeons initializing for NeoForge 1.21.1");

        BlockInit.REGISTER.register(modEventBus);
        ItemInit.REGISTER.register(modEventBus);
        EntityInit.REGISTER.register(modEventBus);
        ArmorInit.REGISTER.register(modEventBus);
        EffectInit.EFFECTS.register(modEventBus);
        EffectInit.POTIONS.register(modEventBus);
        ParticleInit.REGISTER.register(modEventBus);
        AttachmentInit.REGISTER.register(modEventBus);
        TileEntityInit.REGISTER.register(modEventBus);
        SoundInit.REGISTER.register(modEventBus);
        CreativeTabInit.REGISTER.register(modEventBus);
        StructureInit.STRUCTURES.register(modEventBus);
        StructureInit.PIECES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.CLIENT, GDConfigHolder.CLIENT_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, GDConfigHolder.SERVER_SPEC);
        modEventBus.addListener(this::onConfig);
        modEventBus.addListener(this::registerEntityAttributes);
        modEventBus.addListener(this::registerSpawnPlacements);

        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.addListener(EffectInit::brewingRecipes);
    }

    private void onConfig(final ModConfigEvent event) {
        if (event instanceof ModConfigEvent.Unloading) {
            return;
        }
        if (event.getConfig().getSpec() == GDConfigHolder.CLIENT_SPEC) {
            GoblinsDungeonsConfig.bakeClient(event.getConfig());
        } else if (event.getConfig().getSpec() == GDConfigHolder.SERVER_SPEC) {
            GoblinsDungeonsConfig.bakeServer(event.getConfig());
        }
    }

    private void registerEntityAttributes(final EntityAttributeCreationEvent event) {
        event.put(EntityInit.GOB.get(), Gob.createAttributes().build());
        event.put(EntityInit.HOBGOB.get(), HobGob.createAttributes().build());
        event.put(EntityInit.GOBLO.get(), Goblo.createAttributes().build());
        event.put(EntityInit.GARCH.get(), Garch.createAttributes().build());
        event.put(EntityInit.GOOM.get(), Goom.createAttributes().build());
        event.put(EntityInit.GOBBER.get(), Gobber.createAttributes().build());
        event.put(EntityInit.OGRE.get(), Ogre.createAttributes().build());
        event.put(EntityInit.GOB_KING.get(), GobKing.createAttributes().build());
        event.put(EntityInit.MIMIC.get(), Mimic.createAttributes().build());
        event.put(EntityInit.BEHOLDER.get(), Beholder.createAttributes().build());
    }

    private void registerSpawnPlacements(final RegisterSpawnPlacementsEvent event) {
        event.register(
                EntityInit.OGRE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkAnyLightMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Goblins & Dungeons common setup complete");
    }
}
