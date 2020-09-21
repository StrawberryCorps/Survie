package bzh.strawberry.survie.claim.commands.admin.sub

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimAdminChangeOwnerSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:49 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimAdminChangeOwnerSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (args.size < 2) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " changeOwner <joueur> <owner>")
            return false
        }

        val owner = Survie.SURVIE.server.getOfflinePlayer(args[2])
        val player = Survie.SURVIE.server.getOfflinePlayer(args[1])

        if (owner == null || player == null) {
            sender.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.notexist"))
            return false
        }

        val claim = Survie.SURVIE.claimManager.getClaim(owner.uniqueId)

        if (claim == null) {
            sender.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.notclaim"))
            return false
        }

        if (Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(player.uniqueId)) != claim) {
            sender.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.isnothere"))
            return false
        }

        if (claim.owner == player.uniqueId) {
            sender.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.admin.isowner"))
            return false
        }

        claim.changeOwner(claim.getMember(player.uniqueId))
        sender.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.admin.changeowner"))
        return true
    }
}