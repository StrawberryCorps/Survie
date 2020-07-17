/*
 * This file (MoneySetSubCommand.kt) is part of a project Survie.
 * It was created on 06/07/2020 11:02 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */

package bzh.strawberry.survie.economy.commands.admin

import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender

object MoneySetSubCommand {

    fun execute(sender: CommandSender, alias: String, vararg args: String): Boolean {
        if (args.size < 3) {
            sender.sendMessage(Survie.SURVIE.prefix + "§c/" + alias + " set <joueur> <valeur>")
            return false
        }

        val offlinePlayer = Survie.SURVIE.server.getOfflinePlayer(args[1])
        if (offlinePlayer == null) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'existe pas !")
            return false
        }

        var montant = 0.0
        try {
            montant = java.lang.Double.parseDouble(args[2])
            if (montant <= 0)
                throw NumberFormatException("Le montant doit être positif")
        } catch (err: NumberFormatException) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cLe montant doit être un nombre et plus grand que zéro !")
            return false
        }

        sender.sendMessage(Survie.SURVIE.prefix + "§7  " + montant + " Ecu est le nouveau balance du joueur " + offlinePlayer.name)
        Survie.SURVIE.accountManager.setMoney(offlinePlayer.uniqueId, montant)
        return true
    }
}
