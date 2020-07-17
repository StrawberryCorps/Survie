package bzh.strawberry.survie.utils;

import bzh.strawberry.survie.Survie;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
 * This file (Hologram) is part of a project Survie.
 * It was created on 17/07/2020 19:45 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class Hologram {
    private static final double distance = 0.24D;

    private final HashMap<OfflinePlayer, Boolean> receivers;
    private final HashMap<Integer, EntityArmorStand> entities;
    private List<String> lines;
    private Location location;
    private final BukkitTask taskID;
    private final double rangeView = 60;
    private boolean linesChanged = false;
    private String chest;

    private static Hologram hologram;

    public Hologram(String... lines) {
        this.receivers = new HashMap<>();
        this.entities = new HashMap<>();

        hologram = this;

        this.lines = new ArrayList<>();
        this.lines.addAll(Arrays.asList(lines));

        this.linesChanged = true;

        this.taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(Survie.SURVIE, this::sendLinesForPlayers, 10L, 10L);
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getChest() {
        return chest;
    }

    public boolean addReceiver(OfflinePlayer offlinePlayer) {
        if (!offlinePlayer.isOnline())
            return false;

        Player p = offlinePlayer.getPlayer();
        boolean inRange = false;

        if (p.getLocation().getWorld() == this.location.getWorld() && p.getLocation().distance(this.location) <= this.rangeView) {
            inRange = true;
            this.sendLines(offlinePlayer.getPlayer());
        }

        this.receivers.put(offlinePlayer, inRange);

        return true;
    }

    public boolean removeReceiver(OfflinePlayer offlinePlayer) {
        if (!offlinePlayer.isOnline())
            return false;

        this.receivers.remove(offlinePlayer);
        this.removeLines(offlinePlayer.getPlayer());

        return true;
    }

    public boolean removeLineForPlayer(Player p, int line) {
        EntityArmorStand entity = this.entities.get(line);

        if (entity == null)
            return false;

        Reflection.sendPacket(p, new PacketPlayOutEntityDestroy(entity.getId()));

        return true;
    }

    public void removeLinesForPlayers() {
        for (OfflinePlayer offlinePlayer : this.receivers.keySet()) {
            if (!offlinePlayer.isOnline())
                continue;

            this.removeLines(offlinePlayer.getPlayer());
        }
    }

    public void destroy() {
        this.removeLinesForPlayers();

        this.clearEntities();
        this.clearLines();

        this.location = null;
    }

    public void fullDestroy() {
        this.destroy();
        this.receivers.clear();
        this.taskID.cancel();
    }

    public void change(String... lines) {
        this.removeLinesForPlayers();

        this.clearEntities();
        this.clearLines();

        this.lines = new ArrayList<>();
        this.lines.addAll(Arrays.asList(lines));
        this.linesChanged = true;

        this.generateLines(this.location);
        this.sendLinesForPlayers();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void generateLines() {
        this.generateLines(this.location);
    }

    public Hologram generateLines(Location loc) {
        Location first = loc.clone().add(0, (this.lines.size() / 2) * distance, 0);

        for (int i = 0; i < this.lines.size(); i++) {
            this.entities.put(i, generateEntitiesForLine(first.clone(), this.lines.get(i)));
            first.subtract(0, distance, 0);
        }

        this.location = loc;
        return this;
    }

    public void setLinesChanged(boolean linesChanged) {
        this.linesChanged = linesChanged;
    }

    public void sendLinesForPlayers() {
        for (OfflinePlayer offlinePlayer : this.receivers.keySet()) {
            if (!offlinePlayer.isOnline())
                continue;

            Player p = offlinePlayer.getPlayer();
            boolean wasInRange = this.receivers.get(offlinePlayer);
            boolean inRange = false;

            if (p.getLocation().getWorld() == this.location.getWorld() && p.getLocation().distance(this.location) <= this.rangeView)
                inRange = true;

            if (this.linesChanged && inRange) {
                this.removeLines(p);
                this.sendLines(p);
                this.linesChanged = false;
            } else if (!p.getLocation().getWorld().getName().equals(this.location.getWorld().getName())) {
                this.removeLines(p);
            } else if (wasInRange == inRange) {
                continue;
            } else if (wasInRange) {
                this.removeLines(p);
            } else {
                this.sendLines(p);
            }

            this.receivers.put(offlinePlayer, inRange);
        }
    }

    public void sendLines(Player p) {
        for (int i = 0; i < this.lines.size(); i++)
            this.sendPacketForLine(p, i);
    }

    public void removeLines(Player p) {
        for (int i = 0; i < this.lines.size(); i++)
            this.removeLineForPlayer(p, i);
    }

    public void clearEntities() {
        this.entities.clear();
    }

    public void clearLines() {
        this.lines.clear();
    }

    public Location getLocation() {
        return this.location;
    }

    private static EntityArmorStand generateEntitiesForLine(Location loc, String text) {
        EntityArmorStand entity = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ());
        entity.setSmall(true);
        entity.setInvisible(true);
        entity.setNoGravity(true);
        entity.setCustomName(new ChatMessage(text));

        entity.setCustomNameVisible(true);
        entity.setLocation(loc.getX(), loc.getY() - 2, loc.getZ(), 0, 0);

        return entity;
    }

    private boolean sendPacketForLine(Player p, int line) {
        EntityArmorStand entity = this.entities.get(line);

        if (entity == null)
            return false;

        Reflection.sendPacket(p, new PacketPlayOutSpawnEntity(entity, 78));
        Reflection.sendPacket(p, new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true));
        return true;
    }


}