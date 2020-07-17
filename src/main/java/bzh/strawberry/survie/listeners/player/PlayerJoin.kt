package bzh.strawberry.survie.listeners.player

import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.manager.SurviePlayer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin

/*
 * This file (PlayerJoin.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:09 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class PlayerJoin(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (!player.isOnline) return
        if (Survie.SURVIE.server.onlinePlayers.size >= 100) {
            player.kickPlayer("§cLe serveur est complet ! Devenez §eComte §cpour rejoindre le serveur. " + SymbolUtils.DEATH)
            return
        }
        event.joinMessage = null

        /*
           Ce joueur est nouveau, on affiche le message de bienvenue et on le téléporte au spawn du Survie
         */
        if (!player.hasPlayedBefore()) {
            Bukkit.broadcastMessage("§8» §7Bienvenue à §b" + player.name + " §7sur le §bSurvie §8#" + Bukkit.getOfflinePlayers().size)
            player.teleport(Location(Bukkit.getWorld("world"), 58.5, 55.0, 94.5, -130f, 0f))
        }

        // On charge les données du joueur
        val surviePlayer = SurviePlayer(player)

        Survie.SURVIE.surviePlayers.add(surviePlayer)

        var j = ""
        surviePlayer.loadAccount {
            run {
                if (surviePlayer.msgCo != null && player.hasPermission("survie.customjoin")) {
                    j = (surviePlayer.msgCo)
                } else {
                    j = "§8[§a+§8] §7" + player.name
                }

                if (j.isNotEmpty()) {
                    for (pls in Bukkit.getOnlinePlayers()) {
                        // todo sendMessageJson(pls, "[\"\",{\"text\":\""+j+"\""+",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"§3Joueur: §6§n"+player.name+"\"}]}}"+"}]");
                    }
                }
                Survie.SURVIE.awayTask.registerPlayer(player.uniqueId)
            }
        }

        for (shopPlayer in Survie.SURVIE.shopPlayerManager.shopPlayerEmplacements) {
            shopPlayer.hologram.addReceiver(player)
        }
    }
}
