package superlord.goblinsanddungeons.init;

import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.common.util.DeferredSoundType;

public class SoundTypeInit {

    public static final SoundType URN = new DeferredSoundType(
            1.0F, 1.0F,
            SoundInit.URN_BREAK,
            SoundInit.URN_STEP,
            SoundInit.URN_PLACE,
            SoundInit.URN_HIT,
            SoundInit.URN_FALL
    );
}
