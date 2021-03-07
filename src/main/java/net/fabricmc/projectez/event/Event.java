package net.fabricmc.projectez.event;

import net.fabricmc.projectez.Main;
import net.fabricmc.projectez.mods.Mod;

import java.lang.reflect.InvocationTargetException;

public abstract class Event {
    public final String name;
    protected Event(String name) {
        this.name = name;
    }

    private boolean cancelled = false;
    public final void cancel() {
        if (cancelled) return;
        cancelled = true;
        onCancelled();
    }
    public final boolean getCancelled() { return cancelled; }
    public void onCancelled() {}

    public static void call(Event ev) {
        int[] sortedPriorityLevels = Mod.sortedPriorityLevels();
        for (int priority : sortedPriorityLevels)
            for (Mod mod : Main.mods)
                try { mod.callEvent(ev, priority); }
                catch (InvocationTargetException e) {
                    Main.LOGGER.error("MOD EVENT CALLBACK THREW EXCEPTION: \n"+e.getTargetException().toString());
                }
    }
}
