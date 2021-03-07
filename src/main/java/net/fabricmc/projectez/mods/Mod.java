package net.fabricmc.projectez.mods;

import net.fabricmc.projectez.event.Event;
import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.util.ArrayListSet;

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

    @SuppressWarnings("unchecked")
    public Mod(String name) {
        this.name = name;

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
        if (enabled) onEnable();
        else onDisable();
    }
    public final void init() {
        if (cleaned || initialized) throw new IllegalStateException();
        initialized = true;
        onInit();
    }
    public final void cleanup() {
        if (cleaned || !initialized) throw new IllegalStateException();
        cleaned = true;
        setEnabled(false);
        onCleanup();
    }

    protected abstract void onEnable();
    protected abstract void onDisable();
    protected abstract void onInit();
    protected abstract void onCleanup();

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
}
