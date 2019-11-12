package me.randomhashtags.randomsky.util.universal;

import me.randomhashtags.randomsky.RandomSky;
import me.randomhashtags.randomsky.supported.mechanics.SpawnerAPI;
import me.randomhashtags.randomsky.util.Versionable;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UVersion extends YamlUpdater implements Versionable, UVersionable {
    private static UVersion instance;
    public static UVersion getUVersion() {
        if(instance == null) instance = new UVersion();
        return instance;
    }

    public ItemStack item = new ItemStack(Material.APPLE);
    public ItemMeta itemMeta = item.getItemMeta();
    public List<String> lore = new ArrayList<>();

    public final void save(String folder, String file) {
        File f;
        final File d = randomsky.getDataFolder();
        if(folder != null && !folder.equals(""))
            f = new File(d + File.separator + folder + File.separator, file);
        else
            f = new File(d + File.separator, file);
        if(!f.exists()) {
            f.getParentFile().mkdirs();
            randomsky.saveResource(folder != null && !folder.equals("") ? folder + File.separator + file : file, false);
        }
        if(folder == null || !folder.equals("_Data")) {
            updateYaml(f);
        }
    }

    public final void didApply(InventoryClickEvent event, Player player, ItemStack current, ItemStack cursor) {
        event.setCancelled(true);
        final int a = cursor.getAmount();
        if(a == 1) event.setCursor(new ItemStack(Material.AIR));
        else {
            cursor.setAmount(a-1);
            event.setCursor(cursor);
        }
        player.updateInventory();
    }

    public final BigDecimal getBigDecimal(String value) {
        return BigDecimal.valueOf(Double.parseDouble(value));
    }
    public final BigDecimal getRandomBigDecimal(BigDecimal min, BigDecimal max) {
        final BigDecimal range = max.subtract(min);
        return min.add(range.multiply(new BigDecimal(Math.random())));
    }

    public final long getDelay(String input) {
        input = input.toLowerCase();
        long l = 0;
        if(input.contains("d")) {
            final String[] s = input.split("d");
            l += getRemainingDouble(s[0])*1000*60*60*24;
            input = s.length > 1 ? s[1] : input;
        }
        if(input.contains("h")) {
            final String[] s = input.split("h");
            l += getRemainingDouble(s[0])*1000*60*60;
            input = s.length > 1 ? s[1] : input;
        }
        if(input.contains("m")) {
            final String[] s = input.split("m");
            l += getRemainingDouble(s[0])*1000*60;
            input = s.length > 1 ? s[1] : input;
        }
        if(input.contains("s")) {
            l += getRemainingDouble(input.split("s")[0])*1000;
        }
        return l;
    }
    public final void spawnFirework(Firework firework, Location loc) {
        if(firework != null) {
            final Firework fw = loc.getWorld().spawn(new Location(loc.getWorld(), loc.getX()+0.5, loc.getY(), loc.getZ()+0.5), Firework.class);
            fw.setFireworkMeta(firework.getFireworkMeta());
        }
    }
    public final Firework createFirework(FireworkEffect.Type explosionType, Color trailColor, Color explodeColor, int power) {
        final World w = Bukkit.getWorlds().get(0);
        final Firework fw = w.spawn(w.getSpawnLocation(), Firework.class);
        final FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(power);
        fwm.addEffect(FireworkEffect.builder().trail(true).flicker(true).with(explosionType).withColor(trailColor).withFade(explodeColor).withFlicker().withTrail().build());
        fw.setFireworkMeta(fwm);
        return fw;
    }

    public final String toMaterial(String input, boolean realitem) {
        if(input.contains(":")) input = input.split(":")[0];
        if(input.contains(" ")) input = input.replace(" ", "");
        if(input.contains("_")) input = input.replace("_", " ");
        String e = "";
        if(input.contains(" ")) {
            final String[] spaces = input.split(" ");
            final int l = spaces.length;
            for(int i = 0; i < l; i++) {
                e = e + spaces[i].substring(0, 1).toUpperCase() + spaces[i].substring(1).toLowerCase() + (i != l-1 ? (realitem ? "_" : " ") : "");
            }
        } else e = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
        return e;
    }
    public final String toString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
    }
    public final Location toLocation(String string) {
        final String[] a = string.split(";");
        return new Location(Bukkit.getWorld(a[0]), Double.parseDouble(a[1]), Double.parseDouble(a[2]), Double.parseDouble(a[3]), Float.parseFloat(a[4]), Float.parseFloat(a[5]));
    }
    public final String getRemainingTime(long time) {
        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(time), min = sec/60, hr = min/60, d = hr/24;
        hr -= d*24;
        min -= (hr*60)+(d*60*24);
        sec -= (min*60)+(hr*60*60)+(d*60*60*24);
        final String dys = d > 0 ? d + "d" + (hr != 0 ? " " : "") : "";
        final String hrs = hr > 0 ? hr + "h" + (min != 0 ? " " : "") : "";
        final String mins = min != 0 ? min + "m" + (sec != 0 ? " " : "") : "";
        final String secs = sec != 0 ? sec + "s" : "";
        return dys + hrs + mins + secs;
    }
    public final long getTime(String fromString) {
        long time = 0;
        if(fromString != null) {
            fromString = ChatColor.stripColor(fromString);
            if(fromString.contains("d")) {
                time += getRemainingDouble(fromString.split("d")[0])*24*60*60;
                if(fromString.contains("h") || fromString.contains("m") || fromString.contains("s")) fromString = fromString.split("d")[1];
            }
            if(fromString.contains("h")) {
                time += getRemainingDouble(fromString.split("h")[0])*60*60;
                if(fromString.contains("m") || fromString.contains("s")) fromString = fromString.split("h")[1];
            }
            if(fromString.contains("m")) {
                time += getRemainingDouble(fromString.split("m")[0])*60;
                if(fromString.contains("s")) fromString = fromString.split("m")[1];
            }
            if(fromString.contains("s")) {
                time += getRemainingDouble(fromString.split("s")[0]);
                //fromString = fromString.split("s")[0];
            }
        }
        return time*1000;
    }
    public final Enchantment getEnchantment(String string) {
        if(string != null) {
            for(Enchantment enchant : Enchantment.values()) {
                final String name = enchant != null ? enchant.getName() : null;
                if(name != null && string.toLowerCase().replace("_", "").startsWith(name.toLowerCase().replace("_", ""))) {
                    return enchant;
                }
            }
            string = string.toLowerCase().replace("_", "");
            if(string.startsWith("po")) { return Enchantment.ARROW_DAMAGE; // Power
            } else if(string.startsWith("fl")) { return Enchantment.ARROW_FIRE; // Flame
            } else if(string.startsWith("i")) { return Enchantment.ARROW_INFINITE; // Infinity
            } else if(string.startsWith("pu")) { return Enchantment.ARROW_KNOCKBACK; // Punch
            } else if(string.startsWith("bi") && !EIGHT && !NINE && !TEN) { return Enchantment.getByName("BINDING_CURSE"); // Binding Curse
            } else if(string.startsWith("sh")) { return Enchantment.DAMAGE_ALL; // Sharpness
            } else if(string.startsWith("ba")) { return Enchantment.DAMAGE_ARTHROPODS; // Bane of Arthropods
            } else if(string.startsWith("sm")) { return Enchantment.DAMAGE_UNDEAD; // Smite
            } else if(string.startsWith("de")) { return Enchantment.DEPTH_STRIDER; // Depth Strider
            } else if(string.startsWith("e")) { return Enchantment.DIG_SPEED; // Efficiency
            } else if(string.startsWith("u")) { return Enchantment.DURABILITY; // Unbreaking
            } else if(string.startsWith("firea")) { return Enchantment.FIRE_ASPECT; // Fire Aspect
            } else if(string.startsWith("fr") && !EIGHT) { return Enchantment.getByName("FROST_WALKER"); // Frost Walker
            } else if(string.startsWith("k")) { return Enchantment.KNOCKBACK; // Knockback
            } else if(string.startsWith("fo")) { return Enchantment.LOOT_BONUS_BLOCKS; // Fortune
            } else if(string.startsWith("lo")) { return Enchantment.LOOT_BONUS_MOBS; // Looting
            } else if(string.startsWith("luc")) { return Enchantment.LUCK; // Luck
            } else if(string.startsWith("lur")) { return Enchantment.LURE; // Lure
            } else if(string.startsWith("m") && !EIGHT) { return Enchantment.getByName("MENDING"); // Mending
            } else if(string.startsWith("r")) { return Enchantment.OXYGEN; // Respiration
            } else if(string.startsWith("prot")) { return Enchantment.PROTECTION_ENVIRONMENTAL; // Protection
            } else if(string.startsWith("bl") || string.startsWith("bp")) { return Enchantment.PROTECTION_EXPLOSIONS; // Blast Protection
            } else if(string.startsWith("ff") || string.startsWith("fe")) { return Enchantment.PROTECTION_FALL; // Feather Falling
            } else if(string.startsWith("fp") || string.startsWith("firep")) { return Enchantment.PROTECTION_FIRE; // Fire Protection
            } else if(string.startsWith("pp") || string.startsWith("proj")) { return Enchantment.PROTECTION_PROJECTILE; // Projectile Protection
            } else if(string.startsWith("si")) { return Enchantment.SILK_TOUCH; // Silk Touch
            } else if(string.startsWith("th")) { return Enchantment.THORNS; // Thorns
            } else if(string.startsWith("v") && !EIGHT && !NINE && !TEN) { return Enchantment.getByName("VANISHING_CURSE"); // Vanishing Curse
            } else if(string.startsWith("aa") || string.startsWith("aq")) { return Enchantment.WATER_WORKER; // Aqua Affinity
            } else { return null; }
        }
        return null;
    }

    public final double round(double input, int decimals) {
        // From http://www.baeldung.com/java-round-decimal-number
        if(decimals < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(input));
        bd = bd.setScale(decimals, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public final String roundDoubleString(double input, int decimals) {
        final double d = round(input, decimals);
        return Double.toString(d);
    }

    public final int indexOf(Set<? extends Object> collection, Object value) {
        int i = 0;
        for(Object o : collection) {
            if(value.equals(o)) return i;
            i++;
        }
        return -1;
    }

    public final String center(String s, int size) {
        // Credit to "Sahil Mathoo" from StackOverFlow at https://stackoverflow.com/questions/8154366
        return center(s, size, ' ');
    }
    private String center(String s, int size, char pad) {
        if(s == null || size <= s.length()) return s;
        StringBuilder sb = new StringBuilder(size);
        for(int i = 0; i < (size - s.length()) / 2; i++)  sb.append(pad);
        sb.append(s);
        while(sb.length() < size) sb.append(pad);
        return sb.toString();
    }
    public final void giveItem(Player player, ItemStack is) {
        if(is == null || is.getType().equals(Material.AIR)) return;
        final UMaterial m = UMaterial.match(is);
        final ItemMeta meta = is.getItemMeta();
        final PlayerInventory i = player.getInventory();
        final int f = i.first(is.getType()), e = i.firstEmpty(), max = is.getMaxStackSize();
        int amountLeft = is.getAmount();

        if(f != -1) {
            for(int s = 0; s < i.getSize(); s++) {
                final ItemStack t = i.getItem(s);
                if(amountLeft > 0 && t != null && t.getItemMeta().equals(meta) && UMaterial.match(t) == m) {
                    final int a = t.getAmount(), toMax = max-a, given = amountLeft <= toMax ? amountLeft : toMax;
                    if(given > 0) {
                        t.setAmount(a+given);
                        amountLeft -= given;
                    }
                }
            }
            player.updateInventory();
        }
        if(amountLeft > 0) {
            is.setAmount(amountLeft);
            if(e >= 0) {
                i.addItem(is);
                player.updateInventory();
            } else {
                player.getWorld().dropItem(player.getLocation(), is);
            }
        }
    }
    public final void removeItem(Player player, ItemStack itemstack, int amount) {
        final PlayerInventory inv = player.getInventory();
        int nextslot = getNextSlot(player, itemstack);
        for(int i = 1; i <= amount; i++) {
            if(nextslot >= 0) {
                final ItemStack is = inv.getItem(nextslot);
                if(is.getAmount() == 1) {
                    inv.setItem(nextslot, new ItemStack(Material.AIR));
                    nextslot = getNextSlot(player, itemstack);
                } else {
                    is.setAmount(is.getAmount() - 1);
                }
            }
        }
        player.updateInventory();
    }
    private int getNextSlot(Player player, ItemStack itemstack) {
        final PlayerInventory inv = player.getInventory();
        for(int i = 0; i < inv.getSize(); i++) {
            item = inv.getItem(i);
            if(item != null && item.isSimilar(itemstack)) {
                return i;
            }
        }
        return -1;
    }
    public final int getTotalAmount(Inventory inventory, UMaterial umat) {
        final ItemStack i = umat.getItemStack();
        int amount = 0;
        for(ItemStack is : inventory.getContents()) {
            if(is != null && is.isSimilar(i)) {
                amount += is.getAmount();
            }
        }
        return amount;
    }

    public final ItemStack getSpawner(String input) {
        String pi = input.toLowerCase(), type = null;
        if(pi.equals("mysterymobspawner")) {
            return givedpitem.valueOf("mysterymobspawner").clone();
        } else {
            if(RandomSky.spawnerPlugin != null) {
                for(EntityType entitytype : EntityType.values()) {
                    final String s = entitytype.name().toLowerCase().replace("_", "");
                    if(pi.startsWith(s + "spawner")) {
                        type = s;
                    }
                }
                if(type == null) {
                    if(pi.contains("pig") && pi.contains("zombie")) type = "pigzombie";
                }
                if(type == null) return null;
                final ItemStack is = SpawnerAPI.getSpawnerAPI().getItem(type);
                if(is != null) {
                    return is;
                } else {
                    console.sendMessage("[RandomPackage] SilkSpawners or EpicSpawners is required to use this feature!");
                }
            }
        }
        return null;
    }
    public final void sendStringListMessage(CommandSender sender, List<String> message, HashMap<String, String> replacements) {
        if(message != null && message.size() > 0 && !message.get(0).equals("")) {
            for(String s : message) {
                if(replacements != null) {
                    for(String r : replacements.keySet()) {
                        s = s.replace(r, replacements.get(r));
                    }
                }
                if(s != null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
                }
            }
        }
    }
    public final LivingEntity getHitEntity(ProjectileHitEvent event) {
        final List<Entity> n = event.getEntity().getNearbyEntities(0.0, 0.0, 0.0);
        return n.size() > 0 && n.get(0) instanceof LivingEntity ? (LivingEntity) n.get(0) : null;
    }
    public final void playParticle(FileConfiguration config, String path, Location location, int count) {
        if(config != null && config.get(path) != null) {
            final String target = config.getString(path);
            final UParticle up = UParticle.matchParticle(target.toUpperCase());
            if(up != null) up.play(location, count);
        }
    }
    public final void playSound(FileConfiguration config, String path, Player player, Location location, boolean globalsound) {
        if(config.get(path) != null) {
            final String[] p = config.getString(path).split(":");
            final String s = p[0].toUpperCase();
            final int v = Integer.parseInt(p[1]), pp = Integer.parseInt(p[2]);
            try {
                final Sound sound = Sound.valueOf(s);
                if(player != null) {
                    if(!globalsound) player.playSound(location, sound, v, pp);
                    else             player.getWorld().playSound(location, sound, v, pp);
                } else {
                    location.getWorld().playSound(location, sound, v, pp);
                }
            } catch (Exception e) {
                sendConsoleMessage("&6[RandomPackage] &cERROR! Invalid sound name: &f" + s + "&c!");
            }
        }
    }

    public final List<Location> getChunkLocations(Chunk chunk) {
        final List<Location> l = new ArrayList<>();
        final int x = chunk.getX()*16, z = chunk.getZ()*16;
        final World world = chunk.getWorld();
        for(int xx = x; xx < x+16; xx++) {
            for(int zz = z; zz < z+16; zz++) {
                l.add(new Location(world, xx, 0, zz));
            }
        }
        return l;
    }
    public final ItemStack getItemInHand(LivingEntity entity) {
        if(entity == null) return null;
        else {
            final EntityEquipment e = entity.getEquipment();
            return EIGHT ? e.getItemInHand() : e.getItemInMainHand();
        }
    }
    public final Entity getEntity(UUID uuid) {
        if(uuid != null) {
            if(EIGHT || NINE) {
                for(World w : Bukkit.getWorlds()) {
                    for(LivingEntity le : w.getLivingEntities()) {
                        if(uuid.equals(le.getUniqueId())) {
                            return le;
                        }
                    }
                }
            } else {
                return Bukkit.getEntity(uuid);
            }
        }
        return null;
    }
    public final LivingEntity getEntity(String type, Location l) {
        final World w = l.getWorld();
        final LivingEntity le;
        switch (type.toUpperCase()) {
            case "BAT": return w.spawn(l, Bat.class);
            case "BLAZE": return w.spawn(l, Blaze.class);
            case "CAVE_SPIDER": return w.spawn(l, CaveSpider.class);
            case "CHICKEN": return w.spawn(l, Chicken.class);
            case "COW": return w.spawn(l, Cow.class);
            case "CREEPER": return w.spawn(l, Creeper.class);
            case "ENDER_DRAGON": return w.spawn(l, EnderDragon.class);
            case "ENDERMAN": return w.spawn(l, Enderman.class);
            case "GHAST": return w.spawn(l, Ghast.class);
            case "GIANT": return w.spawn(l, Giant.class);
            case "GUARDIAN": return w.spawn(l, Guardian.class);
            case "HORSE": return w.spawn(l, Horse.class);
            case "IRON_GOLEM": return w.spawn(l, IronGolem.class);
            case "LLAMA": return EIGHT || NINE || TEN ? null : w.spawn(l, Llama.class);
            case "MAGMA_CUBE": return w.spawn(l, MagmaCube.class);
            case "MUSHROOM_COW": return w.spawn(l, MushroomCow.class);
            case "OCELOT": return w.spawn(l, Ocelot.class);
            case "PARROT": return EIGHT || NINE || TEN || ELEVEN ? null : w.spawn(l, Parrot.class);
            case "PIG": return w.spawn(l, Pig.class);
            case "PIG_ZOMBIE": return w.spawn(l, PigZombie.class);
            case "RABBIT": return w.spawn(l, Rabbit.class);
            case "SHEEP": return w.spawn(l, Sheep.class);
            case "SHULKER": return EIGHT ? null : w.spawn(l, Shulker.class);
            case "SILVERFISH": return w.spawn(l, Silverfish.class);
            case "SKELETON": return w.spawn(l, Skeleton.class);
            case "SLIME": return w.spawn(l, Slime.class);
            case "SNOWMAN": return w.spawn(l, Snowman.class);
            case "SQUID": return w.spawn(l, Squid.class);
            case "SPIDER": return w.spawn(l, Spider.class);
            case "STRAY": return EIGHT || NINE ? null : w.spawn(l, Stray.class);
            case "VEX": return EIGHT || NINE || TEN ? null : w.spawn(l, Vex.class);
            case "VILLAGER": return w.spawn(l, Villager.class);
            case "VINDICATOR": return EIGHT || NINE || TEN ? null : w.spawn(l, Vindicator.class);
            case "WITHER_SKELETON":
                if(EIGHT || NINE || TEN) {
                    le = w.spawn(l, Skeleton.class);
                    ((Skeleton) le).setSkeletonType(Skeleton.SkeletonType.WITHER);
                    return le;
                } else {
                    return w.spawn(l, WitherSkeleton.class);
                }
            case "ZOMBIE": return w.spawn(l, Zombie.class);
            case "ZOMBIE_HORSE": return EIGHT ? null : w.spawn(l, ZombieHorse.class);
            case "ZOMBIE_VILLAGER": return EIGHT ? null : w.spawn(l, ZombieVillager.class);
            default: return null;
        }
    }
    public final LivingEntity getEntity(String type, Location l, boolean spawn) {
        final boolean baby = type.contains(":") && type.toLowerCase().endsWith(":true");
        type = type.toUpperCase().split(":")[0];
        final LivingEntity mob = getEntity(type, l);
        if(mob instanceof Zombie) {
            ((Zombie) mob).setBaby(baby);
        }
        if(!spawn) {
            mob.remove();
        } else if(mob instanceof Ageable && baby) {
            final Ageable a = (Ageable) mob;
            a.setBaby();
            a.setAgeLock(true);
        }
        return mob;
    }
}
