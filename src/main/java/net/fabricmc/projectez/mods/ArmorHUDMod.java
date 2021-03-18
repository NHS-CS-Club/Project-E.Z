package net.fabricmc.projectez.mods;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.event.client.render.hud.PotionEffectHudRenderEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Environment(EnvType.CLIENT)
public class ArmorHUDMod extends Mod {
    private final MinecraftClient mc;

    public ArmorHUDMod() {
        super("projectez.armorHUD");
        mc = MinecraftClient.getInstance();
    }

    @EventHandler
    public void render(PotionEffectHudRenderEvent e) {
        e.cancel();

        final PlayerEntity player = mc.player;
        final InGameHud inGameHud = mc.inGameHud;
        final TextRenderer textRenderer = mc.textRenderer;
        final MatrixStack matrixStack = new MatrixStack();


        if (player == null) return;

        renderStack(e.matrixStack,player.getMainHandStack());
    }

    private void renderStack(MatrixStack matrixStack, ItemStack stack) {
        ItemRenderer renderer = mc.getItemRenderer();
        renderer.renderInGui(stack,0,0);

    }
}
