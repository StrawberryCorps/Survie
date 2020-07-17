package bzh.strawberry.survie.claim.commands.admin.sub

import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender

/*
 * This file (ClaimAdminChangeOwnerSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:49 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimAdminChangeOwnerSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (args.size < 2) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " changeOwner <joueur> <owner>")
            return false
        }

        val owner = Survie.SURVIE.server.getOfflinePlayer(args[2])
        val player = Survie.SURVIE.server.getOfflinePlayer(args[1])

        if (owner == null || player == null) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'existe pas §l☠")
            return false
        }

        val claim = Survie.SURVIE.claimManager.getClaim(owner.uniqueId)

        if (claim == null) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'a pas de claim ☠")
            return false
        }

        if (Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(player.uniqueId)) != claim) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas dans ce claim ☠")
            return false
        }

        if (claim.owner == player.uniqueId) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur est déjà chef du claim ☠")
            return false
        }

        claim.changeOwner(claim.getMember(player.uniqueId))
        sender.sendMessage(Survie.SURVIE.prefix + "§7Vous venez de changer le chef du claim !")
        return true
    }
}