package superlord.goblinsanddungeons.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import superlord.goblinsanddungeons.GoblinsAndDungeons;

public class SoundInit {

    public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(Registries.SOUND_EVENT, GoblinsAndDungeons.MOD_ID);

    private static DeferredHolder<SoundEvent, SoundEvent> createEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, name);
        return REGISTER.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static final DeferredHolder<SoundEvent, SoundEvent> OGRE_IDLE = createEvent("ogre_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> OGRE_HURT = createEvent("ogre_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> OGRE_DEATH = createEvent("ogre_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> OGRE_ROAR = createEvent("ogre_roar");

    public static final DeferredHolder<SoundEvent, SoundEvent> GARCH_IDLE = createEvent("garch_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> GARCH_HURT = createEvent("garch_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GARCH_DEATH = createEvent("garch_death");

    public static final DeferredHolder<SoundEvent, SoundEvent> GOOM_IDLE = createEvent("goom_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOOM_HURT = createEvent("goom_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOOM_DEATH = createEvent("goom_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOOM_WARNING = createEvent("goom_warning");

    public static final DeferredHolder<SoundEvent, SoundEvent> HOBGOB_IDLE = createEvent("hobgob_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> HOBGOB_HURT = createEvent("hobgob_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> HOBGOB_DEATH = createEvent("hobgob_death");

    public static final DeferredHolder<SoundEvent, SoundEvent> GOB_IDLE = createEvent("gob_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOB_HURT = createEvent("gob_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOB_DEATH = createEvent("gob_death");

    public static final DeferredHolder<SoundEvent, SoundEvent> MIMIC_IDLE = createEvent("mimic_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> MIMIC_HURT = createEvent("mimic_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MIMIC_DEATH = createEvent("mimic_death");

    public static final DeferredHolder<SoundEvent, SoundEvent> GOBBER_IDLE = createEvent("gobber_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBBER_HURT = createEvent("gobber_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBBER_DEATH = createEvent("gobber_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBBER_SNORING = createEvent("gobber_snoring");

    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLO_IDLE = createEvent("goblo_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLO_HURT = createEvent("goblo_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLO_DEATH = createEvent("goblo_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLO_SNORING = createEvent("goblo_snoring");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLO_EATING = createEvent("goblo_eating");

    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLIN_KING_IDLE = createEvent("goblin_king_idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLIN_KING_LAUGH = createEvent("goblin_king_laugh");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLIN_KING_HURT = createEvent("goblin_king_hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOBLIN_KING_DEATH = createEvent("goblin_king_death");

    public static final DeferredHolder<SoundEvent, SoundEvent> SPELL_CASTING = createEvent("spell_casting");
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUL_BULLET_LAUNCH = createEvent("soul_bullet_launch");
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUL_BULLET_COLLISION = createEvent("soul_bullet_collision");

    public static final DeferredHolder<SoundEvent, SoundEvent> URN_PLACE = createEvent("urn_place");
    public static final DeferredHolder<SoundEvent, SoundEvent> URN_FALL = createEvent("urn_fall");
    public static final DeferredHolder<SoundEvent, SoundEvent> URN_HIT = createEvent("urn_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> URN_BREAK = createEvent("urn_break");
    public static final DeferredHolder<SoundEvent, SoundEvent> URN_STEP = createEvent("urn_step");
}
