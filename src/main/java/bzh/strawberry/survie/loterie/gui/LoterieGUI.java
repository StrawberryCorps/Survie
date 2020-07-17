package bzh.strawberry.survie.loterie.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SkullItemBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.loterie.data.Ticket;
import bzh.strawberry.survie.utils.CurrencyFormat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/*
 * This file (LoterieGUI) is part of a project Survie.
 * It was created on 17/07/2020 18:59 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class LoterieGUI extends AbstractInterface {
    private final Ticket ticket;
    private final boolean interact;

    public LoterieGUI(Ticket ticket, boolean interact) {
        super("[Survie] " + SymbolUtils.ARROW_DOUBLE + " Loterie", 45, Bukkit.getPlayer(ticket.getPlayer()));
        this.ticket = ticket;
        this.interact = interact;
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 28, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
        for (int aYellowCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE, 1, ""), aYellowCase, null);

        this.addItem(new SkullItemBuilder(null, "§31", (ticket.getNumber1() == 1 ? "http://textures.minecraft.net/texture/88991697469653c9af8352fdf18d0cc9c67763cfe66175c1556aed33246c7" : "http://textures.minecraft.net/texture/b949df36a1a3f7cb4c67065a9d5350258a3c51b02a1a377b84a82876d77b")), 11, "1_1");
        this.addItem(new SkullItemBuilder(null, "§32", (ticket.getNumber1() == 2 ? "http://textures.minecraft.net/texture/5496c162d7c9e1bc8cf363f1bfa6f4b2ee5dec6226c228f52eb65d96a4635c" : "http://textures.minecraft.net/texture/58cca1e0fb5d9bf77f527356e8bf4e53cb4a4c56f11e779abadae541bbedc6")), 12, "1_2");
        this.addItem(new SkullItemBuilder(null, "§33", (ticket.getNumber1() == 3 ? "http://textures.minecraft.net/texture/c4226f2eb64abc86b38b61d1497764cba03d178afc33b7b8023cf48b49311" : "http://textures.minecraft.net/texture/fe907672a470938525058c0ea319d8be5ba28d972c52f2bd55dbf4182b8")), 13, "1_3");
        this.addItem(new SkullItemBuilder(null, "§34", (ticket.getNumber1() == 4 ? "http://textures.minecraft.net/texture/f920ecce1c8cde5dbca5938c5384f714e55bee4cca866b7283b95d69eed3d2c" : "http://textures.minecraft.net/texture/af2d2601f9c1aceffcc823657e2259d358bb52bd46312bf1c76ee23d3b17")), 14, "1_4");
        this.addItem(new SkullItemBuilder(null, "§35", (ticket.getNumber1() == 5 ? "http://textures.minecraft.net/texture/a2c142af59f29eb35ab29c6a45e33635dcfc2a956dbd4d2e5572b0d38891b354" : "http://textures.minecraft.net/texture/b7973537bd2162a78a586397a1eda8599f4c90dc11a1e7d655288483429251bd")), 15, "1_5");

        this.addItem(new SkullItemBuilder(null, "§31", (ticket.getNumber2() == 1 ? "http://textures.minecraft.net/texture/88991697469653c9af8352fdf18d0cc9c67763cfe66175c1556aed33246c7" : "http://textures.minecraft.net/texture/b949df36a1a3f7cb4c67065a9d5350258a3c51b02a1a377b84a82876d77b")), 20, "2_1");
        this.addItem(new SkullItemBuilder(null, "§32", (ticket.getNumber2() == 2 ? "http://textures.minecraft.net/texture/5496c162d7c9e1bc8cf363f1bfa6f4b2ee5dec6226c228f52eb65d96a4635c" : "http://textures.minecraft.net/texture/58cca1e0fb5d9bf77f527356e8bf4e53cb4a4c56f11e779abadae541bbedc6")), 21, "2_2");
        this.addItem(new SkullItemBuilder(null, "§33", (ticket.getNumber2() == 3 ? "http://textures.minecraft.net/texture/c4226f2eb64abc86b38b61d1497764cba03d178afc33b7b8023cf48b49311" : "http://textures.minecraft.net/texture/fe907672a470938525058c0ea319d8be5ba28d972c52f2bd55dbf4182b8")), 22, "2_3");
        this.addItem(new SkullItemBuilder(null, "§34", (ticket.getNumber2() == 4 ? "http://textures.minecraft.net/texture/f920ecce1c8cde5dbca5938c5384f714e55bee4cca866b7283b95d69eed3d2c" : "http://textures.minecraft.net/texture/af2d2601f9c1aceffcc823657e2259d358bb52bd46312bf1c76ee23d3b17")), 23, "2_4");
        this.addItem(new SkullItemBuilder(null, "§35", (ticket.getNumber2() == 5 ? "http://textures.minecraft.net/texture/a2c142af59f29eb35ab29c6a45e33635dcfc2a956dbd4d2e5572b0d38891b354" : "http://textures.minecraft.net/texture/b7973537bd2162a78a586397a1eda8599f4c90dc11a1e7d655288483429251bd")), 24, "2_5");

        this.addItem(new SkullItemBuilder(null, "§31", (ticket.getNumber3() == 1 ? "http://textures.minecraft.net/texture/88991697469653c9af8352fdf18d0cc9c67763cfe66175c1556aed33246c7" : "http://textures.minecraft.net/texture/b949df36a1a3f7cb4c67065a9d5350258a3c51b02a1a377b84a82876d77b")), 29, "3_1");
        this.addItem(new SkullItemBuilder(null, "§32", (ticket.getNumber3() == 2 ? "http://textures.minecraft.net/texture/5496c162d7c9e1bc8cf363f1bfa6f4b2ee5dec6226c228f52eb65d96a4635c" : "http://textures.minecraft.net/texture/58cca1e0fb5d9bf77f527356e8bf4e53cb4a4c56f11e779abadae541bbedc6")), 30, "3_2");
        this.addItem(new SkullItemBuilder(null, "§33", (ticket.getNumber3() == 3 ? "http://textures.minecraft.net/texture/c4226f2eb64abc86b38b61d1497764cba03d178afc33b7b8023cf48b49311" : "http://textures.minecraft.net/texture/fe907672a470938525058c0ea319d8be5ba28d972c52f2bd55dbf4182b8")), 31, "3_3");
        this.addItem(new SkullItemBuilder(null, "§34", (ticket.getNumber3() == 4 ? "http://textures.minecraft.net/texture/f920ecce1c8cde5dbca5938c5384f714e55bee4cca866b7283b95d69eed3d2c" : "http://textures.minecraft.net/texture/af2d2601f9c1aceffcc823657e2259d358bb52bd46312bf1c76ee23d3b17")), 32, "3_4");
        this.addItem(new SkullItemBuilder(null, "§35", (ticket.getNumber3() == 5 ? "http://textures.minecraft.net/texture/a2c142af59f29eb35ab29c6a45e33635dcfc2a956dbd4d2e5572b0d38891b354" : "http://textures.minecraft.net/texture/b7973537bd2162a78a586397a1eda8599f4c90dc11a1e7d655288483429251bd")), 33, "3_5");

        if (interact) {
            this.addItem(new ItemStackBuilder(Material.RED_CONCRETE, 1, "§cAnnuler", Collections.singletonList("§cAucun Ecu ne sera prélevé !")), 36, "annuler");
            this.addItem(new ItemStackBuilder(Material.GREEN_CONCRETE, 1, "§3Valider", Collections.singletonList("§7Prix du ticket : §3" + new CurrencyFormat().format(Survie.SURVIE.getLotterieManager().getPriceTicket()) + " Ecu")), 44, "valider");
        } else {
            this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour", null), 44, "retour");
        }
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("retour")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new MesTicketsGUI(player), player);
        } else if (interact) {
            if (action.equals("annuler")) {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cAnnulation de la participation " + SymbolUtils.DEATH);
                StrawAPI.getAPI().getInterfaceManager().openInterface(new LoterieMainGUI(player), player);
            } else if (action.equals("valider")) {
                if (ticket.getNumber1() == -1 || ticket.getNumber2() == -1 || ticket.getNumber3() == -1) {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 0);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous devez séléctionner trois numéros !");
                } else {
                    if (Survie.SURVIE.getAccountManager().getBalance(player.getUniqueId()) >= Survie.SURVIE.getLotterieManager().getPriceTicket()) {

                        if (Survie.SURVIE.getLotterieManager().getPlayerTickets(player.getUniqueId(), ticket.getNumber1(), ticket.getNumber2(), ticket.getNumber3()) != null) {
                            player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous avez déjà un ticket avec ces numéros cette semaine " + SymbolUtils.DEATH);
                        } else {
                            try {
                                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `survie_loterie`(`uuid`, `nombre1`, `nombre2`, `nombre3`) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                                preparedStatement.setString(1, player.getUniqueId().toString());
                                preparedStatement.setInt(2, ticket.getNumber1());
                                preparedStatement.setInt(3, ticket.getNumber2());
                                preparedStatement.setInt(4, ticket.getNumber3());
                                preparedStatement.executeUpdate();
                                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                                int pId = -1;
                                if (resultSet.next()) {
                                    pId = resultSet.getInt(1);
                                    ticket.setDate(new Timestamp(new Date().getTime()));
                                }

                                resultSet.close();
                                preparedStatement.close();
                                connection.close();

                                player.sendMessage("§3§m-------- §b§lTicket de loterie §3§m--------");
                                player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Pseudo: §b" + player.getName() + " §8-*- §9" + SymbolUtils.SQUARE + " §3ID: §b#" + pId);
                                player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Date: §b" + new SimpleDateFormat("EEEE d MMMM yyyy à H:m:s", new Locale("fr", "FR")).format(ticket.getDate()));
                                player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Choix 1: §b" + ticket.getNumber1() + " §8-*- §9" + SymbolUtils.SQUARE + " §3Choix 2: §b" + ticket.getNumber2());
                                player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Choix 3: §b" + ticket.getNumber3());
                                player.sendMessage("§3§m-------- §b§lTicket de loterie §3§m--------");

                                Survie.SURVIE.getLotterieManager().addTicket(ticket, false);
                                Survie.SURVIE.getAccountManager().takeMoney(player.getUniqueId(), Survie.SURVIE.getLotterieManager().getPriceTicket());
                            } catch (SQLException e) {
                                player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue ! (" + e.getMessage() + ")");
                            }
                            player.closeInventory();
                        }

                    } else {
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas assez d'Ecu pour acheter un ticket " + SymbolUtils.DEATH);
                    }
                }
            } else if (action.startsWith("1_")) {
                ticket.setNumber1(Integer.parseInt(action.split("_")[1]));
                this.show(player);
            } else if (action.startsWith("2_")) {
                ticket.setNumber2(Integer.parseInt(action.split("_")[1]));
                this.show(player);
            } else if (action.startsWith("3_")) {
                ticket.setNumber3(Integer.parseInt(action.split("_")[1]));
                this.show(player);
            }
        }
    }
}
