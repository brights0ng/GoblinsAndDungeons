package superlord.goblinsanddungeons;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import superlord.goblinsanddungeons.config.GDConfigHolder;
import superlord.goblinsanddungeons.config.GoblinsDungeonsConfig;
import superlord.goblinsanddungeons.init.ArmorInit;
import superlord.goblinsanddungeons.init.AttachmentInit;
import superlord.goblinsanddungeons.init.BlockInit;
import superlord.goblinsanddungeons.init.CreativeTabInit;
import superlord.goblinsanddungeons.init.EffectInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.ParticleInit;
import superlord.goblinsanddungeons.init.SoundInit;
import superlord.goblinsanddungeons.init.TileEntityInit;

@Mod(GoblinsAndDungeons.MOD_ID)
public class GoblinsAndDungeons {
    public static final String MOD_ID = "goblinsanddungeons";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GoblinsAndDungeons(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Goblins & Dungeons initializing for NeoForge 1.21.1");

        BlockInit.REGISTER.register(modEventBus);
        ItemInit.REGISTER.register(modEventBus);
        ArmorInit.REGISTER.register(modEventBus);
        EffectInit.EFFECTS.register(modEventBus);
        EffectInit.POTIONS.register(modEventBus);
        ParticleInit.REGISTER.register(modEventBus);
        AttachmentInit.REGISTER.register(modEventBus);
        TileEntityInit.REGISTER.register(modEventBus);
        SoundInit.REGISTER.register(modEventBus);
        CreativeTabInit.REGISTER.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.CLIENT, GDConfigHolder.CLIENT_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, GDConfigHolder.SERVER_SPEC);
        modEventBus.addListener(this::onConfig);

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

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Goblins & Dungeons common setup complete");
    }
}
