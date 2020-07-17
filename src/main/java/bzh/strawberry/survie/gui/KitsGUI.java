package bzh.strawberry.survie.gui;

import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/*
 * This file (KitsGUI) is part of a project Survie.
 * It was created on 10/07/2020 10:31 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class KitsGUI extends AbstractInterface {

    private final SurviePlayer surviePlayer;

    private final HashMap<String, Long> kitTime;

    private final int slot;

    public KitsGUI(Player player) {
        super("[Survie] " + SymbolUtils.ARROW_DOUBLE + " Kits", 54, player);
        this.slot = 0;
        this.kitTime = new HashMap<>();
        this.surviePlayer = Survie.SURVIE.getSurviePlayer(player.getUniqueId());
    }

    public void show(Player player) {

    }

    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {

    }

    @Override
    public void onInventoryClose(Player player) {
    }
}
