package net.fabricmc.projectez.mods;

import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.event.render.LightmapUpdateEvent;

public class LightLevelDisplayMod extends Mod {

    public LightLevelDisplayMod() {
        super("projectez.lightmapMod");
    }

    @EventHandler()
    public static void lightmapUpdate(LightmapUpdateEvent e) {
        for(int blockLight = 0; blockLight < 16; ++blockLight) {
            for (int skyLight = 0; skyLight < 16; ++skyLight) {
                float r=1f,g=1f,b=1f;
                if (blockLight <= 7) {
                    g=0.5f;b=0f;
                    if (skyLight <= 7) {
                        g=0f;
                    }
                }
                e.setLightColor(blockLight,skyLight,(0xff<<24)|(((int)(b*255))<<16)|(((int)(g*255))<<8)|((int)(r*255)));
            }
        }
    }

}
