package net.fabricmc.projectez.event.render;

import net.fabricmc.projectez.event.Event;
import net.fabricmc.projectez.mixin.LightmapTextureAccessorMixin;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;


public class LightmapUpdateEvent extends Event {

    private final NativeImage img;

    public LightmapUpdateEvent(LightmapTextureManager ltm) {
        super("Lightmap Texture Update");
        NativeImageBackedTexture tex = ((LightmapTextureAccessorMixin) ltm).getTex();
        img = tex.getImage();
    }


    public void setLightColor(int blockLight, int skyLight, int color) {
        img.setPixelColor(blockLight, skyLight, color);
    }
}
