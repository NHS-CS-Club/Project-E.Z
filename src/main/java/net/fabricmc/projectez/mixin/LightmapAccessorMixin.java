package net.fabricmc.projectez.mixin;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LightmapTextureManager.class)
public interface LightmapAccessorMixin {
    @Accessor("image")
    NativeImage getImage();
    @Accessor("texture")
    NativeImageBackedTexture getTex();
}
