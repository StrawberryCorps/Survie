package bzh.strawberry.survie.listeners.entity

import bzh.strawberry.survie.Survie
import org.bukkit.GameMode
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityBreakDoorEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.plugin.Plugin

/*
 * This file (EntityDamage.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:03 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class EntityDamage(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity.world.name.equals("world", ignoreCase = true))
            event.isCancelled = true
    }

    @EventHandler
    fun onEntityDamage(event: HangingBreakByEntityEvent) {
        val entity = event.entity
        if (entity.world.name.equals("world", ignoreCase = true))
            event.isCancelled = true
        if (entity.world.name.equals("survie", ignoreCase = true) && Survie.SURVIE.claimManager.getClaim(entity.location) != null && !Survie.SURVIE.claimManager.getClaim(entity.location)?.isOnClaim(event.remover!!.uniqueId)!!)   //ne pas detruire les peinture et les cadres
            event.isCancelled = true
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        if (entity.world.name.equals("world", ignoreCase = true)) {
            event.isCancelled = true
        } else {
            if ((event.damager is Player || (event.damager is Arrow && (event.damager as Arrow).shooter is Player)) && entity is Player)
                event.isCancelled = true

            if ((event.damager is Player || (event.damager is Trident && (event.damager as Trident).shooter is Player)) && entity is Player)    //stop degats trident entre joueurs
                event.isCancelled = true

            val claim = Survie.SURVIE.claimManager.getClaim(entity.location)
            if (entity.world.name.equals("survie", ignoreCase = true) && claim != null && (event.entity is Player && !claim.isOnClaim(event.entity.uniqueId)))
                event.isCancelled = true

            if (entity.world.name.equals("survie", true) && claim != null && (event.damager is Player && !claim.isOnClaim(event.damager.uniqueId) && (event.damager as Player).gameMode != GameMode.CREATIVE && !(event.damager as Player).isOp)) {
                if (entity is Cat || entity is Chicken || entity is Cod || entity is Cow || entity is Dolphin || entity is Donkey || entity is Fox || entity is Horse || entity is Llama || entity is MushroomCow || entity is Mule || entity is Ocelot || entity is Panda || entity is Parrot || entity is Pig || entity is PolarBear || entity is PufferFish || entity is Rabbit || entity is Salmon || entity is Sheep || entity is Squid || entity is TraderLlama || entity is TropicalFish || entity is Turtle || entity is Villager || entity is WanderingTrader || entity is Wolf)
                    event.isCancelled = true
            }
            if (entity.world.name.equals("survie", true) && claim != null && (event.damager is Player && !claim.isOnClaim(event.damager.uniqueId) || event.damager is Projectile) && event.entity is ItemFrame)
                event.isCancelled = true
        }
    }

    @EventHandler
    fun onDoorBreakByEntity(event: EntityBreakDoorEvent) {
        if (event.entity is Zombie)
            event.isCancelled = true
    }
}
