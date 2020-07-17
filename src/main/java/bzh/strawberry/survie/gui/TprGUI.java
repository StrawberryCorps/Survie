package bzh.strawberry.survie.gui;

import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


/*
 * This file (TprGUI) is part of a project Survie.
 * It was created on 17/07/2020 09:30 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class TprGUI extends AbstractInterface {

    private final SurviePlayer surviePlayer;

    public TprGUI(Player player) {
        super("[Survie] " + SymbolUtils.ARROW_DOUBLE + " Tp Random", 27, player);
        this.surviePlayer = Survie.SURVIE.getSurviePlayer(player.getUniqueId());
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] bord = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        for (int caseBordure : bord)
            this.addItem(new ItemStackBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, ""), caseBordure, null);

        this.addItem(new ItemStackBuilder(Material.STONE_PICKAXE, 1, "§3Aller en monde §b§lRessources"), 12, "Ressources");
        this.addItem(new ItemStackBuilder(Material.LEATHER_BOOTS, 1, "§3Aller en monde §b§lSurvie"), 14, "Survie");

    }

    @Override
    public void onInventoryClose(Player player) {
    }

    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> surviePlayer.randomTeleport(Bukkit.getWorld(action)));
    }

}
