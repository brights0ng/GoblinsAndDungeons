package superlord.goblinsanddungeons;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(GoblinsAndDungeons.MOD_ID)
public class GoblinsAndDungeons {
    public static final String MOD_ID = "goblinsanddungeons";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GoblinsAndDungeons(IEventBus modEventBus) {
        LOGGER.info("Goblins & Dungeons initializing for NeoForge 1.21.1");
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Goblins & Dungeons common setup complete");
    }
}
