package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.gui.ClaimGestionGUI
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimGestionSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:54 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimGestionSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        StrawAPI.getAPI().interfaceManager.openInterface(ClaimGestionGUI(Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId), Survie.SURVIE.claimManager.getClaim(Survie.SURVIE.getSurviePlayer(sender.uniqueId))), sender)
        return true
    }
}
