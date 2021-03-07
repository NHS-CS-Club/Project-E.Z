package net.fabricmc.projectez.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    /**
     * The execution order priority of the handler. Default is zero, higher values go first.
     */
    int priority() default 0;

    /**
     * Whether or not to call this event when the mod is disabled.
     */
    boolean runWhenDisabled() default false;
}
