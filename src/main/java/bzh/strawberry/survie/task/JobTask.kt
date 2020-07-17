package bzh.strawberry.survie.task

import bzh.strawberry.survie.Survie
import org.bukkit.Statistic
import org.bukkit.plugin.Plugin

/*
 * This file (JobTask.kt) is part of a project Survie.
 * It was created on 17/07/2020 19:44 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class JobTask(plugin: Plugin) : Runnable {

    init {
        plugin.logger.info("    -> Enregistrement du Runnable " + javaClass.name)
    }

    override fun run() {
        for (surviePlayer in Survie.SURVIE.surviePlayers) {
            if ((surviePlayer.player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) > 3000 && (surviePlayer.player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) % 3600 == 0) {
                if (surviePlayer.jobData != null)
                    surviePlayer.jobData.sendPaie(surviePlayer.player)
            }
        }
    }
}
