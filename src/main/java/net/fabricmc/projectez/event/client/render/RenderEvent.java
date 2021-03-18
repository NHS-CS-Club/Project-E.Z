package net.fabricmc.projectez.event.client.render;

import net.fabricmc.projectez.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class RenderEvent extends Event {

    public final MatrixStack matrixStack;
    public final float tickDelta;
    protected RenderEvent(MatrixStack matrixStack, float tickDelta) {
        this.matrixStack = matrixStack;
        this.tickDelta = tickDelta;
    }
    protected RenderEvent(MatrixStack matrixStack) { this(matrixStack, 0); }
}
