package bzh.strawberry.survie.shop.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.gui.ConfirmGUI;
import bzh.strawberry.survie.shop.manager.data.ShopItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * This file (ShopItemMenu) is part of a project Survie.
 * It was created on 17/07/2020 19:20 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ShopItemMenu extends AbstractInterface {

    private final ShopItem shopItem;
    private final String type;

    private final int[] glass = new int[]{1, 2, 3, 5, 6, 7, 9, 17, 18, 26, 27, 35, 36, 44, 46, 47, 48, 49, 50, 51, 52};
    private final int[] items = new int[]{20, 21, 22, 23, 24, 29, 30, 31, 32, 33};

    public ShopItemMenu(ShopItem shopItem, Location location, Player player) {
        super("[E] » Shop » " + (shopItem.getType(location).equals("ACHAT") ? "Achat" : "Vente"), 54, player);
        this.shopItem = shopItem;
        this.type = shopItem.getType(location);
    }

    @Override
    public void show(Player player) {
        for (int s : glass)
            this.addItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), s, "");

        if (type.equals("ACHAT"))
            this.addItem(new ItemStackBuilder(shopItem.getMaterial(), 1, "§6§n" + shopItem.getName().replaceAll(":", " "), Arrays.asList("", "§3Prix: §c" + shopItem.getPrixAchat() + "$/item")), 4, "");
        else
            this.addItem(new ItemStackBuilder(shopItem.getMaterial(type.equals("VENTE")), 1, "§6§n" + shopItem.getName().replaceAll(":", " "), Arrays.asList("", "§3Prix: §c" + shopItem.getPrixVente() + "$/item")), 4, "");
        int amount = 1;
        for (int i = 0; i <= 9; i++) {
            if (i == 0) {
                amount = 1;
            } else if (i == 1) {
                amount = 2;
            } else if (i == 2) {
                amount = 4;
            } else if (i == 3) {
                amount = 6;
            } else if (i == 4) {
                amount = 8;
            } else if (i == 5) {
                amount = 16;
            } else if (i == 6) {
                amount = 24;
            } else if (i == 7) {
                amount = 32;
            } else if (i == 8) {
                amount = 48;
            } else {
                amount = 64;
            }
            ItemStack item = new ItemStack(shopItem.getMaterial(type.equals("VENTE")));
            item.setAmount(amount);
            ItemMeta itemM = item.getItemMeta();
            itemM.setDisplayName("§3" + (type.equals("ACHAT") ? "ACHAT" : "VENTE") + ": §6§nx" + item.getAmount() + " " + shopItem.getName().replaceAll(":", " "));
            ArrayList<String> itemL = new ArrayList<String>();
            itemL.add("");
            itemL.add("§3Item: §c" + shopItem.getName().replaceAll("\n", " "));
            itemL.add("§3Prix: §c" + ((type.equals("ACHAT") ? shopItem.getPrixAchat() : shopItem.getPrixVente()) * amount) + " Ecu");
            itemM.setLore(itemL);
            item.setItemMeta(itemM);
            this.addItem(item, items[i], "item");
        }
        this.addItem(new ItemStackBuilder(Material.BOOK, 1, "§cFermer", null), 53, "retour");
        if (type.equals("VENTE")) {
            ItemStackBuilder is = new ItemStackBuilder(Material.ENDER_EYE, 1, "§e» Tout vendre «", null);
            int nbr = 0;
            for (ItemStack s : player.getInventory().getStorageContents()) {
                if (s != null && s.getType() == this.shopItem.getMaterial(type.equals("VENTE")))
                    nbr += s.getAmount();
            }
            List<String> l = new ArrayList<>();
            l.add("§3Nombre d'item à vendre: §c" + nbr);
            l.add("§3Prix: §c" + this.shopItem.getPrixVente() * nbr + " Ecu");
            l.add("§e» §7Cliquer pour vendre tous les items.");
            ItemMeta itemMeta = is.getItemMeta();
            itemMeta.setLore(l);
            is.setItemMeta(itemMeta);
            if (nbr <= 0) {
                this.addItem(is, 49, "no");
            } else {
                this.addItem(is, 49, "all");
            }
        }
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    @Override
    public void onInventoryClick(Player player, ItemStack stack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("retour"))
            player.closeInventory();
        if (type.equals("VENTE")) {
            if (action.equalsIgnoreCase("no")) {
                player.closeInventory();
                // @todo no money
                return;
            }
            if (action.equalsIgnoreCase("all")) {
                player.closeInventory();
                StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("Confimer la vente", stack, this, abstractGui -> {
                    int nbr = 0;
                    for (ItemStack s : player.getInventory().getStorageContents()) {
                        if (s != null && s.getType() == shopItem.getMaterial(type.equals("VENTE"))) {
                            nbr += s.getAmount();
                            player.getInventory().remove(s);
                        }
                    }
                    Survie.SURVIE.getAccountManager().giveMoney(player.getUniqueId(), shopItem.getPrixVente() * nbr);
                    addVenteTransaction(nbr, shopItem.getMaterial(type.equals("VENTE")), Survie.SURVIE.getSurvieIdByUUID(player.getUniqueId()));
                    // todo sendActionBar(player, "§3Vous venez de vendre §e"+nbr+" §c"+shopItem.getName()+" §3pour §e"+shopItem.getPrixVente()*nbr+"§3 Ecu.");
                    player.closeInventory();
                }, player), player);
                return;
            }
            ItemStack item = stack.clone();
            ItemMeta itemM = item.getItemMeta();
            itemM.setDisplayName(null);
            itemM.setLore(null);
            item.setAmount(stack.getAmount());
            item.setItemMeta(itemM);
            if (player.getInventory().containsAtLeast(item, stack.getAmount())) {
                Survie.SURVIE.getAccountManager().giveMoney(player.getUniqueId(), shopItem.getPrixVente() * stack.getAmount());
                player.getInventory().removeItem(item);
                addVenteTransaction(stack.getAmount(), shopItem.getMaterial(type.equals("VENTE")), Survie.SURVIE.getSurvieIdByUUID(player.getUniqueId()));
                // todo sendActionBar(player, "§3Vous venez de vendre §e"+stack.getAmount()+" §c"+shopItem.getName()+" §3pour §e"+shopItem.getPrixVente()*stack.getAmount()+"§3 Ecu.");
                player.updateInventory();
            } else {
                player.closeInventory();
                // todo sendActionBar(player, "§cVous n'avez pas §ex" + stack.getAmount() + " " + shopItem.getName().replaceAll(":", " "));
            }
        } else if (type.equals("ACHAT")) {
            if (Survie.SURVIE.getAccountManager().getBalance(player.getUniqueId()) >= shopItem.getPrixAchat() * stack.getAmount()) {
                Survie.SURVIE.getAccountManager().takeMoney(player.getUniqueId(), shopItem.getPrixAchat() * stack.getAmount());
                ItemStack item = new ItemStack(shopItem.getMaterial());
                item.setAmount(stack.getAmount());
                addAchatTransaction(stack.getAmount(), shopItem.getMaterial(), Survie.SURVIE.getSurvieIdByUUID(player.getUniqueId()));
                player.getInventory().addItem(item);
                // todo sendActionBar(player, "§3Vous venez d'acheter §e"+stack.getAmount()+" §c"+shopItem.getName()+" §3pour §e"+shopItem.getPrixAchat()*stack.getAmount()+"§3 Ecu.");
            } else {
                player.closeInventory();
                // todo no money
            }
        }
    }

    private void addAchatTransaction(int amount, Material material, int idSurvie) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `survie_shop_stats`(`item`, `amount`, `prix`, `type`, `id_player`) VALUES (?, ?, ?, ?, ?)");
                preparedStatement.setString(1, material.name());
                preparedStatement.setInt(2, amount);
                preparedStatement.setDouble(3, amount * shopItem.getPrixAchat());
                preparedStatement.setString(4, "ACHAT");
                preparedStatement.setInt(5, idSurvie);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void addVenteTransaction(int amount, Material material, int idSurvie) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `survie_shop_stats`(`item`, `amount`, `prix`, `type`, `id_player`) VALUES (?, ?, ?, ?, ?)");
                preparedStatement.setString(1, material.name());
                preparedStatement.setInt(2, amount);
                preparedStatement.setDouble(3, amount * shopItem.getPrixVente());
                preparedStatement.setString(4, "VENTE");
                preparedStatement.setInt(5, idSurvie);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}