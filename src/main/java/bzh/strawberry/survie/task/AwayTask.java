package bzh.strawberry.survie.task;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.survie.Survie;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This file (AwayTask) is part of a project Survie.
 * It was created on 17/07/2020 19:42 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class AwayTask implements Runnable {
    private final Map<UUID, Integer> timers;

    public AwayTask() {
        this.timers = new ConcurrentHashMap<>();
    }

    public void run() {
        for (UUID uuid : this.timers.keySet()) {
            if (Survie.SURVIE.getServer().getPlayer(uuid) == null) {
                this.removePlayer(uuid);
                continue;
            }

            int timer = this.timers.get(uuid);
            timer++;

            if (timer == 900 && !Survie.SURVIE.getServer().getPlayer(uuid).isOp()) {
                Survie.SURVIE.getServer().getPlayer(uuid).kickPlayer(StrawAPI.getAPI().getL10n().getTranslation(uuid, "survie.afk"));
            } else {
                this.timers.put(uuid, timer);
            }
        }
    }

    public void registerPlayer(UUID uuid) {
        this.timers.put(uuid, 0);
    }

    public void removePlayer(UUID uuid) {
        this.timers.remove(uuid);
    }

    public void resetPlayer(UUID uuid) {
        if (this.timers.containsKey(uuid))
            this.timers.put(uuid, 0);
    }
}
