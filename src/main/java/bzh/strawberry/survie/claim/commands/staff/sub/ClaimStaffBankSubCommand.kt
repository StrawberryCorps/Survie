package bzh.strawberry.survie.claim.commands.staff.sub

import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender

/*
 * This file (ClaimStaffBankSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:58 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimStaffBankSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (args.size < 2) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cUtilise /$alias bank <joueur>")
            return false
        }

        val player = Survie.SURVIE.server.getOfflinePlayer(args[1])

        if (player == null) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'existe pas §l☠")
            return false
        }

        val claim = Survie.SURVIE.claimManager.getClaim(player)

        if (claim == null) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'a pas de claim ☠")
            return false
        }

        sender.sendMessage(Survie.SURVIE.prefix + "§7Le claim de §e" + player.name + "§7 possède §e " + claim.bank + " §7ecu.")
        return true
    }
}