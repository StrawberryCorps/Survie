package bzh.strawberry.survie.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.survie.Survie;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfirmGUI extends AbstractInterface {

    private final JavaPlugin plugin;
    private final String name;
    private final ItemStack WAITING_STACK;
    private final AbstractInterface parent;
    private final ConfirmCallback async;
    private final int waitingSlot;
    private boolean isInProgress;

    public ConfirmGUI(String name, ItemStack WAITING_STACK, AbstractInterface parent, ConfirmCallback async, Player player) {
        super(name, 18, player);
        this.plugin = Survie.SURVIE;
        this.name = name;
        this.parent = parent;
        this.async = async;
        this.waitingSlot = 11;
        this.isInProgress = false;
        this.WAITING_STACK = WAITING_STACK;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void show(Player player) {
        this.addItem(new ItemStackBuilder(Material.GREEN_CONCRETE, 1, "§aConfirmer"), 12, "confirm");
        this.addItem(new ItemStackBuilder(Material.RED_CONCRETE, 1, "§cAnnuler"), 14, "cancel");

        this.addItem(WAITING_STACK, 4, "none");
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    @Override
    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.equals("confirm") && !this.isInProgress) {
            this.isInProgress = true;
            if (async != null)
                Bukkit.getScheduler().runTask(plugin, () -> async.run(parent));
        } else if (action.equals("cancel") && !this.isInProgress) {
            if (this.parent == null) {
                player.closeInventory();
            } else {
                StrawAPI.getAPI().getInterfaceManager().openInterface(this.parent, player);
            }
        }
    }

    @FunctionalInterface
    public interface ConfirmCallback {
        void run(AbstractInterface parent);
    }
}

