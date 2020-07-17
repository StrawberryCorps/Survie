package bzh.strawberry.survie.listeners.player

import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.plugin.Plugin

/*
 * This file (PlayerDie.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:07 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class PlayerDie(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        //met la location de la mort pour le joueur
        val elPlayer = Survie.SURVIE.getSurviePlayer(event.entity.uniqueId)
        elPlayer.setDerniereMort(event.entity.location)
        val player = event.entity

        if (event.entity.location.world?.name == "Survie" && Survie.SURVIE.claimManager.getClaim(event.entity.location) != null && !Survie.SURVIE.claimManager.getClaim(event.entity.location)!!.isOnClaim(event.entity.uniqueId)) {
            event.keepInventory = true
            event.keepLevel = true
            event.drops.clear()
        }

        if (event.entity.lastDamageCause == null) {
            event.deathMessage = "§8[§9Mort§8] §e" + player.displayName + " §7est mort."
        } else if (event.entity.lastDamageCause!!.cause is EntityDamageEvent.DamageCause) {
            val c = event.entity.lastDamageCause!!.cause
            if (c == EntityDamageEvent.DamageCause.SUFFOCATION) {
                event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a suffoqué."
            } else if (c == EntityDamageEvent.DamageCause.LAVA) {
                event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a pris un bain de lave."
            } else if (c == EntityDamageEvent.DamageCause.CONTACT) { //prend aussi en compte les bushberries
                event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7est resté trop prés d'une plante piquante."
            } else if (c == EntityDamageEvent.DamageCause.FALL) {
                event.deathMessage = null
            } else if (c == EntityDamageEvent.DamageCause.ENTITY_ATTACK || c == EntityDamageEvent.DamageCause.PROJECTILE) {
                val f = event.entity.lastDamageCause as EntityDamageByEntityEvent?
                if (f!!.damager is Player) {
                    val k = f.damager as Player
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par §3" + k.displayName
                } else if (f.damager is Zombie) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Zombie"
                } else if (f.damager is Creeper) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Creeper"
                } else if (f.damager is Skeleton) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Squelette"
                } else if (f.damager is Spider) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par une §3Araignée"
                } else if (f.damager is Slime) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Slime"
                } else if (f.damager is Ghast) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Ghast"
                } else if (f.damager is PigZombie) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Cochon-Zombie"
                } else if (f.damager is Enderman) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Enderman"
                } else if (f.damager is CaveSpider) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par une §3Araignée venimeuse"
                } else if (f.damager is Silverfish) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Poisson d'argent"
                } else if (f.damager is Blaze) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Blaze"
                } else if (f.damager is Witch) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par une §3Sorcière"
                } else if (f.damager is Endermite) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par une §3Endermite"
                } else if (f.damager is Guardian) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Guardian"
                } else if (f.damager is Shulker) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Shulker"
                } else if (f.damager is Llama) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Lama"
                } else if (f.damager is Phantom) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Phantom"
                } else if (f.damager is EnderDragon) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par §3l'Ender Dragon"
                } else if (f.damager is Enderman) {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par un §3Enderman"
                } else if (f.damager is Projectile) {
                    val pro = f.damager as Projectile
                    if (pro.shooter is Player) {
                        val k = pro.shooter as Player?
                        event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a été tué par §3" + k!!.displayName
                    } else {
                        event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7est mort."
                    }
                } else {
                    event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7est mort."
                }
            } else if (c == EntityDamageEvent.DamageCause.VOID) {
                event.deathMessage = null
            } else if (c == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7a explosé."
            } else if (c == EntityDamageEvent.DamageCause.FIRE_TICK) {
                event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7s'est fait réduire en cendre."
            } else if (c == EntityDamageEvent.DamageCause.DROWNING) {
                event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7s'est noyé."
            } else {
                event.deathMessage = "§8[§9Mort§8] §3" + player.displayName + " §7est mort."
            }
        }
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val surviePlayer = Survie.SURVIE.getSurviePlayer(event.player.uniqueId)
        event.respawnLocation = Location(Bukkit.getWorld("world"), 32.5, 99.0, 58.5, -50f, 0f)
    }

}
