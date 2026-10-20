package superlord.goblinsanddungeons.init;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.common.item.BlindnessOrbItem;
import superlord.goblinsanddungeons.common.item.BulletStaffItem;
import superlord.goblinsanddungeons.common.item.ExplosiveOrbItem;
import superlord.goblinsanddungeons.common.item.GoblinCrownItem;
import superlord.goblinsanddungeons.common.item.GoblinSoulBulletItem;
import superlord.goblinsanddungeons.common.item.HealthRingItem;
import superlord.goblinsanddungeons.common.item.JumpStaffItem;
import superlord.goblinsanddungeons.common.item.LevitationOrbItem;
import superlord.goblinsanddungeons.common.item.ScrollItem;
import superlord.goblinsanddungeons.common.item.SoulBottleItem;
import superlord.goblinsanddungeons.common.item.SoulBulletItem;
import superlord.goblinsanddungeons.common.item.SpellTomeItem;
import superlord.goblinsanddungeons.common.item.StaffItem;
import superlord.goblinsanddungeons.common.item.StealthRingItem;
import superlord.goblinsanddungeons.common.item.TeleportationOrbItem;
import superlord.goblinsanddungeons.common.item.WeaknessOrbItem;

public class ItemInit {

    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(GoblinsAndDungeons.MOD_ID);

    private static final int RING_DURABILITY = 126;

    // Equipment & Wearables
    public static final DeferredItem<GoblinCrownItem> GOBLIN_CROWN = REGISTER.register("goblin_crown", () ->
            new GoblinCrownItem(ArmorInit.GOBLIN_CROWN, new Item.Properties()
                    .durability(ArmorItem.Type.HELMET.getDurability(ArmorInit.GOBLIN_CROWN_DURABILITY_FACTOR))));
    // The 1.18.2 rings were off-hand armor items of durability 18 * 7 = 126 (the off-hand's slot index times the ring material's factor).
    public static final DeferredItem<HealthRingItem> RING_OF_HEALTH = REGISTER.register("ring_of_health", () ->
            new HealthRingItem(new Item.Properties().durability(RING_DURABILITY)));
    public static final DeferredItem<StealthRingItem> RING_OF_STEALTH = REGISTER.register("ring_of_stealth", () ->
            new StealthRingItem(new Item.Properties().durability(RING_DURABILITY)));
    public static final DeferredItem<StealthRingItem> RING_OF_EXPERIENCE = REGISTER.register("ring_of_experience", () ->
            new StealthRingItem(new Item.Properties().durability(RING_DURABILITY)));
    public static final DeferredItem<StealthRingItem> RING_OF_GLORY = REGISTER.register("ring_of_glory", () ->
            new StealthRingItem(new Item.Properties().durability(RING_DURABILITY)));

