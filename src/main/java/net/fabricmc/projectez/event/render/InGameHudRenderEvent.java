package net.fabricmc.projectez.event.render;

import net.fabricmc.projectez.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class InGameHudRenderEvent extends Event {

    public final MatrixStack matrixStack;
    public final float tickDelta;
    public InGameHudRenderEvent(MatrixStack matrixStack, float tickDelta) {
        super("InGameHud Render");
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
    }
}
