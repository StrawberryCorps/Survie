package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimMakeLeaderSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:56 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimMakeLeaderSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = Survie.SURVIE.claimManager.getClaim(surviePlayer)

        if (claim!!.getRank(surviePlayer) != ClaimRank.DUC) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous devez être " + ClaimRank.DUC.s + " §cpour définir un nouveau chef de claim ☠")
            return false
        }

        val target = Survie.SURVIE.server.getOfflinePlayer(args[1])

        if (target == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'existe pas ! ☠")
            return false
        }

        if (claim.owner == target.uniqueId) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous êtes le chef du claim ☠")
            return false
        }

        if (claim.claimMembers.stream().filter { t -> t.uuidMember == target.uniqueId }.findFirst().orElse(null) == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas dans ce claim ☠")
            return false
        }

        claim.changeOwner(claim.getMember(target.uniqueId))

        if (Survie.SURVIE.server.getPlayer(claim.owner) != null)
            Survie.SURVIE.server.getPlayer(claim.owner)!!.sendMessage(Survie.SURVIE.prefix + claim.getRank(Survie.SURVIE.getSurviePlayer(target.uniqueId)).s + " " + target.name + " §7est le nouveau chef du claim")
        claim.claimMembers.forEach { t: ClaimMember? ->
            if (Survie.SURVIE.server.getPlayer(t!!.uuidMember) != null)
                Survie.SURVIE.server.getPlayer(t.uuidMember)!!.sendMessage(Survie.SURVIE.prefix + claim.getRank(Survie.SURVIE.getSurviePlayer(target.uniqueId)).s + " " + target.name + " §7est le nouveau chef du claim")
        }
        return true
    }
}
