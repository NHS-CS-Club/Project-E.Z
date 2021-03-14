package net.fabricmc.projectez.mods;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.mixin.client.keybinding.KeyBindingAccessor;
import net.fabricmc.projectez.Main;
import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.event.render.CalculateFovEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;

public class SimpleZoomMod extends Mod {

    public static KeyBinding ZOOM_KEY;

    public SimpleZoomMod() {
        super("simple zoom");
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
        KeyBindingAccessor.fabric_getCategoryMap().put("category",4);
        ZOOM_KEY = new KeyBinding("a.translation.key",86, Main.MOD_ID);
        KeyBindingHelper.registerKeyBinding(ZOOM_KEY);
    }
}
