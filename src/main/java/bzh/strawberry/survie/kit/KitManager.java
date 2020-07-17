package bzh.strawberry.survie.kit;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.kit.data.KitData;
import bzh.strawberry.survie.manager.SurviePlayer;
import bzh.strawberry.survie.utils.InventoryWorkaround;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * This file (KitManager) is part of a project Survie.
 * It was created on 17/07/2020 10:01 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class KitManager {

    private final List<KitData> kits;

    public KitManager() {
        this.kits = new ArrayList<>();
        this.loadKits();
    }

    private void loadKits() {
        List<ItemStack> items = new ArrayList<>();

    }

    public void giveKit(SurviePlayer surviePlayer, String kitName) {
        if (kits.stream().filter(kitData -> kitData.getKitName().equals(kitName)).findFirst().orElse(null) != null) {
            KitData kitData = getKit(kitName);
            try {
                boolean no = false;
                for (ItemStack item : kitData.getItems()) {
                    if (surviePlayer.getPlayer().getInventory().firstEmpty() == -1) {
                        no = true;
                        surviePlayer.getPlayer().getWorld().dropItemNaturally(surviePlayer.getPlayer().getLocation(), item).setVelocity(new Vector(0, 0, 0));
                    } else
                        InventoryWorkaround.addItems(surviePlayer.getPlayer().getInventory(), new ItemStack[]{item});
                }
                if (no)
                    surviePlayer.getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cVotre inventaire est plein, les items sont au sol.");
                surviePlayer.getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§3Kit §b" + kitName + "§3 reçu.");
                surviePlayer.getPlayer().updateInventory();

                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO survie_kits (player_id, kit) VALUES (?, ?)");
                preparedStatement.setInt(1, surviePlayer.getSurvieID());
                preparedStatement.setString(2, kitName);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                surviePlayer.getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de la récupération du kit ! (" + e.getMessage() + ")");
            }
        }
    }

    public KitData getKit(String name) {
        return this.kits.stream().filter(kitData -> kitData.getKitName().equals(name)).findFirst().orElse(null);
    }

    public List<KitData> getKits() {
        return kits;
    }
}