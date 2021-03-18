package net.fabricmc.projectez.mixin;

import net.fabricmc.projectez.event.Event;
import net.fabricmc.projectez.event.client.CalculateFovEvent;
import net.fabricmc.projectez.event.client.LightmapUpdateEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Redirect(
            at=@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/LightmapTextureManager;update(F)V"
            ),
            method="renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V"
    )
    public void lightmapUpdate(LightmapTextureManager ltm, float delta) {
        ltm.update(delta);
        Event.call(new LightmapUpdateEvent(ltm));
        ((LightmapTextureAccessorMixin) ltm).getTex().upload();
    }


    @Redirect(
            method = "getBasicProjectionMatrix(Lnet/minecraft/client/render/Camera;FZ)Lnet/minecraft/util/math/Matrix4f;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/GameRenderer;getFov(Lnet/minecraft/client/render/Camera;FZ)D"
            )
    )
    public double onCalculateFov(GameRenderer gr,Camera cam, float k, boolean z) {
        double originalValue = ((GameRendererAccessorMixin)gr).getFov_(cam,k,z);
        CalculateFovEvent e = new CalculateFovEvent(originalValue);
        Event.call(e);
        return e.getValue();
    }
}

