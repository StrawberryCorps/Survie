/*
 * This file (MoneyBalanceSubCommand.kt) is part of a project Survie.
 * It was created on 06/07/2020 11:02 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */

package bzh.strawberry.survie.economy.commands.player

import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.utils.CurrencyFormat
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object MoneyBalanceSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (sender !is Player)
            return false

        sender.sendMessage(Survie.SURVIE.prefix + "§7Vous avez §e" + CurrencyFormat().format(Survie.SURVIE.accountManager.getBalance(sender.uniqueId)) + " Ecu")
        return true
    }
}
