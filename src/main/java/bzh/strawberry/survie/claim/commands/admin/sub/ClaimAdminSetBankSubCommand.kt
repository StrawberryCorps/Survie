package bzh.strawberry.survie.claim.commands.admin.sub

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimAdminSetBankSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:51 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimAdminSetBankSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (args.size != 3) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " setbank <joueur> <owner>")
            return false
        }

        val target = Survie.SURVIE.server.getOfflinePlayer(args[1])
        val claim = Survie.SURVIE.claimManager.getClaim(target.uniqueId)

        if (claim == null) {
            sender.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.admin.noclaim.found"))
            return false
        }
        val nombre = args[2].toDouble()
        claim.bank = nombre

        return true
    }
}