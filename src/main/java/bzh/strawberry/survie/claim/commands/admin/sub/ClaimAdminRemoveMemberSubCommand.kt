package bzh.strawberry.survie.claim.commands.admin.sub

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimAdminRemoveMemberSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:51 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimAdminRemoveMemberSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (args.size < 2) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " removeMembre <joueur> <owner>")
            return false
        }

        val owner = Survie.SURVIE.server.getOfflinePlayer(args[2])
        val player = Survie.SURVIE.server.getPlayer(args[1])

        if (owner == null) {
            sender.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.notexist"))
            return false
        }

        if (player == null) {
            sender.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.notonline"))
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
            sender.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.admin.isowner.delete"))
            return false
        }

        claim.removClaimMember(claim.getMember(player.uniqueId), true)
        sender.sendMessage(Survie.SURVIE.prefix + String.format(StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.admin.remove"), player.name, owner.name))
        return true
    }
}