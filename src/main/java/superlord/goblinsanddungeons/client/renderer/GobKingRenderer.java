package superlord.goblinsanddungeons.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.client.ClientEvents;
import superlord.goblinsanddungeons.client.model.GoblinKingModel;
import superlord.goblinsanddungeons.common.entity.GobKing;

public class GobKingRenderer extends MobRenderer<GobKing, GoblinKingModel<GobKing>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/entities/goblin_king.png");

    public GobKingRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new GoblinKingModel<>(renderManagerIn.bakeLayer(ClientEvents.GOB_KING)), 0.375F);
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(GobKing entity) {
        return TEXTURE;
    }
}
