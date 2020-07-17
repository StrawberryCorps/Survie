/*
 * This file (PlayerJoin.kt) is part of a project Survie.
 * It was created on 06/07/2020 11:01 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */

package bzh.strawberry.survie.economy.listeners

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import java.sql.SQLException

class PlayerJoin(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener du module d'économie " + this.javaClass.name)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val surviePlayer = Survie.SURVIE.getSurviePlayer(player.uniqueId)
        if (surviePlayer != null) {
            Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, Runnable {
                run {
                    try {
                        val connection = StrawAPI.getAPI().dataFactory.dataSource.connection
                        val preparedStatement = connection.prepareStatement("SELECT * FROM survie_money WHERE survie_id = ?")
                        preparedStatement.setInt(1, surviePlayer.survieID)
                        val resultSet = preparedStatement.executeQuery()
                        if (!resultSet.next()) {
                            val insert = connection.prepareStatement("INSERT INTO survie_money (survie_id) VALUES (?)")
                            insert.setInt(1, surviePlayer.survieID)
                            insert.executeUpdate()
                            insert.close()
                            surviePlayer.balance = 0.0
                        } else
                            surviePlayer.balance = resultSet.getLong("balance").toDouble()
                        resultSet.close()
                        preparedStatement.close()
                        connection.close()
                    } catch (e: SQLException) {
                        Survie.SURVIE.server.scheduler.runTask(Survie.SURVIE, Runnable { player.kickPlayer("§cUne erreur est survenue avec votre compte ! (" + e.message + ")") })
                    }
                }
            })
        } else
            player.kickPlayer("§cUne erreur est survenue avec votre compte !")
    }
}
