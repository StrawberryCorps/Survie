package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimBanlistSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:53 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimBanlistSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = Survie.SURVIE.claimManager.getClaim(surviePlayer)

        if (args.size >= 2) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " banlist")
            return false
        }

        if (claim == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cAucun claim trouvé... ☠")
            return false
        }
        val sb = StringBuilder()
        sb.append("[")
        val iterator = claim.bannis.iterator()
        while (iterator.hasNext()) {
            val element = iterator.next()
            sb.append(Bukkit.getOfflinePlayer(element).name)
            if (iterator.hasNext()) {
                sb.append(", ")
            }
        }
        sb.append("]")

        surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§3Banlist: §9" + sb.toString())

        return true
    }
}
