package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (TpYesCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:05 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class TpYesCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((commandSender as Player).uniqueId)
        if (surviePlayer.teleportRequest == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous n'avez pas reçu de demande de téléportation " + SymbolUtils.DEATH)
            return false
        }

        val teleportRequest = surviePlayer.teleportRequest

        if ((System.currentTimeMillis() - teleportRequest.requestDate) / 1000 > 120) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§c§cLa demande de téléportation a expirée " + SymbolUtils.DEATH)
            return false
        }

        val target = Bukkit.getPlayer(teleportRequest.sender)
        if (target == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est plus connecté !")
            return false
        }

        if (teleportRequest.isTeleportHere) {
            surviePlayer.player.teleport(target)
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Téléportation en cours...")
        } else {
            target.teleport(surviePlayer.player)
            target.sendMessage(Survie.SURVIE.prefix + "§7Téléportation à §e" + surviePlayer.player.name)
        }

        surviePlayer.teleportRequest = null
        return true
    }
}
