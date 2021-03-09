package net.fabricmc.projectez;

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
