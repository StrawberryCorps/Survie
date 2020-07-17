package bzh.strawberry.survie.playershop.data;

import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.claim.manager.data.Claim;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/*
 * This file (ShopPlayerData) is part of a project Survie.
 * It was created on 17/07/2020 19:17 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ShopPlayerData {

    private final Claim claim;
    private final Timestamp expire;
    private final HashMap<Location, ShopItem> itemToSell;
    private final ShopPlayerEmplacement emplacement;

    public ShopPlayerData(Claim claim, Timestamp expire, ShopPlayerEmplacement shopPlayerEmplacement) {
        this.claim = claim;
        this.expire = expire;
        this.itemToSell = new HashMap<>();
        this.emplacement = shopPlayerEmplacement;
        this.emplacement.getHologram().change("§2■ " + (claim.hasCustomName() ? claim.getName() : "§aParcelle de " + Survie.SURVIE.getServer().getOfflinePlayer(claim.getOwner())) + " §2■", "§9» §3Expire dans: §b" + (((expire.getTime() - new Date().getTime()) / 3600000 > 24) ? Math.round((expire.getTime() - new Date().getTime()) / 86400000) + " jours" : ((expire.getTime() - new Date().getTime()) / 3600000 > 0) ? Math.round((expire.getTime() - new Date().getTime()) / 3600000) + " heures" : Math.round((expire.getTime() - new Date().getTime()) / 60000) + " minutes"));
        for (Player player : Survie.SURVIE.getServer().getOnlinePlayers()) {
            this.emplacement.getHologram().sendLines(player);
        }

        Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, () -> {
            for (Location location : emplacement.getSignLocation()) {
                if (location.getBlock().getType() == Material.OAK_SIGN || location.getBlock().getType() == Material.OAK_WALL_SIGN) {
                    Sign sign = (Sign) location.getBlock().getState();
                    sign.setLine(0, " ");
                    sign.setLine(1, "§cAucun " + SymbolUtils.DEATH);
                    sign.setLine(2, " ");
                    sign.setLine(3, " ");
                    sign.update();
                }
            }
        });
    }

    public void addVente(ItemStack itemStack, double price, Location location) {
        // On ajoute en BDD l'item
        // On change le panneau

    }

    public void addItemToShop(ItemStack itemStack, Location location) {
        // On check si le shop est du même type
        // On ajoute en BDD
    }

    public Claim getClaim() {
        return claim;
    }

    public Timestamp getExpire() {
        return expire;
    }

    public ShopPlayerEmplacement getEmplacement() {
        return emplacement;
    }
}
