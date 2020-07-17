package bzh.strawberry.survie.commands.staff

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (InfoBlockCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:11 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class InfoBlockCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.staff") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player

        Survie.SURVIE.getSurviePlayer(player.uniqueId).infoBlock = true
        player.sendMessage(Survie.SURVIE.prefix + "§7Cassez le bloc qui vous intéresse pour avoir les informations.")
        return true
    }
}