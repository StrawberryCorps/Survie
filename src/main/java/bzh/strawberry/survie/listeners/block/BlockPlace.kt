package bzh.strawberry.survie.listeners.block

import bzh.strawberry.survie.Survie
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin

/*
 * This file (BlockPlace.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:02 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class BlockPlace(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        // On empeche la destruction sur le spawn
        if (event.block.world.name.equals("world", ignoreCase = true) && event.player.isOp && event.player.gameMode != GameMode.CREATIVE)
            event.isCancelled = true

        // On empeche la construction dans le monde Survie
        if (event.block.world.name == "Survie" && !event.player.isOp && event.player.gameMode != GameMode.CREATIVE) {
            val claim = Survie.SURVIE.claimManager.getClaim(event.block.location)
            if (claim == null || (!claim.isOnClaim(event.player.uniqueId)))
                event.isCancelled = true
        }

        event.block.setMetadata("pseudo", FixedMetadataValue(Survie.SURVIE, event.player.name))

        if (event.block.location.distance(event.block.world.spawnLocation) < 20 && !event.player.isOp && event.block.world.name != "Survie")
            event.isCancelled = true

    }
}
