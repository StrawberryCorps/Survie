/*
 * This file (Account.kt) is part of a project Survie.
 * It was created on 06/07/2020 11:03 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */

package bzh.strawberry.survie.economy

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.economy.commands.MoneyCommand
import bzh.strawberry.survie.economy.listeners.PlayerJoin
import bzh.strawberry.survie.economy.listeners.PlayerQuit
import org.bukkit.command.PluginCommand
import org.bukkit.plugin.Plugin
import java.sql.SQLException
import java.util.*

class Account(plugin: Plugin) {

    init {
        plugin.server.pluginManager.registerEvents(PlayerJoin(plugin), plugin)
        plugin.server.pluginManager.registerEvents(PlayerQuit(plugin), plugin)
        Objects.requireNonNull<PluginCommand>(plugin.server.getPluginCommand("money")).setExecutor(MoneyCommand(plugin))
    }

    fun getBalance(uuid: UUID): Double {
        var balance = -1.0
        if (Survie.SURVIE.getSurviePlayer(uuid) != null)
            balance = Survie.SURVIE.getSurviePlayer(uuid).balance
        else {
            try {
                val connection = StrawAPI.getAPI().dataFactory.dataSource.connection
                val preparedStatement = connection.prepareStatement("SELECT * FROM survie_money WHERE survie_id = ?")
                preparedStatement.setInt(1, Survie.SURVIE.getSurvieIdByUUID(uuid))
                val resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    balance = resultSet.getDouble("balance")
                }
                resultSet.close()
                preparedStatement.close()
                connection.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
        return balance
    }

    fun takeMoney(uuid: UUID, v: Double) {
        Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, kotlin.run {
            {
                val surviePlayer = Survie.SURVIE.getSurviePlayer(uuid)
                if (surviePlayer != null)
                    surviePlayer.balance = surviePlayer.balance - v
                try {
                    val connection = StrawAPI.getAPI().dataFactory.dataSource.connection
                    var preparedStatement = connection.prepareStatement("UPDATE survie_money SET balance = balance - ? WHERE survie_id = ?")
                    preparedStatement.setDouble(1, v)
                    preparedStatement.setInt(2, Survie.SURVIE.getSurvieIdByUUID(uuid))
                    preparedStatement.executeUpdate()
                    preparedStatement.close()

                    preparedStatement = connection.prepareStatement("INSERT INTO survie_transaction (player_id, montant) VALUES (?, ?)")
                    preparedStatement.setInt(1, Survie.SURVIE.getSurvieIdByUUID(uuid))
                    preparedStatement.setDouble(2, -v)
                    preparedStatement.executeUpdate()
                    preparedStatement.close()
                    connection.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun giveMoney(uuid: UUID, v: Double) {
        Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, kotlin.run {
            {
                val surviePlayer = Survie.SURVIE.getSurviePlayer(uuid)
                if (surviePlayer != null)
                    surviePlayer.balance = surviePlayer.balance + v
                try {
                    val connection = StrawAPI.getAPI().dataFactory.dataSource.connection
                    var preparedStatement = connection.prepareStatement("UPDATE survie_money SET balance = balance + ? WHERE survie_id = ?")
                    preparedStatement.setDouble(1, v)
                    preparedStatement.setInt(2, Survie.SURVIE.getSurvieIdByUUID(uuid))
                    preparedStatement.executeUpdate()
                    preparedStatement.close()

                    preparedStatement = connection.prepareStatement("INSERT INTO survie_transaction (player_id, montant) VALUES (?, ?)")
                    preparedStatement.setInt(1, Survie.SURVIE.getSurvieIdByUUID(uuid))
                    preparedStatement.setDouble(2, v)
                    preparedStatement.executeUpdate()
                    preparedStatement.close()
                    connection.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun setMoney(uuid: UUID, v: Double) {
        Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, kotlin.run {
            {
                val surviePlayer = Survie.SURVIE.getSurviePlayer(uuid)
                if (surviePlayer != null)
                    surviePlayer.balance = v
                try {
                    val connection = StrawAPI.getAPI().dataFactory.dataSource.connection
                    var preparedStatement = connection.prepareStatement("UPDATE survie_money SET balance = ? WHERE survie_id = ?")
                    preparedStatement.setDouble(1, v)
                    preparedStatement.setInt(2, Survie.SURVIE.getSurvieIdByUUID(uuid))
                    preparedStatement.executeUpdate()
                    preparedStatement.close()

                    preparedStatement = connection.prepareStatement("INSERT INTO survie_transaction (player_id, montant) VALUES (?, ?)")
                    preparedStatement.setInt(1, Survie.SURVIE.getSurvieIdByUUID(uuid))
                    preparedStatement.setDouble(2, +v)
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
