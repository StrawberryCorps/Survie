package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimSetWarpSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:57 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimSetWarpSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = Survie.SURVIE.claimManager.getClaim(surviePlayer)
        if (claim!!.getRank(surviePlayer) == ClaimRank.DUC || claim.getRank(surviePlayer) == ClaimRank.MARQUIS) {

            if (args.size == 2 && args[1].equals("off", ignoreCase = true)) {
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Vous venez de supprimer le warp du claim.")
                claim.setWarp(null, true)
            } else {
                if (claim.cuboid.isIn(surviePlayer.player.location)) {
                    claim.setWarp(surviePlayer.player.location, true)
                    surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Vous venez de définir le warp du claim.")
                } else
                    surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous devez être dans le claim pour effectuer cette commande !")
            }

        } else
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous devez être au minimum être " + ClaimRank.MARQUIS.s + " §cdans le claim pour définir le warp ☠")
        return true
    }

}
