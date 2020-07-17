package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (SetHomeCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:07 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class SetHomeCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) {
            commandSender.sendMessage(Survie.SURVIE.prefix + "Utilise §c/" + s + " <nom>")
            return false
        }

        val player: Player = commandSender as Player
        if (player.world.name.equals("world") || player.world.name.equals("world_the_end")) {
            player.sendMessage(Survie.SURVIE.prefix + "§cVous ne pouvez pas définir de home ici !")
            return false
        }

        if (player.world.name.equals("survie", ignoreCase = true) && Survie.SURVIE.claimManager.getClaim(player.location) != null && !Survie.SURVIE.claimManager.getClaim(player.location)!!.isOnClaim(player.uniqueId)) {
            player.sendMessage(Survie.SURVIE.prefix + "§cVous ne pouvez pas définir de home dans un claim !")
            return false
        }

        if (Survie.SURVIE.getSurviePlayer(player.uniqueId).homes.containsKey(strings[0])) {
            Survie.SURVIE.getSurviePlayer(player.uniqueId).changerHome(strings[0], player.location)
            return true
        }

        val nbHome = Survie.SURVIE.getSurviePlayer(player.uniqueId).homes.size + 1

        if (!player.isOp && player.hasPermission("survie.roi") && nbHome > 11) {
            player.sendMessage(Survie.SURVIE.prefix + "§cVous ne pouvez pas poser plus de home !")
            return false
        } else if (!player.hasPermission("survie.roi") && nbHome > 8) {

            return false
        } else if (!player.hasPermission("survie.prince") && nbHome > 5) {

            return false
        } else if (!player.hasPermission("survie.marquis") && nbHome > 3) {

            return false
        } else if (!player.hasPermission("survie.comte") && nbHome > 1) {

            return false
        }
        //todo
        Survie.SURVIE.getSurviePlayer(player.uniqueId).addHome(strings[0], player.location)
        return true
    }
}
