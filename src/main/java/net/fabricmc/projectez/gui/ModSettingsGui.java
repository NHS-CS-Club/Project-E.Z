package net.fabricmc.projectez.gui;

import net.fabricmc.projectez.Main;
import net.fabricmc.projectez.mods.settings.ModSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModSettingsGui extends Screen {
    public static final int X_OFFSET = 100;

    public final ModSettings target;
    public final String name;

    private final List<Row> rows = new ArrayList<>();

    private double baseOff = 0;

    public ModSettingsGui(String name, ModSettings target) {
        super(new TranslatableText("translation.key.here"));
        this.name = name;
        this.target = target;

        rows.add(new KeyRow(target.getToggleKey()));
        for (String id : target.getAllKeys())
            rows.add(new KeyRow(id));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        matrices.push();
        int yOff = (int) baseOff;
        matrices.translate(X_OFFSET,yOff,0);
        for (Row row : rows) {
            row.render(matrices, mouseX-X_OFFSET, mouseY, delta, yOff);
            yOff+=row.getHeight();
            matrices.translate(0,row.getHeight(),0);
        }
        matrices.pop();
    }

    @Override
    public boolean shouldCloseOnEsc() { return false; }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int yOff = (int) baseOff;
        for (Row row : rows) {
            row.click(mouseX-X_OFFSET, mouseY-yOff, button);
            yOff+=row.getHeight();
        }
        return false;
    }
    @Override public boolean mouseReleased(double mouseX, double mouseY, int button) {
        int yOff = (int) baseOff;
        for (Row row : rows) {
            row.release(mouseX-X_OFFSET, mouseY-yOff, button);
            yOff+=row.getHeight();
        }
        return false;
    }
    @Override public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int yOff = (int) baseOff;
        for (Row row : rows) {
            row.drag(mouseX-X_OFFSET, mouseY-yOff, button, deltaX, deltaY);
            yOff+=row.getHeight();
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        baseOff += amount;
        return false;
    }

    private interface Row {
        int getHeight();
        void render(MatrixStack matrices, int mouseX, int mouseY, float delta, int yOff);
        void click(double mouseX, double mouseY, int button);
        void release(double mouseX, double mouseY, int button);
        void drag(double mouseX, double mouseY, int button, double deltaX, double deltaY);
    }

    private class KeyRow implements Row {
        public final String id;
        public final ModSettings.ModKeyBinding binding;

        public final AbstractButtonWidget button;

        private KeyRow(String id) { this(id, ModSettingsGui.this.target.getKey(id)); }
        private KeyRow(ModSettings.ModKeyBinding key) { this(null,key); }
        private KeyRow(String id, ModSettings.ModKeyBinding key) {
            this.id = id;
            binding = key;
            button = new ButtonWidget(100,0,200,20, new TranslatableText(binding.getBoundKeyTranslationKey()), ButtonWidget->{
                Main.LOGGER.info("test button text");
                Main.LOGGER.info("YEET");
            });
        }

        @Override
        public int getHeight() { return 20; }


        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, int yOff) {
            button.render(matrices, mouseX, mouseY-yOff, delta);
        }
        @Override
        public void click(double mouseX, double mouseY, int button) {
            this.button.mouseClicked(mouseX,mouseY,button);
        }
        @Override
        public void release(double mouseX, double mouseY, int button) {
            this.button.mouseReleased(mouseX,mouseY,button);
        }
        @Override
        public void drag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            this.button.mouseDragged(mouseX,mouseY,button,deltaX,deltaY);
        }
    }
}
