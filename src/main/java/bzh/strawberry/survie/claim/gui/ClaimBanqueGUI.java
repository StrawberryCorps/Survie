package bzh.strawberry.survie.claim.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.claim.manager.data.Claim;
import bzh.strawberry.survie.manager.SurviePlayer;
import bzh.strawberry.survie.utils.CurrencyFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/*
 * This file (ClaimBanqueGUI) is part of a project Survie.
 * It was created on 17/07/2020 19:50 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ClaimBanqueGUI extends AbstractInterface {

    private final SurviePlayer surviePlayer;
    private final Claim claim;

    public ClaimBanqueGUI(SurviePlayer surviePlayer, Claim claim) {
        super("Claim " + SymbolUtils.ARROW_DOUBLE + " Banque", 45, surviePlayer.getPlayer());
        this.surviePlayer = surviePlayer;
        this.claim = claim;
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 28, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
        for (int aBlueCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, ""), aBlueCase, null);

        this.addItem(new ItemStackBuilder(Material.CHEST, 1, "§3Banque §b" + new CurrencyFormat().format(claim.getBank()), null), 21, "banque");
        this.addItem(new ItemStackBuilder(Material.NAME_TAG, 1, "§3Taxes §b" + (int) (claim.getTaxe() * 100) + "%", null), 23, "taxe");
        this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour", null), 44, "retour");
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    @Override
    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("banque")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimBanqueChangeGUI(surviePlayer, claim), player);
        } else if (action.equals("taxe")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimTaxeChangeGUI(surviePlayer, claim), player);
        } else if (action.equals("retour")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimGestionGUI(surviePlayer, claim), player);
        }
    }
}
