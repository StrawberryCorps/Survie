package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimAcceptSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:53 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimAcceptSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)

        if (args.size < 2) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " accept <joueur>")
            return false
        }

        if (Survie.SURVIE.claimManager.getClaim(surviePlayer) != null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous devez d'abord quitter votre claim")
            return false
        }

        val invite = Survie.SURVIE.server.getPlayer(args[1])
        if (invite == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas connecté §l☠")
            return false
        }

        val claim = Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(invite.uniqueId))
        if (claim == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'a pas de claim ☠")
            return false
        }

        if (!claim.invitations.containsKey(surviePlayer.uniqueID)) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe claim ne vous a pas invité ☠")
            return false
        }

        if (claim.invitations[surviePlayer.uniqueID]!! < System.currentTimeMillis()) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cL'invitation pour rejoindre le claim de " + args[1] + " a expirée ☠")
            return false
        }

        if (claim.claimMembers.size >= claim.maxMember) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe claim est plein ☠")
            return false
        }

        claim.addClaimMember(ClaimMember(surviePlayer.uniqueID, ClaimRank.BARON), true)
        surviePlayer.player.teleport(claim.home)

        if (Survie.SURVIE.server.getPlayer(claim.owner) != null)
            Survie.SURVIE.server.getPlayer(claim.owner)!!.sendMessage(Survie.SURVIE.prefix + "§3" + surviePlayer.player.name + " §7a rejoint le claim")
        claim.claimMembers.forEach { t: ClaimMember? ->
            if (Survie.SURVIE.server.getPlayer(t!!.uuidMember) != null)
                Survie.SURVIE.server.getPlayer(t.uuidMember)!!.sendMessage(Survie.SURVIE.prefix + "§3" + surviePlayer.player.name + " §7a rejoint  le claim")
        }
        surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Vous venez de rejoindre le claim")
        return true
    }
}
