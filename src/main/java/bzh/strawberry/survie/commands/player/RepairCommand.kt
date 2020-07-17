package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin


/*
 * This file (RepairCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:07 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class RepairCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {

        val player = commandSender as Player

        if (strings.isEmpty())
            Survie.SURVIE.getSurviePlayer(player.uniqueId).repairCommande()
        else if (strings[0].equals("time", ignoreCase = true)) {

            var tempsRestant: Long = 0
            val surviePlayer = Survie.SURVIE.getSurviePlayer(player.uniqueId)

            if (surviePlayer.repairTime != null) {

                if (player.hasPermission("survie.roi"))
                    tempsRestant = ((surviePlayer.repairTime.time + 3600 * 1000) - System.currentTimeMillis())
                else if (player.hasPermission("survie.prince"))
                    tempsRestant = ((surviePlayer.repairTime.time + 3600 * 1000 * 3) - System.currentTimeMillis())
                else if (player.hasPermission("survie.marquis"))
                    tempsRestant = ((surviePlayer.repairTime.time + 3600 * 1000 * 6) - System.currentTimeMillis())
                else if (player.hasPermission("survie.comte"))
                    tempsRestant = ((surviePlayer.repairTime.time + 3600 * 1000 * 8) - System.currentTimeMillis())
                else
                    tempsRestant = ((surviePlayer.repairTime.time + 3600 * 1000 * 24) - System.currentTimeMillis())

            }

            if (tempsRestant <= 0)
                player.sendMessage(Survie.SURVIE.prefix + "§7Vous pouvez réparer votre équipement.")
            else
                player.sendMessage(Survie.SURVIE.prefix + "§7Vous devez encore attendre " + (tempsRestant / 1000) + " pour réparer votre équipement.")

        } else {
            player.sendMessage(Survie.SURVIE.prefix + "§cUsage : /" + s + "< | time>")
        }

        return true
    }
}
