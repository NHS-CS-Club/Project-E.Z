package net.fabricmc.projectez.event.client.render.hud;

import net.fabricmc.projectez.event.client.render.RenderEvent;
import net.minecraft.client.util.math.MatrixStack;

public class InGameHudRenderEvent extends RenderEvent {
    public InGameHudRenderEvent(MatrixStack matrixStack, float tickDelta) {
        super(matrixStack, tickDelta);
    }
}
