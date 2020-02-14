package me.randomhashtags.randomsky.attributesys;

import me.randomhashtags.randomsky.attribute.Combo;
import me.randomhashtags.randomsky.universal.UVersionable;
import me.randomhashtags.randomsky.util.Mathable;
import me.randomhashtags.randomsky.util.RSItemStack;
import org.bukkit.Chunk;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface EventConditions extends Combo, RSItemStack, Mathable, UVersionable, RSStorage {
    List<UUID> SPAWNED_FROM_SPAWNER = new ArrayList<>();
    HashMap<UUID, EntityShootBowEvent> PROJECTILE_EVENTS = new HashMap<>();

    default boolean passedAllConditions(Event event, HashMap<String, Entity> entities, String entityKey, Entity entity, String condition, String key, String value) {
        final boolean pre = passedCustomCondition(event, entities, entityKey, entity, condition, key, value) && passedEvent(event, entity, condition, value), isEntity = condition.startsWith(key);
        condition = condition.substring(key.length()).split("=")[0];
        return pre && (!isEntity
                || passedBasic(entity, condition, value)
                && passedAgeable(entity, condition, value)
                && passedAnimals(entity, condition, value)
                && passedArmorStand(entity, condition, value)
                && passedBat(entity, condition, value)
                && passedCat(entity, condition, value)
                && passedChestedHorse(entity, condition, value)
                && passedCreeper(entity, condition, value)
                && passedEnderCrystal(entity, condition, value)
                && passedEnderDragon(entity, condition, value)
                && passedEnderman(entity, condition, value)
                && passedEndermite(entity, condition, value)
                && passedEntity(entity, condition, value)
                && passedEvoker(entity, condition, value)
                && passedExplosive(entity, condition, value)
                && passedFallenBlock(event, entity, condition, value)
                && passedFirework(entity, condition, value)
                && passedFox(entity, condition, value)
                && passedGuardian(entity, condition, value)
                && passedHorse(entity, condition, value)
                && passedHusk(entity, condition, value)
                && passedIronGolem(entity, condition, value)
                && passedLightingStrike(entity, condition, value)
                && passedLivingEntity(entity, condition, value)
                && passedMinecart(entity, condition, value)
                && passedMob(entity, condition, value)
                && passedPainting(entity, condition, value)
                && passedPanda(entity, condition, value)
                && passedPig(entity, condition, value)
                && passedPigZombie(entity, condition, value)
                && passedPlayer(entity, condition, value)
                && passedProjectile(entity, condition, value)
                && passedRabbit(entity, condition, value)
                && passedRaider(entity, condition, value)
                && passedRandomPackage(entity, condition, value)
                && passedSheep(entity, condition, value)
                && passedSittable(entity, condition, value)
                && passedSkeleton(entity, condition, value)
                && passedSlime(entity, condition, value)
                && passedSnowman(entity, condition, value)
                && passedTameable(entity, condition, value)
                && passedTropicalFish(entity, condition, value)
                && passedVex(entity, condition, value)
                && passedVillager(entity, condition, value)
                && passedWitherSkull(entity, condition, value)
                && passedZombie(entity, condition, value))
        ;
    }

    default boolean passedBasic(Entity e, String condition, String value) {
        switch (condition) {
            case "isfromspawner": return SPAWNED_FROM_SPAWNER.contains(e.getUniqueId()) == Boolean.parseBoolean(value);
            case "isplayer": return e instanceof Player == Boolean.parseBoolean(value);
            case "ismob": return LEGACY ? e instanceof Creature : e instanceof Mob == Boolean.parseBoolean(value);
            case "ismonster": return e instanceof Monster == Boolean.parseBoolean(value);
            case "iscreature": return e instanceof Creature == Boolean.parseBoolean(value);
            case "isanimal": return e instanceof Animals == Boolean.parseBoolean(value);
            case "isflying": return e instanceof Flying || e instanceof Player && ((Player) e).isFlying() == Boolean.parseBoolean(value);
            case "istype": return e.getType().name().equalsIgnoreCase(value);
            case "isfacing": return getFacing(e).name().toLowerCase().startsWith(value);
            case "isop": return e.isOp() == Boolean.parseBoolean(value);
            case "isinsidevehicle": return e.isInsideVehicle() == Boolean.parseBoolean(value);
            case "isriding": return e.isInsideVehicle() && e.getVehicle().getType().name().equalsIgnoreCase(value);
            case "iscustomnamevisible": return e.isCustomNameVisible() == Boolean.parseBoolean(value);
            case "isaggressive": return isAggressive(e.getType()) == Boolean.parseBoolean(value);
            case "isneutral": return isNeutral(e.getType()) == Boolean.parseBoolean(value);
            case "ispassive": return isPassive(e.getType()) == Boolean.parseBoolean(value);
            case "isonground": return e.isOnGround() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedAgeable(Entity e, String condition, String value) {
        switch (condition) {
            case "isadult": return e instanceof Ageable && ((Ageable) e).isAdult() == Boolean.parseBoolean(value);
            case "isbaby": return e instanceof Zombie && ((Zombie) e).isBaby() || e instanceof Ageable && ((Ageable) e).isAdult() != Boolean.parseBoolean(value);
            case "canbreed": return e instanceof Ageable && ((Ageable) e).canBreed() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedAnimals(Entity e, String condition, String value) {
        switch (condition) {
            case "inlovemode": return !LEGACY && !THIRTEEN && e instanceof Animals && ((Animals) e).isLoveMode() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedArmorStand(Entity e, String condition, String value) {
        switch (condition) {
            case "hasbaseplate": return e instanceof ArmorStand && ((ArmorStand) e).hasBasePlate() == Boolean.parseBoolean(value);
            case "hasarms": return e instanceof ArmorStand && ((ArmorStand) e).hasArms() == Boolean.parseBoolean(value);
            case "ismarker": return e instanceof ArmorStand && ((ArmorStand) e).isMarker() == Boolean.parseBoolean(value);
            case "issmall": return e instanceof ArmorStand && ((ArmorStand) e).isSmall() == Boolean.parseBoolean(value);
            case "isvisible": return e instanceof ArmorStand && ((ArmorStand) e).isVisible() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedBat(Entity e, String condition, String value) {
        switch (condition) {
            case "isawake": return e instanceof Bat && ((Bat) e).isAwake() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedCat(Entity e, String condition, String value) {
        switch (condition) {
            case "cattype": return LEGACY ? e instanceof Ocelot && ((Ocelot) e).getCatType().name().equalsIgnoreCase(value) : e instanceof Cat && ((Cat) e).getCatType().name().equalsIgnoreCase(value);
            case "collarcolor":
                if(e instanceof Wolf) {
                    return ((Wolf) e).getCollarColor().name().equalsIgnoreCase(value);
                } else if(!LEGACY && !THIRTEEN) {
                    return e instanceof Cat && ((Cat) e).getCollarColor().name().equalsIgnoreCase(value);
                } else {
                    return false;
                }
            default:
                return true;
        }
    }
    default boolean passedChestedHorse(Entity e, String condition, String value) {
        switch (condition) {
            case "iscarryingchest":
                if(EIGHT || NINE || TEN) {
                    return e instanceof Horse && ((Horse) e).isCarryingChest() == Boolean.parseBoolean(value);
                } else {
                    return e instanceof ChestedHorse && ((ChestedHorse) e).isCarryingChest() == Boolean.parseBoolean(value);
                }
            default: return true;
        }
    }
    default boolean passedCreeper(Entity e, String condition, String value) {
        switch (condition) {
            case "ispowered": return e instanceof Creeper && ((Creeper) e).isPowered() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedEnderCrystal(Entity e, String condition, String value) {
        switch (condition) {
            case "isshowingbottom": return EIGHT ? true : e instanceof EnderCrystal && ((EnderCrystal) e).isShowingBottom() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedEnderDragon(Entity e, String condition, String value) {
        switch (condition) {
            case "phase": return EIGHT ? true : e instanceof EnderDragon && ((EnderDragon) e).getPhase().name().equalsIgnoreCase(value);
            default: return true;
        }
    }
    default boolean passedEnderman(Entity e, String condition, String value) {
        switch (condition) {
            case "iscarrying": return e instanceof Enderman && UMaterial.match(((Enderman) e).getCarriedMaterial().getItemType().name()).name().equalsIgnoreCase(value);
            default: return true;
        }
    }
    default boolean passedEndermite(Entity e, String condition, String value) {
        switch (condition) {
            case "isplayerspawned": return LEGACY || THIRTEEN ? false : e instanceof Endermite && ((Endermite) e).isPlayerSpawned() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedEntity(Entity e, String condition, String value) {
        switch (condition) {
            case "inbiome":
                final Chunk chunk = e.getLocation().getChunk();
                return e.getWorld().getBiome(chunk.getX(), chunk.getZ()).name().equalsIgnoreCase(value);
            case "inworld": return e.getWorld().getName().equals(value);
            case "isglowing": return !EIGHT && e.isGlowing() == Boolean.parseBoolean(value);
            case "isinvulnerable": return !EIGHT && e.isInvulnerable() == Boolean.parseBoolean(value);
            case "issilent": return !EIGHT && !NINE && e.isSilent() == Boolean.parseBoolean(value);
            case "isitem": return e instanceof Item == Boolean.parseBoolean(value);
            case "hasgravity": return e instanceof ArmorStand && ((ArmorStand) e).hasGravity() || !EIGHT && !NINE && !TEN && e.hasGravity() == Boolean.parseBoolean(value);
            case "worlddifficulty": return e.getWorld().getDifficulty().name().equalsIgnoreCase(value);
            default: return true;
        }
    }
    default boolean passedEvoker(Entity e, String condition, String value) {
        switch (condition) {
            case "currentspell": return EIGHT || NINE | TEN ? false : e instanceof Evoker && ((Evoker) e).getCurrentSpell().name().equalsIgnoreCase(value);
            default: return true;
        }
    }
    default boolean passedExplosive(Entity e, String condition, String value) {
        switch (condition) {
            case "isincendiary": return e instanceof Explosive && ((Explosive) e).isIncendiary() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedFallenBlock(Event event, Entity e, String condition, String value) {
        switch (condition) {
            case "material":
                return e instanceof FallingBlock && value.equalsIgnoreCase(UMaterial.match(((FallingBlock) e).getMaterial().name()).name())
                    || event instanceof BlockPlaceEvent && value.equalsIgnoreCase(UMaterial.match(((BlockPlaceEvent) event).getBlock().getType().name()).name())
                    || event instanceof BlockBreakEvent && value.equalsIgnoreCase(UMaterial.match(((BlockBreakEvent) event).getBlock().getType().name()).name());
            case "canhurtentities": return e instanceof FallingBlock && ((FallingBlock) e).canHurtEntities() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedFirework(Entity e, String condition, String value) {
        switch (condition) {
            case "isshotatangle": return LEGACY || THIRTEEN ? false : e instanceof Firework && ((Firework) e).isShotAtAngle() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedFox(Entity e, String condition, String value) {
        switch (condition) {
            case "foxtype": return LEGACY || THIRTEEN ? false : e instanceof Fox && ((Fox) e).getFoxType().name().equalsIgnoreCase(value);
            case "iscrouching": return LEGACY || THIRTEEN ? false : e instanceof Fox && ((Fox) e).isCrouching() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedGuardian(Entity e, String condition, String value) {
        switch (condition) {
            case "iselder": return e instanceof Guardian && ((Guardian) e).isElder() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedHorse(Entity e, String condition, String value) {
        switch (condition) {
            case "isvariant":
                if(e instanceof Horse) {
                    return ((Horse) e).getVariant().name().equalsIgnoreCase(value);
                } else if(e instanceof MushroomCow && (!LEGACY || THIRTEEN)) {
                    return ((MushroomCow) e).getVariant().name().equalsIgnoreCase(value);
                } else if(!(EIGHT || NINE || TEN || ELEVEN)) {
                    return e instanceof Parrot && ((Parrot) e).getVariant().name().equalsIgnoreCase(value);
                } else {
                    return false;
                }
            case "color":
                if(e instanceof Horse) {
                    return ((Horse) e).getColor().name().equalsIgnoreCase(value);
                } else if(!(EIGHT || NINE || TEN)) {
                    return e instanceof Llama && ((Llama) e).getColor().name().equalsIgnoreCase(value);
                } else if(e instanceof Colorable) {
                    return ((Colorable) e).getColor().name().equalsIgnoreCase(value);
                } else {
                    return false;
                }
            case "style": return e instanceof Horse && ((Horse) e).getStyle().name().equalsIgnoreCase(value);
            default: return true;
        }
    }
    default boolean passedHusk(Entity e, String condition, String value) {
        switch (condition) {
            case "isconverting":
                if(LEGACY || THIRTEEN) {
                    return false;
                } else {
                    final boolean b = Boolean.parseBoolean(value);
                    return b && (e instanceof Husk && ((Husk) e).isConverting() || e instanceof PigZombie && ((PigZombie) e).isConverting() || e instanceof Zombie && ((Zombie) e).isConverting());
                }
            default:
                return true;
        }
    }
    default boolean passedIronGolem(Entity e, String condition, String value) {
        switch (condition) {
            case "isplayercreated": return e instanceof IronGolem && ((IronGolem) e).isPlayerCreated() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedLightingStrike(Entity e, String condition, String value) {
        switch (condition) {
            case "iseffect": return e instanceof LightningStrike && ((LightningStrike) e).isEffect() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedLivingEntity(Entity e, String condition, String value) {
        switch (condition) {
            case "isleashed": return e instanceof LivingEntity && ((LivingEntity) e).isLeashed() == Boolean.parseBoolean(value);
            case "isswimming": return LEGACY ? false : e instanceof LivingEntity && ((LivingEntity) e).isSwimming() == Boolean.parseBoolean(value);
            case "isgliding": return EIGHT ? false : e instanceof LivingEntity && ((LivingEntity) e).isGliding() == Boolean.parseBoolean(value);
            case "isholding": return e instanceof LivingEntity && ((LivingEntity) e).getEquipment().getItemInHand().getType().name().toLowerCase().endsWith(value);
            case "issleeping": return LEGACY || THIRTEEN ? false : e instanceof LivingEntity && ((LivingEntity) e).isSleeping() == Boolean.parseBoolean(value);
            case "hasai": return EIGHT || e instanceof LivingEntity && ((LivingEntity) e).hasAI() == Boolean.parseBoolean(value);
            case "iscollideable": return EIGHT || e instanceof LivingEntity && ((LivingEntity) e).isCollidable() == Boolean.parseBoolean(value);
            case "health<": // health<=
                return e instanceof LivingEntity && ((LivingEntity) e).getHealth() <= evaluate(value);
            case "health>": // health>=
                return e instanceof LivingEntity && ((LivingEntity) e).getHealth() >= evaluate(value);
            case "haspotioneffect":
                final PotionEffectType t = getPotionEffectType(value);
                return t != null && e instanceof LivingEntity && ((LivingEntity) e).hasPotionEffect(t);
            case "nodamageticks<": // nodamageticks<=
                return e instanceof LivingEntity && ((LivingEntity) e).getNoDamageTicks() <= evaluate(value);
            case "nodamageticks>": // nodamageticks>=
                return e instanceof LivingEntity && ((LivingEntity) e).getNoDamageTicks() >= evaluate(value);
            case "remainingair": return e instanceof LivingEntity && ((LivingEntity) e).getRemainingAir() == evaluate(value);
            case "remainingair<": // remainingair<=
                return e instanceof LivingEntity && ((LivingEntity) e).getRemainingAir() <= evaluate(value);
            case "remainingair>": // remainingair>=
                return e instanceof LivingEntity && ((LivingEntity) e).getRemainingAir() >= evaluate(value);
            default: return true;
        }
    }
    default boolean passedMinecart(Entity e, String condition, String value) {
        switch (condition) {
            case "isslowwhenempty": return e instanceof Minecart && ((Minecart) e).isSlowWhenEmpty() == Boolean.parseBoolean(value);
            case "displayedblock": return e instanceof Minecart && UMaterial.match(((Minecart) e).getDisplayBlock().getItemType().name()).name().equalsIgnoreCase(value);
            default: return true;
        }
    }
    default boolean passedMob(Entity e, String condition, String value) {
        switch (condition) {
            case "hastarget":
                if(!LEGACY) {
                    return e instanceof Mob && ((Mob) e).getTarget() != null == Boolean.parseBoolean(value);
                } else {
                    return false;
                }
            default:
                return true;
        }
    }
    default boolean passedPainting(Entity e, String condition, String value) {
        switch (condition) {
            case "art": return e instanceof Painting && ((Painting) e).getArt().name().equalsIgnoreCase(value);
            default: return true;
        }
    }
    default boolean passedPanda(Entity e, String condition, String value) {
        switch (condition) {
            case "maingene": return LEGACY || THIRTEEN ? false : e instanceof Panda && ((Panda) e).getMainGene().name().equalsIgnoreCase(value);
            case "maingeneisrecessive": return LEGACY || THIRTEEN ? false : e instanceof Panda && ((Panda) e).getMainGene().isRecessive() == Boolean.parseBoolean(value);
            case "hiddengene": return LEGACY || THIRTEEN ? false : e instanceof Panda && ((Panda) e).getHiddenGene().name().equalsIgnoreCase(value);
            case "hiddengeneisrecessive": return LEGACY || THIRTEEN ? false : e instanceof Panda && ((Panda) e).getHiddenGene().isRecessive() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedPig(Entity e, String condition, String value) {
        switch (condition) {
            case "hassaddle": return e instanceof Pig && ((Pig) e).hasSaddle() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedPigZombie(Entity e, String condition, String value) {
        switch (condition) {
            case "isangry":
                if(e instanceof PigZombie) {
                    return ((PigZombie) e).isAngry() == Boolean.parseBoolean(value);
                } else if(e instanceof Wolf) {
                    return ((Wolf) e).isAngry() == Boolean.parseBoolean(value);
                } else {
                    return false;
                }
            default:
                return true;
        }
    }
    default boolean passedPlayer(Entity e, String condition, String value) {
        switch (condition) {
            case "issneaking": return e instanceof Player && ((Player) e).isSneaking() == Boolean.parseBoolean(value);
            case "isblocking": return e instanceof Player && ((Player) e).isBlocking() == Boolean.parseBoolean(value);
            case "isflying": return e instanceof Player && ((Player) e).isFlying() == Boolean.parseBoolean(value);
            case "issprinting": return e instanceof Player && ((Player) e).isSprinting() == Boolean.parseBoolean(value);
            case "isriptiding": return !LEGACY && e instanceof Player && ((Player) e).isRiptiding() == Boolean.parseBoolean(value);
            case "issleepignored": return e instanceof Player && ((Player) e).isSleepingIgnored() == Boolean.parseBoolean(value);
            case "allowsflight": return e instanceof Player && ((Player) e).getAllowFlight() == Boolean.parseBoolean(value);
            case "ishealthscaled": return e instanceof Player && ((Player) e).isHealthScaled() == Boolean.parseBoolean(value);
            case "weather": return e instanceof Player && ((Player) e).getPlayerWeather().name().equalsIgnoreCase(value);
            case "totalexp": return e instanceof Player && getTotalExperience((Player) e) == (int) evaluate(value);
            case "totalexp<": // totalexp<=
                return e instanceof Player && getTotalExperience((Player) e) <= (int) evaluate(value);
            case "totalexp>": // totalexp>=
                return e instanceof Player && getTotalExperience((Player) e) >= (int) evaluate(value);
            case "explevel": return e instanceof Player && ((Player) e).getLevel() == (int) evaluate(value);
            case "explevel<": // explevel<=
                return e instanceof Player && ((Player) e).getLevel() <= (int) evaluate(value);
            case "explevel>": // explevel>=
                return e instanceof Player && ((Player) e).getLevel() >= (int) evaluate(value);
            case "foodlevel": return e instanceof Player && ((Player) e).getFoodLevel() == (int) evaluate(value);
            case "foodlevel<": // foodlevel<=
                return e instanceof Player && ((Player) e).getFoodLevel() <= (int) evaluate(value);
            case "foodlevel>": // foodlevel>=
                return e instanceof Player && ((Player) e).getFoodLevel() >= (int) evaluate(value);
            case "saturation": return e instanceof Player && ((Player) e).getSaturation() == Float.parseFloat(value);
            case "saturation<": // saturation<=
                return e instanceof Player && ((Player) e).getSaturation() <= Float.parseFloat(value);
            case "saturation>": // saturation>=
                return e instanceof Player && ((Player) e).getSaturation() >= Float.parseFloat(value);
            case "viewdistance": return LEGACY || e instanceof Player && ((Player) e).getClientViewDistance() == (int) evaluate(value);
            case "viewdistance<": // viewdistance<=
                return LEGACY || e instanceof Player && ((Player) e).getClientViewDistance() <= (int) evaluate(value);
            case "viewdistance>": // viewdistance>=
                return LEGACY || e instanceof Player && ((Player) e).getClientViewDistance() >= (int) evaluate(value);
            case "language": return EIGHT || NINE || TEN || ELEVEN || e instanceof Player && ((Player) e).getLocale().equalsIgnoreCase(value);
            case "walkspeed": return e instanceof Player && ((Player) e).getWalkSpeed() == Float.parseFloat(value);
            case "walkspeed<": // walkspeed<=
                return e instanceof Player && ((Player) e).getWalkSpeed() <= Float.parseFloat(value);
            case "walkspeed>": // walkspeed>=
                return e instanceof Player && ((Player) e).getWalkSpeed() >= Float.parseFloat(value);
            case "flyspeed": return e instanceof Player && ((Player) e).getFlySpeed() == Float.parseFloat(value);
            case "flyspeed<": // flyspeed<=
                return e instanceof Player && ((Player) e).getFlySpeed() <= Float.parseFloat(value);
            case "flyspeed>": // flyspeed>=
                return e instanceof Player && ((Player) e).getFlySpeed() >= Float.parseFloat(value);
            default:
                return true;
        }
    }
    default boolean passedProjectile(Entity e, String condition, String value) {
        switch (condition) {
            case "doesbounce": return e instanceof Projectile && ((Projectile) e).doesBounce() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedRabbit(Entity e, String condition, String value) {
        switch (condition) {
            case "rabbittype": return e instanceof Rabbit && ((Rabbit) e).getRabbitType().name().equalsIgnoreCase(value);
            default: return true;
        }
    }
    default boolean passedRaider(Entity e, String condition, String value) {
        switch (condition) {
            case "ispatrolleader": return LEGACY || THIRTEEN ? false : e instanceof Raider && ((Raider) e).isPatrolLeader() == Boolean.parseBoolean(value);
            case "patroltargetblock": return LEGACY ? false : e instanceof Raider && UMaterial.match(((Raider) e).getPatrolTarget().getType().name()).name().toLowerCase().endsWith(value);
            default: return true;
        }
    }
    default boolean passedSheep(Entity e, String condition, String value) {
        switch (condition) {
            case "issheared": return e instanceof Sheep && ((Sheep) e).isSheared() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedSittable(Entity e, String condition, String value) {
        switch (condition) {
            case "issitting":
                final boolean first = Boolean.parseBoolean(value);
                if(EIGHT || NINE || TEN || ELEVEN) {
                    return first == e instanceof Wolf && ((Wolf) e).isSitting()/* || e instanceof Ocelot && ((Ocelot) e).isSitting()*/;
                } else {
                    return first == e instanceof Sittable && ((Sittable) e).isSitting();
                }
            default:
                return true;
        }
    }
    default boolean passedSkeleton(Entity e, String condition, String value) {
        switch (condition) {
            case "skeletontype": return e instanceof Skeleton && ((Skeleton) e).getSkeletonType().name().equalsIgnoreCase(value);
            default: return true;
        }
    }
    default boolean passedSlime(Entity e, String condition, String value) {
        switch (condition) {
            case "size":
                int v = (int) evaluate(value);
                return e instanceof Slime && ((Slime) e).getSize() == v || !LEGACY && e instanceof Phantom && ((Phantom) e).getSize() == v;
            case "size<": // size<=
                v = (int) evaluate(value);
                return e instanceof Slime && ((Slime) e).getSize() <= v || !LEGACY && e instanceof Phantom && ((Phantom) e).getSize() <= v;
            case "size>": // size>=
                v = (int) evaluate(value);
                return e instanceof Slime && ((Slime) e).getSize() >= v || !LEGACY && e instanceof Phantom && ((Phantom) e).getSize() >= v;
            default:
                return true;
        }
    }
    default boolean passedSnowman(Entity e, String condition, String value) {
        switch (condition) {
            case "isderp": return EIGHT || NINE ? false : e instanceof Snowman && ((Snowman) e).isDerp() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedTameable(Entity e, String condition, String value) {
        switch (condition) {
            case "istamed": return e instanceof Tameable && ((Tameable) e).isTamed() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedTropicalFish(Entity e, String condition, String value) {
        switch (condition) {
            case "patterncolor": return LEGACY ? false : e instanceof TropicalFish && ((TropicalFish) e).getPatternColor().name().equalsIgnoreCase(value);
            case "bodycolor": return LEGACY ? false : e instanceof TropicalFish && ((TropicalFish) e).getBodyColor().name().equalsIgnoreCase(value);
            case "pattern": return LEGACY ? false : e instanceof TropicalFish && ((TropicalFish) e).getPattern().name().equalsIgnoreCase(value);
            default: return true;
        }
    }
    default boolean passedVex(Entity e, String condition, String value) {
        switch (condition) {
            case "ischarging": return EIGHT || NINE || TEN ? false : e instanceof Vex && ((Vex) e).isCharging() == Boolean.valueOf(value);
            default: return true;
        }
    }
    default boolean passedVillager(Entity e, String condition, String value) {
        switch (condition) {
            case "profession": return e instanceof Zombie && ((Zombie) e).isVillager() ? ((Zombie) e).getVillagerProfession().name().equalsIgnoreCase(value) : e instanceof Villager && ((Villager) e).getProfession().name().equalsIgnoreCase(value);
            case "villagertype": return e instanceof Villager && !(LEGACY || THIRTEEN) && ((Villager) e).getVillagerType().name().equalsIgnoreCase(value);
            default: return true;
        }
    }
    default boolean passedWitherSkull(Entity e, String condition, String value) {
        switch (condition) {
            case "ischarged": return e instanceof WitherSkull && ((WitherSkull) e).isCharged() == Boolean.parseBoolean(value);
            default: return true;
        }
    }
    default boolean passedZombie(Entity e, String condition, String value) {
        switch (condition) {
            case "isvillager": return e instanceof Zombie && ((Zombie) e).isVillager() == Boolean.parseBoolean(value);
            default: return true;
        }
    }



    default boolean passedEvent(Event event, Entity e, String condition, String value) {
        switch (condition) {
            case "action":
                final String action = event instanceof PlayerInteractEvent ? ((PlayerInteractEvent) event).getAction().name() : null;
                return action != null && action.startsWith(value.toUpperCase());
            case "cause":
                final EntityDamageEvent d = event instanceof EntityDamageEvent ? (EntityDamageEvent) event : null;
                if(d != null) {
                    final String cause = d.getCause().name();
                    final String[] v = value.split("\\|\\|");
                    final List<Boolean> did = new ArrayList<>();
                    for(String ss : v) {
                        did.add(cause.equalsIgnoreCase(ss));
                    }
                    return did.contains(true);
                }
                return false;
            case "tier":
                return event instanceof PlayerClaimEnvoyCrateEvent && ((PlayerClaimEnvoyCrateEvent) event).type.getType().getIdentifier().equals(value);
            case "israritybook":
                return event instanceof EnchanterPurchaseEvent && valueOfCustomEnchantRarity(valueOfCustomEnchant(((EnchanterPurchaseEvent) event).purchased)) != null;
            case "result":
                return event instanceof CustomEnchantApplyEvent && ((CustomEnchantApplyEvent) event).result.equalsIgnoreCase(value);
            case "rarity":
                String identifier = null;
                if(event instanceof CustomEnchantApplyEvent) {
                    identifier = valueOfCustomEnchantRarity(((CustomEnchantApplyEvent) event).enchant).getIdentifier();
                } else if(event instanceof EnchanterPurchaseEvent) {
                    final EnchanterPurchaseEvent epe = (EnchanterPurchaseEvent) event;
                    final CustomEnchant enchant = valueOfCustomEnchant(epe.purchased);
                    final EnchantRarity rarity = enchant != null ? valueOfCustomEnchantRarity(enchant) : null;
                    identifier = rarity != null ? rarity.getIdentifier() : null;
                } else if(event instanceof RandomizationScrollUseEvent) {
                    identifier = ((RandomizationScrollUseEvent) event).scroll.getIdentifier();
                } else if(event instanceof ServerCrateOpenEvent) {
                    identifier = ((ServerCrateOpenEvent) event).crate.getIdentifier();
                }
                return identifier != null && identifier.equals(value);
            case "enchant":
                CustomEnchant enchant = null;
                if(event instanceof CustomEnchantApplyEvent) {
                    enchant = ((CustomEnchantApplyEvent) event).enchant;
                } else if(event instanceof CustomEnchantProcEvent) {
                    enchant = ((CustomEnchantProcEvent) event).getEnchant();
                }
                return enchant != null && enchant.getIdentifier().equals(value);
            case "success<": // success<=
                return event instanceof CustomEnchantApplyEvent && ((CustomEnchantApplyEvent) event).success <= evaluate(value);
            case "destroy<": // destroy<=
                return event instanceof CustomEnchantApplyEvent && ((CustomEnchantApplyEvent) event).destroy <= evaluate(value);
            case "booster":
                return event instanceof BoosterActivateEvent && ((BoosterActivateEvent) event).booster.getIdentifier().equals(value);
            case "inventorypetoncooldown":
                ItemStack is = null;
                if(event instanceof PlayerInteractEvent) {
                    is = ((PlayerInteractEvent) event).getItem();
                }
                String info = is != null ? getRPItemStackValue(is, "InventoryPetInfo") : null;
                return info != null && System.currentTimeMillis() >= Long.parseLong(info.split(":")[3]) == Boolean.parseBoolean(value);
            case "trinketoncooldown":
                is = null;
                if(event instanceof PlayerInteractEvent) {
                    is = ((PlayerInteractEvent) event).getItem();
                }
                info = is != null ? getRPItemStackValue(is, "TrinketInfo") : null;
                return info != null && System.currentTimeMillis() >= Long.parseLong(info.split(":")[1]) == Boolean.parseBoolean(value);
            case "kittype":
                final CustomKit kit = e instanceof KitPreClaimEvent ? ((KitPreClaimEvent) e).getKit() : e instanceof KitClaimEvent ? ((KitClaimEvent) e).getKit() : null;
                return kit != null && kit.getIdentifier().startsWith(value);

            case "skill":
                final com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent ev = (com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent) event;
                final String skill = MCMMOAPI.getMCMMOAPI().getSkillName(ev);
                return skill != null && skill.equalsIgnoreCase(value);
            default:
                return true;
        }
    }
    default boolean passedCustomCondition(Event event, HashMap<String, Entity> entities, String entityKey, Entity e, String condition, String key, String value) {
        String target = condition.startsWith(key) ? condition.split(key)[1] : condition;
        if(target.contains("=")) target = target.split("=")[0];
        final EventCondition con = getEventCondition(target.toUpperCase());
        return con == null || con.check(value) && con.check(event) && con.check(event, value) && con.check(event, e) && con.check(e, value) && con.check(entityKey, entities, value);
    }
    default boolean passedRandomPackage(Entity e, String condition, String value) {
        switch (condition) {
            case "equippedarmorset":
                final ArmorSet armorset = e instanceof Player ? valueOfArmorSet((Player) e) : null;
                return armorset != null && armorset.getIdentifier().equals(value);
            case "equippedmask":
                final EntityEquipment ee = e instanceof Player ? ((Player) e).getEquipment() : null;
                final Mask mask = ee != null ? valueOfMask(ee.getHelmet()) : null;
                return mask != null && mask.getIdentifier().equals(value);
            case "equippedtitle":
                Title t = RPPlayer.get(e.getUniqueId()).getActiveTitle();
                return t != null &&  e instanceof Player && t.getIdentifier().equals(value);
            case "ownstitle":
                t = e instanceof Player ? getTitle(value) : null;
                return t != null && RPPlayer.get(e.getUniqueId()).getTitles().contains(t);
            case "hasactivefilter":
                return e instanceof Player && RPPlayer.get(e.getUniqueId()).hasActiveFilter() == Boolean.parseBoolean(value);
            case "hasactiveplayerquest":
                final PlayerQuest q = e instanceof Player ? getPlayerQuest(value) : null;
                final HashMap<PlayerQuest, ActivePlayerQuest> pquests = q != null ? RPPlayer.get(e.getUniqueId()).getQuests() : null;
                return pquests != null && pquests.containsKey(q) && !pquests.get(q).isExpired();
            case "hasactiveraritygem":
                final String[] values = value.split(":");
                final int l = values.length;
                return e instanceof Player && RPPlayer.get(e.getUniqueId()).hasActiveRarityGem(getRarityGem(values[0])) == (l < 2 || Boolean.parseBoolean(values[1]));
            case "hasactivetitle":
                return e instanceof Player && RPPlayer.get(e.getUniqueId()).getActiveTitle() != null == Boolean.parseBoolean(value);
            case "hascustomentities":
                return e instanceof Player && !RPPlayer.get(e.getUniqueId()).getCustomEnchantEntities().isEmpty() == Boolean.parseBoolean(value);
            case "hasequippedarmorset":
                return e instanceof Player && valueOfArmorSet((Player) e) != null == Boolean.parseBoolean(value);
            case "hasequippedmask":
                final EntityEquipment eq = e instanceof Player ? ((Player) e).getEquipment() : null;
                return eq != null && valueOfMask(eq.getHelmet()) != null == Boolean.parseBoolean(value);
            case "hasfiltereditem":
                final List<UMaterial> m = e instanceof Player ? RPPlayer.get(e.getUniqueId()).getFilteredItems() : null;
                return m != null && m.contains(UMaterial.match(value));
            case "iscustomboss":
                return LivingCustomBoss.living != null && LivingCustomBoss.living.containsKey(e.getUniqueId()) == Boolean.parseBoolean(value);
            default:
                return true;
        }
    }
}
