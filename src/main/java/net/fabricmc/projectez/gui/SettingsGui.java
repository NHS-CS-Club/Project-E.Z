package net.fabricmc.projectez.gui;

import net.fabricmc.projectez.mods.settings.ModSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public class SettingsGui extends Screen {

    private static List<ModSettingsReference> settingsReferences = new ArrayList<>();
    public static void addSettingGui(String translationKey,ModSettings settings) {
        settingsReferences.add(new ModSettingsReference(translationKey,settings));
    }
    private List<ModSettingsGui> settingsGuis;

    private int selectedGui = 0;
    public SettingsGui() {
        super(Text.of("Mod Settings lol"));

    }

    @Override
    protected void init() {
        settingsGuis = new ArrayList<>();
        for (ModSettingsReference r : settingsReferences)
            settingsGuis.add(new ModSettingsGui(r.name, r.settings));

        for (int i = 0; i < settingsGuis.size(); i++) {
            int n = i; // Make a copy of i.
            addButton(new ButtonWidget(0, (i+1) * 20, 100, 20, Text.of(settingsGuis.get(i).name), button -> selectedGui = n));
        }
        addButton(new ButtonWidget(0, 0, 100, 20, new TranslatableText("gui.back"), button -> MinecraftClient.getInstance().openScreen(null)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (settingsGuis.size() > 0)
            settingsGuis.get(selectedGui).render(matrices, mouseX, mouseY, delta);
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (settingsGuis.size() > 0)
            settingsGuis.get(selectedGui).mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (settingsGuis.size() > 0)
            settingsGuis.get(selectedGui).mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }
    @Override public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (settingsGuis.size() > 0)
            settingsGuis.get(selectedGui).mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    @Override public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (settingsGuis.size() > 0)
            settingsGuis.get(selectedGui).mouseScrolled(mouseX, mouseY, amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    private static class ModSettingsReference {
        public final String name;
        public final ModSettings settings;

        private ModSettingsReference(String name, ModSettings settings) {
            this.name = name;
            this.settings = settings;
        }
    }
}
