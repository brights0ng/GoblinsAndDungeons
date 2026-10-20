package superlord.goblinsanddungeons;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import superlord.goblinsanddungeons.init.BlockInit;
import superlord.goblinsanddungeons.init.CreativeTabInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.SoundInit;
import superlord.goblinsanddungeons.init.TileEntityInit;

@Mod(GoblinsAndDungeons.MOD_ID)
public class GoblinsAndDungeons {
    public static final String MOD_ID = "goblinsanddungeons";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GoblinsAndDungeons(IEventBus modEventBus) {
        LOGGER.info("Goblins & Dungeons initializing for NeoForge 1.21.1");

        BlockInit.REGISTER.register(modEventBus);
        ItemInit.REGISTER.register(modEventBus);
        TileEntityInit.REGISTER.register(modEventBus);
        SoundInit.REGISTER.register(modEventBus);
        CreativeTabInit.REGISTER.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Goblins & Dungeons common setup complete");
    }
}
