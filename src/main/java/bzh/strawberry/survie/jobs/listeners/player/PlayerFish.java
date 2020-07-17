package bzh.strawberry.survie.jobs.listeners.player;

import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

/*
 * This file (PlayerFish) is part of a project Survie.
 * It was created on 17/07/2020 09:58 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class PlayerFish implements Listener {

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        SurviePlayer surviePlayer = Survie.SURVIE.getSurviePlayer(player.getUniqueId());
        if (surviePlayer != null && surviePlayer.getJobData() != null && !event.isCancelled() && (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH))) {
            if (surviePlayer.getJobData().getName().equals("Pêcheur") && player.getGameMode() == GameMode.SURVIVAL) {
                Item item = (Item) event.getCaught();
                if (item == null)
                    return;
                switch (item.getItemStack().getType()) {

                    case STICK:
                    case LEATHER_BOOTS:
                    case LEATHER:
                        surviePlayer.getJobData().addHourXP(1);
                        break;
                    case PODZOL:
                    case TRIPWIRE_HOOK:
                    case BOWL:
                    case POTION:
                    case ROTTEN_FLESH:
                        surviePlayer.getJobData().addHourXP(2);
                        break;
                    case STRING:
                        surviePlayer.getJobData().addHourXP(3);
                        break;
                    case INK_SAC:
                        surviePlayer.getJobData().addHourXP(20);
                        break;
                    case COD:
                        surviePlayer.getJobData().addHourXP(25);
                        break;
                    case FISHING_ROD:
                        surviePlayer.getJobData().addHourXP(30);
                        break;
                    case LILY_PAD:
                        surviePlayer.getJobData().addHourXP(35);
                        break;
                    case SALMON:
                        surviePlayer.getJobData().addHourXP(40);
                        break;
                    case NAME_TAG:
                    case BOW:
                    case SADDLE:
                        surviePlayer.getJobData().addHourXP(50);
                        break;
                    case ENCHANTED_BOOK:
                        surviePlayer.getJobData().addHourXP(70);
                        break;
                    case NAUTILUS_SHELL:
                        surviePlayer.getJobData().addHourXP(75);
                        break;
                    case PUFFERFISH:
                        surviePlayer.getJobData().addHourXP(100);
                        break;
                    case TROPICAL_FISH:
                        surviePlayer.getJobData().addHourXP(150);
                        break;

                }
            }
        }
    }
}