package me.randomhashtags.randomsky.utils.universal;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import me.randomhashtags.randomsky.RandomSky;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UVersion {

    private static UVersion instance;
    public static final UVersion getUVersion() {
        if(instance == null) instance = new UVersion();
        return instance;
    }

    protected static File rsd = RandomSky.getPlugin.getDataFolder();
    public final Plugin randomsky = RandomSky.getPlugin;
    public final PluginManager pluginmanager = Bukkit.getPluginManager();

    public final String version = Bukkit.getVersion();
    public final BukkitScheduler scheduler = Bukkit.getScheduler();
    public final ConsoleCommandSender console = Bukkit.getConsoleSender();
    public final Random random = new Random();
    public ItemStack item = new ItemStack(Material.APPLE);
    public ItemMeta itemMeta = item.getItemMeta();
    public List<String> lore = new ArrayList<>();



    public void save(String folder, String file) {
        File f = null;
        final File d = randomsky.getDataFolder();
        if(folder != null && !folder.equals(""))
            f = new File(d + File.separator + folder + File.separator, file);
        else
            f = new File(d + File.separator, file);
        if(!f.exists()) {
            f.getParentFile().mkdirs();
            randomsky.saveResource(folder != null && !folder.equals("") ? folder + File.separator + file : file, false);
        }
    }
    public void sendStringListMessage(CommandSender sender, List<String> message, HashMap<String, String> replacements) {
        if(message != null && message.size() > 0 && !message.get(0).equals("")) {
            for(String s : message) {
                if(replacements != null) {
                    for(String r : replacements.keySet()) {
                        s = s.replace(r, replacements.get(r));
                    }
                }
                if(s != null) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        }
    }
    public void sendConsoleMessage(String msg) {
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }


    public int getTotalExperience(Player player) {
        final double levelxp = LevelToExp(player.getLevel()), nextlevelxp = LevelToExp(player.getLevel() + 1), difference = nextlevelxp - levelxp;
        final double p = (levelxp + (difference * player.getExp()));
        return (int) Math.round(p);
    }
    public void setTotalExperience(Player player, int total) {
        player.setTotalExperience(0);
        player.setExp(0f);
        player.setLevel(0);
        player.giveExp(total);
    }
    public long getDelay(String input) {
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
    private double LevelToExp(int level) {
        return level <= 16 ? (level * level) + (level * 6) : level <= 31 ? (2.5 * level * level) - (40.5 * level) + 360 : (4.5 * level * level) - (162.5 * level) + 2220;
    }
    public String formatDouble(double d) {
        String decimals = Double.toString(d).split("\\.")[1];
        decimals = decimals.equals("0") ? "" : "." + decimals;
        return formatInt((int) d) + decimals;
    }
    public String formatLong(long l) {
        final String f = Long.toString(l);
        final boolean c = f.contains(".");
        String decimals = c ? f.split("\\.")[1] : f;
        decimals = c ? decimals.equals("0") ? "" : "." + decimals : "";
        return formatInt((int) l) + decimals;
    }
    public String formatInt(int integer) { return String.format("%,d", integer); }
    public int getAmount(PlayerInventory i, ItemStack is) {
        int a = 0;
        if(is != null && !is.getType().equals(Material.AIR)) {
            for(int o = 0; o < i.getSize(); o++) {
                final ItemStack p = i.getItem(o);
                if(p != null && p.isSimilar(is))
                    a += p.getAmount();
            }
        }
        return a;
    }
    public int getRemainingInt(String string) {
        string = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', string)).replaceAll("\\p{L}", "").replaceAll("\\s", "").replaceAll("\\p{P}", "").replaceAll("\\p{S}", "");
        return string == null || string.equals("") ? -1 : Integer.parseInt(string);
    }
    public long getEstimatedClassSize(Object object) {
        return ObjectSizeCalculator.getObjectSize(object);
    }
    public Double getRemainingDouble(String string) {
        string = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', string).replaceAll("\\p{L}", "").replaceAll("\\p{Z}", "").replaceAll("\\.", "d").replaceAll("\\p{P}", "").replaceAll("\\p{S}", "").replace("d", "."));
        return string == null || string.equals("") ? -1.00 : Double.parseDouble(string.contains(".") && string.split("\\.").length > 1 && string.split("\\.")[1].length() > 2 ? string.substring(0, string.split("\\.")[0].length() + 3) : string);
    }
    public String toMaterial(String input, boolean realitem) {
        if(input.contains(":")) input = input.split(":")[0];
        if(input.contains(" ")) input = input.replace(" ", "");
        if(input.contains("_")) input = input.replace("_", " ");
        String e = "";
        if(input.contains(" ")) {
            for(int i = 0; i < input.split(" ").length; i++) {
                e = e + input.split(" ")[i].substring(0, 1).toUpperCase() + input.split(" ")[i].substring(1).toLowerCase() + (i != input.split(" ").length - 1 ? (realitem ? "_" : " ") : "");
            }
        } else e = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
        return e;
    }
    public String toString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
    }
    public String getRemainingTime(long time) {
        int sec = (int) TimeUnit.MILLISECONDS.toSeconds(time), min = sec/60, hr = min/60, d = hr/24;
        hr -= d*24;
        min -= (hr*60)+(d*60*24);
        sec -= (min*60)+(hr*60*60)+(d*60*60*24);
        final String dys = d > 0 ? d + "d " : "";
        final String hrs = hr > 0 ? hr + "h" + " " : "";
        final String mins = min != 0 ? min + "m " : "";
        final String secs = sec != 0 ? sec + "s" : "";
        return dys + hrs + mins + secs;
    }
    public Location toLocation(String string) {
        final String[] a = string.split(";");
        return new Location(Bukkit.getWorld(a[0]), Double.parseDouble(a[1]), Double.parseDouble(a[2]), Double.parseDouble(a[3]), Float.parseFloat(a[4]), Float.parseFloat(a[5]));
    }
    public Enchantment getEnchantment(String string) {
        if(string != null) {
            for(Enchantment enchant : Enchantment.values())
                if(enchant != null && enchant.getName() != null && string.toLowerCase().replace("_", "").startsWith(enchant.getName().toLowerCase().replace("_", ""))) return enchant;
            string = string.toLowerCase().replace("_", "");
            if(string.startsWith("po")) { return Enchantment.ARROW_DAMAGE; // Power
            } else if(string.startsWith("fl")) { return Enchantment.ARROW_FIRE; // Flame
            } else if(string.startsWith("i")) { return Enchantment.ARROW_INFINITE; // Infinity
            } else if(string.startsWith("pu")) { return Enchantment.ARROW_KNOCKBACK; // Punch
            } else if(string.startsWith("bi") && !version.contains("1.8") && !version.contains("1.9") && !version.contains("1.10")) { return Enchantment.getByName("BINDING_CURSE"); // Binding Curse
            } else if(string.startsWith("sh")) { return Enchantment.DAMAGE_ALL; // Sharpness
            } else if(string.startsWith("ba")) { return Enchantment.DAMAGE_ARTHROPODS; // Bane of Arthropods
            } else if(string.startsWith("sm")) { return Enchantment.DAMAGE_UNDEAD; // Smite
            } else if(string.startsWith("de")) { return Enchantment.DEPTH_STRIDER; // Depth Strider
            } else if(string.startsWith("e")) { return Enchantment.DIG_SPEED; // Efficiency
            } else if(string.startsWith("u")) { return Enchantment.DURABILITY; // Unbreaking
            } else if(string.startsWith("firea")) { return Enchantment.FIRE_ASPECT; // Fire Aspect
            } else if(string.startsWith("fr") && !version.contains("1.8")) { return Enchantment.getByName("FROST_WALKER"); // Frost Walker
            } else if(string.startsWith("k")) { return Enchantment.KNOCKBACK; // Knockback
            } else if(string.startsWith("fo")) { return Enchantment.LOOT_BONUS_BLOCKS; // Fortune
            } else if(string.startsWith("lo")) { return Enchantment.LOOT_BONUS_MOBS; // Looting
            } else if(string.startsWith("luc")) { return Enchantment.LUCK; // Luck
            } else if(string.startsWith("lur")) { return Enchantment.LURE; // Lure
            } else if(string.startsWith("m") && !version.contains("1.8")) { return Enchantment.getByName("MENDING"); // Mending
            } else if(string.startsWith("r")) { return Enchantment.OXYGEN; // Respiration
            } else if(string.startsWith("prot")) { return Enchantment.PROTECTION_ENVIRONMENTAL; // Protection
            } else if(string.startsWith("bl") || string.startsWith("bp")) { return Enchantment.PROTECTION_EXPLOSIONS; // Blast Protection
            } else if(string.startsWith("ff") || string.startsWith("fe")) { return Enchantment.PROTECTION_FALL; // Feather Falling
            } else if(string.startsWith("fp") || string.startsWith("firep")) { return Enchantment.PROTECTION_FIRE; // Fire Protection
            } else if(string.startsWith("pp") || string.startsWith("proj")) { return Enchantment.PROTECTION_PROJECTILE; // Projectile Protection
            } else if(string.startsWith("si")) { return Enchantment.SILK_TOUCH; // Silk Touch
            } else if(string.startsWith("th")) { return Enchantment.THORNS; // Thorns
            } else if(string.startsWith("v") && !version.contains("1.8") && !version.contains("1.9") && !version.contains("1.10")) { return Enchantment.getByName("VANISHING_CURSE"); // Vanishing Curse
            } else if(string.startsWith("aa") || string.startsWith("aq")) { return Enchantment.WATER_WORKER; // Aqua Affinity
            } else { return null; }
        }
        return null;
    }
    // From http://www.baeldung.com/java-round-decimal-number
    public double round(double input, int decimals) {
        if(decimals < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(input));
        bd = bd.setScale(decimals, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public String roundDoubleString(double input, int decimals) {
        final double d = round(input, decimals);
        return Double.toString(d);
    }

    public List<String> colorizeListString(List<String> input) {
        final List<String> i = new ArrayList<>();
        if(input != null && !input.isEmpty()) {
            for(String s : input) {
                i.add(ChatColor.translateAlternateColorCodes('&', s));
            }
        }
        return i;
    }
    public int indexOf(Set<? extends Object> collection, Object value) {
        int i = 0;
        for(Object o : collection) {
            if(value.equals(o)) return i;
            i++;
        }
        return -1;
    }
    public int indexOf(Collection<? extends Object> collection, Object value) {
        int i = 0;
        for(Object o : collection) {
            if(value.equals(o)) return i;
            i++;
        }
        return -1;
    }
    /*
     * Credit to "Sahil Mathoo" from StackOverFlow at
     * https://stackoverflow.com/questions/8154366/how-to-center-a-string-using-string-format
     */
    public String center(String s, int size) {
        return center(s, size, ' ');
    }
    private String center(String s, int size, char pad) {
        if(s == null || size <= s.length())
            return s;
        StringBuilder sb = new StringBuilder(size);
        for(int i = 0; i < (size - s.length()) / 2; i++)  sb.append(pad);
        sb.append(s);
        while(sb.length() < size) sb.append(pad);
        return sb.toString();
    }

    public void giveItem(Player player, ItemStack is) {
        if(is == null || is.getType().equals(Material.AIR)) return;
        final World w = player.getWorld();
        final Location l = player.getLocation();
        final PlayerInventory i = player.getInventory();
        final int f = i.first(is.getType()), e = i.firstEmpty();
        if(e < 0 && f != -1
                || f != -1 && !is.hasItemMeta() && i.first(new ItemStack(is.getType(), i.getItem(f).getAmount(), is.getData().getData())) != -1 && !(i.getItem(i.first(new ItemStack(is.getType(), i.getItem(f).getAmount(), is.getData().getData()))).getAmount() <= is.getMaxStackSize())
                || e == -1) {
            w.dropItem(l, is);
        } else {
            i.addItem(is);
            player.updateInventory();
        }
    }
    public void removeItem(Player player, ItemStack itemstack, int amount) {
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
            if(item != null && item.getData().getData() == itemstack.getData().getData() && item.getType().equals(itemstack.getType())
                    && item.getItemMeta().equals(itemstack.getItemMeta())) {
                return i;
            }
        }
        return -1;
    }
    public String getEntityType(LivingEntity entity) {
        String type = null;
        if(entity != null) {
            final EntityType t = entity.getType();
            if(t.equals(EntityType.SKELETON)) {
                final Skeleton s = (Skeleton) entity;
                final Skeleton.SkeletonType st = s.getSkeletonType();
                if(st.equals(Skeleton.SkeletonType.WITHER)) {
                    type = "WITHER_SKELETON";
                }
            } else {
                type = t.name();
            }
        }
        return type;
    }

    public LivingEntity getEntity(String type, Location l, boolean spawn) {
        final boolean baby = type.contains(":") && type.toLowerCase().endsWith(":true");
        type = type.toUpperCase().split(":")[0];
        LivingEntity mob = null;
        final World w = l.getWorld();
        if(type.equals("BAT"))              mob = w.spawn(l, Bat.class);
        else if(type.equals("BLAZE"))       mob = w.spawn(l, Blaze.class);
        else if(type.equals("CAVE_SPIDER")) mob = w.spawn(l, CaveSpider.class);
        else if(type.equals("CHICKEN"))     mob = w.spawn(l, Chicken.class);
        else if(type.equals("COW"))         mob = w.spawn(l, Cow.class);
        else if(type.equals("CREEPER"))     mob = w.spawn(l, Creeper.class);
        else if(type.equals("ENDER_DRAGON"))mob = w.spawn(l, EnderDragon.class);
        else if(type.equals("ENDERMAN"))    mob = w.spawn(l, Enderman.class);
        else if(type.equals("ENDERMITE"))   mob = w.spawn(l, Endermite.class);
        else if(type.equals("GHAST"))       mob = w.spawn(l, Ghast.class);
        else if(type.equals("GIANT"))       mob = w.spawn(l, Giant.class);
        else if(type.equals("GUARDIAN"))    mob = w.spawn(l, Guardian.class);
        else if(type.equals("HORSE"))       mob = w.spawn(l, Horse.class);
        else if(type.equals("IRON_GOLEM"))  mob = w.spawn(l, IronGolem.class);
        else if(type.equals("LLAMA") && !version.contains("1.8") && !version.contains("1.9") && !version.contains("1.10"))
            mob = w.spawn(l, org.bukkit.entity.Llama.class);
        else if(type.equals("MAGMA_CUBE"))  mob = w.spawn(l, MagmaCube.class);
        else if(type.equals("MUSHROOM_COW"))mob = w.spawn(l, MushroomCow.class);
        else if(type.equals("OCELOT"))      mob = w.spawn(l, Ocelot.class);
        else if(type.equals("PARROT") && !version.contains("1.8") && !version.contains("1.9") && !version.contains("1.10") && !version.contains("1.11"))
            mob = w.spawn(l, org.bukkit.entity.Parrot.class);
        else if(type.equals("PIG"))         mob = w.spawn(l, Pig.class);
        else if(type.equals("PIG_ZOMBIE")) {
            mob = w.spawn(l, PigZombie.class);
            ((PigZombie) mob).setBaby(baby);
        } else if(type.equals("RABBIT"))    mob = w.spawn(l, Rabbit.class);
        else if(type.equals("SHEEP"))       mob = w.spawn(l, Sheep.class);
        else if(type.equals("SHULKER") && !version.contains("1.8"))
            mob = w.spawn(l, org.bukkit.entity.Shulker.class);
        else if(type.equals("SILVERFISH"))  mob = w.spawn(l, Silverfish.class);
        else if(type.equals("SKELETON"))    mob = w.spawn(l, Skeleton.class);
        else if(type.equals("SLIME"))       mob = w.spawn(l, Slime.class);
        else if(type.equals("SNOWMAN"))     mob = w.spawn(l, Snowman.class);
        else if(type.equals("SQUID"))       mob = w.spawn(l, Squid.class);
        else if(type.equals("SPIDER"))      mob = w.spawn(l, Spider.class);
        else if(type.equals("STRAY") && !version.contains("1.8") && !version.contains("1.9"))
            mob = w.spawn(l, org.bukkit.entity.Stray.class);
        else if(type.equals("VEX") && !version.contains("1.8") && !version.contains("1.9") && !version.contains("1.10"))
            mob = w.spawn(l, org.bukkit.entity.Vex.class);
        else if(type.equals("VILLAGER"))    mob = w.spawn(l, Villager.class);
        else if(type.equals("VINDICATOR") && !version.contains("1.8") && !version.contains("1.9") && !version.contains("1.10"))
            mob = w.spawn(l, org.bukkit.entity.Vindicator.class);
        else if(type.equals("WITCH"))       mob = w.spawn(l, Witch.class);
        else if(type.equals("WITHER"))      mob = w.spawn(l, Wither.class);
        else if(type.equals("WITHER_SKELETON")) {
            if(version.contains("1.8") || version.contains("1.9") || version.contains("1.10")) {
                mob = w.spawn(l, Skeleton.class);
                ((Skeleton) mob).setSkeletonType(Skeleton.SkeletonType.WITHER);
            } else {
                mob = w.spawn(l, org.bukkit.entity.WitherSkeleton.class);
            }
        } else if(type.equals("ZOMBIE"))    mob = w.spawn(l, Zombie.class);
        else if(type.equals("ZOMBIE_HORSE") && !version.contains("1.8"))
            mob = w.spawn(l, org.bukkit.entity.ZombieHorse.class);
        else if(type.equals("ZOMBIE_VILLAGER") && !version.contains("1.8"))
            mob = w.spawn(l, org.bukkit.entity.ZombieVillager.class);
        else return null;
        if(!spawn) {
            mob.remove();
        } else if(mob instanceof Ageable) {
            final Ageable a = (Ageable) mob;
            a.setBaby();
            a.setAgeLock(true);
        }
        return mob;
    }
}
