package net.fabricmc.projectez.event.client.render.hud;

import net.fabricmc.projectez.event.client.render.RenderEvent;
import net.minecraft.client.util.math.MatrixStack;

public class PotionEffectHudRenderEvent extends RenderEvent {
    public PotionEffectHudRenderEvent(MatrixStack matrixStack) { super(matrixStack); }
}
