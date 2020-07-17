package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.manager.request.TeleportRequest
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (TpaCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:06 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class TpaCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player: Player = commandSender as Player
        if (strings.isEmpty()) {
            player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + s + " <joueur>")
            return false
        }

        val requestPlayer: Player? = Survie.SURVIE.server.getPlayer(strings[0])

        if (requestPlayer == null) {
            player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas connecté §l" + SymbolUtils.DEATH)
            return false
        }

        if (requestPlayer.uniqueId == player.uniqueId) {
            player.sendMessage(Survie.SURVIE.prefix + "§cVous ne pouvez pas vous téléporter a vous même !")
            return false
        }

        if (!Survie.SURVIE.getSurviePlayer(requestPlayer.uniqueId).isTpToggle) {
            player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur a la téléportation désactivé " + SymbolUtils.DEATH)
            return false
        }

        Survie.SURVIE.getSurviePlayer(requestPlayer.uniqueId).teleportRequest = TeleportRequest(player.uniqueId, requestPlayer.uniqueId, false)
        player.sendMessage(Survie.SURVIE.prefix + "§7Demande de téléportation envoyé.")
        player.sendMessage("§e» §a/tpcancel §7pour annuler la demande.")
        requestPlayer.sendMessage(Survie.SURVIE.prefix + "§e" + player.name + " §7vous a envoyé une demande de téléportation")
        requestPlayer.sendMessage("§e» §a/tpyes §7pour accepter la demande")
        requestPlayer.sendMessage("§e» §a/tpno §7pour refuser la demande")
        return true
    }
}
