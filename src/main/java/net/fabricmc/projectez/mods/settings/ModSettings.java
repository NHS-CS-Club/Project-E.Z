package net.fabricmc.projectez.mods.settings;

import net.fabricmc.fabric.mixin.client.keybinding.KeyBindingAccessor;
import net.fabricmc.projectez.Main;
import net.fabricmc.projectez.mixin.KeyBindingAccessorMixin;
import net.fabricmc.projectez.mods.Mod;
import net.fabricmc.projectez.util.ArrayListSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.*;

public class ModSettings {
    public final Mod mod;
    protected final Parameter<Boolean> modEnabled;

    protected final ModKeyBinding toggleKey;
    protected final Map<String,ModKeyBinding> keys = new HashMap<>();

    private static final List<ModKeyBinding> customKeybindings = new ArrayList<>();

    public ModSettings(Mod mod, int defaultKey) {
        this.mod = mod;
        this.toggleKey = new ModKeyBinding("projectez.settings.modToggleKey", defaultKey);
        modEnabled = new Parameter<>("projectez.settings.enabled",false);
        modEnabled.setValue(mod.getEnabled());
    }
    public ModSettings(Mod mod) {
        this.mod = mod;
        this.toggleKey = new ModKeyBinding("projectez.settings.modToggleKey");
        modEnabled = new Parameter<>(mod.name,false);
        modEnabled.setValue(mod.getEnabled());
    }

    public Parameter<Boolean> getModEnabledParam() { return modEnabled; }

    public ModKeyBinding getToggleKey() { return toggleKey; }
    public ModKeyBinding getKey(String id) { return keys.get(id); }
    public void addKey(String id, ModKeyBinding key) {
        keys.put(id, key);
    }
    public Set<String> getAllKeys() { return keys.keySet(); }

    public static void updateCustomKeybindings() {
        for (ModKeyBinding keyBinding : customKeybindings)
            keyBinding.update();
    }

    public static class ModKeyBinding extends KeyBinding {
        public ModKeyBinding(String translationKey, int defaultKey) {
            super(translationKey, InputUtil.Type.KEYSYM, defaultKey, null);
            customKeybindings.add(this);
        }
        public ModKeyBinding(String translationKey) {
            super(translationKey, -1, null);
            setKey(InputUtil.UNKNOWN_KEY);
            customKeybindings.add(this);
        }
        public void setKey(int code) { setBoundKey(InputUtil.Type.KEYSYM.createFromCode(code)); }
        public void setKey(InputUtil.Key key) { setBoundKey(key); }
        public void resetKey() { setBoundKey(getDefaultKey()); }
        public int getCode() { return ((KeyBindingAccessorMixin)this).getBoundKey().getCode(); }
        boolean pressedLast = false;
        boolean pressedNow = false;
        public void update() {
            pressedLast = pressedNow;
            if (!isUnbound()) setPressed(InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), getCode()));
            pressedNow = isPressed();
        }
        @Override public boolean wasPressed() { return pressedNow&&!pressedLast; }
    }
    public static class Parameter<T> {
        public final T defaultValue;
        private T value;
        public final Text displayName;
        public Parameter(String translationKey, T defaultValue) {
            this.displayName = new TranslatableText(translationKey);
            this.defaultValue = defaultValue;
            value = defaultValue;
        }

        public T getValue() { return value; }
        public void setValue(T value) { this.value = value; }
        public void resetValue() { value = defaultValue; }
    }
    @FunctionalInterface
    public interface RunnableLikeWithParam<T> {
        void run(T v);
    }
}
