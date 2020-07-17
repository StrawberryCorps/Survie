package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (SpawnCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:07 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class SpawnCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {

        Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, kotlin.run {
            {

                val loc = Location(Bukkit.getWorld("world"), 32.5, 99.0, 58.5, -50f, 0f)
                val commandSender = (commandSender as Player)

                if (!loc.chunk.isLoaded)
                    loc.chunk.load()

                Survie.SURVIE.server.scheduler.runTask(Survie.SURVIE, Runnable { commandSender.teleport(loc) })

                commandSender.sendMessage(Survie.SURVIE.prefix + "§7Téléportation au spawn...")
                commandSender.playSound(commandSender.location, Sound.ENTITY_ENDERMAN_TELEPORT, 100f, 1f)
            }
        })
        return true

    }
}
