package bzh.strawberry.survie.commands.staff

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (TpToggleCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:05 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class TpToggleCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.moderation") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((commandSender as Player).uniqueId)
        if (surviePlayer.isTpToggle) {
            commandSender.sendMessage(Survie.SURVIE.prefix + "§7Téléportation: §cDésactivé")
            surviePlayer.isTpToggle = false
        } else {
            commandSender.sendMessage(Survie.SURVIE.prefix + "§7Téléportation: §aActivé")
            surviePlayer.isTpToggle = true
        }
        return true
    }
}