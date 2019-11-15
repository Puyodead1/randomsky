package me.randomhashtags.randomsky.util.obj;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.Collection;

public final class PolyBoundary {
    private final Location center;
    private int radius;
    private final int x, y, z, maxX, minX, maxZ, minZ, maxY, minY;
    public PolyBoundary(Location center, int radius) {
        this(center, radius, radius, radius);
        this.radius = radius;
    }
    public PolyBoundary(Location center, int x, int y, int z) {
        this.center = center;
        this.x = x;
        this.y = y;
        this.z = z;
        final int cx = center.getBlockX(), cy = center.getBlockY(), cz = center.getBlockZ();
        this.maxX = cx+x;
        this.minX = cx-x;
        this.maxY = cy+y;
        this.minY = cy-y;
        this.maxZ = cz+z;
        this.minZ = cz-z;
    }
    public boolean contains(Location location) {
        final int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
        return x <= maxX && x >= minX && y <= maxY && y >= minY && z <= maxZ && z >= minZ;
    }
    public World getWorld() { return center.getWorld(); }
    public Location getCenter() { return center; }
    public int getRadius() { return radius; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }

    public int getMaxX() { return maxX; }
    public int getMinX() { return minX; }
    public int getMaxZ() { return maxZ; }
    public int getMinZ() { return minZ; }
    public int getMaxY() { return maxY; }
    public int getMinY() { return minY; }

    public Collection<Entity> getEntities() { return getWorld().getNearbyEntities(center, radius, radius, radius); }
}
