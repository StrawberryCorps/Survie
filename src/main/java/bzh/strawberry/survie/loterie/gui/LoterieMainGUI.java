package bzh.strawberry.survie.loterie.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.loterie.data.Ticket;
import bzh.strawberry.survie.utils.CurrencyFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
 * This file (LoterieMainGUI) is part of a project Survie.
 * It was created on 17/07/2020 18:59 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class LoterieMainGUI extends AbstractInterface {

    public LoterieMainGUI(Player player) {
        super("[Survie] " + SymbolUtils.ARROW_DOUBLE + " Loterie", 54, player);
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int aYellowCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE, 1, ""), aYellowCase, null);

        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        if (date.getTime().before(new Date()))
            date.add(Calendar.DAY_OF_WEEK, 7);

        this.addItem(new ItemStackBuilder(Material.DIAMOND, 1, "§3Cagnotte", Arrays.asList("§3La cagnotte s'élève à §b" + new CurrencyFormat().format(Survie.SURVIE.getLotterieManager().getCagnotte()) + " Ecu", "§3Prochain tirage §b" + new SimpleDateFormat("EEEE d MMMM", new Locale("fr", "FR")).format(date.getTime()) + " à 18 heures")), 22, null);
        this.addItem(new ItemStackBuilder(Material.PAPER, 1, "§3Acheter un ticket", Arrays.asList("", "§8" + SymbolUtils.HAND_RIGHT + " §7Prix: §3" + new CurrencyFormat().format(Survie.SURVIE.getLotterieManager().getPriceTicket()) + " Ecu", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic: §3Acheter un ticket")), 30, "achat");
        this.addItem(new ItemStackBuilder(Material.BOOK, 1, "§3Mes tickets", Arrays.asList("", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic: §3Voir mes tickets")), 32, "tickets");
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("achat")) {
            if (Survie.SURVIE.getAccountManager().getBalance(player.getUniqueId()) < Survie.SURVIE.getLotterieManager().getPriceTicket()) {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas assez d'Ecu pour acheter un ticket " + SymbolUtils.DEATH);
                player.closeInventory();
                return;
            }
            if (Survie.SURVIE.getLotterieManager().playerTicket(player.getUniqueId()) > 4) {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous avez atteint le maximum de ticket de lotterie " + SymbolUtils.DEATH);
                player.closeInventory();
                return;
            }

            StrawAPI.getAPI().getInterfaceManager().openInterface(new LoterieGUI(new Ticket(player.getUniqueId(), null, -1, -1, -1), true), player);
        } else if (action.equals("tickets")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new MesTicketsGUI(player), player);
        }
    }
}
