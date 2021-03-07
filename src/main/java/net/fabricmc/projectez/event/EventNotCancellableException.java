package net.fabricmc.projectez.event;

public class EventNotCancellableException extends Exception {
    public EventNotCancellableException(Event event) {
        super("Event "+event.getClass().getName()+" is not cancellable but cancelling was attempted.");
    }
}
