package bzh.strawberry.survie.claim.commands.player.sub

import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
 * This file (ClaimWarpSubCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:57 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
object ClaimWarpSubCommand {

    fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        val surviePlayer = Survie.SURVIE.getSurviePlayer((sender as Player).uniqueId)

        if (args.size != 2) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUsage : /" + alias + " " + args[0] + " <pseudo>")
            return false
        }

        val joueurCible = Bukkit.getOfflinePlayer(args[1])

        if (joueurCible == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cLe joueur spécifié n'est pas trouvé.")
            return false
        }

        val claim = Survie.SURVIE.claimManager.getClaim(joueurCible)

        if (claim == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cLe claim n'est pas trouvé.")
            return false
        }


        if (claim.warp == null) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cCe claim n'a pas de warp.")
            return false
        }

        if (claim.isLock) {
            surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cLe claim est fermé.")
            return false
        }

        surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§7Téléportation au claim §7" + claim.name)

        Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, Runnable {

            if (!claim.warp.chunk.isLoaded)
                claim.warp.chunk.load()

            Survie.SURVIE.server.scheduler.runTask(Survie.SURVIE, Runnable { surviePlayer.player.teleport(claim.warp) })

        })

        return true
    }
}
