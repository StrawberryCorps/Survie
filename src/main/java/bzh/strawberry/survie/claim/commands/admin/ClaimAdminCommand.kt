package bzh.strawberry.survie.claim.commands.admin

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.claim.commands.admin.sub.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (ClaimAdminCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:52 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class ClaimAdminCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.admin") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(sender: CommandSender, alias: String, strings: Array<String>): Boolean {
        if (strings.isNotEmpty() && strings[0] == "addMember") {
            ClaimAdminAddMemberSubCommand.execute(sender, alias, strings)
        } else if (strings.isNotEmpty() && strings[0] == "changeOwner") {
            ClaimAdminChangeOwnerSubCommand.execute(sender, alias, strings)
        } else if (strings.isNotEmpty() && strings[0] == "removeMember") {
            ClaimAdminRemoveMemberSubCommand.execute(sender, alias, strings)
        } else if (strings.isNotEmpty() && strings[0] == "delete") {
            ClaimAdminDeleteSubCommand.execute(sender, alias, strings)
        } else if (strings.isNotEmpty() && strings[0] == "setbank") {
            ClaimAdminSetBankSubCommand.execute(sender, alias, strings)
        } else {
            sender.sendMessage("§6§m----------------------------")
            sender.sendMessage("§e/$alias addMember <joueur> <owner> §8» " + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.claim.admin.addP"))
            sender.sendMessage("§e/$alias removeMember <joueur> <owner> §8» " + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.claim.admin.removeP"))
            sender.sendMessage("§e/$alias delete <owner> §8» " + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.claiù.admin.removeC"))
            sender.sendMessage("§e/$alias changeOwner <joueur> <owner> §8» " + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.claim.admin.owner"))
            sender.sendMessage("§e/$alias setbank <joueur> <banque> §8» " + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.claim.admin.banque"))
            sender.sendMessage("§6§m----------------------------")
        }
        return true
    }
}