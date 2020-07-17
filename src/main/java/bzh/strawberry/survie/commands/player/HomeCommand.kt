package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.manager.SurviePlayer
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

/*
 * This file (HomeCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:11 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class HomeCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur"), TabCompleter {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val surviePlayer: SurviePlayer = Survie.SURVIE.getSurviePlayer((commandSender as Player).uniqueId)
        if (strings.isEmpty()) {
            if (surviePlayer.homes.size == 0) {
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous avez aucun home " + SymbolUtils.DEATH + " ! Utilisez /sethome <nom>")
                commandSender.playSound(commandSender.location, Sound.BLOCK_NOTE_BLOCK_BASS, 100f, 1f)
            } else
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§3Mes homes: §9" + surviePlayer.homes.keys.toString())
        } else {
            if (surviePlayer.homes.containsKey(strings[0])) {
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§3Téléportation au home §9" + strings[0])
                Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, Runnable {

                    val home = surviePlayer.homes[strings[0]]

                    if (home != null) {
                        if (!home.chunk.isLoaded)
                            home.chunk.load()

                        Survie.SURVIE.server.scheduler.runTask(Survie.SURVIE, Runnable { surviePlayer.player.teleport(home) })
                    } else
                        surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous avez aucun home à ce nom !")

                })

            } else
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous avez aucun home à ce nom !")
        }
        return true
    }

    override fun onTabComplete(commandSender: CommandSender, cmd: Command, s: String, strings: Array<String>): MutableList<String> {
        val list = ArrayList<String>()
        if (strings.size == 1)
            list.addAll(Survie.SURVIE.getSurviePlayer((commandSender as Player).uniqueId).homes.keys)
        return list
    }
}
