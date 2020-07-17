package bzh.strawberry.survie.playershop.data;

import bzh.strawberry.survie.utils.CurrencyFormat;
import bzh.strawberry.survie.utils.Hologram;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/*
 * This file (ShopPlayerEmplacement) is part of a project Survie.
 * It was created on 17/07/2020 19:18 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ShopPlayerEmplacement {

    private final int id;
    private final List<Location> signLocation;
    private final int price;
    private final Location buttonBuy;
    private final Hologram hologram;

    public ShopPlayerEmplacement(int id, int price, Location buttonBuy) {
        this.signLocation = new ArrayList<>();
        this.id = id;
        this.price = price;
        this.buttonBuy = buttonBuy;
        this.hologram = new Hologram("§4■ §cDisponible §4■", "§9» §3Durée: §b5 jours", "§9» §3Prix: §b" + ((this.price == -1) ? "§cBientôt" : new CurrencyFormat().format(this.price) + " Ecu")).generateLines(buttonBuy.clone().add(0, 2.7, 0));
    }

    public List<Location> getSignLocation() {
        return signLocation;
    }

    public ShopPlayerEmplacement addSignLocation(Location location) {
        this.signLocation.add(location);
        return this;
    }

    public boolean isLocationSign(Location location) {
        return this.signLocation.stream().anyMatch(location1 -> location1.getBlockX() == location.getBlockX() && location1.getBlockY() == location.getBlockY() && location1.getBlockZ() == location.getBlockZ());
    }

    public int getId() {
        return id;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public int getPrice() {
        return price;
    }

    public Location getButtonBuy() {
        return buttonBuy;
    }
}
