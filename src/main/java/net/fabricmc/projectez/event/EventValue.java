package net.fabricmc.projectez.event;

public interface EventValue<T> {
    void setValue(T value);
    double getValue();
}
