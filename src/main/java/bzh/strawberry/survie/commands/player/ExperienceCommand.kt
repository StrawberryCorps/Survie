package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (ExperienceCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:10 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class ExperienceCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {

        val player = commandSender as Player

        if (strings.size != 2) {
            player.sendMessage(Survie.SURVIE.prefix + "§cUsage : /" + s + " <pseudo> <nombre>")
            return false
        }

        if (strings[1].toInt() <= 0) {
            player.sendMessage(Survie.SURVIE.prefix + "§cVous ne pouvez pas envoyer un montant d'expérience négatif. " + SymbolUtils.DEATH)
            return false
        }

        //todo : revoir cette condition
        if (player.totalExperience < strings[1].toInt()) {
            player.sendMessage(Survie.SURVIE.prefix + "§cVous n'avez pas assez d'expérience. " + SymbolUtils.DEATH)
            return false
        }

        val cible: Player? = Survie.SURVIE.server.getPlayer(strings[0])

        if (cible == null) {
            player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'a pas été trouvé. " + SymbolUtils.DEATH)
            return false
        }

        if (cible.name == player.name) {
            player.sendMessage(Survie.SURVIE.prefix + "§cVous ne pouvez pas envoyer de l'expérience à vous même. " + SymbolUtils.DEATH)
            return false
        }

        player.giveExp(-strings[1].toInt())
        cible.giveExp(strings[1].toInt())

        player.sendMessage(Survie.SURVIE.prefix + "§7Vous avez envoyé " + strings[1] + " points d'expériences à " + cible.name)
        cible.sendMessage(Survie.SURVIE.prefix + "§7Vous avez reçu " + strings[1] + " points d'expériences par " + player.name)

        return true
    }
}
