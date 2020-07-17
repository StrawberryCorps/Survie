package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.manager.SurviePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (CustomJoinCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:09 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class CustomJoinCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player
        val surviePlayer: SurviePlayer = Survie.SURVIE.getSurviePlayer((player).uniqueId)

        if (!player.hasPermission("survie.customjoin")) {
            return false
        }

        if (strings.isEmpty()) { //on affiche l'aide
            surviePlayer.player.sendMessage("§8§n____________________________________________________")
            surviePlayer.player.sendMessage("")
            surviePlayer.player.sendMessage("§6/" + s + " §rAffiche ce message")
            surviePlayer.player.sendMessage("§6/" + s + " help§r Affiche ce message")
            surviePlayer.player.sendMessage("§6/" + s + " off§r Supprime votre message de connexion")
            surviePlayer.player.sendMessage("§6/" + s + " voir§r Voir un apercu de votre message")
            surviePlayer.player.sendMessage("§6/" + s + " <message>§r Change votre message de connexion")
            surviePlayer.player.sendMessage("§6/" + s + " <flag>§r Affiche les drapeaux utilisables")
            surviePlayer.player.sendMessage("§8§n____________________________________________________")
        } else if (strings[0] == "off") {
            surviePlayer.ajoutMessageCo(null, true)
        } else if (strings[0] == "help") {
            surviePlayer.player.sendMessage("§8§n____________________________________________________")
            surviePlayer.player.sendMessage("")
            surviePlayer.player.sendMessage("§6/" + s + " §rAffiche ce message")
            surviePlayer.player.sendMessage("§6/" + s + " help§r Affiche ce message")
            surviePlayer.player.sendMessage("§6/" + s + " off§r Supprime votre message de connexion")
            surviePlayer.player.sendMessage("§6/" + s + " voir§r Voir un apercu de votre message")
            surviePlayer.player.sendMessage("§6/" + s + " <message>§r Change votre message de connexion")
            surviePlayer.player.sendMessage("§6/" + s + " <flag>§r Affiche les drapeaux utilisables")
            surviePlayer.player.sendMessage("§8§n____________________________________________________")
        } else if (strings[0] == "voir") {
            surviePlayer.player.sendMessage("§8§n____________________________________________________")
            surviePlayer.player.sendMessage("")
            if (surviePlayer.msgCo != null)
                surviePlayer.player.sendMessage(surviePlayer.msgCo)
            else
                surviePlayer.player.sendMessage("§cAucun message de connexion trouvé... " + SymbolUtils.DEATH)
            surviePlayer.player.sendMessage("§8§n____________________________________________________")
        } else if (strings[0] == "flag") {
            surviePlayer.player.sendMessage("§8§n____________________________________________________")
            surviePlayer.player.sendMessage("")
            surviePlayer.player.sendMessage("§5➢§e(money) §7Affiche votre argent")
            surviePlayer.player.sendMessage("§5➢§e(time) §7Affiche votre temps de jeu")
            surviePlayer.player.sendMessage("§5➢§e(kill) §7Affiche votre nombre de victimes")
            surviePlayer.player.sendMessage("§5➢§e(monstres) §7Affiche le nombre de monstres tués")
            surviePlayer.player.sendMessage("§5➢§e(grade) §7Affiche votre grade")
            surviePlayer.player.sendMessage("§5➢§e(pseudo) §7Affiche votre pseudo (code couleur inclus)")
            surviePlayer.player.sendMessage("§5➢§e(mort) §7Affiche votre nombre de morts")
            surviePlayer.player.sendMessage("§5➢§e(compteur) §7Affiche votre nombre de connexions")
            surviePlayer.player.sendMessage("§8§n____________________________________________________")
        } else {  //ajoute le message de co

            var concat = ""
            val sb = StringBuilder()
            for (i in 0 until strings.size) {
                sb.append(strings[i])
                sb.append(" ")
            }
            concat = sb.toString()

            if ((!player.hasPermission("survie.chat+")) && (concat.indexOf('&') != -1)) {
                return false
            }
            surviePlayer.ajoutMessageCo(concat, true)
        }


        return true
    }
}
