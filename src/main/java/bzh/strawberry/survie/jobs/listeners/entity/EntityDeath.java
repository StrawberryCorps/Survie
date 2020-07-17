package bzh.strawberry.survie.jobs.listeners.entity;

import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/*
 * This file (EntityDeath) is part of a project Survie.
 * It was created on 17/07/2020 09:58 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class EntityDeath implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            SurviePlayer surviePlayer = Survie.SURVIE.getSurviePlayer(player.getUniqueId());
            if (surviePlayer.getJobData() != null && player.getGameMode() == GameMode.SURVIVAL && surviePlayer.getJobData().getName().equals("Chasseur")) {
                switch (event.getEntity().getType()) {
                    case MAGMA_CUBE:
                    case SLIME:
                        surviePlayer.getJobData().addHourXP(1);
                        break;
                    case ENDERMAN:
                        surviePlayer.getJobData().addHourXP(3);
                        break;
                    case CAVE_SPIDER:
                    case PIG_ZOMBIE:
                        surviePlayer.getJobData().addHourXP(4);
                        break;
                    case TURTLE:
                    case HUSK:
                    case WITCH:
                    case COW:
                    case SPIDER:
                    case ZOMBIE:
                    case CHICKEN:
                    case SKELETON:
                    case RABBIT:
                    case SHEEP:
                    case PIG:
                    case LLAMA:
                    case MULE:
                    case SQUID:
                    case DONKEY:
                    case MUSHROOM_COW:
                    case CREEPER:
                    case ZOMBIE_VILLAGER:
                    case HORSE:
                        surviePlayer.getJobData().addHourXP(6);
                        break;
                    case DROWNED:
                    case VEX:
                        surviePlayer.getJobData().addHourXP(7);
                        break;
                    case PILLAGER:
                    case BLAZE:
                    case WOLF:
                        surviePlayer.getJobData().addHourXP(8);
                        break;
                    case GUARDIAN:
                    case SHULKER:
                    case VINDICATOR:
                    case PHANTOM:
                    case OCELOT:
                    case PARROT:
                    case FOX:
                    case STRAY:
                    case DOLPHIN:
                    case PANDA:
                    case POLAR_BEAR:
                    case CAT:
                        surviePlayer.getJobData().addHourXP(10);
                        break;
                    case EVOKER:
                        surviePlayer.getJobData().addHourXP(12);
                        break;
                    case WITHER_SKELETON:
                    case VILLAGER:
                        surviePlayer.getJobData().addHourXP(15);
                        break;
                    case ELDER_GUARDIAN:
                        surviePlayer.getJobData().addHourXP(20);
                        break;
                    case GHAST:
                        surviePlayer.getJobData().addHourXP(25);
                        break;
                    case RAVAGER:
                        surviePlayer.getJobData().addHourXP(100);
                        break;
                    case WITHER:
                        surviePlayer.getJobData().addHourXP(150);
                        break;
                    case ENDER_DRAGON:
                        surviePlayer.getJobData().addHourXP(500);
                        break;
                }
            }
        }
    }

}
