package net.fabricmc.projectez.mods;

import net.fabricmc.projectez.event.Event;
import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.gui.SettingsGui;
import net.fabricmc.projectez.mods.settings.ModSettings;
import net.fabricmc.projectez.util.ArrayListSet;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public abstract class Mod {

    private boolean enabled = false;
    private boolean initialized = false;
    private boolean cleaned = false;

    public final String name;
    private final EventMethod[] eventCallbacks;

    private static final Set<Integer> priorityLevels = new ArrayListSet<>();

    protected final ModSettings settings;

    public Mod(String name) {
        this(name,0xdeadbeef);
    }
    @SuppressWarnings("unchecked")
    public Mod(String name, int defaultToggleKey) {
        this.name = name;
        if (defaultToggleKey != 0xdeadbeef)
            settings = new ModSettings(this,defaultToggleKey);
        else settings = new ModSettings(this);

        Method[] methods = getClass().getDeclaredMethods();
        List<EventMethod> eventMethods = new ArrayList<>();
        for (Method method : methods)
            if (method.getAnnotation(EventHandler.class) != null && method.getParameterCount() == 1)
                if (Event.class.isAssignableFrom(method.getParameterTypes()[0]))
                    eventMethods.add(new EventMethod((Class<? extends Event>) method.getParameterTypes()[0], method, method.getAnnotation(EventHandler.class)));
        eventCallbacks = new EventMethod[eventMethods.size()];
        for (int i = 0; i < eventCallbacks.length; i++)
            eventCallbacks[i] = eventMethods.get(i);
        for (EventMethod ev : eventCallbacks)
            priorityLevels.add(ev.priority());

    }

    public final void setEnabled(boolean enabled) {
        if (cleaned || !initialized) throw new IllegalStateException();
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        settings.getModEnabledParam().setValue(enabled);
        if (enabled) onEnable();
        else onDisable();
    }
    public final boolean getEnabled() { return enabled; }
    public final void init() {
        if (cleaned || initialized) throw new IllegalStateException();
        initialized = true;
        SettingsGui.addSettingGui(name, settings);
        onInit();
    }
    public final void cleanup() {
        if (cleaned || !initialized) throw new IllegalStateException();
        cleaned = true;
        setEnabled(false);
        onCleanup();
    }

    protected void onEnable() { }
    protected void onDisable() { }
    protected void onInit() { }
    protected void onCleanup() { }
    protected void onTick(TickData e) { }
    protected void onPreTick(TickData e) { }

    public void tick(boolean isPreTick, TickData e) { if (isPreTick) onPreTick(e); else onTick(e); }

    public final void callEvent(Event e, int priority) throws InvocationTargetException {
        for (EventMethod callback : eventCallbacks)
            if (callback.type.isInstance(e) && callback.priority() == priority && (enabled || callback.runWhenDisabled()))
                try {
                    callback.method.setAccessible(true);
                    callback.method.invoke(this, e);
                } catch (IllegalAccessException ex) { /* shouldn't happen */ }
    }

    public static int[] sortedPriorityLevels() {
        Stream<Integer> sorted = priorityLevels.stream().sorted();
        Iterator<Integer> iterator = sorted.iterator();
        int[] out = new int[priorityLevels.size()];
        for (int i = 0; i < out.length; i++)
            out[i] = iterator.next();
        return out;
    }

    public ModSettings getSettings() { return settings; }

    private static class EventMethod {
        private final Class<? extends Event> type;
        private final Method method;
        private final EventHandler annotation;

        private EventMethod(Class<? extends Event> type, Method method, EventHandler annotation) {
            this.type = type;
            this.method = method;
            this.annotation = annotation;
        }
        private int priority() { return annotation.priority(); }
        private boolean runWhenDisabled() { return annotation.runWhenDisabled(); }
    }

    public static class TickData {

    }
}
