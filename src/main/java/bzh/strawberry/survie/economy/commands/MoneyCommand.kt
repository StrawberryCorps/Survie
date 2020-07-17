/*
 * This file (MoneyCommand.kt) is part of a project Survie.
 * It was created on 06/07/2020 11:02 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */

package bzh.strawberry.survie.economy.commands

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.economy.commands.admin.MoneyBalanceOtherSubCommand
import bzh.strawberry.survie.economy.commands.admin.MoneyGiveSubCommand
import bzh.strawberry.survie.economy.commands.admin.MoneySetSubCommand
import bzh.strawberry.survie.economy.commands.admin.MoneyTakeSubCommand
import bzh.strawberry.survie.economy.commands.player.MoneyBalanceSubCommand
import bzh.strawberry.survie.economy.commands.player.MoneyHelpSubCommand
import bzh.strawberry.survie.economy.commands.player.MoneyPaySubCommand
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin


class MoneyCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty())
            MoneyBalanceSubCommand.execute(commandSender, s, strings)
        else {
            if ((strings[0].equals("balance", ignoreCase = true) || strings[0].equals("b", ignoreCase = true)) && strings.size < 2)
                MoneyBalanceSubCommand.execute(commandSender, s, strings)
            else if ((strings[0].equals("balance", ignoreCase = true) || strings[0].equals("b", ignoreCase = true)) && strings.size == 2 && commandSender.hasPermission("survie.admin"))
                MoneyBalanceOtherSubCommand.execute(commandSender, s, *strings)
            else if (strings[0].equals("give", ignoreCase = true) || strings[0].equals("g", ignoreCase = true) && commandSender.hasPermission("survie.admin"))
                MoneyGiveSubCommand.execute(commandSender, s, strings)
            else if (strings[0].equals("take", ignoreCase = true) || strings[0].equals("t", ignoreCase = true) && commandSender.hasPermission("survie.admin"))
                MoneyTakeSubCommand.execute(commandSender, s, *strings)
            else if (strings[0].equals("set", ignoreCase = true) && commandSender.hasPermission("survie.admin"))
                MoneySetSubCommand.execute(commandSender, s, *strings)
            else if (strings[0].equals("pay", ignoreCase = true))
                MoneyPaySubCommand.execute(commandSender, s, strings)
            else
                MoneyHelpSubCommand.execute(commandSender, s, strings)
        }
        return true
    }
}
