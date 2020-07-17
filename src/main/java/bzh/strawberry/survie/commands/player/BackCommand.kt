package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (BackCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:03 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class BackCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {

        val player = commandSender as Player

        Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, Runnable { Survie.SURVIE.getSurviePlayer(player.uniqueId).backCommande() })

        return true
    }
}