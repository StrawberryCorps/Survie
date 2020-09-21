package bzh.strawberry.survie.claim.commands.admin.sub

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.sql.SQLException

/*
 * This file (ClaimAdminDeleteSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:50 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimAdminDeleteSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (args.size < 1) {
            sender.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + alias + " delete <owner>")
            return false
        }

        val owner = Survie.SURVIE.server.getOfflinePlayer(args[1])

        if (owner == null) {
            sender.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.notexist"))
            return false
        }

        val claim = Survie.SURVIE.claimManager.getClaim(owner.uniqueId)

        if (claim == null) {
            sender.sendMessage(Survie.SURVIE.prefix + StrawAPI.getAPI().l10n.getTranslation((sender as Player).uniqueId, "survie.player.notclaim"))
            return false
        }

        Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, kotlin.run {
            {
                sender.sendMessage(Survie.SURVIE.prefix + "§7Suppression du claim...")
                try {
                    val connection = StrawAPI.getAPI().dataFactory.dataSource.connection
                    val preparedStatement = connection.prepareStatement("DELETE FROM `survie_claims` WHERE id = ?")
                    preparedStatement.setInt(1, claim.claimId)
                    preparedStatement.execute()
                    preparedStatement.close()
                    connection.close()
                    Survie.SURVIE.claimManager.getClaims().remove(claim)
                    Survie.SURVIE.server.logger.info("    -> Suppression claim : $claim")
                } catch (e: SQLException) {
                    sender.sendMessage(Survie.SURVIE.prefix + "§cUne erreur est survenue lors de la suppression (" + e.message + ")")
                }
            }
        })
        return true
    }
}