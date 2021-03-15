package net.fabricmc.projectez.gui;

import net.fabricmc.projectez.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SettingsGui extends Screen {

    public static List<ModSettingsGui> settingsGuis = new ArrayList<>();
    public static void addSettingGui(ModSettingsGui gui) {
        settingsGuis.add(gui);
    }

    private int selectedGui = 0;
    public SettingsGui() {
        super(Text.of("Mod Settings lol"));

    }

    @Override
    protected void init() {
        for (int i = 0; i < settingsGuis.size(); i++) {
            int n = i; // Make a copy of i.
            addButton(new ButtonWidget(0, (i+1) * 20, 200, 20, Text.of(settingsGuis.get(i).name), button -> selectedGui = n));
        }
        addButton(new ButtonWidget(0, 20, 200, 20, Text.of("gui.back"), button -> MinecraftClient.getInstance().openScreen(null)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        settingsGuis.get(selectedGui).render(matrices, mouseX, mouseY, delta);
    }
}
