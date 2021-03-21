package net.fabricmc.projectez.mods;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.event.client.render.hud.InGameHudRenderEvent;
import net.fabricmc.projectez.mods.settings.ModSettings;
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

    @Override
    protected void onInit() {
        settings.addParameter("showEnchants", new ModSettings.Parameter<>("projectez.armorHUD.showEnchants",true));
    }

    @EventHandler
    public void render(InGameHudRenderEvent e) {
        final PlayerEntity player = mc.player;
        RenderUtil.updateRenderers();

        if (player == null) return;

        final int xPos = mc.getWindow().getScaledWidth()/2+94, wHeight = mc.getWindow().getScaledHeight();

        final boolean SHOW_ENCHANTS = (Boolean) settings.getParameterValue("showEnchants");
        int yOff = 1;
        for (int i = 0; i < 4; i++) {
            yOff += 1+RenderUtil.getHUDItemRenderHeight(player.inventory.getArmorStack(i), SHOW_ENCHANTS);
            RenderUtil.renderHUDItem(new MatrixStack(), xPos, wHeight - yOff, player.inventory.getArmorStack(i), SHOW_ENCHANTS);
        }

        yOff += 10+RenderUtil.getHUDItemRenderHeight(player.getOffHandStack(), SHOW_ENCHANTS);
        RenderUtil.renderHUDItem(new MatrixStack(),xPos,wHeight-yOff,player.getOffHandStack(),SHOW_ENCHANTS);
        yOff += 1+RenderUtil.getHUDItemRenderHeight(player.getMainHandStack(), SHOW_ENCHANTS);
        RenderUtil.renderHUDItem(new MatrixStack(),xPos,wHeight-yOff,player.getMainHandStack(),SHOW_ENCHANTS);
    }

}
