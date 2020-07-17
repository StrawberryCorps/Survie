package bzh.strawberry.survie.claim.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SkullItemBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.claim.manager.data.Claim;
import bzh.strawberry.survie.claim.manager.data.ClaimMember;
import bzh.strawberry.survie.claim.manager.rank.ClaimRank;
import bzh.strawberry.survie.gui.ConfirmGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/*
 * This file (ClaimTeamMemberGUI) is part of a project Survie.
 * It was created on 10/07/2020 10:22 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ClaimTeamMemberGUI extends AbstractInterface {

    private final ClaimMember claimMember;
    private final Claim claim;

    public ClaimTeamMemberGUI(ClaimMember claimMember, Claim claim, Player player) {
        super("Claim " + SymbolUtils.ARROW_DOUBLE + " Gestion d'un membre", 45, player);
        this.claimMember = claimMember;
        this.claim = claim;
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 28, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
        for (int aBlueCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, ""), aBlueCase, null);

        this.addItem(new SkullItemBuilder(Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName(), claimMember.getClaimRank().getS().substring(0, 2) + "[" + claimMember.getClaimRank().getS() + "] " + Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName(), null), 4, null);

        this.addItem(new ItemStackBuilder(Material.RED_BANNER, 1, "§3Chef du claim", null), 20, "make_leader");
        this.addItem(new ItemStackBuilder(Material.ORANGE_BANNER, 1, "§3Marquis", null), 22, "make_marquis");
        this.addItem(new ItemStackBuilder(Material.YELLOW_BANNER, 1, "§3Comte", null), 23, "make_comte");
        this.addItem(new ItemStackBuilder(Material.GRAY_BANNER, 1, "§3Baron", null), 24, "make_baron");
    }

    @Override
    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("make_leader")) {
            if (claim.getRank(Survie.SURVIE.getSurviePlayer(player.getUniqueId())) == ClaimRank.DUC) {
                StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("Confirmation", new ItemStackBuilder(Material.DIAMOND, 1, "§cATTENTION: Opération irreversible", null),
                        this, abstractGui -> {
                    claim.changeOwner(claimMember);
                    if (Survie.SURVIE.getServer().getPlayer(claim.getOwner()) != null)
                        Survie.SURVIE.getServer().getPlayer(claim.getOwner()).sendMessage(Survie.SURVIE.getPrefix() + claim.getRank(Survie.SURVIE.getSurviePlayer(claimMember.getUuidMember())).getS() + " " + Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName() + " §7est le nouveau chef du claim");
                    claim.getClaimMembers().forEach(claimMember1 -> {
                        if (Survie.SURVIE.getServer().getPlayer(claimMember1.getUuidMember()) != null)
                            Survie.SURVIE.getServer().getPlayer(claimMember1.getUuidMember()).sendMessage(Survie.SURVIE.getPrefix() + claim.getRank(Survie.SURVIE.getSurviePlayer(claimMember.getUuidMember())).getS() + " " + Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName() + " §7est le nouveau chef du claim");
                    });
                    player.closeInventory();
                }, player), player);
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas les droits nécéssaire pour effectuer cette action " + SymbolUtils.DEATH);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
            }
        } else if (action.equals("make_marquis")) {
            if (claim.getRank(Survie.SURVIE.getSurviePlayer(player.getUniqueId())) == ClaimRank.DUC) {
                if (claimMember.getClaimRank() == ClaimRank.MARQUIS) {
                    player.closeInventory();
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cCe joueur est déjà " + ClaimRank.MARQUIS.getS());
                    return;
                }
                StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("Confirmation", new ItemStackBuilder(Material.DIAMOND, 1, "§cATTENTION: Opération irreversible", null),
                        this, abstractGui -> {
                    claim.changeRole(claimMember, ClaimRank.MARQUIS);
                    if (Survie.SURVIE.getServer().getPlayer(claim.getOwner()) != null)
                        Survie.SURVIE.getServer().getPlayer(claim.getOwner()).sendMessage(Survie.SURVIE.getPrefix() + claimMember.getClaimRank().getS() + " " + Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName() + " §7est désormais " + ClaimRank.MARQUIS.getS());
                    claim.getClaimMembers().forEach(claimMember1 -> {
                        if (Survie.SURVIE.getServer().getPlayer(claimMember1.getUuidMember()) != null)
                            Survie.SURVIE.getServer().getPlayer(claimMember1.getUuidMember()).sendMessage(Survie.SURVIE.getPrefix() + claimMember.getClaimRank().getS() + " " + Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName() + " §7est désormais " + ClaimRank.MARQUIS.getS());
                    });
                    player.closeInventory();
                }, player), player);
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas les droits nécéssaire pour effectuer cette action " + SymbolUtils.DEATH);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
            }
        } else if (action.equals("make_comte")) {
            if (claim.getRank(Survie.SURVIE.getSurviePlayer(player.getUniqueId())) == ClaimRank.MARQUIS || claim.getRank(Survie.SURVIE.getSurviePlayer(player.getUniqueId())) == ClaimRank.DUC) {
                if (claimMember.getClaimRank() == ClaimRank.COMTE) {
                    player.closeInventory();
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cCe joueur est déjà " + ClaimRank.COMTE.getS());
                    return;
                }
                StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("Confirmation", new ItemStackBuilder(Material.DIAMOND, 1, "§cATTENTION: Opération irreversible", null),
                        this, abstractGui -> {
                    claim.changeRole(claimMember, ClaimRank.COMTE);
                    if (Survie.SURVIE.getServer().getPlayer(claim.getOwner()) != null)
                        Survie.SURVIE.getServer().getPlayer(claim.getOwner()).sendMessage(Survie.SURVIE.getPrefix() + claimMember.getClaimRank().getS() + " " + Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName() + " §7est désormais " + ClaimRank.COMTE.getS());
                    claim.getClaimMembers().forEach(claimMember1 -> {
                        if (Survie.SURVIE.getServer().getPlayer(claimMember1.getUuidMember()) != null)
                            Survie.SURVIE.getServer().getPlayer(claimMember1.getUuidMember()).sendMessage(Survie.SURVIE.getPrefix() + claimMember.getClaimRank().getS() + " " + Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName() + " §7est désormais " + ClaimRank.COMTE.getS());
                    });
                    player.closeInventory();
                }, player), player);
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas les droits nécéssaire pour effectuer cette action " + SymbolUtils.DEATH);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
            }
        } else if (action.equals("make_baron")) {
            if (claim.getRank(Survie.SURVIE.getSurviePlayer(player.getUniqueId())) == ClaimRank.MARQUIS || claim.getRank(Survie.SURVIE.getSurviePlayer(player.getUniqueId())) == ClaimRank.DUC) {
                if (claimMember.getClaimRank() == ClaimRank.BARON) {
                    player.closeInventory();
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cCe joueur est déjà " + ClaimRank.BARON.getS());
                    return;
                }
                StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("Confirmation", new ItemStackBuilder(Material.DIAMOND, 1, "§cATTENTION: Opération irreversible", null),
                        this, abstractGui -> {
                    claim.changeRole(claimMember, ClaimRank.BARON);
                    if (Survie.SURVIE.getServer().getPlayer(claim.getOwner()) != null)
                        Survie.SURVIE.getServer().getPlayer(claim.getOwner()).sendMessage(Survie.SURVIE.getPrefix() + claimMember.getClaimRank().getS() + " " + Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName() + " §7est désormais " + ClaimRank.BARON.getS());
                    claim.getClaimMembers().forEach(claimMember1 -> {
                        if (Survie.SURVIE.getServer().getPlayer(claimMember1.getUuidMember()) != null)
                            Survie.SURVIE.getServer().getPlayer(claimMember1.getUuidMember()).sendMessage(Survie.SURVIE.getPrefix() + claimMember.getClaimRank().getS() + " " + Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName() + " §7est désormais " + ClaimRank.BARON.getS());
                    });
                    player.closeInventory();
                }, player), player);
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas les droits nécéssaire pour effectuer cette action " + SymbolUtils.DEATH);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
            }
        }
    }

    @Override
    public void onInventoryClose(Player player) {
    }
}
