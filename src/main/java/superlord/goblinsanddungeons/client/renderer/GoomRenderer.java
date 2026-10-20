package superlord.goblinsanddungeons.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.client.ClientEvents;
import superlord.goblinsanddungeons.client.model.GoomModel;
import superlord.goblinsanddungeons.common.entity.Goom;

public class GoomRenderer extends MobRenderer<Goom, GoomModel<Goom>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/entities/goom.png");
    private static final ResourceLocation EXPLODED_TEXTURE = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/entities/goom_ignited.png");

    public GoomRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new GoomModel<>(renderManagerIn.bakeLayer(ClientEvents.GOOM)), 0.375F);
    }

    @Override
    public ResourceLocation getTextureLocation(Goom entity) {
        return entity.isBlownUp() ? EXPLODED_TEXTURE : TEXTURE;
    }
}
