package bzh.strawberry.survie.commands.admin

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (SudoCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:07 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class SudoCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.admin") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player
        if (strings.size > 1) {
            val target = Bukkit.getPlayer(strings[0])
            if (target == null) {
                player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas connecté §l" + SymbolUtils.DEATH)
                return false
            }

            val command = join(strings, 1)
            try {
                Survie.SURVIE.server.dispatchCommand(target, command)
            } catch (e: Exception) {
                player.sendMessage(Survie.SURVIE.prefix + "§cUne erreur est survenue '" + e.message + "'")
            }
        } else
            player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + s + " <joueur> <commande>")
        return true
    }

    private fun join(strings: Array<String>, startIndex: Int): String {
        val sb = StringBuffer()
        for (i in startIndex until strings.size) {
            if (i != startIndex) sb.append(" ")
            sb.append(strings[i])
        }
        return sb.toString()
    }
}