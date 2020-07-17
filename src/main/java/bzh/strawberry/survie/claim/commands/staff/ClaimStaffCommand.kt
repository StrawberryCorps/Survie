package bzh.strawberry.survie.claim.commands.staff

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.claim.commands.staff.sub.ClaimStaffBankSubCommand
import bzh.strawberry.survie.claim.commands.staff.sub.ClaimStaffSpySubCommand
import bzh.strawberry.survie.claim.commands.staff.sub.ClaimStaffTPSubCommand
import bzh.strawberry.survie.claim.commands.staff.sub.ClaimStaffTeamSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

/*
 * This file (ClaimStaffCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 20:00 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class ClaimStaffCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.moderation") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(sender: CommandSender, alias: String, strings: Array<String>): Boolean {
        if (strings.isNotEmpty() && strings[0] == "tp") {
            ClaimStaffTPSubCommand.execute(sender, alias, strings)
        } else if (strings.isNotEmpty() && strings[0] == "team") {
            ClaimStaffTeamSubCommand.execute(sender, alias, strings)
        } else if (strings.isNotEmpty() && strings[0] == "spy") {
            ClaimStaffSpySubCommand.execute(sender, alias, strings)
        } else if (strings.isNotEmpty() && strings[0] == "bank") {
            ClaimStaffBankSubCommand.execute(sender, alias, strings)
        } else {
            sender.sendMessage("§6§m----------------------------")
            sender.sendMessage("§e/$alias tp <joueur> §8» §7Se téléporter a un claim")
            sender.sendMessage("§e/$alias team <joueur> §8» §7Affiche l'équipe d'un joueur")
            sender.sendMessage("§e/$alias info <joueur> §8» §7Informations sur un claim")
            sender.sendMessage("§e/$alias spy §8» §7Affiche les messages en claimchat")
            sender.sendMessage("§e/$alias bank <joueur> §8» §7Affiche le montant de la bank du claim")
            sender.sendMessage("§6§m----------------------------")
        }
        return true
    }
}