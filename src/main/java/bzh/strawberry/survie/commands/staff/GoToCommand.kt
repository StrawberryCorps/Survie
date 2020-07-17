package bzh.strawberry.survie.commands.staff

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.stream.Collectors

/*
 * This file (GoToCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:10 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class GoToCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.staff"), TabCompleter {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player
        if (strings.isEmpty()) {
            player.sendMessage(Survie.SURVIE.prefix + "§cUtilise /" + s + " <world>")
            player.sendMessage(Survie.SURVIE.prefix + "§cMondes disponibles : " + Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()))
            return false
        }

        val target = Bukkit.getWorld(strings[0])
        if (target == null) {
            player.sendMessage(Survie.SURVIE.prefix + "§cCe monde n'existe pas §l" + SymbolUtils.DEATH)
            return false
        }
        player.teleport(target.spawnLocation)
        player.sendMessage(Survie.SURVIE.prefix + "§7Téléportation en cours...")
        player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 100f, 1f)
        return true
    }

    override fun onTabComplete(commandSender: CommandSender, cmd: Command, s: String, strings: Array<String>): MutableList<String> {
        val list = ArrayList<String>()
        if (strings.size == 1)
            list.addAll(Survie.SURVIE.server.worlds.stream().map(World::getName).collect(Collectors.toList()))
        return list
    }
}