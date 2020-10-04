package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimDenySubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:54 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimDenySubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)

        if (args.size < 2) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " refuse <joueur>")
            return false
        }

        val invite = Survie.SURVIE.server.getPlayer(args[1])
        if (invite == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.notonline"))
            return false
        }

        val claim = Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(invite.uniqueId))
        if (claim == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.notclaim"))
            return false
        }

        if (!claim.invitations.containsKey(surviePlayer.uniqueID)) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.cmd.player.accept.notinvite"))
            return false
        }

        claim.invitations.remove(surviePlayer.uniqueID)

        if (Survie.SURVIE.server.getPlayer(claim.owner) != null)
            Survie.SURVIE.server.getPlayer(claim.owner)!!.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation(claim.owner, "survie.cmd.player.deny.annonce").replace("{player}", surviePlayer.player.name))
        claim.claimMembers.forEach { t: ClaimMember? ->
            if (Survie.SURVIE.server.getPlayer(t!!.uuidMember) != null)
                Survie.SURVIE.server.getPlayer(t.uuidMember)!!.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation(claim.owner, "survie.cmd.player.deny.annonce").replace("{player}", surviePlayer.player.name))
        }
        surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.cmd.player.deny.deny"))
        return true
    }
}
