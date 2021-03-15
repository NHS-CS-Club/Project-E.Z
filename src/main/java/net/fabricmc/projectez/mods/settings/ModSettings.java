package net.fabricmc.projectez.mods.settings;

import net.fabricmc.projectez.util.ArrayListSet;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModSettings {
    protected final ModKeyBinding toggleKey;
    protected final Map<String,ModKeyBinding> keys = new HashMap<>();

    public ModSettings(int defaultKey) {
        this.toggleKey = new ModKeyBinding("translation.key.here", defaultKey);

    }

    public ModKeyBinding getToggleKey() {
        return toggleKey;
    }
    public ModKeyBinding getKey(String id) {
        return keys.get(id);
    }
    public Set<String> getAllKeys() {
        return keys.keySet();
    }

    public static class ModKeyBinding extends KeyBinding {
        public ModKeyBinding(String translationKey, int defaultKey) {
            super(translationKey, InputUtil.Type.KEYSYM, defaultKey, null);
        }
        public void setKey(int code) {
            setBoundKey(InputUtil.Type.KEYSYM.createFromCode(code));
        }
        public void resetKey() {
            setBoundKey(getDefaultKey());
        }
    }
}
