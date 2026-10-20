package superlord.goblinsanddungeons.magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * The spells a player has learned. Replaces the 1.18.2 {@code PlayerSpells} capability; stored as a data attachment
 * ({@link superlord.goblinsanddungeons.init.AttachmentInit#SPELLS}).
 */
public record PlayerSpells(boolean knowsSoulBullet, boolean knowsSoulJump) {

    public static final PlayerSpells NONE = new PlayerSpells(false, false);

    public static final Codec<PlayerSpells> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("soul_bullet", false).forGetter(PlayerSpells::knowsSoulBullet),
            Codec.BOOL.optionalFieldOf("soul_jump", false).forGetter(PlayerSpells::knowsSoulJump)
    ).apply(instance, PlayerSpells::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSpells> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, PlayerSpells::knowsSoulBullet,
            ByteBufCodecs.BOOL, PlayerSpells::knowsSoulJump,
            PlayerSpells::new
    );

    public PlayerSpells withSoulBullet(boolean known) {
        return new PlayerSpells(known, knowsSoulJump);
    }

    public PlayerSpells withSoulJump(boolean known) {
        return new PlayerSpells(knowsSoulBullet, known);
    }

}
