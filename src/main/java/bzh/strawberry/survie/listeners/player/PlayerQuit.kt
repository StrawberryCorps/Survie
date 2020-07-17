package bzh.strawberry.survie.listeners.player

import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.util.*

/*
 * This file (PlayerQuit.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:11 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class PlayerQuit(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val surviePlayer = Survie.SURVIE.getSurviePlayer(player.uniqueId)
        event.quitMessage = null

        if (surviePlayer.msgDeco != null) {
            event.quitMessage = surviePlayer.msgDeco
        }

        if (event.quitMessage != null) {
            for (pls in Bukkit.getOnlinePlayers()) {
                //todo sendMessageJson(pls, "[\"\",{\"text\":\""+event.quitMessage+"\""+",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"§3Joueur: §6§n"+player.name+"\"}]}}"+"}]");
            }
        }

        //On vire le joueur de la liste des coop des claims
        for (claims in Survie.SURVIE.claimManager.getClaims()) {
            if (claims.coopMembers.contains(player.uniqueId))
                claims.uncoop(player.uniqueId)
            //retrait des joueurs qu'il a coop
            if (claims.coopMembers.containsValue(player.uniqueId))
                claims.coopMembers.keys.forEach { k: UUID? ->
                    if (claims.coopMembers[k] == player.uniqueId) {
                        claims.uncoop(k)
                    }
                }
        }
        surviePlayer.saveAccount(false)


        Survie.SURVIE.surviePlayers.remove(surviePlayer)
        Survie.SURVIE.awayTask.removePlayer(player.uniqueId)

        for (shopPlayer in Survie.SURVIE.shopPlayerManager.shopPlayerEmplacements) {
            shopPlayer.hologram.removeReceiver(player)
        }
    }
}
