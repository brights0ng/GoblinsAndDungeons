package superlord.goblinsanddungeons.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import superlord.goblinsanddungeons.GoblinsAndDungeons;

@EventBusSubscriber(modid = GoblinsAndDungeons.MOD_ID)
public class GDDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        event.getGenerator().addProvider(event.includeServer(), new EmptyGameTestStructure(output, GoblinsAndDungeons.MOD_ID));
    }
}
