package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.WeatherType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin


/*
 * This file (TempsCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:07 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class TempsCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {

        val player = commandSender as Player

        if (strings.size == 0) {
            player.sendMessage(Survie.SURVIE.prefix + "§cUsage : /" + s + " <clear | rain | reset>")
            return false
        }

        if (strings[0] == "reset") {
            player.sendMessage(Survie.SURVIE.prefix + "§7 Votre météo correspond à celle du serveur.")
            player.resetPlayerWeather()
        } else if (strings[0] == "rain") {
            player.sendMessage(Survie.SURVIE.prefix + "§7 Il fait pleut maintenant sur votre écran.")
            player.setPlayerWeather(WeatherType.DOWNFALL)
        } else if (strings[0] == "clear") {
            player.sendMessage(Survie.SURVIE.prefix + "§7 Il fait désormais beau sur votre écran.")
            player.setPlayerWeather(WeatherType.CLEAR)
        }

        return true
    }
}