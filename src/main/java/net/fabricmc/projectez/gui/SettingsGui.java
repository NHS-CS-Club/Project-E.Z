package net.fabricmc.projectez.gui;

import net.fabricmc.projectez.Main;
import net.fabricmc.projectez.mods.settings.ModSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public class SettingsGui extends Screen {

    private static final List<ModSettingsReference> settingsReferences = new ArrayList<>();
    public static void addSettingGui(String translationKey,ModSettings settings) {
        settingsReferences.add(new ModSettingsReference(translationKey,settings));
    }
    private final List<ModSettingsGui> settingsGuis;

    private int selectedGui = 0;
    public int xOff = 0, yOff = 40;
    public SettingsGui() {
        super(Text.of("Mod Settings lol"));
        settingsGuis = new ArrayList<>();
        for (ModSettingsReference r : settingsReferences)
            settingsGuis.add(new ModSettingsGui(r.name, r.settings,this));
        guiSelectors = new ButtonWidget[settingsGuis.size()];
    }

    private final ButtonWidget[] guiSelectors;
    @Override protected void init() {
        for (int i = 0; i < settingsGuis.size(); i++) {
            int n = i; // Make a copy of i.
            guiSelectors[i] = addButton(new ButtonWidget(0, 15 + (i+1) * 20, 100, 20, new TranslatableText(settingsGuis.get(i).name), button -> selectedGui = n));
        }
        addButton(new ButtonWidget(0, 0, 100, 20, new TranslatableText("gui.back"), button -> MinecraftClient.getInstance().openScreen(null)));
        guiSelectors[selectedGui].active = false;
    }

    @Override public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        renderBackground(matrices);
        Text titleText = new TranslatableText("projectez.settings");
        renderer.draw(matrices, titleText, width/2f-renderer.getWidth(titleText.getString())/2f, 15 - renderer.fontHeight/2f, 0xffffffff);
        matrices.push();
        matrices.translate(xOff,yOff,0);
        xOff = width/2-(ModSettingsGui.X_OFFSET+ModSettingsGui.WIDTH)/2;
        fillGradient(matrices,ModSettingsGui.X_OFFSET,0,ModSettingsGui.X_OFFSET+ModSettingsGui.WIDTH,height,0x30000000,0x20000000);
        fillGradient(matrices,0,0,100,height,0x30000000,0x20000000);
        super.render(matrices, mouseX-xOff, mouseY-yOff, delta);
        if (settingsGuis.size() > 0)
            settingsGuis.get(selectedGui).render(matrices, mouseX-xOff, mouseY-yOff, delta);
        matrices.pop();
    }
    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (settingsGuis.size() > 0)
            if (settingsGuis.get(selectedGui).keyPressed(keyCode, scanCode, modifiers)) return false;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (settingsGuis.size() > 0)
            if (settingsGuis.get(selectedGui).mouseClicked(mouseX-xOff, mouseY-yOff, button)) return false;
        boolean v = super.mouseClicked(mouseX-xOff, mouseY-yOff, button);
        for (int i = 0; i < guiSelectors.length; i++)
            guiSelectors[i].active = i!=selectedGui;
        return v;
    }
    @Override public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (settingsGuis.size() > 0) settingsGuis.get(selectedGui).mouseReleased(mouseX-xOff, mouseY-yOff, button);
        return super.mouseReleased(mouseX-xOff, mouseY-yOff, button);
    }
    @Override public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (settingsGuis.size() > 0) settingsGuis.get(selectedGui).mouseDragged(mouseX-xOff, mouseY-yOff, button, deltaX, deltaY);
        return super.mouseDragged(mouseX-xOff, mouseY-yOff, button, deltaX, deltaY);
    }
    @Override public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (settingsGuis.size() > 0) settingsGuis.get(selectedGui).mouseScrolled(mouseX-xOff, mouseY-yOff, amount);
        return super.mouseScrolled(mouseX-xOff, mouseY-yOff, amount);
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
