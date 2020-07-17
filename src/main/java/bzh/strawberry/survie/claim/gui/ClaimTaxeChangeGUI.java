package bzh.strawberry.survie.claim.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.claim.manager.data.Claim;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/*
 * This file (ClaimTaxeChangeGUI) is part of a project Survie.
 * It was created on 10/07/2020 10:14 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ClaimTaxeChangeGUI extends AbstractInterface {

    private final SurviePlayer surviePlayer;
    private final Claim claim;

    public ClaimTaxeChangeGUI(SurviePlayer surviePlayer, Claim claim) {
        super("Claim " + SymbolUtils.ARROW_DOUBLE + " Taxes", 54, surviePlayer.getPlayer());
        this.surviePlayer = surviePlayer;
        this.claim = claim;
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52};
        for (int aBlueCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE, 1, ""), aBlueCase, null);

        this.addItem(new ItemStackBuilder(Material.CHEST, 1, "§3Taxes §b" + (int) (claim.getTaxe() * 100), null), 22, null);
        this.addItem(new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, 1, "§a+1%", null), 32, "1");
        this.addItem(new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, 1, "§a+10%", null), 33, "10");

        this.addItem(new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1, "§c-1%", null), 30, "-1");
        this.addItem(new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1, "§c-10%", null), 29, "-10");

        this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour", null), 53, "retour");
    }

    @Override
    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("retour")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueGUI(surviePlayer, claim), player);
        } else if (action.equalsIgnoreCase("1")) {
            if (claim.getMember(player.getUniqueId()).getClaimRank().getP() >= 4) {
                if (claim.getTaxe() + 0.01 <= 0.4) {
                    claim.addTaxe(1);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§3Augmentation des taxes de 1% ! Elle est désormais de §b" + (int) (claim.getTaxe() * 100));
                } else
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§bVous ne pouvez pas définir des taxes supérieur a 40% !");
                show(player);
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas la permission de changer les taxes.");
                player.closeInventory();
            }
        } else if (action.equalsIgnoreCase("10")) {
            if (claim.getMember(player.getUniqueId()).getClaimRank().getP() >= 4) {
                if (claim.getTaxe() + 0.1 <= 0.4) {
                    claim.addTaxe(10);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§3Augmentation des taxes de 10% ! Elle est désormais de §b" + (int) (claim.getTaxe() * 100));
                } else
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§bVous ne pouvez pas définir des taxes supérieur a 40% !");
                show(player);
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas la permission de changer les taxes.");
                player.closeInventory();
            }
        } else if (action.equalsIgnoreCase("-1")) {
            if (claim.getMember(player.getUniqueId()).getClaimRank().getP() >= 4) {
                if (claim.getTaxe() - 0.01 >= 0.07) {
                    claim.removeTaxe(1);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§3Diminution des taxes de 1% ! Elle est désormais de §b" + (int) (claim.getTaxe() * 100));
                } else
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§bVous ne pouvez pas définir des taxes inférieur à 7% !");
                show(player);
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas la permission de changer les taxes.");
                player.closeInventory();
            }
        } else if (action.equalsIgnoreCase("-10")) {
            if (claim.getMember(player.getUniqueId()).getClaimRank().getP() >= 4) {
                if (claim.getTaxe() - 0.1 >= 0.07) {
                    claim.removeTaxe(10);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§3Diminution des taxes de 10% ! Elle est désormais de §b" + (int) (claim.getTaxe() * 100));
                } else
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§bVous ne pouvez pas définir des taxes inférieur à 7% !");
                show(player);
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas la permission de changer les taxes.");
                player.closeInventory();
            }
        }
    }

    @Override
    public void onInventoryClose(Player player) {
    }

}
