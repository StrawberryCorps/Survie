package bzh.strawberry.survie.listeners.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerBucketFillEvent
import org.bukkit.plugin.Plugin

/*
 * This file (PlayerBucket.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:05 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class PlayerBucket(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler
    fun onPlayerBucketFill(event: PlayerBucketFillEvent) {
        val player = event.player
        if (player.world.name.equals("world", ignoreCase = true))
            event.isCancelled = true
    }

    @EventHandler
    fun onPlayerBucketEmpty(event: PlayerBucketEmptyEvent) {
        val player = event.player
        if (player.world.name.equals("world", ignoreCase = true))
            event.isCancelled = true
    }
}