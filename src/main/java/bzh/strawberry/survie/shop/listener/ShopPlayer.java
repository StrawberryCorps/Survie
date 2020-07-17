package bzh.strawberry.survie.shop.listener;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.shop.gui.ShopItemMenu;
import bzh.strawberry.survie.shop.manager.data.ShopItem;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/*
 * This file (ShopPlayer) is part of a project Survie.
 * It was created on 17/07/2020 19:25 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ShopPlayer implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null && block.getState() instanceof Sign) {
            if (Survie.SURVIE.getShopManager().getShopItem(block.getLocation()) != null) {
                ShopItem shopItem = Survie.SURVIE.getShopManager().getShopItem(block.getLocation());
                if (shopItem.getType(block.getLocation()).equals("ACHAT") && shopItem.getPrixAchat() != 0) {
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ShopItemMenu(Survie.SURVIE.getShopManager().getShopItem(block.getLocation()), block.getLocation(), player), player);
                } else if (shopItem.getType(block.getLocation()).equals("VENTE") && shopItem.getPrixVente() != 0) {
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ShopItemMenu(Survie.SURVIE.getShopManager().getShopItem(block.getLocation()), block.getLocation(), player), player);
                }
                event.setCancelled(true);
            }
        }
    }
}