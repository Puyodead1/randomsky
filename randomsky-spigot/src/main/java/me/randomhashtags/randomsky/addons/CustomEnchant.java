package me.randomhashtags.randomsky.addons;

import me.randomhashtags.randomsky.addons.utils.Identifiable;

import java.util.List;

public interface CustomEnchant extends Identifiable {
    boolean isEnabled();
    String getName();
    List<String> getLore();
    List<String> getAppliesTo();
}
