package superlord.goblinsanddungeons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.client.model.BeholderModel;
import superlord.goblinsanddungeons.client.model.GarchModel;
import superlord.goblinsanddungeons.client.model.GobModel;
import superlord.goblinsanddungeons.client.model.GobberModel;
import superlord.goblinsanddungeons.client.model.GoblinKingModel;
import superlord.goblinsanddungeons.client.model.GobloModel;
import superlord.goblinsanddungeons.client.model.GoomModel;
import superlord.goblinsanddungeons.client.model.HiddenMimicModel;
import superlord.goblinsanddungeons.client.model.HobgobModel;
import superlord.goblinsanddungeons.client.model.MimicModel;
import superlord.goblinsanddungeons.client.model.OgreModel;
import superlord.goblinsanddungeons.client.model.SittingOgreModel;
import superlord.goblinsanddungeons.client.model.SleepingGobloModel;
import superlord.goblinsanddungeons.client.model.armor.GoblinCrownModel;
import superlord.goblinsanddungeons.client.renderer.BeholderRenderer;
import superlord.goblinsanddungeons.client.renderer.BlindnessOrbRenderer;
import superlord.goblinsanddungeons.client.renderer.ExplosiveOrbRenderer;
import superlord.goblinsanddungeons.client.renderer.FallingBlockRenderer;
import superlord.goblinsanddungeons.client.renderer.GarchRenderer;
import superlord.goblinsanddungeons.client.renderer.GobKingRenderer;
import superlord.goblinsanddungeons.client.renderer.GobRenderer;
import superlord.goblinsanddungeons.client.renderer.GobberRenderer;
import superlord.goblinsanddungeons.client.renderer.GoblinSoulBulletRenderer;
import superlord.goblinsanddungeons.client.renderer.GobloRenderer;
import superlord.goblinsanddungeons.client.renderer.GoomRenderer;
import superlord.goblinsanddungeons.client.renderer.HobGobRenderer;
import superlord.goblinsanddungeons.client.renderer.LevitationOrbRenderer;
import superlord.goblinsanddungeons.client.renderer.ManaGUIRenderer;
import superlord.goblinsanddungeons.client.renderer.MimicRenderer;
import superlord.goblinsanddungeons.client.renderer.OgreRenderer;
import superlord.goblinsanddungeons.client.renderer.SoulAshCampfireTileEntityRenderer;
import superlord.goblinsanddungeons.client.renderer.SoulBulletRenderer;
import superlord.goblinsanddungeons.client.renderer.TeleportationOrbRenderer;
import superlord.goblinsanddungeons.client.renderer.WeaknessOrbRenderer;
import superlord.goblinsanddungeons.common.particle.BlindnessOrbParticle;
import superlord.goblinsanddungeons.common.particle.GoblinSoulBulletParticle;
import superlord.goblinsanddungeons.common.particle.LevitationOrbParticle;
import superlord.goblinsanddungeons.common.particle.SoulBulletParticle;
import superlord.goblinsanddungeons.common.particle.TeleportationOrbParticle;
import superlord.goblinsanddungeons.init.BlockInit;
import superlord.goblinsanddungeons.init.EntityInit;
import superlord.goblinsanddungeons.init.ItemInit;
import superlord.goblinsanddungeons.init.ParticleInit;
import superlord.goblinsanddungeons.init.TileEntityInit;

