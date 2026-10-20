package superlord.goblinsanddungeons.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import superlord.goblinsanddungeons.GoblinsAndDungeons;
import superlord.goblinsanddungeons.config.GoblinsDungeonsConfig;
import superlord.goblinsanddungeons.init.AttachmentInit;

@OnlyIn(Dist.CLIENT)
public class ManaGUIRenderer {

    public static final ResourceLocation GUI_ICONS = ResourceLocation.fromNamespaceAndPath(GoblinsAndDungeons.MOD_ID, "textures/gui/icons.png");

    public static void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || !GoblinsDungeonsConfig.magicalWorld) {
            return;
        }
        boolean isMounted = player.getVehicle() instanceof LivingEntity;
        if (!isMounted && !minecraft.options.hideGui && minecraft.gameMode != null && minecraft.gameMode.canHurtPlayer()) {
            RenderSystem.enableBlend();
            int screenWidth = guiGraphics.guiWidth();
            int screenHeight = guiGraphics.guiHeight();
            int left = screenWidth / 2 + 91;
            int top = screenHeight - minecraft.gui.rightHeight;
            int mana = player.getData(AttachmentInit.MANA).mana();
            minecraft.getProfiler().push("mana");
            for (int i = 0; i < 10; ++i) {
                int x = left - i * 8 - 9;
                guiGraphics.blit(GUI_ICONS, x, top, 16, 54, 9, 9);
                if (i * 2 + 1 < mana) {
                    guiGraphics.blit(GUI_ICONS, x, top, 52, 54, 9, 9);
                } else if (i * 2 + 1 == mana) {
                    guiGraphics.blit(GUI_ICONS, x, top, 61, 54, 9, 9);
                }
            }
            minecraft.getProfiler().pop();
            minecraft.gui.rightHeight += 10;
            RenderSystem.disableBlend();
        }
    }
}
