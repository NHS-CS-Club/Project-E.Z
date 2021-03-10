package net.fabricmc.projectez.event.render;

import net.fabricmc.projectez.event.Event;

public class CalculateFovEvent extends Event {
    private double value;
    public CalculateFovEvent(double value) {
        super("calc fov");
        this.value = value;
    }

    public void setValue(double value) { this.value = value; }
    public double getValue() { return value; }
}
