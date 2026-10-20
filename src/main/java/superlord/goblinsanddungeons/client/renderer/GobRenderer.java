package superlord.goblinsanddungeons.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.client.ClientEvents;
import superlord.goblinsanddungeons.client.model.GobModel;
import superlord.goblinsanddungeons.common.entity.Gob;

public class GobRenderer extends MobRenderer<Gob, GobModel> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/entities/gob.png");

    public GobRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new GobModel(renderManagerIn.bakeLayer(ClientEvents.GOB)), 0.375F);
        this.addLayer(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(Gob entity) {
        return TEXTURE;
    }
}
