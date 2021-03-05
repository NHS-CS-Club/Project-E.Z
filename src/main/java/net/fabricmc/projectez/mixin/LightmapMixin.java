package net.fabricmc.projectez.mixin;

import net.fabricmc.projectez.mixinhooks.LightMapHook;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class LightmapMixin {
    @Redirect(at=@At(value = "INVOKE",target = "Lnet/minecraft/client/render/LightmapTextureManager;update(F)V"),method="renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V")
    public void updateMixin(LightmapTextureManager ltm, float delta) {
        LightMapHook.lightmapUpdate(ltm,delta);
    }
}

