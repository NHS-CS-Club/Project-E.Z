package net.fabricmc.projectez.mixin;

import net.fabricmc.projectez.event.Event;
import net.fabricmc.projectez.event.render.LightmapUpdateEvent;
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
}

