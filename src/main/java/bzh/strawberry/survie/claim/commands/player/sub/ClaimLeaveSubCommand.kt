package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.api.util.ItemStackBuilder
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import bzh.strawberry.survie.gui.ConfirmGUI
import bzh.strawberry.survie.utils.CurrencyFormat
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.sql.SQLException

/*
 * This file (ClaimLeaveSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:56 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimLeaveSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = Survie.SURVIE.claimManager.getClaim(surviePlayer)

        if (claim!!.getRank(surviePlayer) != ClaimRank.DUC) {
            StrawAPI.getAPI().interfaceManager.openInterface(ConfirmGUI("Quitter le claim ?",
                    ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Quitter le claim ?"), null, ConfirmGUI.ConfirmCallback {
                claim.removClaimMember(claim.getMember(surviePlayer.player.uniqueId), true)
                if (Survie.SURVIE.server.getPlayer(claim.owner) != null)
                    Survie.SURVIE.server.getPlayer(claim.owner)!!.sendMessage(Survie.SURVIE.prefix + claim.getRank(surviePlayer).s + " " + sender.name + " §7a quitté  le claim")
                claim.claimMembers.forEach { t: ClaimMember? ->
                    if (Survie.SURVIE.server.getPlayer(t!!.uuidMember) != null)
                        Survie.SURVIE.server.getPlayer(t.uuidMember)!!.sendMessage(Survie.SURVIE.prefix + claim.getRank(surviePlayer).s + " " + sender.name + " §7a quitté le claim")
                }

                surviePlayer.player.teleport(Location(Bukkit.getWorld("world"), 32.5, 99.0, 58.5, -50f, 0f))
                surviePlayer.player.closeInventory()
            }, surviePlayer.player), surviePlayer.player)
            return true
        }

        StrawAPI.getAPI().interfaceManager.openInterface(ConfirmGUI("Supprimer le claim ?",
                ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Supprimer le claim ?"), null, ConfirmGUI.ConfirmCallback {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Suppression du claim...")
            val bank = claim.bank
            try {
                val connection = StrawAPI.getAPI().dataFactory.dataSource.connection
                val preparedStatement = connection.prepareStatement("DELETE FROM `survie_claims` WHERE id = ?")
                preparedStatement.setInt(1, claim.claimId)
                preparedStatement.execute()
                preparedStatement.close()
                connection.close()
                Survie.SURVIE.claimManager.getClaims().remove(claim)
                Survie.SURVIE.server.logger.info("    -> Suppression claim : $claim")

                surviePlayer.player.teleport(Location(Bukkit.getWorld("world"), 32.5, 99.0, 58.5, -50f, 0f))
                surviePlayer.player.closeInventory()
                Survie.SURVIE.accountManager.giveMoney(surviePlayer.player.uniqueId, bank)
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Vous avez récupéré §e" + CurrencyFormat().format(bank) + " Ecu §7de la banque du claim.")
            } catch (e: SQLException) {
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUne erreur est survenue lors de la suppression (" + e.message + ")")
            }
        }, surviePlayer.player), surviePlayer.player)
        return true
    }
}
