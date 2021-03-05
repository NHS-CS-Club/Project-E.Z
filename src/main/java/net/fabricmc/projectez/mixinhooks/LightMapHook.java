package net.fabricmc.projectez.mixinhooks;

import net.fabricmc.projectez.mixin.LightmapAccessorMixin;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;

import java.util.Random;

public class LightMapHook {

    public static void lightmapUpdate(LightmapTextureManager ltm, float delta) {
        NativeImage image = ((LightmapAccessorMixin)ltm).getImage();
        NativeImageBackedTexture tex = ((LightmapAccessorMixin)ltm).getTex();
        //ltm.update(delta);
        Random rng = new Random();
        for(int blockLight = 0; blockLight < 16; ++blockLight) {
            for (int skyLight = 0; skyLight < 16; ++skyLight) {
                float r=1f,g=1f,b=1f;

                if (blockLight <= 7) {
                    r=1f;g=0.5f;b=0f;
                    if (skyLight <= 7) {
                        r=1f;g=0f;b=0f;
                    }
                }


                image.setPixelColor(blockLight,skyLight,(0xff<<24)|(((int)(b*255))<<16)|(((int)(g*255))<<8)|((int)(r*255)));
            }
        }
        tex.upload();
        //if (rng.nextBoolean())
            //ltm.update(delta);
    }
}
