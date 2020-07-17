package bzh.strawberry.survie.listeners.player

import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.api.world.Cuboid
import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector

/*
 * This file (PlayerMove.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:09 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class PlayerMove(plugin: Plugin) : Listener {

    var survieWorld: Cuboid
    var ressourceWorld: Cuboid

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
        survieWorld = Cuboid(Location(Survie.SURVIE.server.getWorld("world"), 51.0, 89.0, 50.0), Location(Survie.SURVIE.server.getWorld("world"), 48.0, 84.0, 50.0))
        ressourceWorld = Cuboid(Location(Survie.SURVIE.server.getWorld("world"), 40.0, 88.0, 51.0), Location(Survie.SURVIE.server.getWorld("world"), 38.0, 84.0, 51.0))
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player

        if (player.location.world?.name == "world") {
            if (survieWorld.isIn(player.location) && !Survie.SURVIE.getSurviePlayer(player.uniqueId).teleport) {
                player.sendMessage(Survie.SURVIE.prefix + "§7Téléportation en cours...")
                Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, kotlin.run { { Survie.SURVIE.getSurviePlayer(player.uniqueId).randomTeleport(Survie.SURVIE.server.getWorld("Survie")) } })
            }
            if (ressourceWorld.isIn(player.location) && !Survie.SURVIE.getSurviePlayer(player.uniqueId).teleport) {
                player.sendMessage(Survie.SURVIE.prefix + "§7Téléportation en cours...")
                Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, kotlin.run { { Survie.SURVIE.getSurviePlayer(player.uniqueId).randomTeleport(Survie.SURVIE.server.getWorld("Ressources")) } })
            }
        }

        if (player.location.world!!.name.equals("world_nether", ignoreCase = true) && player.location.y > 127 && !player.isOp) {
            player.sendMessage(Survie.SURVIE.prefix + "§cVous ne pouvez pas vous déplacer sur le toit du nether ! " + SymbolUtils.DEATH)
            Bukkit.dispatchCommand(player, "spawn")
        }

        if (Survie.SURVIE.claimManager.getClaim(player.location) != null && !Survie.SURVIE.claimManager.getClaim(player.location)!!.isOnClaim(player.uniqueId)
                && (Survie.SURVIE.claimManager.getClaim(player.location)!!.isLock || Survie.SURVIE.claimManager.getClaim(player.location)!!.isBanned(player))) {
            if (player.isInsideVehicle) {
                if ((player.vehicle is LivingEntity)) {
                    player.leaveVehicle()
                    event.isCancelled = true
                }
            } else {
                val v = player.location.toVector().subtract(Survie.SURVIE.claimManager.getClaim(player.location)!!.cuboid.center.toVector()).normalize().multiply(Vector(1.6, 1.0, 1.6))
                player.velocity = v
            }
            if (Survie.SURVIE.claimManager.getClaim(player.location)!!.isLock)
                player.sendMessage(Survie.SURVIE.prefix + "§cCe claim est fermé " + SymbolUtils.DEATH)
            else
                player.sendMessage(Survie.SURVIE.prefix + "§cVous êtes banni de ce claim ! " + SymbolUtils.DEATH)
        }
        if (event.from.distance(event.to!!) >= 0.1) {
            Survie.SURVIE.awayTask.resetPlayer(player.uniqueId)
        }
        val locTo = event.to
        val locFrom = event.from
        if (Survie.SURVIE.claimManager.getClaim(locTo!!) != null && Survie.SURVIE.claimManager.getClaim(event.from) == null) {
            val claim = Survie.SURVIE.claimManager.getClaim(locTo)
            if (claim != null && claim.bienvenue == null)
                player.sendMessage(Survie.SURVIE.prefix + "§7Vous entrez dans le claim §e" + Survie.SURVIE.claimManager.getClaim(locTo)!!.name)
            else if (claim != null)
                player.sendMessage("§8[§9" + claim.name + "§8]§r " + claim.bienvenue)
        } else if (Survie.SURVIE.claimManager.getClaim(locFrom) != null && Survie.SURVIE.claimManager.getClaim(locTo) == null) {
            val claim = Survie.SURVIE.claimManager.getClaim(locFrom)
            if (claim != null && claim.farewell == null)
                player.sendMessage(Survie.SURVIE.prefix + "§7Vous quittez le claim §e" + Survie.SURVIE.claimManager.getClaim(event.from)!!.name)
            else if (claim != null)
                player.sendMessage("§8[§9" + claim.name + "§8]§r " + claim.farewell)
        }
    }
}