package bzh.strawberry.survie.listeners.player

import bzh.strawberry.survie.Survie
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.Plugin

/*
 * This file (PlayerPortal.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:10 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class PlayerPortal(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler()
    fun onPlayerPortal(event: PlayerPortalEvent) {
        val player = event.player
        if (event.cause == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            event.isCancelled = true
            if (event.from.world?.equals(Survie.SURVIE.server.getWorld("world_nether"))!!) {
                if (Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(player.uniqueId)) != null) {
                    player.teleport(Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(player.uniqueId))!!.home)
                } else {
                    player.performCommand("spawn")
                }
            } else {
                val spawnLoc: Location = Survie.SURVIE.server.getWorld("world_nether")!!.spawnLocation
                Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, Runnable {
                    if (!spawnLoc.chunk.isLoaded)
                        spawnLoc.chunk.load()
                    Survie.SURVIE.server.scheduler.runTask(Survie.SURVIE, Runnable { player.teleport(spawnLoc) })
                })
            }
        }

        if (event.cause == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            event.isCancelled = true
            if (event.from.world?.equals(Survie.SURVIE.server.getWorld("world_the_end"))!!) {
                if (Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(player.uniqueId)) != null) {
                    player.teleport(Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(player.uniqueId))!!.home)
                } else {
                    player.performCommand("spawn")
                }
            } else {
                val spawnLoc: Location = Survie.SURVIE.server.getWorld("world_the_end")!!.spawnLocation
                Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, Runnable {
                    if (!spawnLoc.chunk.isLoaded)
                        spawnLoc.chunk.load()
                    Survie.SURVIE.server.scheduler.runTask(Survie.SURVIE, Runnable { player.teleport(spawnLoc) })
                })

            }
        }

    }
}