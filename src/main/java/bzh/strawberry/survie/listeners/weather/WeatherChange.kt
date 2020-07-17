package bzh.strawberry.survie.listeners.weather

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.weather.WeatherChangeEvent
import org.bukkit.plugin.Plugin

/*
 * This file (WeatherChange.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:12 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class WeatherChange(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler
    fun onWeatherChange(event: WeatherChangeEvent) {
        if (event.world.name.equals("world", ignoreCase = true))
            event.isCancelled = true
    }
}
