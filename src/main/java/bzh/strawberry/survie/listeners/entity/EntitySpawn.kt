package bzh.strawberry.survie.listeners.entity

import bzh.strawberry.survie.Survie
import org.bukkit.entity.EntityType
import org.bukkit.entity.WanderingTrader
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.plugin.Plugin

/*
 * This file (EntitySpawn.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:04 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class EntitySpawn(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler
    fun onEntitySpawn(event: EntitySpawnEvent) {
        if (event.location.world!!.name.equals("world", ignoreCase = true) && event.entityType != EntityType.ITEM_FRAME && event.entityType != EntityType.DROPPED_ITEM && event.entityType != EntityType.FIREWORK && event.entityType != EntityType.EXPERIENCE_ORB && event.entityType != EntityType.THROWN_EXP_BOTTLE && (event.entityType != EntityType.WANDERING_TRADER && event.entity.customName != null && event.entity.customName == "" && !(event.entity as WanderingTrader).hasAI()))
            event.isCancelled = true

        if (event.location.world!!.name.equals("survie", true) && event.entityType == EntityType.WITHER)
            event.isCancelled = true
    }

    @EventHandler
    fun OnCreatureSpawnEvent(event: CreatureSpawnEvent) {
        if (event.location.world!!.name.equals("survie", ignoreCase = true) && event.entityType == EntityType.PHANTOM && Survie.SURVIE.claimManager.getClaim(event.location) != null && event.spawnReason != CreatureSpawnEvent.SpawnReason.EGG)
            event.isCancelled = true
    }

}
