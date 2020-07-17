package bzh.strawberry.survie.commands.admin

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (HomeAdminCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:09 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class HomeAdminCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.admin") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {

        //usage de la commande ; pouvoir visualiser rapidement les Homes d'un joueur ciblé
        //et pouvoir s'y téléporter au besoin.
        //  ->/homeadmin <joueur> -> [listeHomes], taille
        //  ->/homeadmin <joueur> <nomHome> -> teleporte le joueur vers le home


        val player = commandSender as Player

        if (strings.isEmpty()) {
            player.sendMessage(Survie.SURVIE.prefix + "§cUsage : /" + s + " <joueur> [nomDuHome]")
            return false
        }

        val target = Survie.SURVIE.server.getOfflinePlayer(strings[0])
        if (target == null) {
            player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est jamais venu sur le survie ! §l" + SymbolUtils.DEATH)
            return false
        }
        val id = Survie.SURVIE.getSurvieIdByUUID(target.uniqueId)
        val listeHomes = HashMap<String, Location>()

        // On charge la list des Homes du joueur
        val connection = StrawAPI.getAPI().dataFactory.dataSource.connection
        val preparedStatement = connection.prepareStatement("SELECT * FROM survie_homes WHERE `player_id` = ?")
        preparedStatement.setInt(1, id)
        val resultSet = preparedStatement.executeQuery()
        while (resultSet.next()) {
            val location: Array<String> = resultSet.getString("location").split(";".toRegex()).toTypedArray()
            listeHomes[resultSet.getString("home_name")] = Location(Survie.SURVIE.server.getWorld(location[0]), location[1].toDouble(), location[2].toDouble(), location[3].toDouble(), location[4].toFloat(), location[5].toFloat())
        }
        resultSet.close()
        preparedStatement.close()
        connection.close()

        if (strings.size == 1) {

            var concat = ""
            val sb = StringBuilder()
            val iterator = listeHomes.iterator()
            sb.append("[")
            while (iterator.hasNext()) {
                val element = iterator.next()
                sb.append(element.key)
                if (iterator.hasNext())
                    sb.append(", ")
            }
            sb.append("]")

            concat = sb.toString()

            player.sendMessage(Survie.SURVIE.prefix + "§3Liste des homes de §e" + target.name + "§8 " + concat + " §3Taille totale : §e" + listeHomes.size)

        } else {
            if (!listeHomes.containsKey(strings[1])) {
                player.sendMessage(Survie.SURVIE.prefix + "§cAucun home trouvé pour §3" + target.name + "§c avec le nom §3" + strings[1] + "§c.")
                return false
            }

            //y'a un home, on y teleporte le staff.
            listeHomes.get(strings[1])?.let { player.teleport(it) }

        }

        return true
    }
}