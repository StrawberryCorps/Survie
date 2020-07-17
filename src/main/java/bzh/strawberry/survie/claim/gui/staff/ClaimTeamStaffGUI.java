package bzh.strawberry.survie.claim.gui.staff;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SkullItemBuilder;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.claim.manager.data.Claim;
import bzh.strawberry.survie.claim.manager.data.ClaimMember;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * This file (ClaimTeamStaffGUI) is part of a project Survie.
 * It was created on 09/07/2020 20:01 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ClaimTeamStaffGUI extends AbstractInterface {

    private final Claim claim;

    public ClaimTeamStaffGUI(Claim claim, Player player) {
        super("Claim » Membres", 54, player);
        this.claim = claim;
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int aBlueCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, ""), aBlueCase, null);

        this.addItem(new SkullItemBuilder(Survie.SURVIE.getServer().getOfflinePlayer(claim.getOwner()).getName(), "[" + claim.getMember(claim.getOwner()).getClaimRank().getS() + "] " + Survie.SURVIE.getServer().getOfflinePlayer(claim.getOwner()).getName(), null), 4, null);

        int[] slots = {20, 21, 22, 23, 24, 29, 30, 31, 32, 33};
        int slot = 0;
        for (ClaimMember claimMember : claim.getClaimMembers().stream().sorted(Comparator.comparing(ClaimMember::getClaimRank)).collect(Collectors.toList())) {
            this.addItem(new SkullItemBuilder(Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName(), "[" + claimMember.getClaimRank().getS() + "] " + Survie.SURVIE.getServer().getOfflinePlayer(claimMember.getUuidMember()).getName(), null), slots[slot], "member:" + claimMember.getUuidMember());
            slot++;
        }
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    @Override
    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (player.hasPermission("survie.admin")) {
            if (action.startsWith("member:"))
                StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimTeamMemberAdminGUI(claim.getMember(UUID.fromString(action.split(":")[1])), claim, player), player);
        }
    }
}
