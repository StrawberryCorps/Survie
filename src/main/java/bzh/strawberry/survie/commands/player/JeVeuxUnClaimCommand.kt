/*
 * This file (JeVeuxUnClaimCommand.kt) is part of a project Survie.
 * It was created on 06/07/2020 11:10 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */

package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class JeVeuxUnClaimCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player: Player = commandSender as Player
        //async... again
        player.sendMessage(Survie.SURVIE.prefix + "§7Vous allez être téléporté vers une zone claimable.")
        Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, kotlin.run { { Survie.SURVIE.getSurviePlayer(player.uniqueId).tpVersZoneClaimable() } })

        return true
    }
}
