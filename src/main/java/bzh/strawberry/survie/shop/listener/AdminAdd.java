package bzh.strawberry.survie.shop.listener;

import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/*
 * This file (AdminAdd) is part of a project Survie.
 * It was created on 17/07/2020 19:25 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class AdminAdd implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        SurviePlayer surviePlayer = Survie.SURVIE.getSurviePlayer(player.getUniqueId());
        if (player.getGameMode() == GameMode.CREATIVE && surviePlayer.getShopItem() != null) {
            if (event.getBlock().getType() == Material.OAK_SIGN || event.getBlock().getType() == Material.OAK_WALL_SIGN) {
                if (surviePlayer.getShopItem().getAchat() == null) {
                    surviePlayer.getShopItem().setAchat(event.getBlock().getLocation());
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cDéfinition de la pancarte d'achat ! Clique gauche pour définir la pancarte de vente");
                } else if (surviePlayer.getShopItem().getVente() == null) {
                    surviePlayer.getShopItem().setVente(event.getBlock().getLocation());
                    Survie.SURVIE.getShopManager().addShopItem(surviePlayer.getShopItem());
                    surviePlayer.setShopItem(null);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cAjout d'un item avec succès !");
                }
                event.setCancelled(true);
            }
        }
    }
}