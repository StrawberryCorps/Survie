package bzh.strawberry.survie.listeners.food

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.plugin.Plugin

/*
 * This file (FoodLevelChange.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:04 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class FoodLevelChange(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        val player = event.entity as Player
        if (player.world.name.equals("world", ignoreCase = true) && player.foodLevel > event.foodLevel)
            event.isCancelled = true
    }

}
