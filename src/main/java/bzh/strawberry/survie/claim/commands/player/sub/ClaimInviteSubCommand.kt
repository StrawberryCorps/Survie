package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

/*
 * This file (ClaimInviteSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:55 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimInviteSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = Survie.SURVIE.claimManager.getClaim(surviePlayer)
        if (args.size < 2) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " invite <joueur>")
            return false
        }

        if (claim!!.getRank(surviePlayer) != ClaimRank.DUC) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous devez être " + ClaimRank.DUC.s + " §cpour inviter des personnes dans le claim ☠")
            return false
        }

        if (claim.claimMembers.size >= claim.maxMember) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation(sender.uniqueId, "survie.cmd.player.invite.max"))
            return false
        }

        val player = Survie.SURVIE.server.getPlayer(args[1])
        if (player == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.notonline"))
            return false
        }

        if (Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(player.uniqueId)) != null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.isonclaim"))
            return false
        }

        if (claim.invitations[player.uniqueId] != null && claim.invitations[player.uniqueId]!! > System.currentTimeMillis()) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUne invitation est déjà en cours pour ce joueur ☠")
            return false
        }

        claim.invitations[player.uniqueId] = (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(3))
        player.sendMessage(Survie.SURVIE.prefix + surviePlayer.player.name + "§7 vous a invité à rejoindre son claim §7(expire dans 3 minutes)")
        player.sendMessage(Survie.SURVIE.prefix + "§b» §3/claim accept " + surviePlayer.player.name + " §8» §7Pour accepter")
        player.sendMessage(Survie.SURVIE.prefix + "§b» §3/claim deny " + surviePlayer.player.name + " §8» §7Pour refuser")

        if (Survie.SURVIE.server.getPlayer(claim.owner) != null)
            Survie.SURVIE.server.getPlayer(claim.owner)!!.sendMessage(Survie.SURVIE.prefix + claim.getRank(surviePlayer).s + " " + sender.name + " §7a invité §3" + player.name + " §7 dans le claim")
        claim.claimMembers.forEach { t: ClaimMember? ->
            if (Survie.SURVIE.server.getPlayer(t!!.uuidMember) != null)
                Survie.SURVIE.server.getPlayer(t.uuidMember)!!.sendMessage(Survie.SURVIE.prefix + claim.getRank(surviePlayer).s + " " + sender.name + " §7a invité §3" + player.name + " §7 dans le claim")
        }
        return true
    }
}
