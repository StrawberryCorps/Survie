package bzh.strawberry.survie.listeners.entity

import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.plugin.Plugin

/*
 * This file (EntityExplode.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:03 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class EntityExplode(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler
    fun onBlockExplode(event: EntityExplodeEvent) {
        // On empeche les explosions sur le spawn
        if (event.location.world!!.name.equals("world", ignoreCase = true))
            event.blockList().clear()

        if (event.entityType == EntityType.CREEPER && event.location.world?.name == "Survie") {
            event.blockList().clear()
        }
    }

}
