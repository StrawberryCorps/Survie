package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimBanSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:53 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimBanSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = Survie.SURVIE.claimManager.getClaim(surviePlayer)

        if (args.size < 2) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " ban <joueur>")
            return false
        }

        if (claim!!.getRank(surviePlayer) != ClaimRank.DUC && claim.getRank(surviePlayer) != ClaimRank.MARQUIS && claim.getRank(surviePlayer) != ClaimRank.COMTE) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.cmd.player.ban.rank").replace("{rank}", ClaimRank.COMTE.s))
            return false
        }

        val target = Survie.SURVIE.server.getOfflinePlayer(args[1])

        if (target == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.notexist"))
            return false
        }

        if (claim.owner == target.uniqueId || claim.claimMembers.stream().filter { t -> t.uuidMember == target.uniqueId }.findFirst().orElse(null) != null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.cmd.player.ban.cannot"))
            return false
        }

        if (claim.bannis.contains(target.uniqueId)) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.cmd.player.ban.already"))
            return false
        }

        claim.addBan(target.uniqueId, true)

        if (Survie.SURVIE.server.getPlayer(claim.owner) != null)
            Survie.SURVIE.server.getPlayer(claim.owner)!!.sendMessage(Survie.SURVIE.prefix + String.format(StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.cmd.player.ban.add"), claim.getRank(surviePlayer).s, sender.name, target.name))
        claim.claimMembers.forEach { t: ClaimMember? ->
            if (Survie.SURVIE.server.getPlayer(t!!.uuidMember) != null)
                Survie.SURVIE.server.getPlayer(t.uuidMember)!!.sendMessage(Survie.SURVIE.prefix + String.format(StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.cmd.player.ban.add"), claim.getRank(surviePlayer).s, sender.name, target.name))
        }
        return true
    }
}
