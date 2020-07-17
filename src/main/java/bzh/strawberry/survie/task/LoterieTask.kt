package bzh.strawberry.survie.task

import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.util.*

/*
 * This file (LoterieTask.kt) is part of a project Survie.
 * It was created on 17/07/2020 19:44 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class LoterieTask(plugin: Plugin) : Runnable {

    init {
        plugin.logger.info("    -> Enregistrement du Runnable " + javaClass.name)
    }

    override fun run() {
        val tirage = Calendar.getInstance()
        tirage.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        tirage.set(Calendar.HOUR_OF_DAY, 17)
        tirage.set(Calendar.MINUTE, 0)
        tirage.set(Calendar.SECOND, 0)
        if (tirage.time.before(Date())) tirage.add(Calendar.DAY_OF_WEEK, 7)
        tirage.timeZone = TimeZone.getTimeZone("Europe/Paris")

        println(tirage.time.toString())

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                tirage.add(Calendar.HOUR_OF_DAY, 1)
                Bukkit.getScheduler().scheduleAsyncRepeatingTask(Survie.SURVIE, {
                    if (tirage.timeInMillis / 1000 == System.currentTimeMillis() / 1000) {
                        Survie.SURVIE.lotterieManager.generateResult()
                    } else if (tirage.timeInMillis / 1000 - 3600 == System.currentTimeMillis() / 1000) Bukkit.broadcastMessage("§8[§3§lLoterie§8] §7Le tirage va avoir lieu dans 1 heure ! §b/loterie")
                    else if (tirage.timeInMillis / 1000 - 1800 == System.currentTimeMillis() / 1000) Bukkit.broadcastMessage("§8[§3§lLoterie§8] §7Le tirage va avoir lieu dans 30 minutes ! §b/loterie")
                }, 0, 20)
            }
        }, tirage.time)

    }
}
