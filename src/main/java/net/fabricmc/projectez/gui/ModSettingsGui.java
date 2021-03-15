package net.fabricmc.projectez.gui;

import net.fabricmc.projectez.mods.settings.ModSettings;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ModSettingsGui {
    public final ModSettings target;
    public final String name;

    private final List<Row> rows = new ArrayList<>();

    public ModSettingsGui(String name, ModSettings target) {
        this.name = name;
        this.target = target;

        for (String id : target.getAllKeys())
            rows.add(new KeyRow(id));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int yOff = 0;
        for (Row row : rows) {
            row.render(yOff);
            yOff+=row.getHeight();
        }
    }

    private interface Row {
        int getHeight();
        void render(int yOff);
    }

    private class KeyRow implements Row {
        public final String id;
        public final ModSettings.ModKeyBinding binding;

        public final AbstractButtonWidget button;

        private KeyRow(String id) {
            this.id = id;
            binding = ModSettingsGui.this.target.getKey(id);

            button = new ButtonWidget(0,0,20,20, Text.of(binding.getBoundKeyTranslationKey()), ButtonWidget->{

            });
        }

        @Override
        public int getHeight() {
            return 20;
        }

        @Override
        public void render(int yOff) {

        }
    }
}
