package superlord.goblinsanddungeons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import superlord.goblinsanddungeons.common.blocks.SoulAshCampfireBlock;
import superlord.goblinsanddungeons.common.entity.tile.SoulAshCampfireTileEntity;

@OnlyIn(Dist.CLIENT)
public class SoulAshCampfireTileEntityRenderer implements BlockEntityRenderer<SoulAshCampfireTileEntity> {

    public SoulAshCampfireTileEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SoulAshCampfireTileEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Direction direction = entity.getBlockState().getValue(SoulAshCampfireBlock.FACING);
        NonNullList<ItemStack> nonnulllist = entity.getItems();
        int i = (int) entity.getBlockPos().asLong();

        for (int j = 0; j < nonnulllist.size(); ++j) {
            ItemStack itemstack = nonnulllist.get(j);
            if (itemstack != ItemStack.EMPTY) {
                poseStack.pushPose();
                poseStack.translate(0.5D, 0.44921875D, 0.5D);
                Direction direction1 = Direction.from2DDataValue((j + direction.get2DDataValue()) % 4);
                float f = -direction1.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(f));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.translate(-0.3125D, -0.3125D, 0.0D);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, entity.getLevel(), i + j);
                poseStack.popPose();
            }
        }
    }
}
