package bzh.strawberry.survie.loterie.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.loterie.data.Ticket;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/*
 * This file (MesTicketsGUI) is part of a project Survie.
 * It was created on 17/07/2020 18:59 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class MesTicketsGUI extends AbstractInterface {

    public MesTicketsGUI(Player player) {
        super("[Survie] " + SymbolUtils.ARROW_DOUBLE + " Mes tickets", 54, player);
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] blueCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 35, 36, 37, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int aYellowCase : blueCase)
            this.addItem(new ItemStackBuilder(Material.YELLOW_STAINED_GLASS_PANE, 1, ""), aYellowCase, null);

        this.addItem(new ItemStackBuilder(Material.MAP, 1, "§3Ticket #1", null), 22, null);
        this.addItem(new ItemStackBuilder(Material.MAP, 1, "§3Ticket #2", null), 29, null);
        this.addItem(new ItemStackBuilder(Material.MAP, 1, "§3Ticket #3", null), 30, null);
        this.addItem(new ItemStackBuilder(Material.MAP, 1, "§3Ticket #4", null), 32, null);
        this.addItem(new ItemStackBuilder(Material.MAP, 1, "§3Ticket #5", null), 33, null);


        if (Survie.SURVIE.getLotterieManager().playerTicket(player.getUniqueId()) >= 1) {
            this.addItem(new ItemStackBuilder(Material.FILLED_MAP, 1, "§3Ticket #1", null), 22, "ticket_1");
        }

        if (Survie.SURVIE.getLotterieManager().playerTicket(player.getUniqueId()) >= 2) {
            this.addItem(new ItemStackBuilder(Material.FILLED_MAP, 1, "§3Ticket #2", null), 29, "ticket_2");
        }

        if (Survie.SURVIE.getLotterieManager().playerTicket(player.getUniqueId()) >= 3) {
            this.addItem(new ItemStackBuilder(Material.FILLED_MAP, 1, "§3Ticket #3", null), 30, "ticket_3");
        }

        if (Survie.SURVIE.getLotterieManager().playerTicket(player.getUniqueId()) >= 4) {
            this.addItem(new ItemStackBuilder(Material.FILLED_MAP, 1, "§3Ticket #4", null), 32, "ticket_4");
        }

        if (Survie.SURVIE.getLotterieManager().playerTicket(player.getUniqueId()) >= 5) {
            this.addItem(new ItemStackBuilder(Material.FILLED_MAP, 1, "§3Ticket #5", null), 33, "ticket_5");
        }

        this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour", null), 53, "retour");
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("retour")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new LoterieMainGUI(player), player);
        } else if (action.startsWith("ticket_")) {
            action = action.substring(7);
            Ticket ticket = Survie.SURVIE.getLotterieManager().getPlayerTicket(player.getUniqueId(), Integer.parseInt(action));
            if (ticket != null) {
                StrawAPI.getAPI().getInterfaceManager().openInterface(new LoterieGUI(new Ticket(player.getUniqueId(), ticket.getDate(), ticket.getNumber1(), ticket.getNumber2(), ticket.getNumber3()), false), player);
            } else {
                player.closeInventory();
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de l'ouverture du ticket " + SymbolUtils.DEATH);
            }
        }
    }
}
