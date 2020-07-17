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
 * This file (MineurGUI) is part of a project Survie.
 * It was created on 17/07/2020 09:54 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class MineurGUI extends AbstractInterface {

    public MineurGUI(Player player) {
        super("[Jobs] " + SymbolUtils.ARROW_DOUBLE + " Mineur", 54, player);
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int aPurpleCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, ""), aPurpleCase, null);

        ArrayList<String> al = new ArrayList<>();
        al.add("§7Rapporte §e0.12§7 point");
        this.addItem(new ItemStackBuilder(Material.STONE, 1, "§3Roche", al), 20, null);
        this.addItem(new ItemStackBuilder(Material.COBBLESTONE, 1, "§3Pierres", al), 21, null);
        al.set(0, "§7Rapporte §e0.75§7 point");
        this.addItem(new ItemStackBuilder(Material.GRANITE, 1, "§3Granite", al), 22, null);
        this.addItem(new ItemStackBuilder(Material.DIORITE, 1, "§3Diorite", al), 23, null);
        this.addItem(new ItemStackBuilder(Material.ANDESITE, 1, "§3Andésite", al), 24, null);
        al.set(0, "§7Rapporte §e1§7 point");
        this.addItem(new ItemStackBuilder(Material.OBSIDIAN, 1, "§3Obsidienne", al), 40, null);
        al.set(0, "§7Rapporte §e4§7 points");
        this.addItem(new ItemStackBuilder(Material.COAL_ORE, 1, "§3Minerai de charbon", al), 29, null);
        al.set(0, "§7Rapporte §e5§7 points");
        this.addItem(new ItemStackBuilder(Material.IRON_ORE, 1, "§3Minerai de fer", al), 32, null);
        this.addItem(new ItemStackBuilder(Material.REDSTONE_ORE, 1, "§3Minerai de redstone", al), 31, null);
        al.set(0, "§7Rapporte §e6§7 points");
        this.addItem(new ItemStackBuilder(Material.LAPIS_ORE, 1, "§3Minerai de lapis-lazuli", al), 30, null);
        al.set(0, "§7Rapporte §e8§7 points");
        this.addItem(new ItemStackBuilder(Material.GOLD_ORE, 1, "§3Minerai d'or", al), 33, null);
        al.set(0, "§7Rapporte §e12§7 points");
        this.addItem(new ItemStackBuilder(Material.DIAMOND_ORE, 1, "§3Minerai de diamant", al), 39, null);
        al.set(0, "§7Rapporte §e15§7 points");
        this.addItem(new ItemStackBuilder(Material.EMERALD_ORE, 1, "§3Minerai d'émeraude", al), 41, null);


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
