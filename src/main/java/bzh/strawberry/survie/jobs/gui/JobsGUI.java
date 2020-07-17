package bzh.strawberry.survie.jobs.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.gui.ConfirmGUI;
import bzh.strawberry.survie.jobs.gui.job.*;
import bzh.strawberry.survie.jobs.manager.JobData;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/*
 * This file (JobsGUI) is part of a project Survie.
 * It was created on 17/07/2020 09:34 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class JobsGUI extends AbstractInterface {
    private final SurviePlayer surviePlayer;

    public JobsGUI(Player player) {
        super("[Survie] " + SymbolUtils.ARROW_DOUBLE + " Jobs", 54, player);
        this.surviePlayer = Survie.SURVIE.getSurviePlayer(player.getUniqueId());
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int aPurpleCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, 1, ""), aPurpleCase, null);

        this.addItem(new ItemStackBuilder(Material.IRON_PICKAXE, 1, "§3Mineur", Arrays.asList("", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic droit: §3Informations", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic gauche: §3Devenir Mineur")), 21, "job_Mineur");
        this.addItem(new ItemStackBuilder(Material.STONE_AXE, 1, "§3Bûcheron", Arrays.asList("", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic droit: §3Informations", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic gauche: §3Devenir Bûcheron")), 23, "job_Bûcheron");
        this.addItem(new ItemStackBuilder(Material.GOLDEN_HOE, 1, "§3Fermier", Arrays.asList("", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic droit: §3Informations", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic gauche: §3Devenir Fermier")), 29, "job_Fermier");
        this.addItem(new ItemStackBuilder(Material.BOW, 1, "§3Chasseur", Arrays.asList("", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic droit: §3Informations", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic gauche: §3Devenir Chasseur")), 31, "job_Chasseur");
        this.addItem(new ItemStackBuilder(Material.FISHING_ROD, 1, "§3Pêcheur", Arrays.asList("", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic droit: §3Informations", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic gauche: §3Devenir Pêcheur")), 33, "job_Pêcheur");
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.startsWith("job_")) {
            action = action.split("_")[1];
            final String act = action;
            if (clickType.isLeftClick()) {
                if (surviePlayer.getJobData() != null && surviePlayer.getJobData().getName().equals(action)) {
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous exercez déjà le métier de " + action + " " + SymbolUtils.DEATH);
                } else {
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("§7Devenir §e" + action + "§7 ?",
                            new ItemStackBuilder(Material.PAPER, 1, "§3Devenir §e" + action + "§3 ?"), null, it -> {
                        SurviePlayer surviePlayer = Survie.SURVIE.getSurviePlayer(player.getUniqueId());
                        surviePlayer.addJob(new JobData(act, 0, 0, 0, player));
                    }, player), player);
                }
            } else if (clickType.isRightClick()) {
                if (action.equals("Mineur"))
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new MineurGUI(player), player);
                else if (action.equals("Bûcheron"))
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new BucheronGUI(player), player);
                else if (action.equals("Fermier"))
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new FermierGUI(player), player);
                else if (action.equals("Chasseur"))
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ChasseurGUI(player), player);
                else if (action.equals("Pêcheur"))
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new PecheurGUI(player), player);
            }
        }
    }
}
