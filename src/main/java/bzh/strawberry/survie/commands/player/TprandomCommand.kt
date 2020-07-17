package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.gui.TprGUI
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (TprandomCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:00 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class TprandomCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val world: World? = (commandSender as Player).world
        val player: Player = commandSender

        if (world?.name != "Survie" && world?.name != "Ressources") {
            StrawAPI.getAPI().interfaceManager.openInterface(TprGUI(commandSender), commandSender)
        } else {
            Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, kotlin.run { { Survie.SURVIE.getSurviePlayer(player.uniqueId).randomTeleport(world) } })
        }
        return true
    }
}
