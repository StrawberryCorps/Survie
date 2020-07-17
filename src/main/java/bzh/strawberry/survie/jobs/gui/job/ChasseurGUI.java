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
 * This file (ChasseurGUI) is part of a project Survie.
 * It was created on 17/07/2020 09:40 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ChasseurGUI extends AbstractInterface {

    private int page = 0;

    public ChasseurGUI(Player player) {
        super("[Jobs] " + SymbolUtils.ARROW_DOUBLE + " Chasseur", 54, player);
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int aPurpleCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE, 1, ""), aPurpleCase, null);

        ArrayList<String> al = new ArrayList<>();
        al.add("");
        if (page == 0) {
            this.addItem(new ItemStackBuilder(Material.DIAMOND_SWORD, 1, "§3Agressif", null), 21, "agressif");
            this.addItem(new ItemStackBuilder(Material.APPLE, 1, "§3Passif", null), 23, "passif");
            this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour"), 53, "retour");
        } else if (page == 1) {
            al.add("");
            //1 point
            al.set(0, "§7Rapporte §e1§7 point");
            this.addItem(new ItemStackBuilder(Material.SLIME_SPAWN_EGG, 1, "§3Slime", al), 16, null);
            this.addItem(new ItemStackBuilder(Material.MAGMA_CUBE_SPAWN_EGG, 1, "§3Cube de magma", al), 42, null);

            //3 points
            al.set(0, "§7Rapporte §e3§7 points");
            this.addItem(new ItemStackBuilder(Material.ENDERMAN_SPAWN_EGG, 1, "§3Enderman", al), 25, null);

            //4 points
            al.set(0, "§7Rapporte §e4§7 points");
            this.addItem(new ItemStackBuilder(Material.CAVE_SPIDER_SPAWN_EGG, 1, "§3Araignée venimeuse", al), 12, null);
            this.addItem(new ItemStackBuilder(Material.ZOMBIE_PIGMAN_SPAWN_EGG, 1, "§3Cochon-zombie", al), 15, null);

            //6 points
            al.set(0, "§7Rapporte §e6§7 points");
            this.addItem(new ItemStackBuilder(Material.ZOMBIE_VILLAGER_SPAWN_EGG, 1, "§3Zombie-villageois", al), 19, null);
            this.addItem(new ItemStackBuilder(Material.CREEPER_SPAWN_EGG, 1, "§3Creeper", al), 39, null);
            this.addItem(new ItemStackBuilder(Material.SKELETON_SPAWN_EGG, 1, "§3Squelette", al), 23, null);
            this.addItem(new ItemStackBuilder(Material.WITCH_SPAWN_EGG, 1, "§3Sorcière", al), 10, null);
            this.addItem(new ItemStackBuilder(Material.SPIDER_SPAWN_EGG, 1, "§3Araignée", al), 11, null);
            this.addItem(new ItemStackBuilder(Material.ZOMBIE_SPAWN_EGG, 1, "§3Zombie", al), 13, null);


            //7 points
            al.set(0, "§7Rapporte §e7§7 points");
            this.addItem(new ItemStackBuilder(Material.HUSK_SPAWN_EGG, 1, "§3Zombie momifié", al), 14, null);
            this.addItem(new ItemStackBuilder(Material.VEX_SPAWN_EGG, 1, "§3Vex", al), 33, null);
            this.addItem(new ItemStackBuilder(Material.DROWNED_SPAWN_EGG, 1, "§3Noyé", al), 20, null);

            //8 points
            al.set(0, "§7Rapporte §e8§7 points");
            this.addItem(new ItemStackBuilder(Material.PILLAGER_SPAWN_EGG, 1, "§3Pillard", al), 28, null);
            this.addItem(new ItemStackBuilder(Material.BLAZE_SPAWN_EGG, 1, "§3Blaze", al), 38, null);

            //10 points
            this.addItem(new ItemStackBuilder(Material.STRAY_SPAWN_EGG, 1, "§3Vagabond", al), 32, null);
            this.addItem(new ItemStackBuilder(Material.PHANTOM_SPAWN_EGG, 1, "§3Phantom", al), 43, null);
            this.addItem(new ItemStackBuilder(Material.VINDICATOR_SPAWN_EGG, 1, "§3Vindicateur", al), 34, null);
            this.addItem(new ItemStackBuilder(Material.SHULKER_SPAWN_EGG, 1, "§3Shulker", al), 30, null);
            this.addItem(new ItemStackBuilder(Material.GUARDIAN_SPAWN_EGG, 1, "§3Gardien", al), 22, null);

            //12 points
            al.set(0, "§7Rapporte §e12§7 points");
            this.addItem(new ItemStackBuilder(Material.EVOKER_SPAWN_EGG, 1, "§3Evocateur", al), 31, null);


            //15 points
            al.set(0, "§7Rapporte §e15§7 points");
            this.addItem(new ItemStackBuilder(Material.WITHER_SKELETON_SPAWN_EGG, 1, "§3Wither squelette", al), 24, null);

            //20 points
            al.set(0, "§7Rapporte §e20§7 points");
            this.addItem(new ItemStackBuilder(Material.ELDER_GUARDIAN_SPAWN_EGG, 1, "§3Grand gardien", al), 21, null);

            //25 points
            al.set(0, "§7Rapporte §e25§7 points");
            this.addItem(new ItemStackBuilder(Material.GHAST_SPAWN_EGG, 1, "§3Ghast", al), 41, null);

            //100 points
            al.set(0, "§7Rapporte §e100§7 points");
            this.addItem(new ItemStackBuilder(Material.RAVAGER_SPAWN_EGG, 1, "§3Ravageur", al), 29, null);

            //150 points
            al.set(0, "§7Rapporte §e150§7 points");
            this.addItem(new ItemStackBuilder(Material.NETHER_STAR, 1, "§3Wither", al), 40, null);

            //500 points
            al.set(0, "§7Rapporte §e500§7 points");
            this.addItem(new ItemStackBuilder(Material.DRAGON_EGG, 1, "§3Dragon de l'End", al), 37, null);

            this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour"), 53, "retour_1");

        } else if (page == 2) {
            al.add("");

            //6 points
            al.set(0, "§7Rapporte §e6§7 points");
            this.addItem(new ItemStackBuilder(Material.RABBIT_SPAWN_EGG, 1, "§3Lapin", al), 19, null);
            this.addItem(new ItemStackBuilder(Material.MOOSHROOM_SPAWN_EGG, 1, "§3Champimeuh", al), 20, null);
            this.addItem(new ItemStackBuilder(Material.LLAMA_SPAWN_EGG, 1, "§3Lama", al), 31, null);
            this.addItem(new ItemStackBuilder(Material.TURTLE_SPAWN_EGG, 1, "§3Tortue", al), 41, null);
            this.addItem(new ItemStackBuilder(Material.COW_SPAWN_EGG, 1, "§3Vache", al), 12, null);
            this.addItem(new ItemStackBuilder(Material.PIG_SPAWN_EGG, 1, "§3Cochon", al), 13, null);
            this.addItem(new ItemStackBuilder(Material.CHICKEN_SPAWN_EGG, 1, "§3Poulet", al), 14, null);
            this.addItem(new ItemStackBuilder(Material.SHEEP_SPAWN_EGG, 1, "§3Mouton", al), 15, null);
            this.addItem(new ItemStackBuilder(Material.HORSE_SPAWN_EGG, 1, "§3Cheval", al), 24, null);
            this.addItem(new ItemStackBuilder(Material.DONKEY_SPAWN_EGG, 1, "§3Âne", al), 25, null);
            this.addItem(new ItemStackBuilder(Material.SQUID_SPAWN_EGG, 1, "§3Poulpe", al), 28, null);
            this.addItem(new ItemStackBuilder(Material.MULE_SPAWN_EGG, 1, "§3Mule", al), 29, null);

            //8 points
            al.set(0, "§7Rapporte §e8§7 points");
            this.addItem(new ItemStackBuilder(Material.WOLF_SPAWN_EGG, 1, "§3Loup", al), 30, null);

            //10 points
            al.set(0, "§7Rapporte §e10§7 points");
            this.addItem(new ItemStackBuilder(Material.CAT_SPAWN_EGG, 1, "§3Chat", al), 21, null);
            this.addItem(new ItemStackBuilder(Material.POLAR_BEAR_SPAWN_EGG, 1, "§3Ours blanc", al), 33, null);
            this.addItem(new ItemStackBuilder(Material.PANDA_SPAWN_EGG, 1, "§3Panda", al), 32, null);
            this.addItem(new ItemStackBuilder(Material.DOLPHIN_SPAWN_EGG, 1, "§3Dauphin", al), 39, null);
            this.addItem(new ItemStackBuilder(Material.FOX_SPAWN_EGG, 1, "§3Renard", al), 23, null);
            this.addItem(new ItemStackBuilder(Material.PARROT_SPAWN_EGG, 1, "§3Perroquet", al), 34, null);
            this.addItem(new ItemStackBuilder(Material.OCELOT_SPAWN_EGG, 1, "§3Ocelot", al), 22, null);

            //15 points
            al.set(0, "§7Rapporte §e15§7 points");
            this.addItem(new ItemStackBuilder(Material.VILLAGER_SPAWN_EGG, 1, "§3Villageois", al), 11, null);


            this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour"), 53, "retour_1");
        }
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("agressif")) {
            page = 1;
            show(player);
        } else if (action.equals("passif")) {
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
