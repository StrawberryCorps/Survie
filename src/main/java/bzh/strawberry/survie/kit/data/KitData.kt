package bzh.strawberry.survie.kit.data

import bzh.strawberry.api.util.ItemStackBuilder
import bzh.strawberry.survie.Survie
import org.bukkit.inventory.ItemStack
import java.util.*

/*
 * This file (KitData.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:01 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class KitData(kitName: String, items: List<ItemStack>, time: Long, itemBuilder: ItemStackBuilder) {
    val kitName: String
    val items: List<ItemStack>
    val time: Long
    private val itemBuilder: ItemStackBuilder

    fun getItemBuilder(): ItemStackBuilder {
        return ItemStackBuilder(itemBuilder.type, 1, Objects.requireNonNull(itemBuilder.itemMeta)!!.displayName, itemBuilder.lore)
    }

    init {
        Survie.SURVIE.server.logger.info("    -> Ajout d'un kit $kitName")
        this.kitName = kitName
        this.items = items
        this.time = time
        this.itemBuilder = itemBuilder
    }
}