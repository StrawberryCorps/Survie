package bzh.strawberry.survie.shop.manager.data;

import org.bukkit.Location;
import org.bukkit.Material;

/*
 * This file (ShopItem) is part of a project Survie.
 * It was created on 17/07/2020 19:26 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ShopItem {

    private final Material material;
    private final double prixAchat;
    private final double maxAchat;
    private final double minAchat;
    private final double prixVente;
    private final double maxVente;
    private final double minVente;
    private Location vente;
    private Location achat;
    private final String name;

    public ShopItem(Material material, double prixAchat, double maxAchat, double minAchat, double prixVente, double maxVente, double minVente, Location vente, Location achat, String name) {
        this.material = material;
        this.prixAchat = prixAchat;
        this.maxAchat = maxAchat;
        this.minAchat = minAchat;
        this.prixVente = prixVente;
        this.maxVente = maxVente;
        this.minVente = minVente;
        this.vente = vente;
        this.achat = achat;
        this.name = name.replaceAll("_", " ");
    }

    public String getName() {
        return name;
    }

    public String getType(Location location) {
        String type = null;
        if (getAchat().equals(location))
            type = "ACHAT";
        else if (getVente().equals(location))
            type = "VENTE";
        return type;
    }

    public Material getMaterial() {
        return material;
    }

    public Material getMaterial(boolean vente) {
        Material material = null;
        if (vente) {
            if (getMaterial() == Material.IRON_INGOT)
                material = Material.IRON_BLOCK;
            if (getMaterial() == Material.GOLD_INGOT)
                material = Material.GOLD_BLOCK;
            if (getMaterial() == Material.QUARTZ)
                material = Material.QUARTZ_BLOCK;
            if (getMaterial() == Material.LAPIS_LAZULI)
                material = Material.LAPIS_BLOCK;
            if (getMaterial() == Material.DIAMOND)
                material = Material.DIAMOND_BLOCK;
            if (getMaterial() == Material.EMERALD)
                material = Material.EMERALD_BLOCK;
            if (getMaterial() == Material.COAL)
                material = Material.COAL_BLOCK;
            if (getMaterial() == Material.REDSTONE)
                material = Material.REDSTONE;
            else
                material = this.getMaterial();
        } else {
            material = this.getMaterial();
        }
        return material;
    }

    public double getPrixAchat() {
        return prixAchat;
    }

    public double getMaxAchat() {
        return maxAchat;
    }

    public double getMinAchat() {
        return minAchat;
    }

    public double getPrixVente() {
        return prixVente;
    }

    public double getMaxVente() {
        return maxVente;
    }

    public double getMinVente() {
        return minVente;
    }

    public Location getVente() {
        return vente;
    }

    public Location getAchat() {
        return achat;
    }

    public void setAchat(Location achat) {
        this.achat = achat;
    }

    public void setVente(Location vente) {
        this.vente = vente;
    }
}