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
 * This file (PecheurGUI) is part of a project Survie.
 * It was created on 17/07/2020 09:57 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class PecheurGUI extends AbstractInterface {

    public PecheurGUI(Player player) {
        super("[Jobs] " + SymbolUtils.ARROW_DOUBLE + " Pêcheur", 54, player);
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int aPurpleCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.BROWN_STAINED_GLASS_PANE, 1, ""), aPurpleCase, null);

        ArrayList<String> al = new ArrayList<>();
        al.add("");

        //1 point
        al.set(0, "§7Rapporte §e1 point");
        this.addItem(new ItemStackBuilder(Material.LEATHER, 1, "§3Cuir", al), 28, null);
        this.addItem(new ItemStackBuilder(Material.LEATHER_BOOTS, 1, "§3Bottes en cuir", al), 29, null);
        this.addItem(new ItemStackBuilder(Material.STICK, 1, "§3Bâton", al), 30, null);

        //2 points
        al.set(0, "§7Rapporte §e2 points");
        this.addItem(new ItemStackBuilder(Material.TRIPWIRE_HOOK, 1, "§3Crochet", al), 33, null);
        this.addItem(new ItemStackBuilder(Material.POTION, 1, "§3Fiole", al), 34, null);
        this.addItem(new ItemStackBuilder(Material.BOWL, 1, "§3Bol", al), 25, null);
        this.addItem(new ItemStackBuilder(Material.ROTTEN_FLESH, 1, "§3Chair putréfiée", al), 31, null);

        //3 points
        al.set(0, "§7Rapporte §§3 points");
        this.addItem(new ItemStackBuilder(Material.STRING, 1, "§3Ficelle", al), 32, null);

        //20 points
        al.set(0, "§7Rapporte §e20 points");
        this.addItem(new ItemStackBuilder(Material.INK_SAC, 1, "§3Poche d'encre", al), 40, null);

        //25 points
        al.set(0, "§7Rapporte §e25 points");
        this.addItem(new ItemStackBuilder(Material.COD, 1, "§3Morue crue", al), 11, null);

        //30 points
        al.set(0, "§7Rapporte §e30 points");
        this.addItem(new ItemStackBuilder(Material.FISHING_ROD, 1, "§3Canne à pêche", al), 13, null);

        //35 points
        al.set(0, "§7Rapporte §e35 points");
        this.addItem(new ItemStackBuilder(Material.LILY_PAD, 1, "§3Nénuphar", al), 24, null);

        //40 points
        al.set(0, "§7Rapporte §e40 points");
        this.addItem(new ItemStackBuilder(Material.SALMON, 1, "§3Saumon crue", al), 12, null);

        //50 points
        al.set(0, "§7Rapporte §e50 points");
        this.addItem(new ItemStackBuilder(Material.BOW, 1, "§3Arc", al), 19, null);
        this.addItem(new ItemStackBuilder(Material.NAME_TAG, 1, "§3Etiquette", al), 21, null);
        this.addItem(new ItemStackBuilder(Material.SADDLE, 1, "§3Selle", al), 23, null);

        //70 points
        al.set(0, "§7Rapporte §e70 points");
        this.addItem(new ItemStackBuilder(Material.ENCHANTED_BOOK, 1, "§3Livre enchanté", al), 20, null);

        //75 points
        al.set(0, "§7Rapporte §e75 points");
        this.addItem(new ItemStackBuilder(Material.NAUTILUS_SHELL, 1, "§3Coquille de nautile", al), 22, null);

        //100 points
        al.set(0, "§7Rapporte §e100 points");
        this.addItem(new ItemStackBuilder(Material.PUFFERFISH, 1, "§3Poisson-globe", al), 15, null);

        //150 points
        al.set(0, "§7Rapporte §e150 points");
        this.addItem(new ItemStackBuilder(Material.TROPICAL_FISH, 1, "§3Poisson tropical", al), 14, null);


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
