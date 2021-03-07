package net.fabricmc.projectez.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InGameHud.class)
public interface InGameHudAccessorMixin {
    @Invoker("renderStatusEffectOverlay")
    void renderPotionsOverlay(MatrixStack matrixStack);
}
