package bzh.strawberry.survie.commands.admin

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (TpoHereCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:05 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class TpoHereCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.admin") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player
        if (strings.isNotEmpty()) {
            val target = Bukkit.getPlayer(strings[0])
            if (target == null) {
                player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas connecté §l" + SymbolUtils.DEATH)
                return false
            }
            target.teleport(player)
            target.sendMessage(Survie.SURVIE.prefix + "§7Vous venez d'être téléporté par §e" + player.name)
            target.playSound(target.location, Sound.ENTITY_ENDERMAN_TELEPORT, 100f, 1f)
            player.sendMessage(Survie.SURVIE.prefix + "§7Téléportation en cours...")
        } else
            player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + s + " <joueur>")
        return true
    }
}