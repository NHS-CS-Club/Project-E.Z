package net.fabricmc.projectez.mods;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.event.client.render.hud.InGameHudRenderEvent;
import net.fabricmc.projectez.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public class ArmorHUDMod extends Mod {
    private final MinecraftClient mc;


    public ArmorHUDMod() {
        super("projectez.armorHUD");
        mc = MinecraftClient.getInstance();
    }

    @EventHandler
    public void render(InGameHudRenderEvent e) {
        final PlayerEntity player = mc.player;
        RenderUtil.updateRenderers();

        if (player == null) return;

        final int xPos = mc.getWindow().getScaledWidth()/2+94, wHeight = mc.getWindow().getScaledHeight();

        RenderUtil.renderStack(new MatrixStack(),xPos,wHeight-130,player.getMainHandStack());
        RenderUtil.renderStack(new MatrixStack(),xPos,wHeight-110,player.getOffHandStack());

        for (int i = 0; i < 4; i++)
            RenderUtil.renderStack(new MatrixStack(),xPos,wHeight-20*(i+1),player.inventory.getArmorStack(i));
    }

}
