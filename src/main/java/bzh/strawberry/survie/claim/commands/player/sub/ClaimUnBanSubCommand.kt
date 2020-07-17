package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimUnBanSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:57 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimUnBanSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = Survie.SURVIE.claimManager.getClaim(surviePlayer)

        if (args.size < 2) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " unban <joueur>")
            return false
        }

        if (claim!!.getRank(surviePlayer) != ClaimRank.DUC && claim.getRank(surviePlayer) != ClaimRank.MARQUIS && claim.getRank(surviePlayer) != ClaimRank.COMTE) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous devez être " + ClaimRank.COMTE.s + " §cpour débannir des joueurs du claim ☠")
            return false
        }

        val target = Survie.SURVIE.server.getOfflinePlayer(args[1])

        if (target == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'existe pas ! ☠")
            return false
        }

        if (!claim.bannis.contains(target.uniqueId)) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas banni du claim ☠")
            return false
        }

        claim.removeBan(target.uniqueId, true)

        if (Survie.SURVIE.server.getPlayer(claim.owner) != null)
            Survie.SURVIE.server.getPlayer(claim.owner)!!.sendMessage(Survie.SURVIE.prefix + claim.getRank(surviePlayer).s + " " + sender.name + " §7a débanni §3" + target.name + " §7 du claim")
        claim.claimMembers.forEach { t: ClaimMember? ->
            if (Survie.SURVIE.server.getPlayer(t!!.uuidMember) != null)
                Survie.SURVIE.server.getPlayer(t.uuidMember)!!.sendMessage(Survie.SURVIE.prefix + claim.getRank(surviePlayer).s + " " + sender.name + " §7a débanni §3" + target.name + " §7 du claim")
        }
        return true
    }
}
