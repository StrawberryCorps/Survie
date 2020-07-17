package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie.SURVIE
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.sql.SQLException

/*
 * This file (ClaimNameSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:56 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimNameSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = SURVIE.getSurviePlayer((sender as Player).uniqueId)
        val claim = SURVIE.claimManager.getClaim(surviePlayer)
        val player = surviePlayer.player

        if (args.size < 2) {
            player.sendMessage(SURVIE.prefix + "§cUtilise /$alias name <name>")
            return false
        }

        if (sender.hasPermission("survie.roi")) {
            if ((!player.hasPermission("survie.chat+")) && (args[1].indexOf('&') != -1)) {
                surviePlayer.player.sendMessage(SURVIE.prefix + "§cVous n'avez pas acheté l'extra §6chat+ : §e/store")
                return false
            }

            val name: String = args[1].replace('&', '§')
            val copieName = name.replace("§.".toRegex(), "")

            if (copieName.length > 26) {
                player.sendMessage(SURVIE.prefix + "§cLa taille maximale de votre nom de claim doit être 16 !")
                return false
            } else if (!copieName.matches(Regex("^[a-zA-Z0-9_]+$"))) {
                player.sendMessage(SURVIE.prefix + "§cVotre nom de claim doit contenir que des caractères alpha-numériques !")
                return false
            }

            SURVIE.server.scheduler.runTaskAsynchronously(SURVIE, kotlin.run {
                {
                    try {
                        val connecion = StrawAPI.getAPI().dataFactory.dataSource.connection
                        val preparedStatement = connecion.prepareStatement("UPDATE `survie_claims_data` SET `name` = ? WHERE `claim_id` = ?")
                        preparedStatement.setString(1, name)
                        if (claim != null) {
                            preparedStatement.setInt(2, claim.claimId)
                        }
                        preparedStatement.execute()
                        preparedStatement.close()
                        connecion.close()
                        claim!!.name = name
                        player.sendMessage(SURVIE.prefix + "§3Le nom du claim est désormais §b" + name)
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }
                }
            })
        }

        return true
    }
}
