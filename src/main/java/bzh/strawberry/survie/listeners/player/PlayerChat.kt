package bzh.strawberry.survie.listeners.player

import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie.SURVIE
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import bzh.strawberry.survie.manager.SurviePlayer
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.Plugin


/*
 * This file (PlayerChat.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:05 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class PlayerChat(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        var surviePlayer = SURVIE.getSurviePlayer(player.uniqueId)

        event.isCancelled = true
        var message = event.message
        var chars = message.toCharArray()
        var maj = 0
        for (c in chars) if (Character.isUpperCase(c)) maj++
        if (maj > 6) {
            message = message.toLowerCase()
            chars = message.toCharArray()
        }

        chars[0] = Character.toUpperCase(chars[0])
        message = String(chars)
        if (player.hasPermission("survie.chat+"))
            message = ChatColor.translateAlternateColorCodes('&', message)

        if (message.startsWith("!") && SURVIE.claimManager.getClaim(surviePlayer) != null) {
            val claim = SURVIE.claimManager.getClaim(surviePlayer)
            message = message.substring(1)
            if (claim != null) {
                if (SURVIE.server.getPlayer(claim.owner) != null) {
                    SURVIE.server.getPlayer(claim.owner)!!.sendMessage("§8[§cTeamChat§8] " + claim.getRank(surviePlayer).s + " " + player.name + " §8" + SymbolUtils.ARROW_DOUBLE + " §7" + message)
                }
                claim.claimMembers.forEach { t: ClaimMember? ->
                    if (SURVIE.server.getPlayer(t!!.uuidMember) != null) {
                        SURVIE.server.getPlayer(t.uuidMember)!!.sendMessage("§8[§cTeamChat§8] " + claim.getRank(surviePlayer).s + " " + player.name + " §8" + SymbolUtils.ARROW_DOUBLE + " §7" + message)
                    }
                }
            }

            SURVIE.surviePlayers.forEach { p: SurviePlayer ->
                if (claim != null)
                    if (p.socialSpy && event.player != p.player && !claim.isOnClaim(p.uniqueID)) p.player.sendMessage("§8[§cClaimSpy§8] " + player.name + " §8" + SymbolUtils.ARROW_DOUBLE + " §7" + message)

            }

            return
        }

        for (players in event.recipients) {
            val tmp = message.replace(("(?i)" + players.name).toRegex(), "§3§l" + players.name + "§r")
            if (tmp.toLowerCase().contains(players.name.toLowerCase())) {

            }


        }
    }

}
