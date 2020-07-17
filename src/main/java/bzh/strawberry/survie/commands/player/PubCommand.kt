package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin


/*
 * This file (PubCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:08 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class PubCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {

        val player = commandSender as Player

        if (strings.isEmpty()) {
            player.sendMessage(Survie.SURVIE.prefix + "§cUsage : /" + s + " <message>.")
            return false
        }

        var concat = ""
        val sb = StringBuilder()
        for (element in strings) {
            sb.append(element)
            sb.append(" ")
        }
        concat = sb.toString()

        Survie.SURVIE.getSurviePlayer(player.uniqueId).afficherPub(concat)

        return true
    }
}