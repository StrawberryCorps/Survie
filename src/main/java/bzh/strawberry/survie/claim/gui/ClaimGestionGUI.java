package bzh.strawberry.survie.claim.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SkullItemBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.claim.manager.data.Claim;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/*
 * This file (ClaimGestionGUI) is part of a project Survie.
 * It was created on 10/07/2020 10:08 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ClaimGestionGUI extends AbstractInterface {

    private final SurviePlayer surviePlayer;
    private final Claim claim;

    public ClaimGestionGUI(SurviePlayer surviePlayer, Claim claim) {
        super("Claim " + SymbolUtils.ARROW_DOUBLE + " Gestion", 45, surviePlayer.getPlayer());
        this.surviePlayer = surviePlayer;
        this.claim = claim;
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 28, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
        for (int aBlueCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, ""), aBlueCase, null);

        this.addItem(new ItemStackBuilder(Material.NAME_TAG, 1, "§3Banque", null), 20, "banque");
        this.addItem(new ItemStackBuilder(Material.DIAMOND, 1, "§3Emplacement du marché", null), 21, "soon");
        if (claim != null && claim.getOwner() != null) {
            this.addItem(new SkullItemBuilder(Survie.SURVIE.getServer().getOfflinePlayer(claim.getOwner()).getName(), "§3Gestion des membres", null), 23, "gestion_team");
        } else {
            surviePlayer.getPlayer().closeInventory();
            surviePlayer.getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue. " + SymbolUtils.DEATH);
        }
        this.addItem(new ItemStackBuilder(Material.IRON_PICKAXE, 1, "§3Agrandissement de claim", Arrays.asList("", "§8" + SymbolUtils.HAND_RIGHT + "§3Réservé a: " + claim.getMember(claim.getOwner()).getClaimRank().getS().substring(0, 2) + "[" + claim.getMember(claim.getOwner()).getClaimRank().getS() + "] " + Bukkit.getOfflinePlayer(claim.getOwner()).getName(), "§8" + SymbolUtils.HAND_RIGHT + " §7Clic: §3Gérér")), 24, "claim_agrandissement");
    }

    @Override
    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;

        if (action.equals("soon")) {
            player.sendMessage(Survie.SURVIE.getPrefix() + "§cFonctionnalitée en cours de développement " + SymbolUtils.DEATH);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 1);
        } else if (action.equals("banque")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueGUI(surviePlayer, claim), player);
        } else if (action.equals("gestion_team"))
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimTeamGUI(surviePlayer, claim), player);
        else if (action.equals("claim_agrandissement") && claim.getOwner().toString().equals(player.getUniqueId().toString()))
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimAgrandissementGUI(surviePlayer, claim), player);
        else if (action.equals("claim_agrandissement")) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 1);
            player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous devez être le chef du claim " + SymbolUtils.DEATH);
        }
    }

    @Override
    public void onInventoryClose(Player player) {
    }
}
