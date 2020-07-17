package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (DelHomeCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:08 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class DelHomeCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) {
            commandSender.sendMessage(Survie.SURVIE.prefix + "Utilise §c/" + s + " <nom>")
            return false
        }

        val player: Player = commandSender as Player
        Survie.SURVIE.getSurviePlayer(player.uniqueId).removeHome(strings[0])
        return true
    }
}
