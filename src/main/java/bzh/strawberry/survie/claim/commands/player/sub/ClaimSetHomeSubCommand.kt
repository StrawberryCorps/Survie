package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimSetHomeSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:57 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimSetHomeSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = Survie.SURVIE.claimManager.getClaim(surviePlayer)
        if (claim!!.getRank(surviePlayer) == ClaimRank.DUC || claim.getRank(surviePlayer) == ClaimRank.MARQUIS) {
            if (claim.cuboid.isIn(surviePlayer.player.location)) {
                claim.home = surviePlayer.player.location
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Vous venez de définir le spawn du claim")
            } else
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous devez être dans le claim pour effectuer cette commande !")
        } else
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous devez être au minimum être " + ClaimRank.MARQUIS.s + " dans le claim pour définir le home ☠")
        return true
    }
}