@EventBusSubscriber(modid = GoblinsAndDungeons.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    public static final ModelLayerLocation GOB = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "gob"), "gob");
    public static final ModelLayerLocation GOBBER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "gobber"), "gobber");
    public static final ModelLayerLocation HOBGOB = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "hobgob"), "hobgob");
    public static final ModelLayerLocation GOBLO = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "goblo"), "goblo");
    public static final ModelLayerLocation SLEEPING_GOBLO = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "sleeping_goblo"), "sleeping_goblo");
    public static final ModelLayerLocation GARCH = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "garch"), "garch");
    public static final ModelLayerLocation GOOM = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "goom"), "goom");
    public static final ModelLayerLocation OGRE = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "ogre"), "ogre");
    public static final ModelLayerLocation SITTING_OGRE = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "sitting_ogre"), "sitting_ogre");
    public static final ModelLayerLocation MIMIC = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "mimic"), "mimic");
    public static final ModelLayerLocation HIDDEN_MIMIC = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "hidden_mimic"), "hidden_mimic");
    public static final ModelLayerLocation GOB_KING = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "gob_king"), "gob_king");
    public static final ModelLayerLocation GOBLIN_CROWN = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "goblin_crown"), "goblin_crown");
    public static final ModelLayerLocation BEHOLDER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "beholder"), "beholder");

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.GOB.get(), GobRenderer::new);
        event.registerEntityRenderer(EntityInit.HOBGOB.get(), HobGobRenderer::new);
        event.registerEntityRenderer(EntityInit.GOBLO.get(), GobloRenderer::new);
        event.registerEntityRenderer(EntityInit.GARCH.get(), GarchRenderer::new);
        event.registerEntityRenderer(EntityInit.GOOM.get(), GoomRenderer::new);
        event.registerEntityRenderer(EntityInit.GOBBER.get(), GobberRenderer::new);
        event.registerEntityRenderer(EntityInit.OGRE.get(), OgreRenderer::new);
        event.registerEntityRenderer(EntityInit.GOBLIN_SOUL_BULLET.get(), GoblinSoulBulletRenderer::new);
        event.registerEntityRenderer(EntityInit.SOUL_BULLET.get(), SoulBulletRenderer::new);
        event.registerEntityRenderer(EntityInit.TELEPORTATION_ORB.get(), TeleportationOrbRenderer::new);
        event.registerEntityRenderer(EntityInit.LEVITATION_ORB.get(), LevitationOrbRenderer::new);
        event.registerEntityRenderer(EntityInit.BLINDNESS_ORB.get(), BlindnessOrbRenderer::new);
        event.registerEntityRenderer(EntityInit.EXPLOSIVE_ORB.get(), ExplosiveOrbRenderer::new);
        event.registerEntityRenderer(EntityInit.WEAKNESS_ORB.get(), WeaknessOrbRenderer::new);
        event.registerEntityRenderer(EntityInit.GOB_KING.get(), GobKingRenderer::new);
        event.registerEntityRenderer(EntityInit.MIMIC.get(), MimicRenderer::new);
        event.registerEntityRenderer(EntityInit.BEHOLDER.get(), BeholderRenderer::new);
        event.registerEntityRenderer(EntityInit.FALLING_BLOCK.get(), FallingBlockRenderer::new);
        event.registerBlockEntityRenderer(TileEntityInit.SOUL_ASH_CAMPFIRE.get(), SoulAshCampfireTileEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GOB, GobModel::createBodyLayer);
        event.registerLayerDefinition(GOBBER, GobberModel::createBodyLayer);
        event.registerLayerDefinition(HOBGOB, HobgobModel::createBodyLayer);
        event.registerLayerDefinition(GOBLO, GobloModel::createBodyLayer);
        event.registerLayerDefinition(SLEEPING_GOBLO, SleepingGobloModel::createBodyLayer);
        event.registerLayerDefinition(GARCH, GarchModel::createBodyLayer);
        event.registerLayerDefinition(GOOM, GoomModel::createBodyLayer);
        event.registerLayerDefinition(OGRE, OgreModel::createBodyLayer);
        event.registerLayerDefinition(SITTING_OGRE, SittingOgreModel::createBodyLayer);
        event.registerLayerDefinition(MIMIC, MimicModel::createBodyLayer);
        event.registerLayerDefinition(HIDDEN_MIMIC, HiddenMimicModel::createBodyLayer);
        event.registerLayerDefinition(GOB_KING, GoblinKingModel::createBodyLayer);
        event.registerLayerDefinition(BEHOLDER, BeholderModel::createBodyLayer);
        event.registerLayerDefinition(GOBLIN_CROWN, () -> GoblinCrownModel.createArmorLayer(new CubeDeformation(0.5F)));
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleInit.GOB_SOUL_BULLET.get(), GoblinSoulBulletParticle.Provider::new);
        event.registerSpriteSet(ParticleInit.SOUL_BULLET.get(), SoulBulletParticle.Provider::new);
        event.registerSpriteSet(ParticleInit.BLINDNESS_ORB.get(), BlindnessOrbParticle.Provider::new);
        event.registerSpriteSet(ParticleInit.LEVITATION_ORB.get(), LevitationOrbParticle.Provider::new);
        event.registerSpriteSet(ParticleInit.TELEPORTATION_ORB.get(), TeleportationOrbParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private GoblinCrownModel crownModel;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.crownModel == null) {
                    this.crownModel = new GoblinCrownModel(Minecraft.getInstance().getEntityModels().bakeLayer(GOBLIN_CROWN));
                }
                return this.crownModel;
            }
        }, ItemInit.GOBLIN_CROWN.get());
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
                VanillaGuiLayers.FOOD_LEVEL,
                ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "mana_level"),
                ManaGUIRenderer::renderOverlay
        );
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(BlockInit.SOUL_ASH_CAMPFIRE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(BlockInit.SOUL_ASH_SOUL_CAMPFIRE.get(), RenderType.cutout());
        });
    }
}
