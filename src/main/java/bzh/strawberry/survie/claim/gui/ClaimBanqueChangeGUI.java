package bzh.strawberry.survie.claim.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.claim.manager.data.Claim;
import bzh.strawberry.survie.gui.ConfirmGUI;
import bzh.strawberry.survie.manager.SurviePlayer;
import bzh.strawberry.survie.utils.CurrencyFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

/*
 * This file (ClaimBanqueChangeGUI) is part of a project Survie.
 * It was created on 17/07/2020 19:50 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ClaimBanqueChangeGUI extends AbstractInterface {

    private final SurviePlayer surviePlayer;
    private final Claim claim;

    public ClaimBanqueChangeGUI(SurviePlayer surviePlayer, Claim claim) {
        super("Claim " + SymbolUtils.ARROW_DOUBLE + " Banque", 54, surviePlayer.getPlayer());
        this.surviePlayer = surviePlayer;
        this.claim = claim;
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52};
        for (int aBlueCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE, 1, ""), aBlueCase, null);

        this.addItem(new ItemStackBuilder(Material.CHEST, 1, "§3Banque §b" + new CurrencyFormat().format(claim.getBank())), 22, null);
        this.addItem(new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, 1, "§a+1000 Ecu", Collections.singletonList("§a» §7Ajouter §e1000 Ecu §7dans la banque de claim")), 32, "+1000");
        this.addItem(new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, 1, "§a+10 000 Ecu", Collections.singletonList("§a» §7Ajouter §e10 000 Ecu §7dans la banque de claim")), 33, "+10000");
        this.addItem(new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, 1, "§a+100 000 Ecu", Collections.singletonList("§a» §7Ajouter §e100 000 Ecu §7dans la banque de claim")), 34, "+100000");

        this.addItem(new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1, "§c-1000 Ecu", Collections.singletonList("§c» §7Retirer §e1000 Ecu §7de la banque de claim")), 30, "-1000");
        this.addItem(new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1, "§c-10 000 Ecu", Collections.singletonList("§c» §7Retirer §e10 000 Ecu §7de la banque de claim")), 29, "-10000");
        this.addItem(new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, 1, "§c-100 000 Ecu", Collections.singletonList("§c» §7Retirer §e100 000 Ecu §7de la banque de claim")), 28, "-100000");

        this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour", null), 53, "retour");
    }

    @Override
    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("retour")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueGUI(surviePlayer, claim), player);
        } else if (action.equalsIgnoreCase("+1000")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("[E] » Déposer de l'argent", itemStack, this, abstractGui -> {
                if (surviePlayer.getBalance() >= 1000) {
                    claim.addBank(1000);
                    Survie.SURVIE.getAccountManager().takeMoney(player.getUniqueId(), 1000);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous venez de déposer §e" + new CurrencyFormat().format(1000) + " Ecu §7dans la banque du claim.");
                } else {
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas assez d'argent.");
                }
                Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, player::closeInventory);
                StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueChangeGUI(surviePlayer, claim), player);
            }, player), player);
        } else if (action.equalsIgnoreCase("+10000")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("[E] » Déposer de l'argent", itemStack, this, abstractGui -> {
                if (surviePlayer.getBalance() >= 10000) {
                    claim.addBank(10000);
                    Survie.SURVIE.getAccountManager().takeMoney(player.getUniqueId(), 10000);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous venez de déposer §e" + new CurrencyFormat().format(1000) + " Ecu §7dans la banque du claim.");
                } else {
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas assez d'argent.");
                }
                Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, player::closeInventory);
                StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueChangeGUI(surviePlayer, claim), player);
            }, player), player);
        } else if (action.equalsIgnoreCase("+100000")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("[E] » Déposer de l'argent", itemStack, this, abstractGui -> {
                if (surviePlayer.getBalance() >= 100000) {
                    claim.addBank(100000);
                    Survie.SURVIE.getAccountManager().takeMoney(player.getUniqueId(), 100000);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous venez de déposer §e" + new CurrencyFormat().format(1000) + " Ecu §7dans la banque du claim.");
                } else {
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas assez d'argent.");
                }
                Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, player::closeInventory);
                StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueChangeGUI(surviePlayer, claim), player);
            }, player), player);
        } else if (action.equalsIgnoreCase("+1000000")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("[E] » Déposer de l'argent", itemStack, this, abstractGui -> {
                if (surviePlayer.getBalance() >= 1000000) {
                    claim.addBank(1000000);
                    Survie.SURVIE.getAccountManager().takeMoney(player.getUniqueId(), 1000000);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous venez de déposer §e" + new CurrencyFormat().format(1000) + " Ecu §7dans la banque du claim.");
                } else {
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas assez d'argent.");
                }
                Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, player::closeInventory);
                StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueChangeGUI(surviePlayer, claim), player);
            }, player), player);
        } else if (action.equalsIgnoreCase("-1000")) {
            if (claim.getMember(player.getUniqueId()).getClaimRank().getP() >= 3) {
                if (claim.getBank() >= 1000d) {
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("[E] » Prendre de l'argent", itemStack, this, abstractGui -> {
                        claim.removeBank(1000);
                        Survie.SURVIE.getAccountManager().giveMoney(player.getUniqueId(), 1000);
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous venez de prendre §e" + new CurrencyFormat().format(1000) + " Ecu §7dans la banque du claim.");
                        Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, player::closeInventory);
                        StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueChangeGUI(surviePlayer, claim), player);
                    }, player), player);
                } else {
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVotre claim n'est pas assez riche. " + SymbolUtils.DEATH);
                }
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas la permission de prendre de l'argent dans la banque.");
                player.closeInventory();
            }
        } else if (action.equalsIgnoreCase("-10000")) {
            if (claim.getMember(player.getUniqueId()).getClaimRank().getP() >= 3) {
                if (claim.getBank() >= 10000d) {
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("[E] » Prendre de l'argent", itemStack, this, abstractGui -> {
                        claim.removeBank(10000);
                        Survie.SURVIE.getAccountManager().giveMoney(player.getUniqueId(), 10000);
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous venez de prendre §e" + new CurrencyFormat().format(10000) + " Ecu §7dans la banque du claim.");
                        Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, player::closeInventory);
                        StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueChangeGUI(surviePlayer, claim), player);
                    }, player), player);
                } else {
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVotre claim n'est pas assez riche. " + SymbolUtils.DEATH);
                }
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas la permission de prendre de l'argent dans la banque.");
                player.closeInventory();
            }
        } else if (action.equalsIgnoreCase("-100000")) {
            if (claim.getMember(player.getUniqueId()).getClaimRank().getP() >= 3) {
                if (claim.getBank() >= 100000d) {
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("[E] » Prendre de l'argent", itemStack, this, abstractGui -> {
                        claim.removeBank(100000);
                        Survie.SURVIE.getAccountManager().giveMoney(player.getUniqueId(), 100000);
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous venez de prendre §e" + new CurrencyFormat().format(100000) + " Ecu §7dans la banque du claim.");
                        Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, player::closeInventory);
                        StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueChangeGUI(surviePlayer, claim), player);
                    }, player), player);
                } else {
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVotre claim ne possède pas assez d'argent. " + SymbolUtils.DEATH);
                }
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas la permission de prendre de l'argent dans la banque.");
                player.closeInventory();
            }
        } else if (action.equalsIgnoreCase("-1000000")) {
            if (claim.getMember(player.getUniqueId()).getClaimRank().getP() >= 3) {
                if (claim.getBank() >= 1000000d) {
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("[E] » Prendre de l'argent", itemStack, this, abstractGui -> {
                        claim.removeBank(1000000);
                        Survie.SURVIE.getAccountManager().giveMoney(player.getUniqueId(), 1000000);
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous venez de prendre §e" + new CurrencyFormat().format(1000000) + " Ecu §7dans la banque du claim.");
                        Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, player::closeInventory);
                        StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueChangeGUI(surviePlayer, claim), player);
                    }, player), player);
                } else {
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVotre claim ne possède pas assez d'argent. " + SymbolUtils.DEATH);
                    player.closeInventory();
                }
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas la permission de prendre de l'argent dans la banque.");
                player.closeInventory();
            }
        }
    }

    @Override
    public void onInventoryClose(Player player) {
    }

}
