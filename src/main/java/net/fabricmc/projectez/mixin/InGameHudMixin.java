package net.fabricmc.projectez.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.projectez.event.Event;
import net.fabricmc.projectez.event.render.InGameHudRenderEvent;
import net.fabricmc.projectez.event.render.PotionEffectRenderEvent;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public class InGameHudMixin {

    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V",
            at=@At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;color4f(FFFF)V",
                    shift = At.Shift.BEFORE,
                    ordinal = 3
            )
    )
    public void renderEnd(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        Event.call(new InGameHudRenderEvent(matrixStack,tickDelta));
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V",
            at=@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/util/math/MatrixStack;)V"
            )
    )
    public void renderStatusEffectHUD(InGameHud hud,MatrixStack matrixStack) {
        Event e = new PotionEffectRenderEvent(matrixStack);
        Event.call(e);
        if (!e.getCancelled())
            ((InGameHudAccessorMixin)hud).renderPotionsOverlay(matrixStack);
    }
}
