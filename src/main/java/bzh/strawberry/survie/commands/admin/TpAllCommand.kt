package bzh.strawberry.survie.commands.admin

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (TpAllCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 11:13 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class TpAllCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.admin") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player
        Survie.SURVIE.server.onlinePlayers.forEach { o ->
            if (player.uniqueId != o.uniqueId) {
                o.teleport(player)
                o.sendMessage(Survie.SURVIE.prefix + "§7Téléportation en cours...")
                o.playSound(o.location, Sound.ENTITY_ENDERMAN_TELEPORT, 100f, 1f)
            }
        }
        player.sendMessage(Survie.SURVIE.prefix + "§7Téléportation des joueurs en cours...")
        return true
    }
}