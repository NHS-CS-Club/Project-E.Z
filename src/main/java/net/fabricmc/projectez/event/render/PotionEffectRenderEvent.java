package net.fabricmc.projectez.event.render;

import net.fabricmc.projectez.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class PotionEffectRenderEvent extends Event {

    public final MatrixStack matrixStack;
    public PotionEffectRenderEvent(MatrixStack matrixStack) {
        super("InGameHud Render");
        this.matrixStack = matrixStack;
    }
}
