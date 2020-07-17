package bzh.strawberry.survie.task

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.gui.KitsGUI
import org.bukkit.plugin.Plugin

/*
 * This file (GUIRefreshTask.kt) is part of a project Survie.
 * It was created on 17/07/2020 19:42 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class GUIRefreshTask(plugin: Plugin) : Runnable {

    init {
        plugin.logger.info("    -> Enregistrement du Runnable " + javaClass.name)
    }

    override fun run() {
        StrawAPI.getAPI().interfaceManager.openedInterface.keys.stream().filter { uuid -> StrawAPI.getAPI().interfaceManager.openedInterface[uuid] is KitsGUI }.forEach { uuid -> StrawAPI.getAPI().interfaceManager.openedInterface[uuid]?.show(Survie.SURVIE.server.getPlayer(uuid)) }
    }
}
          