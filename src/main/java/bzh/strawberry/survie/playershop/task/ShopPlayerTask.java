package bzh.strawberry.survie.playershop.task;

import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.playershop.data.ShopPlayerData;
import org.bukkit.entity.Player;

import java.util.Date;

/*
 * This file (ShopPlayerTask) is part of a project Survie.
 * It was created on 17/07/2020 19:20 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ShopPlayerTask implements Runnable {
    @Override
    public void run() {
        for (ShopPlayerData shopPlayerData : Survie.SURVIE.getShopPlayerManager().getShopPlayerData()) {
            shopPlayerData.getEmplacement().getHologram().change("§2■ " + (shopPlayerData.getClaim().hasCustomName() ? shopPlayerData.getClaim().getName() : "§aParcelle de " + Survie.SURVIE.getServer().getOfflinePlayer(shopPlayerData.getClaim().getOwner())) + " §2■", "§9» §3Expire dans: §b" + (((shopPlayerData.getExpire().getTime() - new Date().getTime()) / 3600000 > 24) ? Math.round((shopPlayerData.getExpire().getTime() - new Date().getTime()) / 86400000) + " jours" : ((shopPlayerData.getExpire().getTime() - new Date().getTime()) / 3600000 > 0) ? Math.round((shopPlayerData.getExpire().getTime() - new Date().getTime()) / 3600000) + " heures" : Math.round((shopPlayerData.getExpire().getTime() - new Date().getTime()) / 60000) + " minutes"));
            for (Player player : Survie.SURVIE.getServer().getOnlinePlayers()) {
                shopPlayerData.getEmplacement().getHologram().sendLines(player);
            }
        }
    }
}
