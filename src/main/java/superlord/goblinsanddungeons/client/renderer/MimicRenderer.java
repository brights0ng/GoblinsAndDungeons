package superlord.goblinsanddungeons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.client.ClientEvents;
import superlord.goblinsanddungeons.client.model.HiddenMimicModel;
import superlord.goblinsanddungeons.client.model.MimicModel;
import superlord.goblinsanddungeons.common.entity.Mimic;

public class MimicRenderer extends MobRenderer<Mimic, EntityModel<Mimic>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/entities/mimic.png");
    private static MimicModel<?> MIMIC;
    private static HiddenMimicModel<?> HIDDEN_MIMIC;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MimicRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HiddenMimicModel(renderManager.bakeLayer(ClientEvents.HIDDEN_MIMIC)), 0.0F);
        MIMIC = new MimicModel(renderManager.bakeLayer(ClientEvents.MIMIC));
        HIDDEN_MIMIC = new HiddenMimicModel(renderManager.bakeLayer(ClientEvents.HIDDEN_MIMIC));
    }

    @Override
    protected void scale(Mimic entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        if (entitylivingbaseIn.isHiding()) {
            model = HIDDEN_MIMIC;
        } else {
            model = MIMIC;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Mimic entity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(Mimic entity, Frustum frustum, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    protected void setupRotations(Mimic entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot + 180.0F, partialTick, scale);
    }
}
