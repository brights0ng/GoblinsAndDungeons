package superlord.goblinsanddungeons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.client.ClientEvents;
import superlord.goblinsanddungeons.client.model.BeholderModel;
import superlord.goblinsanddungeons.client.renderer.layer.Eye1Layer;
import superlord.goblinsanddungeons.client.renderer.layer.Eye2Layer;
import superlord.goblinsanddungeons.client.renderer.layer.Eye3Layer;
import superlord.goblinsanddungeons.client.renderer.layer.Eye4Layer;
import superlord.goblinsanddungeons.client.renderer.layer.Eye5Layer;
import superlord.goblinsanddungeons.common.entity.Beholder;

public class BeholderRenderer extends MobRenderer<Beholder, EntityModel<Beholder>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/entities/beholder/beholder.png");
    private static final ResourceLocation STUNNED_TEXTURE = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/entities/beholder/beholder_stunned.png");
    private static final ResourceLocation FIRE_BEAM_LOCATION = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/entities/beholder/fire_beam.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(FIRE_BEAM_LOCATION);
    private static final ResourceLocation PARALYSIS_BEAM_LOCATION = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/entities/beholder/fire_beam.png");
    private static final RenderType PARALYSIS_BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(PARALYSIS_BEAM_LOCATION);

    public BeholderRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BeholderModel(renderManagerIn.bakeLayer(ClientEvents.BEHOLDER)), 0.375F);
        this.addLayer(new Eye1Layer(this));
        this.addLayer(new Eye2Layer(this));
        this.addLayer(new Eye3Layer(this));
        this.addLayer(new Eye4Layer(this));
        this.addLayer(new Eye5Layer(this));
    }

    @Override
    public boolean shouldRender(Beholder entity, Frustum frustum, double camX, double camY, double camZ) {
        if (super.shouldRender(entity, frustum, camX, camY, camZ)) {
            return true;
        } else {
            if (entity.hasActiveAttackTarget()) {
                LivingEntity livingentity = entity.getActiveAttackTarget();
                if (livingentity != null) {
                    Vec3 vec3 = this.getPosition(livingentity, (double) livingentity.getBbHeight() * 0.5D, 1.0F);
                    Vec3 vec31 = this.getPosition(entity, (double) entity.getEyeHeight(), 1.0F);
                    return frustum.isVisible(new AABB(vec31.x, vec31.y, vec31.z, vec3.x, vec3.y, vec3.z));
                }
            }
            return false;
        }
    }

    private Vec3 getPosition(LivingEntity entity, double yOffset, float partialTicks) {
        double d0 = Mth.lerp((double) partialTicks, entity.xOld, entity.getX());
        double d1 = Mth.lerp((double) partialTicks, entity.yOld, entity.getY()) + yOffset;
        double d2 = Mth.lerp((double) partialTicks, entity.zOld, entity.getZ());
        return new Vec3(d0, d1, d2);
    }

    @Override
    public void render(Beholder entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        LivingEntity livingentity = entity.getActiveAttackTarget();
        double rot1 = entity.yBodyRot * (Math.PI / 180F);
        double rot2 = (Math.sin(rot1 - 6.5F * -1.2F) * 1.25F);
        double rot3 = (Math.cos(rot1 - 6.5F * -1.2F) * 1.25F);
        double rot4 = (Math.sin(rot1 - 6.5F * -1.2F) * 1.25F * 1.25F);
        double rot5 = (Math.cos(rot1 - 6.5F * -1.2F) * 1.25F * 1.25F);

        if (livingentity != null && entity.startUsingFireAttack() && entity.isPhase1()) {
            renderBeam(entity, livingentity, partialTicks, poseStack, bufferSource, BEAM_RENDER_TYPE, rot2, 2.7F, -rot3, -rot2, 0.0F, rot3);
        }
        if (livingentity != null && entity.startUsingParalysisAttack() && entity.isPhase1()) {
            renderBeam(entity, livingentity, partialTicks, poseStack, bufferSource, PARALYSIS_BEAM_RENDER_TYPE, -rot2, 2.7F, rot3, rot2, 0.0F, -rot3);
        }
        if (livingentity != null && entity.startUsingSlownessAttack() && entity.isPhase2()) {
            renderBeam(entity, livingentity, partialTicks, poseStack, bufferSource, BEAM_RENDER_TYPE, -rot4, 1.6F, rot5, rot4, 1.6F, -rot5);
        }
        if (livingentity != null && entity.startUsingEnergyAttack() && entity.isPhase2()) {
            renderBeam(entity, livingentity, partialTicks, poseStack, bufferSource, BEAM_RENDER_TYPE, rot4, 1.6F, -rot5, -rot4, 1.6F, rot5);
        }
        if (livingentity != null && entity.startUsingWitherAttack() && entity.isPhase2()) {
            renderBeam(entity, livingentity, partialTicks, poseStack, bufferSource, BEAM_RENDER_TYPE, 0.0D, 3.3F, 0.0D, 0.0D, -0.6F, 0.0D);
        }
    }

    private void renderBeam(Beholder entity, LivingEntity target, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, RenderType renderType, double tx, float ty, double tz, double targetOffsetX, float targetOffsetY, double targetOffsetZ) {
        float f = 0;
        float f1 = (float) entity.level().getGameTime() + partialTicks;
        float f2 = f1 * 0.5F % 1.0F;
        float f3 = entity.getEyeHeight();
        poseStack.pushPose();
        poseStack.translate(tx, ty, tz);
        Vec3 vec3 = new Vec3(target.getX() + targetOffsetX, target.getY() + targetOffsetY, target.getZ() + targetOffsetZ);
        Vec3 vec31 = this.getPosition(entity, (double) f3, partialTicks);
        Vec3 vec32 = vec3.subtract(vec31);
        float f4 = (float) (vec32.length());
        vec32 = vec32.normalize();
        float f5 = (float) Math.acos(vec32.y);
        float f6 = (float) Math.atan2(vec32.z, vec32.x);
        poseStack.mulPose(Axis.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
        poseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));
        float f7 = f1 * 0.05F * -1.5F;
        float f8 = f * f;
        int j = 255 + (int) (f8 * 255.0F);
        int k = 255 + (int) (f8 * 255.0F);
        int l = 255 + (int) (f8 * 255.0F);
        float f11 = Mth.cos(f7 + 2.3561945F) * 0.282F;
        float f12 = Mth.sin(f7 + 2.3561945F) * 0.282F;
        float f13 = Mth.cos(f7 + ((float) Math.PI / 4F)) * 0.282F;
        float f14 = Mth.sin(f7 + ((float) Math.PI / 4F)) * 0.282F;
        float f15 = Mth.cos(f7 + 3.926991F) * 0.282F;
        float f16 = Mth.sin(f7 + 3.926991F) * 0.282F;
        float f17 = Mth.cos(f7 + 5.4977875F) * 0.282F;
        float f18 = Mth.sin(f7 + 5.4977875F) * 0.282F;
        float f19 = Mth.cos(f7 + (float) Math.PI) * 0.2F;
        float f20 = Mth.sin(f7 + (float) Math.PI) * 0.2F;
        float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
        float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
        float f23 = Mth.cos(f7 + ((float) Math.PI / 2F)) * 0.2F;
        float f24 = Mth.sin(f7 + ((float) Math.PI / 2F)) * 0.2F;
        float f25 = Mth.cos(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
        float f26 = Mth.sin(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
        float f29 = -1.0F + f2;
        float f30 = f4 * 2.5F + f29;
        VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
        PoseStack.Pose posestack$pose = poseStack.last();

        vertex(vertexconsumer, posestack$pose, f19, f4, f20, j, k, l, 0.4999F, f30);
        vertex(vertexconsumer, posestack$pose, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
        vertex(vertexconsumer, posestack$pose, f21, 0.0F, f22, j, k, l, 0.0F, f29);
        vertex(vertexconsumer, posestack$pose, f21, f4, f22, j, k, l, 0.0F, f30);
        vertex(vertexconsumer, posestack$pose, f23, f4, f24, j, k, l, 0.4999F, f30);
        vertex(vertexconsumer, posestack$pose, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
        vertex(vertexconsumer, posestack$pose, f25, 0.0F, f26, j, k, l, 0.0F, f29);
        vertex(vertexconsumer, posestack$pose, f25, f4, f26, j, k, l, 0.0F, f30);

        float f31 = 0.0F;
        if (entity.tickCount % 2 == 0) {
            f31 = 0.5F;
        }

        vertex(vertexconsumer, posestack$pose, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
        vertex(vertexconsumer, posestack$pose, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
        vertex(vertexconsumer, posestack$pose, f17, f4, f18, j, k, l, 1.0F, f31);
        vertex(vertexconsumer, posestack$pose, f15, f4, f16, j, k, l, 0.5F, f31);
        poseStack.popPose();
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, int r, int g, int b, float u, float v) {
        consumer.addVertex(pose, x, y, z)
                .setColor(r, g, b, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(240)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Beholder entity) {
        return entity.isStunned() ? STUNNED_TEXTURE : TEXTURE;
    }
}
