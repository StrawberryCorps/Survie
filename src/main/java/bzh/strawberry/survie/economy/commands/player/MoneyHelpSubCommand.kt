/*
 * This file (MoneyHelpSubCommand.kt) is part of a project Survie.
 * It was created on 06/07/2020 11:02 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */

package bzh.strawberry.survie.economy.commands.player

import org.bukkit.command.CommandSender

object MoneyHelpSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        sender.sendMessage("§6§m----------------------------")
        sender.sendMessage("§e/$alias §8» §7Solde de votre compte")
        sender.sendMessage("§e/$alias pay <joueur> <montant> §8» §7Envoyer de l'argent")
        if (sender.hasPermission("survie.admin")) {
            sender.sendMessage("§e/$alias take <joueur> <montant> §8» §7Prendre de l'argent")
            sender.sendMessage("§e/$alias give <joueur> <montant> §8» §7Give de l'argent")
            sender.sendMessage("§e/$alias set <joueur> <valeur> §8» §7Définir un solde")
            sender.sendMessage("§e/$alias balance <joueur> §8» §7Solde d'un joueur")
        }
        sender.sendMessage("§6§m----------------------------")
        return true
    }
}
