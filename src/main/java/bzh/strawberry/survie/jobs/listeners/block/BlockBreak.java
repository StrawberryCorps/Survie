package bzh.strawberry.survie.jobs.listeners.block;

import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.CropState;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Crops;

/*
 * This file (BlockBreak) is part of a project Survie.
 * It was created on 17/07/2020 09:58 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class BlockBreak implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        SurviePlayer surviePlayer = Survie.SURVIE.getSurviePlayer(player.getUniqueId());
        if (surviePlayer != null && surviePlayer.getJobData() != null && !event.isCancelled()) {
            if (surviePlayer.getJobData().getName().equals("Bûcheron") && player.getGameMode() == GameMode.SURVIVAL) {
                Block block = event.getBlock();
                if (!block.hasMetadata("job")) {
                    switch (block.getType()) {
                        case OAK_LOG:
                        case STRIPPED_OAK_LOG:
                        case BIRCH_LOG:
                        case STRIPPED_BIRCH_LOG:
                            surviePlayer.getJobData().addHourXP(5);
                            break;
                        case SPRUCE_LOG:
                        case STRIPPED_SPRUCE_LOG:
                        case DARK_OAK_LOG:
                        case STRIPPED_DARK_OAK_LOG:
                        case JUNGLE_LOG:
                        case STRIPPED_JUNGLE_LOG:
                            surviePlayer.getJobData().addHourXP(4);
                            break;
                        case ACACIA_LOG:
                        case STRIPPED_ACACIA_LOG:
                            surviePlayer.getJobData().addHourXP(6);
                            break;
                    }
                }
            } else if (surviePlayer.getJobData().getName().equals("Mineur") && player.getGameMode() == GameMode.SURVIVAL) {
                Block block = event.getBlock();
                if (!block.hasMetadata("job")) {
                    switch (block.getType()) {
                        case STONE:
                        case COBBLESTONE:
                            surviePlayer.getJobData().addHourXP(0.12);
                            break;
                        case DIORITE:
                        case ANDESITE:
                        case GRANITE:
                            surviePlayer.getJobData().addHourXP(0.75);
                            break;
                        case OBSIDIAN:
                            surviePlayer.getJobData().addHourXP(1);
                            break;
                        case COAL_ORE:
                            surviePlayer.getJobData().addHourXP(4);
                            break;
                        case REDSTONE_ORE:
                        case IRON_ORE:
                            surviePlayer.getJobData().addHourXP(5);
                            break;
                        case LAPIS_ORE:
                            surviePlayer.getJobData().addHourXP(6);
                            break;
                        case GOLD_ORE:
                            surviePlayer.getJobData().addHourXP(8);
                            break;
                        case DIAMOND_ORE:
                            surviePlayer.getJobData().addHourXP(12);
                            break;
                        case EMERALD_ORE:
                            surviePlayer.getJobData().addHourXP(15);
                            break;
                    }
                }
            } else if (surviePlayer.getJobData().getName().equals("Fermier") && player.getGameMode() == GameMode.SURVIVAL && !event.getBlock().hasMetadata("job")) {
                if (event.getBlock().getState().getData() instanceof Crops) {
                    Crops crops = (Crops) event.getBlock().getState().getData();
                    if (crops.getState().equals(CropState.RIPE)) {
                        switch (event.getBlock().getType()) {
                            case CARROTS:
                            case COCOA:
                            case BEETROOTS:
                            case WHEAT:
                            case POTATOES:
                                surviePlayer.getJobData().addHourXP(4);
                                break;
                        }
                    }
                } else {
                    switch (event.getBlock().getType()) {
                        case KELP:
                        case PUMPKIN:
                        case MELON:
                            surviePlayer.getJobData().addHourXP(2);
                            break;
                        case SUGAR_CANE:
                            surviePlayer.getJobData().addHourXP(1);
                            break;
                        case BAMBOO:
                            surviePlayer.getJobData().addHourXP(0.5);
                    }
                }
            }
        }
    }

}
