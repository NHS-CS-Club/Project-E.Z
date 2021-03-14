package net.fabricmc.projectez.gui;

import net.fabricmc.projectez.Main;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class SettingsGui extends Screen {
    public SettingsGui() {
        super(Text.of("Mod Settings lol"));

    }

    @Override
    protected void init() {
        addButton(new ButtonWidget(1, 1, 200, 20, Text.of("Yeet"), button -> Main.LOGGER.info("yeet")));
        addButton(new ButtonWidget(1, 21, 200, 20, Text.of("close"), button -> MinecraftClient.getInstance().openScreen(null)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        super.render(matrices, mouseX, mouseY, delta);
    }
}
