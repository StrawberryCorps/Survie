package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimCoopSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:54 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimCoopSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = Survie.SURVIE.claimManager.getClaim(surviePlayer)
        if (args.size < 2) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " coop <joueur>")
            return false
        }

        if (claim!!.getRank(surviePlayer).p < ClaimRank.COMTE.p) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous devez être " + ClaimRank.MARQUIS.s + " §cpour coop des personnes dans le claim ☠")
            return false
        }

        val player = Survie.SURVIE.server.getPlayer(args[1])
        if (player == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas connecté §l☠")
            return false
        }

        if (claim.coopMembers.contains(player.uniqueId)) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur est déjà coop §l☠")
            return false
        }

        claim.coop(player.uniqueId, surviePlayer.player.uniqueId)
        surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous avez coop un joueur. Attention : toutes ses actions sur votre claim sont sous VOTRE responsabilité !")
        player.sendMessage(Survie.SURVIE.prefix + surviePlayer.player.name + "§7 vous a coop sur son claim")

        claim.claimMembers.forEach { t: ClaimMember? ->
            if (Survie.SURVIE.server.getPlayer(t!!.uuidMember) != null)
                Survie.SURVIE.server.getPlayer(t.uuidMember)!!.sendMessage(Survie.SURVIE.prefix + claim.getRank(surviePlayer).s + " " + sender.name + " §7a coop §3" + player.name + " §7 dans le claim")
        }
        return true
    }
}
