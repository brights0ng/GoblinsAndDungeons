package superlord.goblinsanddungeons.init;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.magic.PlayerMana;
import superlord.goblinsanddungeons.magic.PlayerSpells;

/**
 * Player data that was a Forge capability in 1.18.2 (mana, learned spells), now NeoForge data attachments.
 *
 * <p>1.18.2 parity: neither attachment is {@code copyOnDeath()}. The 1.18.2 clone handler copied each capability onto
 * itself rather than onto the new player, so mana and learned spells reset on death. Add {@code .copyOnDeath()} to the
 * builders below to keep them instead.
 */
public class AttachmentInit {

    public static final DeferredRegister<AttachmentType<?>> REGISTER =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, GoblinsAndDungeons.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerMana>> MANA = REGISTER.register("mana", () ->
            AttachmentType.builder(() -> PlayerMana.EMPTY)
                    .serialize(PlayerMana.CODEC)
                    .sync(PlayerMana.STREAM_CODEC)
                    .build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerSpells>> SPELLS = REGISTER.register("spells", () ->
            AttachmentType.builder(() -> PlayerSpells.NONE)
                    .serialize(PlayerSpells.CODEC)
                    .sync(PlayerSpells.STREAM_CODEC)
                    .build());
}
