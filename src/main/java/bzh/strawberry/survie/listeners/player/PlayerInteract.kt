package bzh.strawberry.survie.listeners.player

import bzh.strawberry.survie.Survie
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.WanderingTrader
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.Plugin

/*
 * This file (PlayerInteract.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:07 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class PlayerInteract(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        //@todo autoriser le fait de pouvoir casser les peintures et cadres dans son claim; bloquer les interactions avec les objets dans les cadres dans les claims des autres
        if (event.player.location.world?.name.equals("world", ignoreCase = true) && !event.player.isOp && event.player.gameMode != GameMode.CREATIVE && (event.action != Action.RIGHT_CLICK_AIR || event.action != Action.RIGHT_CLICK_BLOCK) && (event.clickedBlock != null && event.clickedBlock?.type != Material.ENDER_CHEST))
            event.isCancelled = true
        if (event.player.location.world?.name == "Survie" && !event.player.isOp && event.player.gameMode != GameMode.CREATIVE) {
            val claim = Survie.SURVIE.claimManager.getClaim(event.player.location)
            var mange = false

            val type = event.player.itemInHand.type
            val type2 = event.player.inventory.itemInOffHand.type

            if (event.action == Action.RIGHT_CLICK_AIR && (type != null && (type == Material.POTION || type.isEdible)) || (type2 != null && (type == Material.POTION || type.isEdible)) && event.clickedBlock != null && event.clickedBlock!!.type == Material.AIR) {
                mange = true
                return //quoi qu'il arrive on ne cancel pas quand on mange
            }

            if ((claim == null && !mange && event.action != Action.LEFT_CLICK_AIR && event.player.inventory.itemInMainHand.type != Material.BOW && event.player.inventory.itemInOffHand.type != Material.BOW) || (claim != null && !claim.isOnClaim(event.player.uniqueId)))
                event.isCancelled = true
        }
        if ((event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK || event.action != Action.LEFT_CLICK_BLOCK && event.action != Action.LEFT_CLICK_AIR) && event.hand == EquipmentSlot.OFF_HAND && event.player.inventory.itemInOffHand.type == Material.SPAWNER) {
            event.isCancelled = true
        }
        if (event.clickedBlock != null && event.item != null && event.player != null && event.clickedBlock!!.type == Material.SPAWNER && event.item!!.type.name.endsWith("SPAWN_EGG", true) && !event.player.isOp)
            event.isCancelled = true
    }

    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        if (event.player.location.world?.name.equals("world", ignoreCase = true) && !event.player.isOp && event.player.gameMode != GameMode.CREATIVE && event.rightClicked.customName != null && event.rightClicked !is WanderingTrader)
            event.isCancelled = true
        if (event.player.location.world?.name == "Survie" && !event.player.isOp && event.player.gameMode != GameMode.CREATIVE) {
            val claim = Survie.SURVIE.claimManager.getClaim(event.player.location)
            if (claim == null || (!claim.isOnClaim(event.player.uniqueId)))
                event.isCancelled = true
        }

    }

    @EventHandler
    fun onPlayerBedEnter(event: PlayerBedEnterEvent) {
        if (!event.player.isOp && (event.bed.location.world?.name.equals("world_the_end") || event.bed.location.world?.name.equals("world_nether"))) {
            event.isCancelled = true
        }

        if (!event.isCancelled) {
            val restant = (Survie.SURVIE.surviePlayers.size / 3).toInt() + 1
            Survie.SURVIE.addSleepingPlayer()
        }

    }

    @EventHandler
    fun onPlayerBedExit(event: PlayerBedLeaveEvent) {
        Survie.SURVIE.removeSleepingPlayer()
    }

    @EventHandler
    fun onBucketEmpty(event: PlayerBucketEmptyEvent) {
        if (event.block.location.distance(event.block.world.spawnLocation) < 25 && !event.player.isOp && event.block.world.name != "Survie")
            event.isCancelled = true
    }

}