    // Spawn Eggs
    public static final DeferredItem<DeferredSpawnEggItem> GOB_SPAWN_EGG = REGISTER.register("gob_spawn_egg", () ->
            new DeferredSpawnEggItem(EntityInit.GOB, 0x647D14, 0xA7A322, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> HOBGOB_SPAWN_EGG = REGISTER.register("hobgob_spawn_egg", () ->
            new DeferredSpawnEggItem(EntityInit.HOBGOB, 0x261F3A, 0x9163B8, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> GOBLO_SPAWN_EGG = REGISTER.register("goblo_spawn_egg", () ->
            new DeferredSpawnEggItem(EntityInit.GOBLO, 0xD9BE48, 0x843C8B, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> GARCH_SPAWN_EGG = REGISTER.register("garch_spawn_egg", () ->
            new DeferredSpawnEggItem(EntityInit.GARCH, 0x992318, 0x2C394C, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> GOOM_SPAWN_EGG = REGISTER.register("goom_spawn_egg", () ->
            new DeferredSpawnEggItem(EntityInit.GOOM, 0x468D70, 0x313533, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> MIMIC_SPAWN_EGG = REGISTER.register("mimic_spawn_egg", () ->
            new DeferredSpawnEggItem(EntityInit.MIMIC, 0x8F691D, 0x407133, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> GOBBER_SPAWN_EGG = REGISTER.register("gobber_spawn_egg", () ->
            new DeferredSpawnEggItem(EntityInit.GOBBER, 0x677FE8, 0x82A190, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> OGRE_SPAWN_EGG = REGISTER.register("ogre_spawn_egg", () ->
            new DeferredSpawnEggItem(EntityInit.OGRE, 0xB8AE4D, 0x875D2A, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> GOBLIN_KING_SPAWN_EGG = REGISTER.register("goblin_king_spawn_egg", () ->
            new DeferredSpawnEggItem(EntityInit.GOB_KING, 0x79911E, 0xCF4466, new Item.Properties()));
    public static final DeferredItem<DeferredSpawnEggItem> BEHOLDER_SPAWN_EGG = REGISTER.register("beholder_spawn_egg", () ->
            new DeferredSpawnEggItem(EntityInit.BEHOLDER, 0xD65286, 0x9417A1, new Item.Properties()));

    // Staves
    public static final DeferredItem<StaffItem> STAFF_AMETHYST = REGISTER.register("staff_amethyst", () ->
            new StaffItem(new Item.Properties().stacksTo(1).durability(250)));
    public static final DeferredItem<BulletStaffItem> STAFF_BULLET = REGISTER.register("staff_bullet", () ->
            new BulletStaffItem(new Item.Properties().stacksTo(1).durability(250)));
    public static final DeferredItem<JumpStaffItem> STAFF_JUMP = REGISTER.register("staff_jump", () ->
            new JumpStaffItem(new Item.Properties().stacksTo(1).durability(250)));

    // Mob Drops & Materials
    public static final DeferredItem<Item> OGRE_TUSK = REGISTER.register("ogre_tusk", () ->
            new Item(new Item.Properties()));
    public static final DeferredItem<Item> GOBLIN_EYE = REGISTER.register("goblin_eye", () ->
            new Item(new Item.Properties()));
    public static final DeferredItem<Item> SOUL_ASH = REGISTER.register("soul_ash", () ->
            new Item(new Item.Properties()));
    public static final DeferredItem<SoulBottleItem> BOTTLED_SOULS = REGISTER.register("bottled_souls", () ->
            new SoulBottleItem(new Item.Properties().stacksTo(1)));

    // Projectiles & Orbs
    public static final DeferredItem<GoblinSoulBulletItem> GOBLIN_SOUL_BULLET = REGISTER.register("goblin_soul_bullet", () ->
            new GoblinSoulBulletItem(new Item.Properties()));
    public static final DeferredItem<SoulBulletItem> SOUL_BULLET = REGISTER.register("soul_bullet", () ->
            new SoulBulletItem(new Item.Properties()));
    public static final DeferredItem<BlindnessOrbItem> BLINDNESS_ORB = REGISTER.register("blindness_orb", () ->
            new BlindnessOrbItem(new Item.Properties()));
    public static final DeferredItem<TeleportationOrbItem> TELEPORTATION_ORB = REGISTER.register("teleportation_orb", () ->
            new TeleportationOrbItem(new Item.Properties()));
    public static final DeferredItem<LevitationOrbItem> LEVITATION_ORB = REGISTER.register("levitation_orb", () ->
            new LevitationOrbItem(new Item.Properties()));
    public static final DeferredItem<WeaknessOrbItem> WEAKNESS_ORB = REGISTER.register("weakness_orb", () ->
            new WeaknessOrbItem(new Item.Properties()));
    public static final DeferredItem<ExplosiveOrbItem> EXPLOSIVE_ORB = REGISTER.register("explosive_orb", () ->
            new ExplosiveOrbItem(new Item.Properties()));

    // Spells & Scrolls
    public static final DeferredItem<SpellTomeItem> SOUL_BULLET_SPELL_TOME = REGISTER.register("soul_bullet_spell_tome", () ->
            new SpellTomeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<SpellTomeItem> SOUL_JUMP_SPELL_TOME = REGISTER.register("soul_jump_spell_tome", () ->
            new SpellTomeItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<ScrollItem> SOUL_BULLET_SCROLL = REGISTER.register("soul_bullet_scroll", () ->
            new ScrollItem(ScrollItem.SpellType.SOUL_BULLET, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<ScrollItem> SOUL_JUMP_SCROLL = REGISTER.register("soul_jump_scroll", () ->
            new ScrollItem(ScrollItem.SpellType.SOUL_JUMP, new Item.Properties().stacksTo(1)));

    // Block Items
    public static final DeferredItem<BlockItem> URN = REGISTER.register("urn", () ->
            new BlockItem(BlockInit.URN.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SCORIA = REGISTER.register("scoria", () ->
            new BlockItem(BlockInit.SCORIA.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> POLISHED_SCORIA = REGISTER.register("polished_scoria", () ->
            new BlockItem(BlockInit.POLISHED_SCORIA.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SCORIA_BRICKS = REGISTER.register("scoria_bricks", () ->
            new BlockItem(BlockInit.SCORIA_BRICKS.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> CRACKED_SCORIA_BRICKS = REGISTER.register("cracked_scoria_bricks", () ->
            new BlockItem(BlockInit.CRACKED_SCORIA_BRICKS.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> CHISELED_SCORIA = REGISTER.register("chiseled_scoria", () ->
            new BlockItem(BlockInit.CHISELED_SCORIA.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> SCORIA_SLAB = REGISTER.register("scoria_slab", () ->
            new BlockItem(BlockInit.SCORIA_SLAB.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> POLISHED_SCORIA_SLAB = REGISTER.register("polished_scoria_slab", () ->
            new BlockItem(BlockInit.POLISHED_SCORIA_SLAB.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SCORIA_BRICK_SLAB = REGISTER.register("scoria_brick_slab", () ->
            new BlockItem(BlockInit.SCORIA_BRICK_SLAB.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> SCORIA_STAIRS = REGISTER.register("scoria_stairs", () ->
            new BlockItem(BlockInit.SCORIA_STAIRS.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> POLISHED_SCORIA_STAIRS = REGISTER.register("polished_scoria_stairs", () ->
            new BlockItem(BlockInit.POLISHED_SCORIA_STAIRS.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SCORIA_BRICK_STAIRS = REGISTER.register("scoria_brick_stairs", () ->
            new BlockItem(BlockInit.SCORIA_BRICK_STAIRS.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> SCORIA_WALL = REGISTER.register("scoria_wall", () ->
            new BlockItem(BlockInit.SCORIA_WALL.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> POLISHED_SCORIA_WALL = REGISTER.register("polished_scoria_wall", () ->
            new BlockItem(BlockInit.POLISHED_SCORIA_WALL.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SCORIA_BRICK_WALL = REGISTER.register("scoria_brick_wall", () ->
            new BlockItem(BlockInit.SCORIA_BRICK_WALL.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> ASHED_SOUL_SAND = REGISTER.register("ashed_soul_sand", () ->
            new BlockItem(BlockInit.ASHED_SOUL_SAND.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SOUL_ASH_CAMPFIRE = REGISTER.register("soul_ash_campfire", () ->
            new BlockItem(BlockInit.SOUL_ASH_CAMPFIRE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SOUL_ASH_SOUL_CAMPFIRE = REGISTER.register("soul_ash_soul_campfire", () ->
            new BlockItem(BlockInit.SOUL_ASH_SOUL_CAMPFIRE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> CHISELED_SCORIA_BRICKS = REGISTER.register("chiseled_scoria_bricks", () ->
            new BlockItem(BlockInit.CHISELED_SCORIA_BRICKS.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SCORIA_PILLAR = REGISTER.register("scoria_pillar", () ->
            new BlockItem(BlockInit.SCORIA_PILLAR.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SCORIA_PAVEMENT = REGISTER.register("scoria_pavement", () ->
            new BlockItem(BlockInit.SCORIA_PAVEMENT.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> SCORIA_VERTICAL_SLAB = REGISTER.register("scoria_vertical_slab", () ->
            new BlockItem(BlockInit.SCORIA_VERTICAL_SLAB.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> POLISHED_SCORIA_VERTICAL_SLAB = REGISTER.register("polished_scoria_vertical_slab", () ->
            new BlockItem(BlockInit.POLISHED_SCORIA_VERTICAL_SLAB.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> SCORIA_BRICK_VERTICAL_SLAB = REGISTER.register("scoria_brick_vertical_slab", () ->
            new BlockItem(BlockInit.SCORIA_BRICK_VERTICAL_SLAB.get(), new Item.Properties()));
}
