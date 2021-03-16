package net.fabricmc.projectez.mods;

import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.event.render.LightmapUpdateEvent;
import net.fabricmc.projectez.mods.Mod;

public class FullBrightMod extends Mod {
    public FullBrightMod() {
        super("projectez.fullBright",66);
    }

    @EventHandler()
    public void on(LightmapUpdateEvent e) {
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
                e.setLightColor(i,j,0xffffffff);
    }

}
