package net.fabricmc.projectez.mods;

import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.event.render.CalculateFovEvent;
import net.fabricmc.projectez.mods.settings.ModSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;

public class SimpleZoomMod extends Mod {

    public static KeyBinding ZOOM_KEY;

    public SimpleZoomMod() {
        super("projectez.simpleZoom");
    }

    private boolean wasSmoothCamEnabled = false;
    private boolean wasPressed = false;
    @EventHandler
    public void on(CalculateFovEvent e) {
        if (ZOOM_KEY.isPressed()) {
            e.setValue(e.getValue() * 0.15);
            MinecraftClient.getInstance().options.smoothCameraEnabled = true;
            wasPressed = true;
        } else if (wasPressed) {
            wasPressed = false;
            MinecraftClient.getInstance().options.smoothCameraEnabled = wasSmoothCamEnabled;
        } else {
            wasSmoothCamEnabled = MinecraftClient.getInstance().options.smoothCameraEnabled;
        }
    }

    @Override
    protected void onInit() {
        settings.addKey("doZoom",new ModSettings.ModKeyBinding("projectez.simpleZoom.zoomKey",86));
        ZOOM_KEY = settings.getKey("doZoom");
    }
}
