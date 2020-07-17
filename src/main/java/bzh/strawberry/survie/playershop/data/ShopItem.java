package bzh.strawberry.survie.playershop.data;

import org.bukkit.inventory.ItemStack;

/*
 * This file (ShopItem) is part of a project Survie.
 * It was created on 17/07/2020 19:17 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ShopItem {

    private final ItemStack[] itemStacks;
    private final double price;
    private final int number;

    public ShopItem(ItemStack[] itemStacks, double price, int number) {
        this.itemStacks = itemStacks;
        this.price = price;
        this.number = number;
    }

    public ItemStack[] getItemStacks() {
        return itemStacks;
    }

    public double getPrice() {
        return price;
    }

    public int getNumber() {
        return number;
    }
}
