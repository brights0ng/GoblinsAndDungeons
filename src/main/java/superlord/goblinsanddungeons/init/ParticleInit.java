package superlord.goblinsanddungeons.init;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import superlord.goblinsanddungeons.GoblinsAndDungeons;

/**
 * The particle types. The client-side particle providers (the 1.18.2 {@code registerFactories} handler) come back with
 * the client work in phase 4.
 */
public class ParticleInit {

    public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(Registries.PARTICLE_TYPE, GoblinsAndDungeons.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GOB_SOUL_BULLET = REGISTER.register("goblin_soul_bullet", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SOUL_BULLET = REGISTER.register("soul_bullet", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLINDNESS_ORB = REGISTER.register("blindness_orb", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> TELEPORTATION_ORB = REGISTER.register("teleportation_orb", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> LEVITATION_ORB = REGISTER.register("levitation_orb", () -> new SimpleParticleType(false));

}
