package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimDenySubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:54 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimDenySubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)

        if (args.size < 2) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " refuse <joueur>")
            return false
        }

        val invite = Survie.SURVIE.server.getPlayer(args[1])
        if (invite == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas connecté §l☠")
            return false
        }

        val claim = Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(invite.uniqueId))
        if (claim == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'a pas de claim ☠")
            return false
        }

        if (!claim.invitations.containsKey(surviePlayer.uniqueID)) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe claim ne vous a pas invité ☠")
            return false
        }

        claim.invitations.remove(surviePlayer.uniqueID)

        if (Survie.SURVIE.server.getPlayer(claim.owner) != null)
            Survie.SURVIE.server.getPlayer(claim.owner)!!.sendMessage(Survie.SURVIE.prefix + "§3" + surviePlayer.player.name + " §7a refusé l'invitation")
        claim.claimMembers.forEach { t: ClaimMember? ->
            if (Survie.SURVIE.server.getPlayer(t!!.uuidMember) != null)
                Survie.SURVIE.server.getPlayer(t.uuidMember)!!.sendMessage(Survie.SURVIE.prefix + "§3" + surviePlayer.player.name + " §7a refusé l'invitation")
        }
        surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Vous venez de refuser l'invitation")
        return true
    }
}
