package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.manager.SurviePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (NickCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:08 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class NickCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player
        val surviePlayer: SurviePlayer = Survie.SURVIE.getSurviePlayer((player).uniqueId)
        if (strings.isEmpty()) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + s + " <pseudo>")
            return false
        }

        if (player.hasPermission("survie.nick")) {
            if (strings[0] == "off") {
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Vous venez de supprimer votre surnom !")
                surviePlayer.setNickname(null)
                return true
            }
            if ((!player.hasPermission("survie.chat+")) && (strings[0].indexOf('&') != -1)) {
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous n'avez pas acheté l'extra §6chat+ : §e/store")
                return false
            }
            surviePlayer.setNickname(strings[0])
        } else {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous n'avez pas acheté cet extra : §e/store")
        }
        return true
    }
}
