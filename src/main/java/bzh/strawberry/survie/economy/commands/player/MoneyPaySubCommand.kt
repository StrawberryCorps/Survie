/*
 * This file (MoneyPaySubCommand.kt) is part of a project Survie.
 * It was created on 06/07/2020 11:02 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */

package bzh.strawberry.survie.economy.commands.player

import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


object MoneyPaySubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (sender !is Player)
            return false

        if (args.size < 3) {
            sender.sendMessage(Survie.SURVIE.prefix + "§c/" + alias + " pay <joueur> <montant>")
            return false
        }

        val montant: Double
        try {
            montant = java.lang.Double.parseDouble(args[2])
            if (montant <= 0)
                throw NumberFormatException("Le montant doit être positif")
        } catch (err: NumberFormatException) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cLe montant doit être un nombre et plus grand que zéro !")
            return false
        }

        val player = Bukkit.getPlayer(args[1])
        if (player == null) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas connecté.")
            return false
        }

        if (player.uniqueId == sender.uniqueId) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cVous ne pouvez pas vous envoyer de l'argent à vous même !")
            return false
        }

        if (Survie.SURVIE.accountManager.getBalance(sender.uniqueId) < montant) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cVous n'avez pas assez d'argent")
            return false
        }

        Survie.SURVIE.accountManager.takeMoney(sender.uniqueId, montant)
        player.sendMessage(Survie.SURVIE.prefix + "§7Vous avez reçu §e" + montant + " Ecu §7de §a" + sender.getName())

        Survie.SURVIE.accountManager.giveMoney(player.uniqueId, montant)
        sender.sendMessage(Survie.SURVIE.prefix + "§7Vous venez d'envoyer §e" + montant + " Ecu §7à §a" + player.name)
        return true
    }
}
