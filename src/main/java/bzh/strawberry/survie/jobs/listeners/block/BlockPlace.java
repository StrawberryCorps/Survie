package bzh.strawberry.survie.jobs.listeners.block;

import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

/*
 * This file (BlockPlace) is part of a project Survie.
 * It was created on 17/07/2020 09:58 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class BlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        SurviePlayer surviePlayer = Survie.SURVIE.getSurviePlayer(player.getUniqueId());
        switch (event.getBlock().getType()) {
            case OAK_LOG:
            case BIRCH_LOG:
            case SPRUCE_LOG:
            case DARK_OAK_LOG:
            case JUNGLE_LOG:
            case ACACIA_LOG:
            case STONE:
            case COBBLESTONE:
            case DIORITE:
            case COAL_ORE:
            case ANDESITE:
            case GRANITE:
            case LAPIS_ORE:
            case REDSTONE_ORE:
            case IRON_ORE:
            case GOLD_ORE:
            case EMERALD_ORE:
            case DIAMOND_ORE:
            case CACTUS:
            case BAMBOO:
            case PUMPKIN:
            case MELON:
            case CARROT:
            case POTATO:
            case SUGAR_CANE:
            case KELP:
                event.getBlock().setMetadata("job", new FixedMetadataValue(Survie.SURVIE, true));
                break;
        }

        if (surviePlayer.getJobData() != null && surviePlayer.getJobData().getName().equals("Fermier")) {
            switch (event.getBlock().getType()) {
                case SWEET_BERRY_BUSH:
                    surviePlayer.getJobData().addHourXP(0.2);
                    break;
                case WHEAT:
                case COCOA:
                case PUMPKIN_STEM:
                case MELON_STEM:
                case BEETROOTS:
                case CARROTS:
                case POTATOES:
                    surviePlayer.getJobData().addHourXP(0.3);
                    break;
            }
        }

    }

}
