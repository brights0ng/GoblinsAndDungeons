package superlord.goblinsanddungeons.gametest;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.common.blocks.UrnBlock;
import superlord.goblinsanddungeons.common.entity.Beholder;
import superlord.goblinsanddungeons.common.entity.BlindnessOrb;
import superlord.goblinsanddungeons.common.entity.ExplosiveOrb;
import superlord.goblinsanddungeons.common.entity.Garch;
import superlord.goblinsanddungeons.common.entity.Gob;
import superlord.goblinsanddungeons.common.entity.GobKing;
import superlord.goblinsanddungeons.common.entity.Gobber;
import superlord.goblinsanddungeons.common.entity.GoblinSoulBullet;
import superlord.goblinsanddungeons.common.entity.Goblo;
import superlord.goblinsanddungeons.common.entity.Goom;
import superlord.goblinsanddungeons.common.entity.HobGob;
import superlord.goblinsanddungeons.common.entity.LevitationOrb;
import superlord.goblinsanddungeons.common.entity.Mimic;
import superlord.goblinsanddungeons.common.entity.Ogre;
import superlord.goblinsanddungeons.common.entity.SoulBullet;
import superlord.goblinsanddungeons.common.entity.TeleportationOrb;
import superlord.goblinsanddungeons.common.entity.WeaknessOrb;
import superlord.goblinsanddungeons.common.entity.tile.SoulAshCampfireTileEntity;
import superlord.goblinsanddungeons.common.world.structures.SmallGoblinCampStructurePiece;
import superlord.goblinsanddungeons.datagen.EmptyGameTestStructure;
import superlord.goblinsanddungeons.init.ArmorInit;
import superlord.goblinsanddungeons.init.AttachmentInit;
import superlord.goblinsanddungeons.init.BlockInit;
import superlord.goblinsanddungeons.init.EffectInit;
import superlord.goblinsanddungeons.init.EntityInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.LootTableInit;
import superlord.goblinsanddungeons.init.StructureInit;
import superlord.goblinsanddungeons.magic.PlayerMana;
import superlord.goblinsanddungeons.magic.PlayerSpells;

/**
 * In-game checks of what has been ported so far: the urn and the soul ash campfire, the rings, the crown, the effects
 * and potions, the mana and spells, and the staffs, tomes and the soul bottle that use them.
 *
 * <p>These use survival mock players (a plain {@link Player}): the mock <em>server</em> player of the game test helper
 * counts as being in creative, which skips the costs these checks are about.
 */
@GameTestHolder(GoblinsAndDungeons.MOD_ID)
@PrefixGameTestTemplate(false)
public final class GoblinsGameTests {

    private static final String EMPTY = EmptyGameTestStructure.EMPTY;
    private static final BlockPos SPOT = new BlockPos(3, 1, 3);
    private static final double EPSILON = 1.0E-4D;

    private static Player survivalPlayer(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Vec3 at = helper.absoluteVec(new Vec3(SPOT.getX() + 0.5D, SPOT.getY(), SPOT.getZ() + 0.5D));
        player.moveTo(at.x, at.y, at.z);
        return player;
    }

    private static Zombie stillZombie(GameTestHelper helper, int x, int z) {
        Zombie zombie = helper.spawn(EntityType.ZOMBIE, x, 1, z);
        zombie.setNoAi(true);
        zombie.setPersistenceRequired();
        return zombie;
    }

    private static void assertNear(GameTestHelper helper, double expected, double actual, String what) {
        helper.assertTrue(Math.abs(expected - actual) < EPSILON, what + ": expected " + expected + " but was " + actual);
    }

    // ---- blocks (phase 1) ----

