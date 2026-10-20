package superlord.goblinsanddungeons.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.common.entity.Beholder;

public class Eye2Layer extends RenderLayer<Beholder, EntityModel<Beholder>> {

    private static final RenderType TEXTURE = RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/entities/beholder/eye_2_shut.png"));

    public Eye2Layer(RenderLayerParent<Beholder, EntityModel<Beholder>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Beholder beholder, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (beholder.isStalk2Shut()) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(TEXTURE);
            this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(beholder, 0.0F));
        }
    }
}
