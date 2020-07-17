package bzh.strawberry.survie.listeners.sign

import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.plugin.Plugin

/*
 * This file (SignChange.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:12 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class SignChange(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler
    fun onSignChange(event: SignChangeEvent) {
        val player = event.player
        if (player.hasPermission("survie.chat+")) {
            val l1: String = ChatColor.translateAlternateColorCodes('&', event.getLine(0) as String)
            val l2: String = ChatColor.translateAlternateColorCodes('&', event.getLine(1) as String)
            val l3: String = ChatColor.translateAlternateColorCodes('&', event.getLine(2) as String)
            val l4: String = ChatColor.translateAlternateColorCodes('&', event.getLine(3) as String)
            event.setLine(0, l1)
            event.setLine(1, l2)
            event.setLine(2, l3)
            event.setLine(3, l4)
        }
    }
}
