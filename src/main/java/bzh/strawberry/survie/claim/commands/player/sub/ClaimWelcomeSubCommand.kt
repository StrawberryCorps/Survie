package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimWelcomeSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:58 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimWelcomeSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = Survie.SURVIE.claimManager.getClaim(surviePlayer)
        if (claim!!.getRank(surviePlayer) == ClaimRank.DUC || claim.getRank(surviePlayer) == ClaimRank.MARQUIS) {

            if (args.size == 2 && args[1] == "off") {
                claim.bienvenue = null
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Vous venez de supprimer le message de bienvenue !")
            } else if (args.size > 1) {
                val ite = args.iterator()
                var msg = ""
                ite.next() //on skip le welcome
                ite.forEachRemaining { s: String -> msg += s + " " }

                if (surviePlayer.player.hasPermission("survie.chat+"))
                    msg = msg.replace("&", "§")
                else if (msg.contains(Regex("&.")))
                    surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous n'avez pas acheté l'extra §6chat+ : §e/store")
                claim.bienvenue = msg
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Vous venez de définir le message d'arrivée dans votre claim : " + msg)

            }

        } else
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous devez être au minimum être " + ClaimRank.MARQUIS.s + " dans le claim pour définir le message ☠")
        return true
    }
}
