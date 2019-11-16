package me.randomhashtags.randomsky.addon;

import com.sun.istack.internal.NotNull;
import me.randomhashtags.randomsky.addon.util.Identifiable;
import me.randomhashtags.randomsky.util.Mathable;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import java.util.HashMap;

public interface EventCondition extends Identifiable, Mathable {
    void load();
    void unload();
    boolean check(@NotNull String value);
    boolean check(@NotNull Event event);
    boolean check(@NotNull Event event, @NotNull Entity entity);
    boolean check(@NotNull Event event, @NotNull String value);
    boolean check(@NotNull Entity entity, @NotNull String value);
    boolean check(@NotNull String entity, @NotNull HashMap<String, Entity> entities, @NotNull String value);
}
