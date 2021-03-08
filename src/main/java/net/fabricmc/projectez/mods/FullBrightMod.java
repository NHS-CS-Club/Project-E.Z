package net.fabricmc.projectez.mods;

import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.event.render.LightmapUpdateEvent;
import net.fabricmc.projectez.mods.Mod;

public class FullBrightMod extends Mod {
    public FullBrightMod() {
        super("FullBright");
    }

    @EventHandler()
    public void on(LightmapUpdateEvent e) {
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
                e.setLightColor(i,j,0xffffffff);
    }

    @Override protected void onEnable() { }
    @Override protected void onDisable() { }
    @Override protected void onInit() { }
    @Override protected void onCleanup() { }
}
