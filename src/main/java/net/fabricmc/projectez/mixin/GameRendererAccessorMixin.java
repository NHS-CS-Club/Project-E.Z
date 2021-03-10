package net.fabricmc.projectez.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererAccessorMixin {
    @Invoker("getFov")
    double getFov_(Camera camera, float tickDelta, boolean changingFov);
}
