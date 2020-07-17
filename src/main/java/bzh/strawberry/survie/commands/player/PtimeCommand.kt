package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin


/*
 * This file (PtimeCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:08 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class PtimeCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {

        val player = commandSender as Player

        if (strings.isEmpty()) {
            player.sendMessage(Survie.SURVIE.prefix + "§7 Votre heure correspond à celle du serveur.")
            player.resetPlayerTime()
        } else if (strings[0] == "day") {
            player.sendMessage(Survie.SURVIE.prefix + "§7 Il fait désormais jour sur votre écran.")
            player.setPlayerTime(6500, false)
        } else if (strings[0] == "night") {
            player.sendMessage(Survie.SURVIE.prefix + "§7 Il fait désormais nuit sur votre écran.")
            player.setPlayerTime(18000, false)
        } else {
            player.sendMessage(Survie.SURVIE.prefix + "§8/ptime ➢ Vous synchronise avec le serveur")
            player.sendMessage(Survie.SURVIE.prefix + "§8/ptime day ➢ Vous met le jour")
            player.sendMessage(Survie.SURVIE.prefix + "§8/ptime night ➢ Vous met la nuit")
        }

        return true
    }
}