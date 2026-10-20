package superlord.goblinsanddungeons.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import superlord.goblinsanddungeons.GoblinsAndDungeons;

public class CreativeTabInit {

    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GoblinsAndDungeons.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = REGISTER.register("goblinsanddungeons_item_group", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.goblinsanddungeons_item_group"))
                    .icon(() -> new ItemStack(ItemInit.GOBLIN_CROWN.get()))
                    .displayItems((parameters, output) -> {
                        // Equipment & Rings
                        output.accept(ItemInit.GOBLIN_CROWN.get());
                        output.accept(ItemInit.RING_OF_HEALTH.get());
                        output.accept(ItemInit.RING_OF_STEALTH.get());
                        output.accept(ItemInit.RING_OF_EXPERIENCE.get());
                        output.accept(ItemInit.RING_OF_GLORY.get());

                        // Spawn Eggs
                        output.accept(ItemInit.GOB_SPAWN_EGG.get());
                        output.accept(ItemInit.HOBGOB_SPAWN_EGG.get());
                        output.accept(ItemInit.GOBLO_SPAWN_EGG.get());
                        output.accept(ItemInit.GARCH_SPAWN_EGG.get());
                        output.accept(ItemInit.GOOM_SPAWN_EGG.get());
                        output.accept(ItemInit.MIMIC_SPAWN_EGG.get());
                        output.accept(ItemInit.GOBBER_SPAWN_EGG.get());
                        output.accept(ItemInit.OGRE_SPAWN_EGG.get());
                        output.accept(ItemInit.GOBLIN_KING_SPAWN_EGG.get());
                        output.accept(ItemInit.BEHOLDER_SPAWN_EGG.get());

                        // Staves
                        output.accept(ItemInit.STAFF_AMETHYST.get());
                        output.accept(ItemInit.STAFF_BULLET.get());
                        output.accept(ItemInit.STAFF_JUMP.get());

                        // Items & Drops
                        output.accept(ItemInit.OGRE_TUSK.get());
                        output.accept(ItemInit.GOBLIN_EYE.get());
                        output.accept(ItemInit.SOUL_ASH.get());
                        output.accept(ItemInit.BOTTLED_SOULS.get());

                        // Orbs & Bullets
                        output.accept(ItemInit.GOBLIN_SOUL_BULLET.get());
                        output.accept(ItemInit.SOUL_BULLET.get());
                        output.accept(ItemInit.BLINDNESS_ORB.get());
                        output.accept(ItemInit.TELEPORTATION_ORB.get());
                        output.accept(ItemInit.LEVITATION_ORB.get());
                        output.accept(ItemInit.WEAKNESS_ORB.get());
                        output.accept(ItemInit.EXPLOSIVE_ORB.get());

                        // Blocks
                        output.accept(ItemInit.URN.get());
                        output.accept(ItemInit.SCORIA.get());
                        output.accept(ItemInit.POLISHED_SCORIA.get());
                        output.accept(ItemInit.SCORIA_BRICKS.get());
                        output.accept(ItemInit.CRACKED_SCORIA_BRICKS.get());
                        output.accept(ItemInit.CHISELED_SCORIA.get());
                        output.accept(ItemInit.SCORIA_SLAB.get());
                        output.accept(ItemInit.POLISHED_SCORIA_SLAB.get());
                        output.accept(ItemInit.SCORIA_BRICK_SLAB.get());
                        output.accept(ItemInit.SCORIA_STAIRS.get());
                        output.accept(ItemInit.POLISHED_SCORIA_STAIRS.get());
                        output.accept(ItemInit.SCORIA_BRICK_STAIRS.get());
                        output.accept(ItemInit.SCORIA_WALL.get());
                        output.accept(ItemInit.POLISHED_SCORIA_WALL.get());
                        output.accept(ItemInit.SCORIA_BRICK_WALL.get());

                        // Campfire & Soul Sand
                        output.accept(ItemInit.ASHED_SOUL_SAND.get());
                        output.accept(ItemInit.SOUL_ASH_CAMPFIRE.get());
                        output.accept(ItemInit.SOUL_ASH_SOUL_CAMPFIRE.get());

                        // Additional stone variants
                        output.accept(ItemInit.CHISELED_SCORIA_BRICKS.get());
                        output.accept(ItemInit.SCORIA_PILLAR.get());
                        output.accept(ItemInit.SCORIA_PAVEMENT.get());
                        output.accept(ItemInit.SCORIA_VERTICAL_SLAB.get());
                        output.accept(ItemInit.POLISHED_SCORIA_VERTICAL_SLAB.get());
                        output.accept(ItemInit.SCORIA_BRICK_VERTICAL_SLAB.get());
                    })
                    .build()
    );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGIC_TAB = REGISTER.register("goblinsanddungeons_magic_item_group", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.goblinsanddungeons_magic_item_group"))
                    .icon(() -> new ItemStack(ItemInit.SOUL_BULLET_SPELL_TOME.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ItemInit.SOUL_BULLET_SPELL_TOME.get());
                        output.accept(ItemInit.SOUL_JUMP_SPELL_TOME.get());
                        output.accept(ItemInit.SOUL_BULLET_SCROLL.get());
                        output.accept(ItemInit.SOUL_JUMP_SCROLL.get());
                    })
                    .build()
    );
}
