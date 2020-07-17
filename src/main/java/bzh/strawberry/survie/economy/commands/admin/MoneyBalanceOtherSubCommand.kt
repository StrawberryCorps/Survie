/*
 * This file (MoneyBalanceOtherSubCommand.kt) is part of a project Survie.
 * It was created on 06/07/2020 11:01 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */

package bzh.strawberry.survie.economy.commands.admin

import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender


object MoneyBalanceOtherSubCommand {

    fun execute(sender: CommandSender, alias: String, vararg args: String): Boolean {
        if (args.size < 2) {
            sender.sendMessage(Survie.SURVIE.prefix + "§c/" + alias + " balance <joueur>")
            return false
        }

        val offlinePlayer = Survie.SURVIE.server.getOfflinePlayer(args[1])
        if (offlinePlayer == null) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'existe pas !")
            return false
        }

        sender.sendMessage(Survie.SURVIE.prefix + "§7Ce joueur a " + Survie.SURVIE.accountManager.getBalance(offlinePlayer.uniqueId) + " Ecu")
        return true
    }
}
