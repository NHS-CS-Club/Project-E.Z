package net.fabricmc.projectez.gui;

import net.fabricmc.projectez.mods.settings.ModSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class ModSettingsGui {
    public static final int X_OFFSET = 110;
    public static final int WIDTH = 300;

    public final ModSettings target;
    public final String name;

    private final List<Row> rows = new ArrayList<>();

    private double baseOff = 0;

    private KeyRow focusedKeyRow = null;

    private final SettingsGui parent;

    public ModSettingsGui(String name, ModSettings target, SettingsGui parent) {
        this.name = name;
        this.target = target;
        this.parent = parent;

        rows.add(new Row() {
            @Override public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, int yOff) {
                TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
                renderer.draw(matrices, new TranslatableText(ModSettingsGui.this.name), WIDTH/2f-renderer.getWidth(ModSettingsGui.this.name)/2f, getHeight() / 2f - renderer.fontHeight / 2f, 0xffffffff);
            }
            @Override public int getHeight() { return 20; }
            @Override public void key(int key, int scanCode, int modifiers) { }
            @Override public void click(double mouseX, double mouseY, int button) { }
            @Override public void release(double mouseX, double mouseY, int button) { }
            @Override public void drag(double mouseX, double mouseY, int button, double deltaX, double deltaY) { }
        });
        rows.add(new BoolRow(this.target.mod::setEnabled, target.getModEnabledParam()));
        rows.add(new KeyRow(target.getToggleKey()));
        for (String id : target.getAllKeys())
            rows.add(new KeyRow(id));
        for (String id : target.getAllParameters()) {
            ModSettings.Parameter<?> v = target.getParameter(id);
            if (v.parameterType == Boolean.class) // noinspection unchecked
                rows.add(new BoolRow(b->{},(ModSettings.Parameter<Boolean>) v));
        }
    }

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

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (focusedKeyRow != null) {
            if (keyCode==256) focusedKeyRow.update(InputUtil.UNKNOWN_KEY);
            else              focusedKeyRow.update(InputUtil.Type.KEYSYM.createFromCode(keyCode));
            return true;
        }
        for (Row row : rows) { row.key(keyCode,scanCode,modifiers); }
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (focusedKeyRow != null) {
            focusedKeyRow.update(InputUtil.Type.MOUSE.createFromCode(button));
            return true;
        }
        int yOff = (int) baseOff;
        for (Row row : rows) { row.click(mouseX-X_OFFSET, mouseY-yOff, button); yOff += row.getHeight(); }
        return false;
    }
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        int yOff = (int) baseOff;
        for (Row row : rows) { row.release(mouseX-X_OFFSET, mouseY-yOff, button); yOff += row.getHeight(); }
        return false;
    }
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int yOff = (int) baseOff;
        for (Row row : rows) { row.drag(mouseX-X_OFFSET, mouseY-yOff, button, deltaX, deltaY); yOff += row.getHeight(); }
        return false;
    }
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        baseOff += amount*10;
        int h = 0;
        for (Row row : rows) h += row.getHeight();
        baseOff = Math.max(parent.height - h, baseOff);
        baseOff = Math.min(0, baseOff);
        return false;
    }

    private interface Row {
        int getHeight();
        void key(int key, int scanCode, int modifiers);
        void render(MatrixStack matrices, int mouseX, int mouseY, float delta, int yOff);
        void click(double mouseX, double mouseY, int button);
        void release(double mouseX, double mouseY, int button);
        void drag(double mouseX, double mouseY, int button, double deltaX, double deltaY);
    }

    private class KeyRow implements Row {
        public final String id;
        public final ModSettings.ModKeyBinding binding;
        public final AbstractButtonWidget button;
        public final AbstractButtonWidget resetButton;

        private KeyRow(String id) { this(id, ModSettingsGui.this.target.getKey(id)); }
        private KeyRow(ModSettings.ModKeyBinding key) { this(null,key); }
        private KeyRow(String id, ModSettings.ModKeyBinding key) {
            this.id = id;
            binding = key;

            button = new ButtonWidget(WIDTH-136,0,76,20, binding.getBoundKeyLocalizedText(), b->{
                focusedKeyRow = this;
                b.setMessage((new LiteralText("> ")).append(b.getMessage().shallowCopy().formatted(Formatting.YELLOW)).append(" <").formatted(Formatting.YELLOW));
            });
            resetButton = new ButtonWidget(WIDTH-60,0,50,20,new TranslatableText("controls.reset"), b->{
                binding.resetKey();
                button.setMessage(binding.getBoundKeyLocalizedText());
            });
        }

        @Override public int getHeight() { return 20; }
        @Override public void click(double mouseX, double mouseY, int button) { this.button.mouseClicked(mouseX,mouseY,button); this.resetButton.mouseClicked(mouseX,mouseY,button); }
        @Override public void release(double mouseX, double mouseY, int button) { this.button.mouseReleased(mouseX,mouseY,button); this.resetButton.mouseReleased(mouseX,mouseY,button); }
        @Override public void drag(double mouseX, double mouseY, int button, double deltaX, double deltaY) { this.button.mouseDragged(mouseX,mouseY,button,deltaX,deltaY); this.resetButton.mouseDragged(mouseX,mouseY,button,deltaX,deltaY); }
        @Override public void key(int key, int scanCode, int modifiers) { }

        @Override public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, int yOff) {
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
            float calcX = 10;
            float calcY = (float)(getHeight() / 2 - renderer.fontHeight / 2);
            renderer.draw(matrices, new TranslatableText(binding.getTranslationKey()), calcX, calcY, 0xffffffff);
            button.render(matrices, mouseX, mouseY-yOff, delta);
            resetButton.render(matrices,mouseX, mouseY-yOff, delta);
        }

        public void update(InputUtil.Key keyCode) {
            binding.setKey(keyCode);
            button.setMessage(binding.getBoundKeyLocalizedText());
            focusedKeyRow = null;
        }
    }
    private class BoolRow implements Row {
        public final ModSettings.RunnableLikeWithParam<Boolean> callback;
        public final ModSettings.Parameter<Boolean> parameter;
        public final AbstractButtonWidget button;
        public final AbstractButtonWidget resetButton;

        private BoolRow(ModSettings.RunnableLikeWithParam<Boolean> callback, ModSettings.Parameter<Boolean> parameter) {
            this.callback = callback;
            this.parameter = parameter;

            button = new ButtonWidget(WIDTH-136,0,76,20, new TranslatableText(this.parameter.getValue()?"projectez.settings.true":"projectez.settings.false"), b->{
                this.parameter.setValue(!this.parameter.getValue());
                b.setMessage(new TranslatableText(this.parameter.getValue()?"projectez.settings.true":"projectez.settings.false"));
                this.callback.run(this.parameter.getValue());
            });
            resetButton = new ButtonWidget(WIDTH-60,0,50,20,new TranslatableText("controls.reset"), b->{
                this.parameter.resetValue();
                button.setMessage(new TranslatableText(this.parameter.getValue()?"projectez.settings.true":"projectez.settings.false"));
                this.callback.run(this.parameter.getValue());
            });
        }

        @Override public int getHeight() { return 20; }
        @Override public void click(double mouseX, double mouseY, int button) { this.button.mouseClicked(mouseX,mouseY,button); this.resetButton.mouseClicked(mouseX,mouseY,button); }
        @Override public void release(double mouseX, double mouseY, int button) { this.button.mouseReleased(mouseX,mouseY,button); this.resetButton.mouseReleased(mouseX,mouseY,button); }
        @Override public void drag(double mouseX, double mouseY, int button, double deltaX, double deltaY) { this.button.mouseDragged(mouseX,mouseY,button,deltaX,deltaY); this.resetButton.mouseDragged(mouseX,mouseY,button,deltaX,deltaY); }
        @Override public void key(int key, int scanCode, int modifiers) { }

        @Override public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, int yOff) {
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
            float calcX = 10;
            float calcY = (float)(getHeight() / 2 - 9 / 2);
            renderer.draw(matrices, parameter.displayName, calcX, calcY, 0xffffffff);
            button.render(matrices, mouseX, mouseY-yOff, delta);
            resetButton.render(matrices,mouseX, mouseY-yOff, delta);
        }
    }
}
