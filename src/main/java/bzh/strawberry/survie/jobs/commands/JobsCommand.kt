package bzh.strawberry.survie.jobs.commands

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.jobs.gui.JobsGUI
import bzh.strawberry.survie.utils.TimeUtils
import org.bukkit.Statistic
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (JobsCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 09:30 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class JobsCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player
        val jobdata = Survie.SURVIE.getSurviePlayer(player.uniqueId).jobData

        if (strings.isEmpty()) {
            StrawAPI.getAPI().interfaceManager.openInterface(JobsGUI(commandSender), commandSender)
        } else if (strings[0].equals("info", ignoreCase = true)) {

            if (jobdata != null) {
                val level = jobdata.level
                player.sendMessage(Survie.SURVIE.prefix + "§7Encore §e" + jobdata.remainingBeforeLevelUp() + "§7 points pour passer niveau §e" + (level + 1) + "§7 !")
                player.sendMessage(Survie.SURVIE.prefix + "§7Salaire potentiel (brut): §e" + (1521.22 + (1521.22 * (jobdata.hourXP / 1000)) + (1521.22 * (jobdata.level / 100))) + "§7.")
                player.sendMessage(Survie.SURVIE.prefix + "§7Temps restant avant salaire : §e" + TimeUtils.getTime((3600 - (player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20 % 3600).toLong())) + " §7(min:sec)")
            } else {
                player.sendMessage(Survie.SURVIE.prefix + "§cAucun job trouvé. " + SymbolUtils.DEATH)
            }

        } else if (strings[0].equals("help", ignoreCase = true)) {
            player.sendMessage("")
            player.sendMessage("§7§l§n__________________________")
            player.sendMessage("")
            player.sendMessage("§3➢ §e/" + s + " §7→ Ouvre l'interface de choix de métier")
            player.sendMessage("§3➢ §e/" + s + " info §7→ Affiche l'expérience restante avant de passer au niveau supérieur")
            player.sendMessage("§3➢ §e/" + s + " help §7→ Affiche ce message")
            player.sendMessage("")
            player.sendMessage("§7§l§n__________________________")
            player.sendMessage("")
        }

        return true
    }
}
