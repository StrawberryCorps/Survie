package bzh.strawberry.survie.claim.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.world.Cuboid
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.commands.player.sub.*
import net.minecraft.server.v1_15_R1.ChatMessageType
import net.minecraft.server.v1_15_R1.IChatBaseComponent
import net.minecraft.server.v1_15_R1.PacketPlayOutChat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.stream.Collectors

/*
 * This file (ClaimCommand.kt) is part of a project Survie.
 * It was created on 09/07/2020 19:52 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class ClaimCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur"), TabCompleter {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player
        val surviePlayer = Survie.SURVIE.getSurviePlayer(player.uniqueId)
        if (strings.isEmpty()) {
            if (Survie.SURVIE.claimManager.getClaim(surviePlayer) != null) {
                player.sendMessage(Survie.SURVIE.prefix + "§7Téléportation en cours a votre claim...")

                Survie.SURVIE.server.scheduler.runTask(Survie.SURVIE, Runnable {
                    val home = Survie.SURVIE.claimManager.getClaim(surviePlayer)!!.home

                    if (!home.chunk.isLoaded)
                        home.chunk.load()

                    Survie.SURVIE.server.scheduler.runTask(Survie.SURVIE, Runnable { player.teleport(home) })
                })

            } else {
                if (player.location.world!!.name == "Survie") {
                    var allow = true
                    for (claim in Survie.SURVIE.claimManager.getClaims()) {
                        if (claim.cuboid.isInWithMarge(player.location, 200.0)) {
                            allow = false
                            break
                        }
                    }
                    if (allow) {
                        player.sendMessage(Survie.SURVIE.prefix + "§7Création du claim en cours...")
                        Survie.SURVIE.claimManager.addClaim(surviePlayer, Cuboid.fromCenter(player.location, 50))
                    } else {
                        player.sendMessage(Survie.SURVIE.prefix + "§cVous êtes trop proche d'un autre claim ☠")

                        val json = "{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"§6Se téléporter à un endroit claimable\"}]}}},\"text\":\"§b§l[§9§lSurvie§b§l] §3Vous voulez rejoindre une zone pù vous pouvez claim ? §3Cliquez §e§l[§6§lICI§e§l]\"}"
                        val packet = PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(json), ChatMessageType.CHAT)
                        (commandSender as CraftPlayer).handle.playerConnection.sendPacket(packet)
                    }
                } else {
                    player.sendMessage(Survie.SURVIE.prefix + "§cPour claim vous devez être dans le monde Survie ☠")
                }
            }
        } else {
            if (Survie.SURVIE.claimManager.getClaim(surviePlayer) != null) {

                if (Survie.SURVIE.claimManager.isCoop(surviePlayer.player.uniqueId) && (Survie.SURVIE.claimManager.getClaim(surviePlayer.player.location) != null && !Survie.SURVIE.claimManager.getClaim(surviePlayer.player.location)!!.isMember(surviePlayer.player.uniqueId))) {
                    surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous ne pouvez pas effectuer cette commande étant coop et en dehors de votre claim. ☠")
                    return false
                }

                if (strings[0] == "home" || strings[0] == "h")
                    ClaimHomeSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "sethome" || strings[0] == "sh")
                    ClaimSetHomeSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "lock" || strings[0] == "l")
                    ClaimLockSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "invite" || strings[0] == "inv")
                    ClaimInviteSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "kick" || strings[0] == "k")
                    ClaimKickSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "ban" || strings[0] == "b")
                    ClaimBanSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "leave" || strings[0] == "l")
                    ClaimLeaveSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "unban" || strings[0] == "u")
                    ClaimUnBanSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "gestion" || strings[0] == "team")
                    ClaimGestionSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "makeleader" || strings[0] == "ml")
                    ClaimMakeLeaderSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "name")
                    ClaimNameSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "coop" || strings[0] == "c")
                    ClaimCoopSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "uncoop" || strings[0] == "uc")
                    ClaimUncoopSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "banlist" || strings[0] == "bl")
                    ClaimBanlistSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "welcome" || strings[0] == "bienvenue")
                    ClaimWelcomeSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "farewell" || strings[0] == "goodbye")
                    ClaimFarewellSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "setwarp" || strings[0] == "sw")
                    ClaimSetWarpSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "warp")
                    ClaimWarpSubCommand.execute(commandSender, s, strings)
                else
                    helpPlayer(surviePlayer.player, s)
            } else {
                if (strings[0] == "accept" || strings[0] == "acc")
                    ClaimAcceptSubCommand.execute(commandSender, s, strings)
                else if (strings[0] == "refuse" || strings[0] == "deny")
                    ClaimDenySubCommand.execute(commandSender, s, strings)
                else {
                    surviePlayer.player.sendMessage("§6§m----------------------------")
                    surviePlayer.player.sendMessage("§e/$s accept <joueur> §8» §7Accepte une invitation")
                    surviePlayer.player.sendMessage("§e/$s refuse <joueur> §8» §7Refuse une invitation")
                    surviePlayer.player.sendMessage("§e/$s §8» §7Créer un claim")
                    surviePlayer.player.sendMessage("§6§m----------------------------")
                }
            }
        }
        return true
    }

    private fun helpPlayer(player: Player, alias: String) {
        player.sendMessage("§6§m----------------------------")
        player.sendMessage("§e/$alias invite <joueur> <owner> §8» §7Inviter un joueur à rejoindre le claim")
        player.sendMessage("§e/$alias ban <joueur> <owner> §8» §7Bannir un joueur du claim")
        player.sendMessage("§e/$alias unban <joueur> <owner> §8» §7Débannir un joueur du claim")
        player.sendMessage("§e/$alias kick <joueur> <owner> §8» §7Expulser un joueur du claim")
        player.sendMessage("§e/$alias leave <joueur> <owner> §8» §7Quitter/supprimer le claim")
        player.sendMessage("§e/$alias makeleader <joueur> <owner> §8» §7Définir un nouveau chef de claim")
        player.sendMessage("§e/$alias home §8» §7Se téléporter au claim")
        player.sendMessage("§e/$alias sethome §8» §7Définir le home du claim")
        player.sendMessage("§e/$alias lock §8» §7Vérrouiller le claim")
        player.sendMessage("§e/$alias gestion §8» §7Gestion du claim")
        player.sendMessage("§e/$alias name §8» §7Rename le claim")
        player.sendMessage("§e/$alias welcome §8» §7Change le message de bienvenue sur le claim")
        player.sendMessage("§e/$alias farewell §8» §7Change le message d'au revoir sur le claim")
        player.sendMessage("§e/$alias setwarp §8» §7Change le warp du claim, setwarp off pour le supprimer")
        player.sendMessage("§e/$alias warp <pseudo> §8» §7Téléporte au warp du claim")
        player.sendMessage("§e/$alias ccop <pseudo> §8» §7Coop un joueur au claim")
        player.sendMessage("§e/$alias uncoop <pseudo> §8» §7Enlève un joueur coop du claim")
        player.sendMessage("§6§m----------------------------")
    }

    override fun onTabComplete(commandSender: CommandSender, cmd: Command, alias: String, args: Array<out String>): MutableList<String> {
        val list = ArrayList<String>()
        if (args.size == 1)
            list.addAll(listOf("home", "sethome", "lock", "invite", "accept", "deny", "kick", "ban", "unban", "leave", "gestion", "makeleader", "coop", "uncoop", "welcome", "farewell"))
        if (args.size >= 2 && args[0] != "warp")
            list.addAll(Survie.SURVIE.server.onlinePlayers.stream().map(Player::getName).collect(Collectors.toList()))
        if (args.size >= 2 && args[0] == "warp") {

            val claimsWithWarp = Survie.SURVIE.claimManager.getClaimsWithWarp()
            claimsWithWarp.forEach { c ->

                if (c.owner != null && Bukkit.getOfflinePlayer(c.owner).name != null) {
                    val name = Bukkit.getOfflinePlayer(c.owner).name
                    if (name != null)
                        list.add(name)
                }
                c.claimMembers.forEach { cm ->
                    if (cm != null) {

                        val name = Bukkit.getOfflinePlayer(cm.uuidMember).name
                        if (name != null)
                            list.add(name)

                    }
                }
            }
        }

        return list
    }
}