package bzh.strawberry.survie.playershop;

import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.claim.manager.data.Claim;
import bzh.strawberry.survie.playershop.data.ShopPlayerData;
import bzh.strawberry.survie.playershop.data.ShopPlayerEmplacement;
import bzh.strawberry.survie.playershop.listeners.PlayerInteractBuy;
import bzh.strawberry.survie.playershop.task.ShopPlayerTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
 * This file (ShopPlayerManager) is part of a project Survie.
 * It was created on 17/07/2020 19:20 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ShopPlayerManager {

    private final List<ShopPlayerEmplacement> shopPlayerEmplacements;
    private final List<ShopPlayerData> shopPlayerData;

    public ShopPlayerManager() {
        this.shopPlayerEmplacements = new ArrayList<>();
        this.shopPlayerData = new ArrayList<>();

        Survie.SURVIE.getServer().getPluginManager().registerEvents(new PlayerInteractBuy(), Survie.SURVIE);

        this.load();
    }

    private void load() {
        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(0, 100, new Location(Survie.SURVIE.getServer().getWorld("world"), 89.5, 90, 55.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 89, 91, 57))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 87, 91, 57))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 87, 90, 56)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(1, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 87.5, 90, 44.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 85, 91, 41))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 85, 91, 40))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 87, 91, 40))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 88, 90, 41))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 88, 90, 43)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(2, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 97.5, 90, 38.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 93, 90, 38))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 92, 91, 37))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 92, 91, 35))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 92, 91, 33))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 93, 91, 33))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 95, 90, 33))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 97, 91, 33))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 97, 90, 35)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(3, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 105.5, 90, 36.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 102, 91, 35))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 102, 91, 33))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 102, 90, 31))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 103, 91, 30))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 105, 91, 30))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 107, 91, 30))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 107, 91, 32))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 107, 90, 34)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(4, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 112.5, 90, 38.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 112, 91, 35))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 113, 91, 34))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 115, 91, 34))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 115, 90, 35))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 115, 90, 37)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(5, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 108.5, 90, 44.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 106, 90, 44))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 104, 91, 45))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 104, 91, 47))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 105, 90, 47))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 107, 90, 47)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(6, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 100.5, 90, 45.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 101, 90, 46))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 100, 91, 48))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 98, 91, 48)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(7, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 100.5, 90, 56.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 100, 90, 52))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 102, 91, 52))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 102, 90, 54))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 102, 91, 57))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 104, 91, 57))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 106, 91, 57))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 106, 90, 54))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 104, 90, 52)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(8, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 113.5, 90, 61.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 116, 91, 61))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 116, 91, 63))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 114, 90, 63)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(9, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 106.5, 90, 62.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 104, 90, 62))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 104, 90, 60))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 105, 91, 60)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(10, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 95.5, 90, 64.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 95, 90, 66))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 94, 90, 66))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 92, 91, 65))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 91, 91, 63))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 92, 90, 63)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(11, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 93.5, 89, 74.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 90, 90, 74))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 90, 90, 73))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 91, 89, 72)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(12, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 97.5, 88, 94.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 97, 88, 92))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 95, 89, 92))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 94, 89, 92))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 94, 89, 94))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 95, 88, 95)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(13, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 94.5, 87, 103.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 91, 87, 104))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 89, 88, 105))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 88, 88, 104))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 88, 88, 102))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 88, 88, 100))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 90, 88, 100))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 92, 87, 100)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(14, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 103.5, 87, 115.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 103, 88, 112))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 105, 88, 112))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 105, 87, 114)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(15, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 111.5, 87, 118.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 109, 88, 115))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 110, 88, 115))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 112, 87, 116)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(16, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 117.5, 87, 111.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 114, 88, 110))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 114, 88, 108))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 116, 87, 108)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(17, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 116.5, 87, 101.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 115, 87, 104))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 114, 88, 105))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 112, 88, 105))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 111, 87, 104))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 111, 88, 102))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 111, 88, 100))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 112, 87, 100)));

        this.shopPlayerEmplacements.add(new ShopPlayerEmplacement(18, -1, new Location(Survie.SURVIE.getServer().getWorld("world"), 104.5, 87, 105.5))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 106, 87, 104))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 107, 88, 105))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 107, 88, 107))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 106, 87, 107))
                .addSignLocation(new Location(Survie.SURVIE.getServer().getWorld("world"), 104, 87, 107)));

        Survie.SURVIE.getServer().getScheduler().runTaskTimerAsynchronously(Survie.SURVIE, new ShopPlayerTask(), 0, 20 * 60);
    }

    public List<ShopPlayerData> getShopPlayerData() {
        return shopPlayerData;
    }

    public boolean hasAShopPlayer(Claim claim) {
        return this.shopPlayerData.stream().noneMatch(shopPlayerData1 -> shopPlayerData1.getClaim().getClaimId() == claim.getClaimId());
    }

    public ShopPlayerData getShopPlayer(Claim claim) {
        return this.shopPlayerData.stream().filter(shopPlayerData1 -> shopPlayerData1.getClaim().getClaimId() == claim.getClaimId()).findFirst().orElse(null);
    }

    public List<ShopPlayerEmplacement> getShopPlayerEmplacements() {
        return shopPlayerEmplacements;
    }

    public ShopPlayerEmplacement getEmplacement(Location location) {
        return this.shopPlayerEmplacements.stream().filter(shopPlayerEmplacement -> shopPlayerEmplacement.getButtonBuy().getBlockX() == location.getBlockX()
                && shopPlayerEmplacement.getButtonBuy().getBlockY() == location.getBlockY()
                && shopPlayerEmplacement.getButtonBuy().getBlockZ() == location.getBlockZ()).findFirst().orElse(null);
    }

    public boolean isLibre(ShopPlayerEmplacement shopPlayerEmplacement) {
        return this.shopPlayerData.stream().noneMatch(shopPlayerData1 -> shopPlayerData1.getEmplacement().getId() == shopPlayerEmplacement.getId());
    }

    public void initializeShopPlayer(Player player, ShopPlayerEmplacement emplacement) {
        if (!isLibre(emplacement)) {
            player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de l'attribution de la parcelle " + SymbolUtils.DEATH);
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, 5);
        this.shopPlayerData.add(new ShopPlayerData(Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())), new Timestamp(calendar.getTime().getTime()), emplacement));
    }
}
