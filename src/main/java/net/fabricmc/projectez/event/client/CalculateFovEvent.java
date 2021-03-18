package net.fabricmc.projectez.event.client;

import net.fabricmc.projectez.event.Event;
import net.fabricmc.projectez.event.EventValue;

public class CalculateFovEvent extends Event implements EventValue<Double> {
    private double value;
    public CalculateFovEvent(double value) {
        this.value = value;
    }

    public void setValue(Double value) { this.value = value; }
    public double getValue() { return value; }
}
