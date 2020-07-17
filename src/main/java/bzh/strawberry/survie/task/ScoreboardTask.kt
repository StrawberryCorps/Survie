package bzh.strawberry.survie.task

import org.bukkit.plugin.Plugin

/*
 * This file (ScoreboardTask.kt) is part of a project Survie.
 * It was created on 17/07/2020 19:45 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class ScoreboardTask(plugin: Plugin) : Runnable {

    init {
        plugin.logger.info("    -> Enregistrement du Runnable " + javaClass.name)
    }

    override fun run() {
        // todo
    }
}
