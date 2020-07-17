package bzh.strawberry.survie.commands.admin

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (AdminWarpCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:03 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */

class AdminWarpCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.admin") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.size < 2) {
            commandSender.sendMessage(Survie.SURVIE.prefix + "Utilise §c/" + s + " add <nom>")
            commandSender.sendMessage(Survie.SURVIE.prefix + "Utilise §c/" + s + " remove <nom>")
            return false
        }

        val player: Player = commandSender as Player

        if (strings[0] == "add") {
            Survie.SURVIE.addWarp(strings[1], player.location, player)
        } else if (strings[0] == "remove") {
            Survie.SURVIE.removeWarp(strings[1], player)
        }

        return true
    }
}
