package me.randomhashtags.randomsky.attribute.condition;

import me.randomhashtags.randomsky.attribute.AbstractEventCondition;
import me.randomhashtags.randomsky.attribute.Combo;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class HasCombo extends AbstractEventCondition implements Combo {
    @Override
    public boolean check(Entity entity, String value) {
        final String[] values = value.split(":");
        final String key = values[0];
        final boolean status = values.length == 1 || Boolean.parseBoolean(values[1]);
        final UUID u = entity.getUniqueId();
        return combos.containsKey(u) && combos.get(u).containsKey(key) == status;
    }
}
