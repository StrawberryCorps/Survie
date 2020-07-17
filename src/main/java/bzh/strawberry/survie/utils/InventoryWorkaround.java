package bzh.strawberry.survie.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/*
 * This file (InventoryWorkaround) is part of a project Survie.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class InventoryWorkaround {
    private static int firstPartial(Inventory inventory, ItemStack item, int maxAmount) {
        if (item == null)
            return -1;
        ItemStack[] stacks = inventory.getContents();
        for (int i = 0; i < stacks.length; i++) {
            ItemStack cItem = stacks[i];
            if ((cItem != null) && (cItem.getAmount() < maxAmount) && (cItem.isSimilar(item)))
                return i;
        }
        return -1;
    }

    public static Map<Integer, ItemStack> addItems(Inventory inventory, ItemStack[] items) {
        return addOversizedItems(inventory, 0, items);
    }

    public static Map<Integer, ItemStack> addOversizedItems(Inventory inventory, int oversizedStacks, ItemStack[] items) {
        Map leftover = new HashMap();

        ItemStack[] combined = new ItemStack[items.length];
        for (ItemStack item : items) {
            if ((item != null) && (item.getAmount() >= 1)) {
                for (int j = 0; j < combined.length; j++) {
                    if (combined[j] == null) {
                        combined[j] = item.clone();
                        break;
                    }
                    if (combined[j].isSimilar(item)) {
                        combined[j].setAmount(combined[j].getAmount() + item.getAmount());
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < combined.length; i++) {
            ItemStack item = combined[i];
            if ((item != null) && (item.getType() != Material.AIR)) {
                while (true) {
                    int maxAmount = oversizedStacks > item.getType().getMaxStackSize() ? oversizedStacks : item.getType().getMaxStackSize();
                    int firstPartial = firstPartial(inventory, item, maxAmount);

                    if (firstPartial == -1) {
                        int firstFree = inventory.firstEmpty();
                        if (firstFree == -1) {
                            leftover.put(Integer.valueOf(i), item);
                            break;
                        }

                        if (item.getAmount() > maxAmount) {
                            ItemStack stack = item.clone();
                            stack.setAmount(maxAmount);
                            inventory.setItem(firstFree, stack);
                            item.setAmount(item.getAmount() - maxAmount);
                        } else {
                            inventory.setItem(firstFree, item);
                            break;
                        }
                    } else {
                        ItemStack partialItem = inventory.getItem(firstPartial);

                        int amount = item.getAmount();
                        int partialAmount = partialItem.getAmount();

                        if (amount + partialAmount <= maxAmount) {
                            partialItem.setAmount(amount + partialAmount);
                            break;
                        }

                        partialItem.setAmount(maxAmount);
                        item.setAmount(amount + partialAmount - maxAmount);
                    }
                }
            }
        }
        return leftover;
    }
}