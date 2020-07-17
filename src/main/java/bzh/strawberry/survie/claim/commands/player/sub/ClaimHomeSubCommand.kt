package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimHomeSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:55 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimHomeSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)
        surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Téléportation a votre claim...")
        surviePlayer.player.teleport(Survie.SURVIE.claimManager.getClaim(surviePlayer)!!.home)
        return true
    }
}
