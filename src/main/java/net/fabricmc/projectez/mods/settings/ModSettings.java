package net.fabricmc.projectez.mods.settings;

import net.fabricmc.projectez.mods.Mod;
import net.fabricmc.projectez.util.ArrayListSet;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModSettings {
    public final Mod mod;
    protected final Parameter<Boolean> modEnabled;

    protected final ModKeyBinding toggleKey;
    protected final Map<String,ModKeyBinding> keys = new HashMap<>();

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

    public static class ModKeyBinding extends KeyBinding {
        public ModKeyBinding(String translationKey, int defaultKey) {
            super(translationKey, InputUtil.Type.KEYSYM, defaultKey, null);
        }
        public ModKeyBinding(String translationKey) {
            super(translationKey, -1, null);
            setKey(InputUtil.UNKNOWN_KEY);
        }
        public void setKey(int code) {
            setBoundKey(InputUtil.Type.KEYSYM.createFromCode(code));
        }
        public void setKey(InputUtil.Key key) {
            setBoundKey(key);
        }
        public void resetKey() {
            setBoundKey(getDefaultKey());
        }
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
    public interface K<T> {
        void run(T v);
    }
}
