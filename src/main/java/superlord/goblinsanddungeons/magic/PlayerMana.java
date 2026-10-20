package superlord.goblinsanddungeons.magic;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * A player's mana. Replaces the 1.18.2 {@code PlayerMana} capability: immutable, stored as a data attachment
 * ({@link superlord.goblinsanddungeons.init.AttachmentInit#MANA}) so it is saved, synced to the client and copied on death.
 */
public record PlayerMana(int mana) {

    public static final int MIN_MANA = 0;
    public static final int MAX_MANA = 20;

    public static final PlayerMana EMPTY = new PlayerMana(MIN_MANA);

    public static final Codec<PlayerMana> CODEC = Codec.INT.xmap(PlayerMana::new, PlayerMana::mana);
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerMana> STREAM_CODEC =
            ByteBufCodecs.VAR_INT.map(PlayerMana::new, PlayerMana::mana).cast();

    public PlayerMana {
        mana = Math.max(MIN_MANA, Math.min(MAX_MANA, mana));
    }

    public PlayerMana add(int add) {
        return new PlayerMana(mana + add);
    }

    public PlayerMana sub(int sub) {
        return new PlayerMana(mana - sub);
    }

}
