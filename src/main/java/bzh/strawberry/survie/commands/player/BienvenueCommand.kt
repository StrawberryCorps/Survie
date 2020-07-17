package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.manager.SurviePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (BienvenueCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:09 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class BienvenueCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player
        val surviePlayer: SurviePlayer = Survie.SURVIE.getSurviePlayer((player).uniqueId)

        if (strings.isEmpty()) { //on affiche le /b si joueur recent
            //TODO : bloquer le spam
            var success = false
            var spam = true
            loop@ for (pl: SurviePlayer in Survie.SURVIE.surviePlayers) {
                if (!pl.player.hasPlayedBefore()) {

                    if (surviePlayer.lastBienvenue != null) {
                        if (surviePlayer.lastBienvenue.player.name != pl.player.name) {
                            val maxi: Int = surviePlayer.bienvenues.size
                            player.chat(surviePlayer.bienvenues[kotlin.random.Random.nextInt(maxi)])
                            surviePlayer.lastBienvenue = pl
                            success = true
                            spam = false
                            break@loop
                        } else
                            spam = true
                    } else {
                        val maxi: Int = surviePlayer.bienvenues.size
                        player.chat(surviePlayer.bienvenues[kotlin.random.Random.nextInt(maxi)])
                        surviePlayer.lastBienvenue = pl
                        success = true
                        spam = false
                        break@loop
                    }

                }
            }
            if (!success) {
                if (!spam)
                    surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cMerci de le pas spam le chat. " + SymbolUtils.DEATH)
                else
                    surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cAucun nouveau joueur. " + SymbolUtils.DEATH)
            }
        } else if (strings[0] == "list") {  //Affiche la liste des /b du joueur
            val liste: Array<String> = surviePlayer.bienvenues
            surviePlayer.player.sendMessage("§8§n____________________________________________________")
            surviePlayer.player.sendMessage("")//met un espace pour pouvoir lire le premier message
            for (i in liste.indices) {
                surviePlayer.player.sendMessage("§6§n" + i + "§8 >> §r" + liste[i])
            }
            surviePlayer.player.sendMessage("§8§n____________________________________________________")
        } else if (strings[0] == "add") {   //On ajoute un /b, regarder si le joueur à la perm pour les couleurs
            if (strings.size == 1) {
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVeuillez entrer un message.")
                return false
            }

            var concat = ""
            val sb = StringBuilder()
            for (i in 1 until strings.size) {
                sb.append(strings[i])
                sb.append(" ")
            }
            concat = sb.toString()
            if ((concat.indexOf('&') != -1) && !player.hasPermission("survie.chat+")) {
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVous n'avez pas acheté l'extra §6chat+ : §e/store")
                return false
            }

            concat = concat.replace("&", "§")
            surviePlayer.addBienvenue(concat)
        } else if (strings[0] == "help") {  //Afficher la page d'help

            surviePlayer.player.sendMessage("§8§n____________________________________________________")
            surviePlayer.player.sendMessage("")
            surviePlayer.player.sendMessage("§a/b §8>> §7Envoyer le message de bienvenue")
            surviePlayer.player.sendMessage("§a/b list §8>> §7Liste de vos messages de bienvenue")
            surviePlayer.player.sendMessage("§a/b add <message>§8>> §7Ajouter un message de bienvenue")
            surviePlayer.player.sendMessage("§a/b remove <id>§8>> §7Supprimer un message de bienvenue")
            surviePlayer.player.sendMessage("§8§n____________________________________________________")

        } else if (strings[0] == "remove") {
            if (strings.size == 1) {
                surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cVeuillez préciser quel message vous voulez supprimer.")
                return false
            }
            try {
                surviePlayer.removeBienvenue(strings[1].toInt())
            } catch (e: NumberFormatException) {
                player.sendMessage(Survie.SURVIE.prefix + "§cMerci de saisir un entier : l'indice du message. " + SymbolUtils.DEATH)
            }

        }

        return true
    }
}
