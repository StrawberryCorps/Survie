package bzh.strawberry.survie.claim.commands.admin.sub

import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import org.bukkit.command.CommandSender

/*
 * This file (ClaimAdminAddMemberSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:49 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimAdminAddMemberSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (args.size < 2) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " addMembre <joueur> <owner>")
            return false
        }

        val owner = Survie.SURVIE.server.getOfflinePlayer(args[2])
        val player = Survie.SURVIE.server.getPlayer(args[1])

        if (owner == null) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'existe pas §l☠")
            return false
        }

        if (player == null) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas connecté §l☠")
            return false
        }

        val claim = Survie.SURVIE.claimManager.getClaim(owner.uniqueId)

        if (claim == null) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'a pas de claim ☠")
            return false
        }

        if (Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(player.uniqueId)) != null) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cCe joueur est déjà dans un claim ☠")
            return false
        }

//        if (claim.claimMembers.size >= claim.maxMember) {
//            sender.sendMessage(Survie.SURVIE.prefix + "§cCe claim est plein " + SymbolUtils.DEATH)
//            return false
//        }

        claim.addClaimMember(ClaimMember(player.uniqueId, ClaimRank.BARON), true)
        sender.sendMessage(Survie.SURVIE.prefix + "§3Vous venez d'ajouter §b" + player.name + " §3au claim de §b" + owner.name)
        return true
    }
}