package me.randomhashtags.randomsky.attribute.todo;

import me.randomhashtags.randomsky.attribute.AbstractEventAttribute;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import java.util.HashMap;

public class ChangeVelocity extends AbstractEventAttribute {
    // TODO: finish this attribute
    @Override
    public void execute(Event event, HashMap<Entity, String> recipientValues) {
        for(Entity e : recipientValues.keySet()) {
        }
    }
}
