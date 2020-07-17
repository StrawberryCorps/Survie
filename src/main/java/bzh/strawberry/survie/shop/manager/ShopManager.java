package bzh.strawberry.survie.shop.manager;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.survie.shop.manager.data.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * This file (ShopManager) is part of a project Survie.
 * It was created on 17/07/2020 19:26 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ShopManager {

    private final List<ShopItem> shopItems;

    public ShopManager() {
        this.shopItems = new ArrayList<>();
        load();
    }

    private void load() {
        try {
            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM survie_shop");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String[] achat = resultSet.getString("achat").split(";");
                String[] vente = resultSet.getString("vente").split(";");
                this.shopItems.add(new ShopItem(Material.valueOf(resultSet.getString("item")),
                        resultSet.getDouble("prixAchat"),
                        resultSet.getDouble("maxAchat"),
                        resultSet.getDouble("minAchat"),
                        resultSet.getDouble("prixVente"),
                        resultSet.getDouble("maxVente"),
                        resultSet.getDouble("minVente"),
                        new Location(Bukkit.getWorld(vente[0]), Double.parseDouble(vente[1]), Double.parseDouble(vente[2]), Double.parseDouble(vente[3])),
                        new Location(Bukkit.getWorld(achat[0]), Double.parseDouble(achat[1]), Double.parseDouble(achat[2]), Double.parseDouble(achat[3])),
                        resultSet.getString("name")));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.update();
    }

    public void reload() {
        shopItems.clear();
        load();
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }

    public ShopItem getShopItem(Material material) {
        return this.shopItems.stream().filter(shopItem -> shopItem.getMaterial() == material).findFirst().orElse(null);
    }

    public ShopItem getShopItem(Location location) {
        return this.shopItems.stream().filter(shopItem -> shopItem.getAchat().equals(location) || shopItem.getVente().equals(location)).findFirst().orElse(null);
    }

    public void addShopItem(ShopItem shopItem) {
        if (this.shopItems.contains(shopItem)) return;
        try {
            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `survie_shop`(`item`, `prixAchat`, `maxAchat`, `minAchat`, `prixVente`, `maxVente`, `minVente`, `achat`, `vente`, `name`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, shopItem.getMaterial().toString());
            preparedStatement.setDouble(2, shopItem.getPrixAchat());
            preparedStatement.setDouble(3, shopItem.getMaxAchat());
            preparedStatement.setDouble(4, shopItem.getMinAchat());
            preparedStatement.setDouble(5, shopItem.getPrixVente());
            preparedStatement.setDouble(6, shopItem.getMaxVente());
            preparedStatement.setDouble(7, shopItem.getMinVente());
            preparedStatement.setString(8, shopItem.getAchat().getWorld().getName() + ";" + shopItem.getAchat().getBlockX() + ";" + shopItem.getAchat().getBlockY() + ";" + shopItem.getAchat().getBlockZ());
            preparedStatement.setString(9, shopItem.getVente().getWorld().getName() + ";" + shopItem.getVente().getBlockX() + ";" + shopItem.getVente().getBlockY() + ";" + shopItem.getVente().getBlockZ());
            preparedStatement.setString(10, shopItem.getName());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
            this.shopItems.add(shopItem);
            this.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeShopItem(ShopItem shopItem) {
        if (!this.shopItems.contains(shopItem)) return;
        try {
            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `survie_shop` WHERE `item` = ?");
            preparedStatement.setString(1, shopItem.getMaterial().toString());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();

            if (shopItem.getVente().getBlock().getType() == Material.OAK_SIGN || shopItem.getVente().getBlock().getType() == Material.OAK_WALL_SIGN) {
                Sign sign = (Sign) shopItem.getVente().getBlock().getState();
                sign.setLine(0, "");
                sign.setLine(1, "");
                sign.setLine(2, "");
                sign.setLine(3, "");
                sign.update();
            }
            if (shopItem.getAchat().getBlock().getType() == Material.OAK_SIGN || shopItem.getAchat().getBlock().getType() == Material.OAK_WALL_SIGN) {
                Sign sign = (Sign) shopItem.getAchat().getBlock().getState();
                sign.setLine(0, "");
                sign.setLine(1, "");
                sign.setLine(2, "");
                sign.setLine(3, "");
                sign.update();
            }

            this.shopItems.remove(shopItem);
            this.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        for (ShopItem shopItem : this.shopItems) {
            if (shopItem.getAchat().getBlock().getType() == Material.OAK_SIGN || shopItem.getAchat().getBlock().getType() == Material.OAK_WALL_SIGN) {
                if (shopItem.getPrixAchat() == 0) {
                    Sign sign = (Sign) shopItem.getAchat().getBlock().getState();
                    sign.setLine(0, "§8§l[§3§lACHAT§8§l]");
                    sign.setLine(1, "§8" + (shopItem.getName().contains(":") ? shopItem.getName().split(":")[0] : shopItem.getName()));
                    sign.setLine(2, "§8" + (shopItem.getName().contains(":") ? shopItem.getName().split(":")[1] : ""));
                    sign.setLine(3, "§cINDISPONIBLE");
                    sign.update();
                } else {
                    Sign sign = (Sign) shopItem.getAchat().getBlock().getState();
                    sign.setLine(0, "§8§l[§3§lACHAT§8§l]");
                    sign.setLine(1, "§8" + (shopItem.getName().contains(":") ? shopItem.getName().split(":")[0] : shopItem.getName()));
                    sign.setLine(2, "§8" + (shopItem.getName().contains(":") ? shopItem.getName().split(":")[1] : ""));
                    sign.setLine(3, "§c" + shopItem.getPrixAchat() + "$/item");
                    sign.update();
                }
            }
            if (shopItem.getVente().getBlock().getType() == Material.OAK_SIGN || shopItem.getVente().getBlock().getType() == Material.OAK_WALL_SIGN) {
                if (shopItem.getPrixVente() == 0) {
                    Sign sign = (Sign) shopItem.getVente().getBlock().getState();
                    sign.setLine(0, "§8§l[§3§lVENTE§8§l]");
                    sign.setLine(1, "§8" + (shopItem.getName().contains(":") ? shopItem.getName().split(":")[0] : shopItem.getName()));
                    sign.setLine(2, "§8" + (shopItem.getName().contains(":") ? shopItem.getName().split(":")[1] : ""));
                    sign.setLine(3, "§cINDISPONIBLE");
                    sign.update();
                } else {
                    Sign sign = (Sign) shopItem.getVente().getBlock().getState();
                    sign.setLine(0, "§8§l[§3§lVENTE§8§l]");
                    sign.setLine(1, "§8" + (shopItem.getName().contains(":") ? shopItem.getName().split(":")[0] : shopItem.getName()));
                    sign.setLine(2, "§8" + (shopItem.getName().contains(":") ? shopItem.getName().split(":")[1] : ""));
                    sign.setLine(3, "§c" + shopItem.getPrixVente() + "$/item");
                    sign.update();
                }
            }
        }
    }
}
