/*
 * This file (PlayerQuit.kt) is part of a project Survie.
 * It was created on 06/07/2020 11:02 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */

package bzh.strawberry.survie.economy.listeners

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.sql.SQLException

class PlayerQuit(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener du module d'économie " + this.javaClass.name)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val surviePlayer = Survie.SURVIE.getSurviePlayer(player.uniqueId)
        if (surviePlayer != null) {
            Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, kotlin.run {
                {
                    try {
                        val connection = StrawAPI.getAPI().dataFactory.dataSource.connection
                        val preparedStatement = connection.prepareStatement("UPDATE survie_money SET balance = ? WHERE elid = ?")
                        preparedStatement.setDouble(1, surviePlayer.balance)
                        preparedStatement.setInt(2, 0)
                        preparedStatement.executeUpdate()
                        preparedStatement.close()
                        connection.close()
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }
}
