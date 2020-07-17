package bzh.strawberry.survie.claim.commands.staff.sub

import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimStaffSpySubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:58 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimStaffSpySubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val player = sender

            Survie.SURVIE.getSurviePlayer(player.uniqueId).socialSpy = !Survie.SURVIE.getSurviePlayer(player.uniqueId).socialSpy

            if (Survie.SURVIE.getSurviePlayer(player.uniqueId).socialSpy)
                player.sendMessage(Survie.SURVIE.prefix + "§7ChatSpy §aActivé !")
            else
                player.sendMessage(Survie.SURVIE.prefix + "§7ChatSpy §cDésactivé !")
        }

        return true
    }
}