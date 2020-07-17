package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.manager.SurviePlayer
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

/*
 * This file (WarpCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:06 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class WarpCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur"), TabCompleter {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val surviePlayer: SurviePlayer = Survie.SURVIE.getSurviePlayer((commandSender as Player).uniqueId)
        if (strings.isEmpty()) {
            if (Survie.SURVIE.warps.size == 0) {
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cIl n'y a aucun warp " + SymbolUtils.DEATH)
                commandSender.playSound(commandSender.location, Sound.BLOCK_NOTE_BLOCK_BASS, 100f, 1f)
            } else
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§3Warps: §9" + Survie.SURVIE.warps.keys.toString())
        } else {

            var loc: Location?
            loc = null
            val ite = Survie.SURVIE.warps.keys.iterator()
            var key: String

            while (ite.hasNext() && loc == null) {
                key = ite.next()
                if (key.equals(strings[0], ignoreCase = true)) {
                    if (Survie.SURVIE.warps[key] != null)
                        loc = Survie.SURVIE.warps[key]
                }
            }

            if (loc != null) {
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§3Téléportation au warp §9" + strings[0])
                surviePlayer.player.teleport(loc)
            } else
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cIl n'y a aucun warp avec ce nom !")
        }
        return true
    }

    override fun onTabComplete(commandSender: CommandSender, cmd: Command, s: String, strings: Array<String>): MutableList<String> {
        val list = ArrayList<String>()
        if (strings.size == 1)
            list.addAll(Survie.SURVIE.warps.keys)
        return list
    }
}
