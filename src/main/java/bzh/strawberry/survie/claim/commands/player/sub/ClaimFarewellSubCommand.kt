package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimFarewellSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:54 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimFarewellSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = Survie.SURVIE.claimManager.getClaim(surviePlayer)
        if (claim!!.getRank(surviePlayer) == ClaimRank.DUC || claim.getRank(surviePlayer) == ClaimRank.MARQUIS) {

            if (args.size == 2 && args[1] == "off") {
                claim.farewell = null
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.cmd.player.farewell.remove"))
            } else if (args.size > 1) {
                val ite = args.iterator()
                var msg = ""
                ite.next()
                ite.forEachRemaining { s: String -> msg += s + " " }

                if (surviePlayer.player.hasPermission("survie.chat+"))
                    msg = msg.replace("&", "§")
                else if (msg.contains(Regex("&.")))
                    surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "") // @TODO faire un message
                claim.farewell = msg
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation(sender.uniqueId, "survie.cmd.player.farewell.set").replace("{message}", msg))

            }

        } else
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation(sender.uniqueId, "survie.cmd.player.farewell.rank").replace("{rank}", ClaimRank.MARQUIS.s))
        return true
    }
}
