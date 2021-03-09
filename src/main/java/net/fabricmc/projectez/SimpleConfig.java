package net.fabricmc.projectez;

import me.sargunvohra.mcmods.autoconfig1.ConfigData;
import me.sargunvohra.mcmods.autoconfig1.annotation.Config;

@Config(name = "simplezoom")
public class SimpleConfig implements ConfigData
{
    private double zoomAmount = 5.0;
    private boolean smoothZoom = false;

    public boolean isSmoothZoom() {
        return smoothZoom;
    }

    public double getZoomAmount() {
        return zoomAmount;
    }
}
