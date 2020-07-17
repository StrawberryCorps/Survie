package bzh.strawberry.survie.commands.staff

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.manager.SurviePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.text.DecimalFormat

/*
 * This file (NearCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:11 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class NearCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.moderation") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player
        var distance = 99999

        try {
            if (!strings.isEmpty())
                distance = strings[0].toInt()
        } catch (e: NumberFormatException) {
            player.sendMessage(Survie.SURVIE.prefix + "§cMerci d'utiliser un nombre.")
        }


        val liste = Survie.SURVIE.surviePlayers.stream().filter { t: SurviePlayer -> t.player.location.world == player.world && t.player.uniqueId != player.uniqueId }
        val tmp = liste.filter { t: SurviePlayer -> t.player.location.distance(player.location) < distance }
        val decimalFormat = DecimalFormat("#.0")

        val iterator = tmp.iterator()

        if (!iterator.hasNext())
            player.sendMessage(Survie.SURVIE.prefix + "§7Aucun joueur à coté de vous.")
        else {
            val sb = StringBuilder()
            sb.append("§8[")
            while (iterator.hasNext()) {
                val joueur = iterator.next()
                sb.append("§7{§3" + decimalFormat.format(joueur.player.location.distance(player.location)) + "§7,§3 ${joueur.player.name}§7}")
                if (iterator.hasNext()) {
                    sb.append("§8, ")
                }
            }
            sb.append("§8]")
            player.sendMessage(sb.toString())
        }

        return true
    }
}