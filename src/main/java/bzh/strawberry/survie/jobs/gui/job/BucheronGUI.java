package bzh.strawberry.survie.jobs.gui.job;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.jobs.gui.JobsGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/*
 * This file (BucheronGUI) is part of a project Survie.
 * It was created on 17/07/2020 09:37 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class BucheronGUI extends AbstractInterface {

    public BucheronGUI(Player player) {
        super("[Jobs] " + SymbolUtils.ARROW_DOUBLE + " Bûcheron", 54, player);
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int aPurpleCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.BROWN_STAINED_GLASS_PANE, 1, ""), aPurpleCase, null);

        ArrayList<String> al = new ArrayList<>();
        al.add("§7Rapporte §e4§7 points");
        this.addItem(new ItemStackBuilder(Material.DARK_OAK_LOG, 1, "§3Bûche de chêne noir", al), 31, null);
        this.addItem(new ItemStackBuilder(Material.SPRUCE_LOG, 1, "§3Bûche de sapin", al), 21, null);
        this.addItem(new ItemStackBuilder(Material.JUNGLE_LOG, 1, "§3Bûche d'acajou", al), 23, null);

        al.set(0, "§7Rapporte §e5§7 points");
        this.addItem(new ItemStackBuilder(Material.OAK_LOG, 1, "§3Bûche de chêne", al), 20, null);
        this.addItem(new ItemStackBuilder(Material.BIRCH_LOG, 1, "§3Bûche de bouleau", al), 22, null);

        al.set(0, "§7Rapporte §e6§7 points");
        this.addItem(new ItemStackBuilder(Material.ACACIA_LOG, 1, "§3Bûche d'acacia", al), 24, null);

        this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour"), 53, "retour");
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("retour")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new JobsGUI(player), player);
        }
    }
}