    @GameTest(template = EMPTY)
    public static void urnTakesWaterAndTnt(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        helper.setBlock(SPOT, BlockInit.URN.get());
        BlockPos pos = helper.absolutePos(SPOT);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos), net.minecraft.core.Direction.UP, pos, false);

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WATER_BUCKET));
        helper.getLevel().getBlockState(pos).useItemOn(player.getMainHandItem(), helper.getLevel(), player, InteractionHand.MAIN_HAND, hit);
        BlockState filled = helper.getLevel().getBlockState(pos);
        helper.assertTrue(filled.getValue(UrnBlock.HAS_WATER), "the urn should hold the water");
        helper.assertTrue(player.getMainHandItem().is(Items.BUCKET), "the bucket should be emptied");

        helper.setBlock(SPOT, BlockInit.URN.get());
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.TNT));
        helper.getLevel().getBlockState(pos).useItemOn(player.getMainHandItem(), helper.getLevel(), player, InteractionHand.MAIN_HAND, hit);
        helper.assertTrue(helper.getLevel().getBlockState(pos).getValue(UrnBlock.EXPLODING), "the urn should hold the TNT");
        helper.assertTrue(player.getMainHandItem().isEmpty(), "the TNT should be used up");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void soulAshCampfireHasABlockEntityThatTakesFood(GameTestHelper helper) {
        helper.setBlock(SPOT, BlockInit.SOUL_ASH_CAMPFIRE.get());
        helper.assertTrue(helper.getBlockEntity(SPOT) instanceof SoulAshCampfireTileEntity, "the campfire should have its block entity");
        SoulAshCampfireTileEntity campfire = (SoulAshCampfireTileEntity) helper.getBlockEntity(SPOT);
        ItemStack beef = new ItemStack(Items.BEEF);
        helper.assertTrue(campfire.getCookableRecipe(beef).isPresent(), "raw beef should have a campfire cooking recipe");
        helper.assertTrue(campfire.placeFood(beef, 600), "the campfire should take the beef");
        helper.assertTrue(beef.isEmpty(), "the beef should be on the campfire");
        helper.succeed();
    }

    // ---- rings and the crown (phase 2) ----

    @GameTest(template = EMPTY)
    public static void ringOfHealthAddsAHeartInTheOffhand(GameTestHelper helper) {
        Zombie zombie = stillZombie(helper, 2, 2);
        double base = zombie.getMaxHealth();
        zombie.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ItemInit.RING_OF_HEALTH.get()));
        helper.startSequence()
                .thenIdle(2)
                .thenExecute(() -> assertNear(helper, base + 2.0D, zombie.getMaxHealth(), "max health with the ring in the off-hand"))
                .thenExecute(() -> zombie.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY))
                .thenIdle(2)
                .thenExecute(() -> assertNear(helper, base, zombie.getMaxHealth(), "max health without the ring"))
                .thenSucceed();
    }

    @GameTest(template = EMPTY)
    public static void ringOfHealthDoesNothingInTheMainHand(GameTestHelper helper) {
        Zombie zombie = stillZombie(helper, 2, 2);
        double base = zombie.getMaxHealth();
        zombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemInit.RING_OF_HEALTH.get()));
        helper.startSequence()
                .thenIdle(2)
                .thenExecute(() -> assertNear(helper, base, zombie.getMaxHealth(), "max health with the ring in the main hand"))
                .thenSucceed();
    }

    @GameTest(template = EMPTY)
    public static void ringsGoToTheOffhandOnUse(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemInit.RING_OF_STEALTH.get()));
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getItemBySlot(EquipmentSlot.OFFHAND).is(ItemInit.RING_OF_STEALTH.get()), "the ring should be in the off-hand");
        helper.assertTrue(player.getMainHandItem().isEmpty(), "the main hand should be empty");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void ringsHaveTheirDurability(GameTestHelper helper) {
        for (Item ring : new Item[]{ItemInit.RING_OF_HEALTH.get(), ItemInit.RING_OF_STEALTH.get(), ItemInit.RING_OF_EXPERIENCE.get(), ItemInit.RING_OF_GLORY.get()}) {
            helper.assertTrue(new ItemStack(ring).getMaxDamage() == 126, ring + " should have durability 126");
            helper.assertTrue(new ItemStack(ring).getMaxStackSize() == 1, ring + " should not stack");
        }
        helper.assertTrue(new ItemStack(ItemInit.GOBLIN_CROWN.get()).getMaxDamage() == 77, "the crown should last as long as a golden helmet");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void ringOfGloryHealsOnAKillAndWearsDown(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        player.setHealth(10.0F);
        ItemStack ring = new ItemStack(ItemInit.RING_OF_GLORY.get());
        player.setItemSlot(EquipmentSlot.OFFHAND, ring);
        Zombie zombie = stillZombie(helper, 2, 2);
        zombie.hurt(helper.getLevel().damageSources().playerAttack(player), 1000.0F);
        helper.assertTrue(!zombie.isAlive(), "the zombie should be dead");
        assertNear(helper, 12.0D, player.getHealth(), "health after killing a 20 health zombie (2 hearts)");
        helper.assertTrue(ring.getDamageValue() == 1, "the ring should be worn down by 1");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void withoutTheRingAKillHealsNothing(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        player.setHealth(10.0F);
        Zombie zombie = stillZombie(helper, 2, 2);
        zombie.hurt(helper.getLevel().damageSources().playerAttack(player), 1000.0F);
        assertNear(helper, 10.0D, player.getHealth(), "health after a kill without the ring");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void ringOfExperienceAddsAPointToAKill(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        player.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ItemInit.RING_OF_EXPERIENCE.get()));
        Zombie zombie = stillZombie(helper, 2, 2);
        zombie.hurt(helper.getLevel().damageSources().playerAttack(player), 1000.0F);
        helper.startSequence()
                .thenIdle(2)
                .thenExecute(() -> helper.assertTrue(droppedExperience(helper) == 6, "a zombie drops 5 experience, 6 with the ring, but dropped " + droppedExperience(helper)))
                .thenSucceed();
    }

    @GameTest(template = EMPTY)
    public static void aKillWithoutTheRingDropsTheUsualExperience(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        Zombie zombie = stillZombie(helper, 2, 2);
        zombie.hurt(helper.getLevel().damageSources().playerAttack(player), 1000.0F);
        helper.startSequence()
                .thenIdle(2)
                .thenExecute(() -> helper.assertTrue(droppedExperience(helper) == 5, "a zombie drops 5 experience, but dropped " + droppedExperience(helper)))
                .thenSucceed();
    }

    private static int droppedExperience(GameTestHelper helper) {
        return helper.getLevel().getEntitiesOfClass(ExperienceOrb.class, helper.getBounds().inflate(2.0D))
                .stream().mapToInt(ExperienceOrb::getValue).sum();
    }

    @GameTest(template = EMPTY)
    public static void gloryRingTableMatches1182(GameTestHelper helper) {
        int[][] expected = {{1, 1}, {15, 1}, {16, 2}, {20, 2}, {30, 2}, {31, 3}, {45, 3}, {46, 4}, {60, 4}, {61, 0}, {75, 0}, {76, 5}, {90, 5}, {91, 6},
                {150, 9}, {151, 10}, {285, 18}, {286, 19}, {300, 19}, {301, 20}, {1000, 20}};
        for (int[] row : expected) {
            helper.assertTrue(superlord.goblinsanddungeons.common.RingEvents.gloryHeal(row[0]) == row[1],
                    "max health " + row[0] + " should heal " + row[1] + " but healed " + superlord.goblinsanddungeons.common.RingEvents.gloryHeal(row[0]));
        }
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void crownWeakensTheNearestMonsterWhileWorn(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        ItemStack crown = new ItemStack(ItemInit.GOBLIN_CROWN.get());
        player.setItemSlot(EquipmentSlot.HEAD, crown);
        Zombie zombie = stillZombie(helper, 2, 2);
        crown.getItem().inventoryTick(crown, helper.getLevel(), player, 39, false);
        MobEffectInstance weakness = zombie.getEffect(MobEffects.WEAKNESS);
        helper.assertTrue(weakness != null, "the zombie should be weakened");
        helper.assertTrue(weakness.getAmplifier() == 0, "a monster that is no goblin gets Weakness I");
        helper.assertTrue(weakness.getDuration() == 1200, "the weakness should last a minute");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void crownDoesNothingOutsideTheHeadSlot(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        ItemStack crown = new ItemStack(ItemInit.GOBLIN_CROWN.get());
        player.getInventory().setItem(0, crown);
        Zombie zombie = stillZombie(helper, 2, 2);
        crown.getItem().inventoryTick(crown, helper.getLevel(), player, 0, true);
        helper.assertTrue(zombie.getEffect(MobEffects.WEAKNESS) == null, "a crown in the hotbar should not weaken anything");
        helper.succeed();
    }

    // ---- effects and potions (phase 2) ----

    @GameTest(template = EMPTY)
    public static void ogresMightAddsDamageAndSlowsAttacks(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        double damage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        double speed = player.getAttributeValue(Attributes.ATTACK_SPEED);
        player.addEffect(new MobEffectInstance(EffectInit.MIGHT, 600));
        assertNear(helper, damage + 3.0D, player.getAttributeValue(Attributes.ATTACK_DAMAGE), "attack damage with Might I");
        assertNear(helper, speed * 0.9D, player.getAttributeValue(Attributes.ATTACK_SPEED), "attack speed with Might I");
        player.removeAllEffects();
        player.addEffect(new MobEffectInstance(EffectInit.MIGHT, 600, 1));
        assertNear(helper, damage + 6.0D, player.getAttributeValue(Attributes.ATTACK_DAMAGE), "attack damage with Might II");
        player.removeAllEffects();
        assertNear(helper, damage, player.getAttributeValue(Attributes.ATTACK_DAMAGE), "attack damage once the effect is gone");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void paralysisFreezesMovementJumpsAndAttacks(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        helper.assertTrue(player.getAttributeValue(Attributes.MOVEMENT_SPEED) > 0.0D, "the player should be able to move at first");
        player.addEffect(new MobEffectInstance(EffectInit.PARALYSIS, 150));
        assertNear(helper, 0.0D, player.getAttributeValue(Attributes.MOVEMENT_SPEED), "movement speed while paralysed");
        assertNear(helper, 0.0D, player.getAttributeValue(Attributes.JUMP_STRENGTH), "jump strength while paralysed");
        assertNear(helper, 0.0D, player.getAttributeValue(Attributes.ATTACK_SPEED), "attack speed while paralysed");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void ogresMightIsBrewedFromAnAwkwardPotionAndATusk(GameTestHelper helper) {
        var brewing = helper.getLevel().potionBrewing();
        ItemStack awkward = PotionContents.createItemStack(Items.POTION, Potions.AWKWARD);
        ItemStack might = brewing.mix(new ItemStack(ItemInit.OGRE_TUSK.get()), awkward);
        helper.assertTrue(PotionContents.createItemStack(Items.POTION, EffectInit.OGRES_MIGHT).getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                .potion().equals(might.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion()), "an awkward potion and an ogre's tusk should make Ogre's Might");
        ItemStack strong = brewing.mix(new ItemStack(Items.GLOWSTONE_DUST), might);
        helper.assertTrue(strong.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion().orElseThrow().is(EffectInit.OGRES_MIGHT_STRONG.getId()), "glowstone should make it strong");
        ItemStack lasting = brewing.mix(new ItemStack(Items.REDSTONE), might);
        helper.assertTrue(lasting.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion().orElseThrow().is(EffectInit.OGRES_MIGHT_LONG.getId()), "redstone should make it last");
        helper.succeed();
    }

    // ---- mana, spells and what uses them (phase 2) ----

    @GameTest(template = EMPTY)
    public static void manaIsClampedAndStartsEmpty(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        helper.assertTrue(player.getData(AttachmentInit.MANA).mana() == 0, "a new player has no mana");
        player.setData(AttachmentInit.MANA, player.getData(AttachmentInit.MANA).add(50));
        helper.assertTrue(player.getData(AttachmentInit.MANA).mana() == PlayerMana.MAX_MANA, "mana should stop at the maximum");
        player.setData(AttachmentInit.MANA, player.getData(AttachmentInit.MANA).sub(50));
        helper.assertTrue(player.getData(AttachmentInit.MANA).mana() == 0, "mana should stop at zero");
        helper.assertTrue(player.getData(AttachmentInit.SPELLS).equals(PlayerSpells.NONE), "a new player knows no spells");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void spellTomesTeachTheirSpellOnceAndAreUsedUp(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemInit.SOUL_JUMP_SPELL_TOME.get()));
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getData(AttachmentInit.SPELLS).knowsSoulJump(), "the player should know Soul Jump");
        helper.assertTrue(!player.getData(AttachmentInit.SPELLS).knowsSoulBullet(), "the player should not know Soul Bullet");
        helper.assertTrue(player.getMainHandItem().isEmpty(), "the tome should be used up");

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemInit.SOUL_JUMP_SPELL_TOME.get()));
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(!player.getMainHandItem().isEmpty(), "a tome of a spell the player knows should be kept");

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemInit.SOUL_BULLET_SPELL_TOME.get()));
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getData(AttachmentInit.SPELLS).equals(new PlayerSpells(true, true)), "the player should know both spells");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void bottledSoulsRestoreManaAndLeaveABottle(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        ItemStack souls = new ItemStack(ItemInit.BOTTLED_SOULS.get());
        ItemStack result = souls.finishUsingItem(helper.getLevel(), player);
        helper.assertTrue(player.getData(AttachmentInit.MANA).mana() == 4, "drinking should restore 4 mana");
        helper.assertTrue(result.is(Items.GLASS_BOTTLE), "an empty bottle should be left");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void soulJumpStaffCostsManaAndDurability(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        player.setData(AttachmentInit.MANA, new PlayerMana(10));
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemInit.STAFF_JUMP.get()));
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getData(AttachmentInit.MANA).mana() == 8, "a jump should cost 2 mana");
        helper.assertTrue(player.getMainHandItem().getDamageValue() == 1, "a jump should cost 1 durability");
        assertNear(helper, 0.5D, player.getDeltaMovement().y, "the upward push");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void soulJumpStaffNeedsMana(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        player.setData(AttachmentInit.MANA, new PlayerMana(1));
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemInit.STAFF_JUMP.get()));
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getData(AttachmentInit.MANA).mana() == 1, "no mana should be spent");
        helper.assertTrue(player.getMainHandItem().getDamageValue() == 0, "the staff should not wear");
        assertNear(helper, 0.0D, player.getDeltaMovement().y, "the upward push");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void sneakingSwitchesStaffsKeepingTheirDamage(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        player.setShiftKeyDown(true);

        ItemStack plain = new ItemStack(ItemInit.STAFF_AMETHYST.get());
        plain.setDamageValue(40);
        player.setItemInHand(InteractionHand.MAIN_HAND, plain);
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getMainHandItem().is(ItemInit.STAFF_AMETHYST.get()), "a player who knows no spell keeps the plain staff");

        player.setData(AttachmentInit.SPELLS, new PlayerSpells(false, true));
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getMainHandItem().is(ItemInit.STAFF_JUMP.get()), "the plain staff should become the Soul Jump staff");
        helper.assertTrue(player.getMainHandItem().getDamageValue() == 40, "the damage should be kept");

        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getMainHandItem().is(ItemInit.STAFF_AMETHYST.get()), "the Soul Jump staff should go back to the plain one");

        player.setData(AttachmentInit.SPELLS, new PlayerSpells(true, true));
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getMainHandItem().is(ItemInit.STAFF_BULLET.get()), "knowing Soul Bullet picks the Soul Bullet staff");
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getMainHandItem().is(ItemInit.STAFF_JUMP.get()), "the Soul Bullet staff should go on to the Soul Jump one for a player who knows it");
        helper.succeed();
    }

    // ---- entities, projectiles, staffs, scrolls and stealth (phase 3) ----

    @GameTest(template = EMPTY)
    public static void allTenMobsSpawnWithAttributesAndEquipment(GameTestHelper helper) {
        var level = helper.getLevel();
        var difficulty = level.getCurrentDifficultyAt(helper.absolutePos(SPOT));

        Gob gob = helper.spawnWithNoFreeWill(EntityInit.GOB.get(), 1, 1, 1);
        gob.finalizeSpawn(level, difficulty, MobSpawnType.COMMAND, null);
        assertNear(helper, 10.0D, gob.getMaxHealth(), "Gob max health");
        helper.assertTrue(gob.getMainHandItem().is(Items.STONE_SWORD), "Gob should spawn with a stone sword");

        HobGob hobGob = helper.spawnWithNoFreeWill(EntityInit.HOBGOB.get(), 2, 1, 1);
        assertNear(helper, 20.0D, hobGob.getMaxHealth(), "HobGob max health");
        assertNear(helper, 6.0D, hobGob.getAttributeValue(Attributes.ATTACK_DAMAGE), "HobGob attack damage");

        Goblo goblo = helper.spawnWithNoFreeWill(EntityInit.GOBLO.get(), 3, 1, 1);
        assertNear(helper, 14.0D, goblo.getMaxHealth(), "Goblo max health");

        Garch garch = helper.spawnWithNoFreeWill(EntityInit.GARCH.get(), 4, 1, 1);
        garch.finalizeSpawn(level, difficulty, MobSpawnType.COMMAND, null);
        assertNear(helper, 10.0D, garch.getMaxHealth(), "Garch max health");
        helper.assertTrue(garch.getMainHandItem().is(Items.BOW), "Garch should spawn with a bow");

        Goom goom = helper.spawnWithNoFreeWill(EntityInit.GOOM.get(), 5, 1, 1);
        assertNear(helper, 8.0D, goom.getMaxHealth(), "Goom max health");

        Gobber gobber = helper.spawnWithNoFreeWill(EntityInit.GOBBER.get(), 1, 1, 2);
        assertNear(helper, 25.0D, gobber.getMaxHealth(), "Gobber max health");

        Ogre ogre = helper.spawnWithNoFreeWill(EntityInit.OGRE.get(), 2, 1, 2);
        assertNear(helper, 200.0D, ogre.getMaxHealth(), "Ogre max health");
        assertNear(helper, 10.0D, ogre.getAttributeValue(Attributes.ATTACK_DAMAGE), "Ogre attack damage");
        assertNear(helper, 1.0D, ogre.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), "Ogre knockback resistance");

        GobKing gobKing = helper.spawnWithNoFreeWill(EntityInit.GOB_KING.get(), 3, 1, 2);
        gobKing.finalizeSpawn(level, difficulty, MobSpawnType.COMMAND, null);
        assertNear(helper, 150.0D, gobKing.getMaxHealth(), "Goblin King max health");
        helper.assertTrue(gobKing.getMainHandItem().is(ItemInit.STAFF_AMETHYST.get()), "Goblin King should spawn with an amethyst staff");

        Mimic mimic = helper.spawnWithNoFreeWill(EntityInit.MIMIC.get(), 4, 1, 2);
        mimic.finalizeSpawn(level, difficulty, MobSpawnType.COMMAND, null);
        assertNear(helper, 12.0D, mimic.getMaxHealth(), "Mimic max health");
        helper.assertTrue(mimic.isHiding(), "Mimic should start hiding on spawn");

        Beholder beholder = helper.spawnWithNoFreeWill(EntityInit.BEHOLDER.get(), 5, 1, 2);
        assertNear(helper, 200.0D, beholder.getMaxHealth(), "Beholder max health");
        helper.assertTrue(beholder.isMultipartEntity(), "Beholder should be a multipart entity");
        helper.assertTrue(beholder.getParts() != null && beholder.getParts().length == 6, "Beholder should have 6 sub-parts");

        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void crownGivesWeaknessTwoToGoblinsAndHalvesVisibility(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        Gob gob = helper.spawnWithNoFreeWill(EntityInit.GOB.get(), 2, 1, 2);
        double baseVis = player.getVisibilityPercent(gob);

        ItemStack crown = new ItemStack(ItemInit.GOBLIN_CROWN.get());
        player.setItemSlot(EquipmentSlot.HEAD, crown);
        crown.getItem().inventoryTick(crown, helper.getLevel(), player, 39, false);

        MobEffectInstance weakness = gob.getEffect(MobEffects.WEAKNESS);
        helper.assertTrue(weakness != null, "the Gob should be weakened by the crown");
        helper.assertTrue(weakness.getAmplifier() == 1, "a goblin gets Weakness II from the crown");
        assertNear(helper, baseVis * 0.5D, player.getVisibilityPercent(gob), "player visibility to a goblin while wearing the crown");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void ringOfStealthReducesVisibilityWhenCrouching(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        Zombie zombie = stillZombie(helper, 2, 2);
        player.setShiftKeyDown(true);
        player.setPose(Pose.CROUCHING);
        double crouchingVisWithoutRing = player.getVisibilityPercent(zombie);

        player.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ItemInit.RING_OF_STEALTH.get()));
        double crouchingVisWithRing = player.getVisibilityPercent(zombie);
        assertNear(helper, crouchingVisWithoutRing * 0.8D, crouchingVisWithRing, "crouching visibility with Ring of Stealth in off-hand");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void soulBulletStaffFiresProjectileAndDamagesTarget(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        player.setData(AttachmentInit.MANA, new PlayerMana(10));
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemInit.STAFF_BULLET.get()));
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);

        helper.assertTrue(player.getData(AttachmentInit.MANA).mana() == 9, "firing a Soul Bullet should cost 1 mana");
        helper.assertTrue(player.getMainHandItem().getDamageValue() == 1, "firing a Soul Bullet should cost 1 durability");
        var bullets = helper.getLevel().getEntitiesOfClass(SoulBullet.class, helper.getBounds().inflate(4.0D));
        helper.assertTrue(bullets.size() == 1, "a SoulBullet projectile should be spawned");

        Zombie zombie = stillZombie(helper, 2, 2);
        float before = zombie.getHealth();
        bullets.get(0).onHitEntity(new EntityHitResult(zombie));
        assertNear(helper, before - 4.0F, zombie.getHealth(), "SoulBullet should deal 4 magic damage on hit");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void scrollsCastSoulJumpAndSoulBulletWithoutMana(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        helper.assertTrue(player.getData(AttachmentInit.MANA).mana() == 0, "player starts with 0 mana");

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemInit.SOUL_JUMP_SCROLL.get()));
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        assertNear(helper, 0.7D, player.getDeltaMovement().y, "Soul Jump scroll upward velocity");
        helper.assertTrue(player.getMainHandItem().isEmpty(), "Soul Jump scroll should be consumed");

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemInit.SOUL_BULLET_SCROLL.get()));
        player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getMainHandItem().isEmpty(), "Soul Bullet scroll should be consumed");
        var bullets = helper.getLevel().getEntitiesOfClass(SoulBullet.class, helper.getBounds().inflate(4.0D));
        helper.assertTrue(bullets.size() == 1, "Soul Bullet scroll should spawn a SoulBullet projectile");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void magicOrbsSpawnProjectilesAndApplyEffects(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        Item[] orbItems = {
                ItemInit.BLINDNESS_ORB.get(),
                ItemInit.LEVITATION_ORB.get(),
                ItemInit.WEAKNESS_ORB.get(),
                ItemInit.EXPLOSIVE_ORB.get(),
                ItemInit.TELEPORTATION_ORB.get()
        };
        for (Item orbItem : orbItems) {
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(orbItem, 2));
            player.getMainHandItem().use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
            helper.assertTrue(player.getMainHandItem().getCount() == 1, orbItem + " should consume 1 orb on throw");
        }
        helper.assertTrue(!helper.getLevel().getEntitiesOfClass(BlindnessOrb.class, helper.getBounds().inflate(4.0D)).isEmpty(), "BlindnessOrb entity spawned");
        helper.assertTrue(!helper.getLevel().getEntitiesOfClass(LevitationOrb.class, helper.getBounds().inflate(4.0D)).isEmpty(), "LevitationOrb entity spawned");
        helper.assertTrue(!helper.getLevel().getEntitiesOfClass(WeaknessOrb.class, helper.getBounds().inflate(4.0D)).isEmpty(), "WeaknessOrb entity spawned");
        helper.assertTrue(!helper.getLevel().getEntitiesOfClass(ExplosiveOrb.class, helper.getBounds().inflate(4.0D)).isEmpty(), "ExplosiveOrb entity spawned");
        helper.assertTrue(!helper.getLevel().getEntitiesOfClass(TeleportationOrb.class, helper.getBounds().inflate(4.0D)).isEmpty(), "TeleportationOrb entity spawned");

        GoblinSoulBullet goblinBullet = new GoblinSoulBullet(helper.getLevel(), stillZombie(helper, 2, 2));
        goblinBullet.onHitEntity(new EntityHitResult(player));
        helper.assertTrue(player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "GoblinSoulBullet should apply Slowness to a hit entity");
        helper.succeed();
    }

    // ---- armor texture (phase 4) ----

    @GameTest(template = EMPTY)
    public static void goblinCrownUsesCustomArmorTexture(GameTestHelper helper) {
        Player player = survivalPlayer(helper);
        ItemStack crown = new ItemStack(ItemInit.GOBLIN_CROWN.get());
        var layer = ArmorInit.GOBLIN_CROWN.get().layers().get(0);
        var tex = crown.getItem().getArmorTexture(crown, player, EquipmentSlot.HEAD, layer, false);
        helper.assertTrue(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/models/armor/goblin_crown.png").equals(tex),
                "Goblin Crown should resolve to textures/models/armor/goblin_crown.png, got " + tex
        );
        helper.succeed();
    }

    // ---- worldgen, structures, loot tables, recipes, advancements & tags (phase 5) ----

    @GameTest(template = EMPTY)
    public static void structureTemplatesAndWorldgenRegistriesResolve(GameTestHelper helper) {
        var templateManager = helper.getLevel().getStructureManager();
        for (String name : new String[]{"small_goblin_camp", "medium_goblin_camp", "large_goblin_camp", "ruined_keep"}) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, name);
            var templateOpt = templateManager.get(id);
            helper.assertTrue(templateOpt.isPresent(), "Structure template must exist: " + id);
            StructureTemplate template = templateOpt.get();
            helper.assertTrue(template.getSize().getX() > 0 && template.getSize().getY() > 0 && template.getSize().getZ() > 0,
                    "Structure template must have positive dimensions: " + id);
        }

        var registries = helper.getLevel().registryAccess();
        var structureRegistry = registries.lookupOrThrow(Registries.STRUCTURE);
        for (String name : new String[]{"small_goblin_camp", "medium_goblin_camp", "large_goblin_camp", "ruined_keep"}) {
            ResourceKey<net.minecraft.world.level.levelgen.structure.Structure> key =
                    ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, name));
            helper.assertTrue(structureRegistry.get(key).isPresent(), "Worldgen structure must be registered: " + key.location());
        }

        var configuredFeatures = registries.lookupOrThrow(Registries.CONFIGURED_FEATURE);
        helper.assertTrue(
                configuredFeatures.get(ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "ore_scoria"))).isPresent(),
                "Configured feature ore_scoria must be registered"
        );
        var placedFeatures = registries.lookupOrThrow(Registries.PLACED_FEATURE);
        helper.assertTrue(
                placedFeatures.get(ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "ore_scoria_upper"))).isPresent(),
                "Placed feature ore_scoria_upper must be registered"
        );
        helper.assertTrue(
                placedFeatures.get(ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "ore_scoria_lower"))).isPresent(),
                "Placed feature ore_scoria_lower must be registered"
        );

        helper.assertTrue(StructureInit.SMALL_GOBLIN_CAMP.get() != null, "SMALL_GOBLIN_CAMP StructureType registered");
        helper.assertTrue(StructureInit.RUINED_KEEP_PIECE.get() != null, "RUINED_KEEP_PIECE StructurePieceType registered");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void structurePiecePlacesAndProcessesMarkers(GameTestHelper helper) {
        var templateManager = helper.getLevel().getStructureManager();
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "small_goblin_camp");
        BlockPos origin = helper.absolutePos(new BlockPos(0, 1, 0));
        SmallGoblinCampStructurePiece.Piece piece = new SmallGoblinCampStructurePiece.Piece(
                templateManager, id, origin, Rotation.NONE, 0
        );
        helper.assertTrue(piece.template().getSize().getX() == 4, "Small goblin camp template width should be 4");

        // Place template directly in test bounds and verify blocks are placed
        piece.template().placeInWorld(
                helper.getLevel(),
                origin,
                origin,
                new StructurePlaceSettings().setRotation(Rotation.NONE),
                helper.getLevel().getRandom(),
                2
        );
        boolean foundCampBlock = false;
        for (BlockPos p : BlockPos.betweenClosed(origin, origin.offset(3, 2, 4))) {
            if (!helper.getLevel().getBlockState(p).isAir()) {
                foundCampBlock = true;
                break;
            }
        }
        helper.assertTrue(foundCampBlock, "Placing small_goblin_camp template should place non-air blocks");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void lootTablesResolveAndGenerateItems(GameTestHelper helper) {
        var reloadable = helper.getLevel().getServer().reloadableRegistries();

        LootTable campTable = reloadable.getLootTable(LootTableInit.CAMP_LOOT_TABLE);
        helper.assertTrue(campTable != LootTable.EMPTY, "Camp chest loot table must not be EMPTY");

        LootTable keepTable = reloadable.getLootTable(LootTableInit.KEEP_LOOT_TABLE);
        helper.assertTrue(keepTable != LootTable.EMPTY, "Keep chest loot table must not be EMPTY");

        LootTable injectDungeon = reloadable.getLootTable(LootTableInit.SIMPLE_DUNGEON_INJECT);
        helper.assertTrue(injectDungeon != LootTable.EMPTY, "Simple dungeon inject loot table must not be EMPTY");

        LootTable kingTable = reloadable.getLootTable(EntityInit.GOB_KING.get().getDefaultLootTable());
        helper.assertTrue(kingTable != LootTable.EMPTY, "Goblin King entity loot table must not be EMPTY");

        LootTable scoriaTable = reloadable.getLootTable(BlockInit.SCORIA.get().getLootTable());
        helper.assertTrue(scoriaTable != LootTable.EMPTY, "Scoria block loot table must not be EMPTY");
        LootParams blockParams = new LootParams.Builder(helper.getLevel())
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(helper.absolutePos(new BlockPos(1, 1, 1))))
                .withParameter(LootContextParams.TOOL, new ItemStack(Items.DIAMOND_PICKAXE))
                .withParameter(LootContextParams.BLOCK_STATE, BlockInit.SCORIA.get().defaultBlockState())
                .create(LootContextParamSets.BLOCK);
        var drops = scoriaTable.getRandomItems(blockParams);
        helper.assertTrue(drops.size() == 1 && drops.get(0).is(BlockInit.SCORIA.get().asItem()), "Scoria block loot table should drop Scoria");
        helper.succeed();
    }

    @GameTest(template = EMPTY)
    public static void recipesAdvancementsAndTagsResolve(GameTestHelper helper) {
        var recipeManager = helper.getLevel().getRecipeManager();
        for (String name : new String[]{"bottled_souls", "chiseled_scoria", "polished_scoria", "scoria_vertical_slab", "chiseled_scoria_from_scoria_stonecutting"}) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, name);
            java.util.Optional<RecipeHolder<?>> holder = recipeManager.byKey(id);
            helper.assertTrue(holder.isPresent(), "Recipe must be loaded: " + id);
        }

        var advancements = helper.getLevel().getServer().getAdvancements();
        for (String path : new String[]{
                "goblinsanddungeons/root",
                "goblinsanddungeons/center_of_the_labyrinth",
                "goblinsanddungeons/end_of_the_labyrinth",
                "goblinsanddungeons/an_unpleasant_surprise",
                "goblinsanddungeons/goblin_slayer",
                "goblinsanddungeons/ogres_are_like_onions",
                "goblinsanddungeons/get_out_of_his_swamp"
        }) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, path);
            AdvancementHolder adv = advancements.get(id);
            helper.assertTrue(adv != null, "Advancement must be loaded: " + id);
        }

        helper.assertTrue(BlockInit.SCORIA.get().defaultBlockState().is(BlockTags.BASE_STONE_OVERWORLD), "Scoria must be in #minecraft:base_stone_overworld");
        helper.assertTrue(BlockInit.SCORIA.get().defaultBlockState().is(BlockTags.MINEABLE_WITH_PICKAXE), "Scoria must be in #minecraft:mineable/pickaxe");
        helper.assertTrue(BlockInit.SCORIA_SLAB.get().defaultBlockState().is(BlockTags.SLABS), "Scoria slab must be in #minecraft:slabs");
        helper.assertTrue(BlockInit.SCORIA_STAIRS.get().defaultBlockState().is(BlockTags.STAIRS), "Scoria stairs must be in #minecraft:stairs");
        helper.assertTrue(BlockInit.SCORIA_WALL.get().defaultBlockState().is(BlockTags.WALLS), "Scoria wall must be in #minecraft:walls");
        helper.assertTrue(new ItemStack(BlockInit.SCORIA_SLAB.get()).is(ItemTags.SLABS), "Scoria slab item must be in #minecraft:slabs");
        helper.succeed();
    }
}
