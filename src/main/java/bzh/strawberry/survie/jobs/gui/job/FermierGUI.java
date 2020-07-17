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
 * This file (FermierGUI) is part of a project Survie.
 * It was created on 17/07/2020 09:53 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class FermierGUI extends AbstractInterface {

    private int page = 0;

    public FermierGUI(Player player) {
        super("[Jobs] " + SymbolUtils.ARROW_DOUBLE + " Fermier", 54, player);
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int aPurpleCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE, 1, ""), aPurpleCase, null);

        ArrayList<String> al = new ArrayList<>();
        al.add("");
        if (page == 0) {
            this.addItem(new ItemStackBuilder(Material.WHEAT_SEEDS, 1, "§3Planter", null), 21, "planter");
            this.addItem(new ItemStackBuilder(Material.DIAMOND_HOE, 1, "§3Récolter", null), 23, "recolter");
            this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour"), 53, "retour");
        } else if (page == 1) {
            al.set(0, "§7Rapporte §e0.2§7 point");
            this.addItem(new ItemStackBuilder(Material.SWEET_BERRIES, 1, "§3Baies sucrées", al), 30, null);

            al.set(0, "§7Rapporte §e0.3§7 points");
            this.addItem(new ItemStackBuilder(Material.WHEAT_SEEDS, 1, "§3Blé", al), 20, null);
            this.addItem(new ItemStackBuilder(Material.PUMPKIN_SEEDS, 1, "§3Citrouille", al), 21, null);
            this.addItem(new ItemStackBuilder(Material.MELON_SEEDS, 1, "§3Pastèque", al), 22, null);
            this.addItem(new ItemStackBuilder(Material.BEETROOT_SEEDS, 1, "§3Betterave", al), 23, null);
            this.addItem(new ItemStackBuilder(Material.CARROT, 1, "§3Carotte", al), 24, null);
            this.addItem(new ItemStackBuilder(Material.POTATO, 1, "§3Pomme de terre", al), 32, null);
            this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour"), 53, "retour_1");
        } else if (page == 2) {

            al.set(0, "§7Rapporte §e0.5§7 points");
            this.addItem(new ItemStackBuilder(Material.BAMBOO, 1, "§3Bambou", al), 32, null);

            al.set(0, "§7Rapporte §e2 §7points");
            this.addItem(new ItemStackBuilder(Material.KELP, 1, "§3Algue", al), 31, null);
            this.addItem(new ItemStackBuilder(Material.MELON, 1, "§3Pastèque", al), 29, null);
            this.addItem(new ItemStackBuilder(Material.PUMPKIN, 1, "§3Citrouille", al), 30, null);

            al.set(0, "§7Rapporte §e1 §7point");
            this.addItem(new ItemStackBuilder(Material.SUGAR_CANE, 1, "§3Canne à sucre", al), 33, null);

            al.set(0, "§7Rapporte §e4 §7points");
            this.addItem(new ItemStackBuilder(Material.WHEAT, 1, "§3Blé", al), 20, null);
            this.addItem(new ItemStackBuilder(Material.CARROT, 1, "§3Carotte", al), 21, null);
            this.addItem(new ItemStackBuilder(Material.POTATO, 1, "§3Pomme de terre", al), 22, null);
            this.addItem(new ItemStackBuilder(Material.BEETROOT, 1, "§3Betterave", al), 23, null);
            this.addItem(new ItemStackBuilder(Material.COCOA_BEANS, 1, "§3Fève de cacao", al), 24, null);
            this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour"), 53, "retour_1");
        }
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("planter")) {
            page = 1;
            show(player);
        } else if (action.equals("recolter")) {
            page = 2;
            show(player);
        }
        if (action.equals("retour")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new JobsGUI(player), player);
        } else if (action.equals("retour_1")) {
            page = 0;
            show(player);
        }
    }
}
