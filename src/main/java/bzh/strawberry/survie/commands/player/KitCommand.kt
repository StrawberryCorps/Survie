package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.gui.KitsGUI
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (KitCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:11 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class KitCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (commandSender is Player)
            StrawAPI.getAPI().interfaceManager.openInterface(KitsGUI(commandSender), commandSender)
        return true
    }
}
