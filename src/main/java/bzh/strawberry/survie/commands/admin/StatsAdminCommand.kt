package bzh.strawberry.survie.commands.admin

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Statistic
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * This file (StatsAdminCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:07 by Uicias.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class StatsAdminCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.admin") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {
        val player = commandSender as Player
        if (strings.size == 3 || strings.size == 4) {
            val target = Bukkit.getPlayer(strings[0])
            if (target == null) {
                player.sendMessage(Survie.SURVIE.prefix + "§cCe joueur n'est pas connecté §l" + SymbolUtils.DEATH)
                return false
            }
            val stat: Statistic
            val valeur: Int
            try {
                stat = Statistic.valueOf(strings[1].toUpperCase())
            } catch (e: IllegalArgumentException) {
                player.sendMessage(Survie.SURVIE.prefix + "§cAucune statistique Bukkit trouvée pour : §e " + strings[1])
                return false
            }

            if (strings.size == 3) {

                try {
                    valeur = strings[2].toInt()
                } catch (e: NumberFormatException) {
                    player.sendMessage(Survie.SURVIE.prefix + " §cUsage : /statadmin <joueur> <Statistique> <valeur : int>")
                    return false
                }

                target.setStatistic(stat, valeur)
                player.sendMessage(Survie.SURVIE.prefix + "§7Vous avez bien changé la statistique pour §e " + target.name)

            } else {
                //Material ou EntityType

                if (strings[2].startsWith("Material:", true) || strings[2].startsWith("EntityType:", true)) {

                    val param: String
                    try {
                        param = strings[2].split(':')[1]
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        player.sendMessage(Survie.SURVIE.prefix + "§cUtiliser Material:Block")
                        return false
                    }

                    if (strings[2].startsWith("Material:", true)) {
                        val mat: Material

                        try {
                            mat = Material.valueOf(strings[2].split(':')[1].toUpperCase())
                            valeur = strings[3].toInt()
                        } catch (e: IllegalArgumentException) {
                            player.sendMessage(Survie.SURVIE.prefix + "§cAucun Material trouvé...")
                            return false
                        } catch (e: NumberFormatException) {
                            player.sendMessage(Survie.SURVIE.prefix + "§cUsage : /statadmin <joueur> <Statistique> <Paramètre> <valeur : int>")
                            return false
                        }

                        target.setStatistic(stat, mat, valeur)
                        player.sendMessage(Survie.SURVIE.prefix + "§7Vous avez bien changé la statistique pour §e " + target.name)

                    } else {
                        val entity: EntityType?

                        try {
                            entity = EntityType.fromName(strings[2].split(':')[1].toUpperCase())
                            if (entity == null)
                                throw IllegalArgumentException("")
                            valeur = strings[3].toInt()
                        } catch (e: IllegalArgumentException) {
                            player.sendMessage(Survie.SURVIE.prefix + "§cAucune entité trouvé...")
                            return false
                        } catch (e: NumberFormatException) {
                            player.sendMessage(Survie.SURVIE.prefix + "§cUsage : /statadmin <joueur> <Statistique> <Paramètre> <valeur : int>")
                            return false
                        }

                        target.setStatistic(stat, entity, valeur)
                        player.sendMessage(Survie.SURVIE.prefix + "§7Vous avez bien changé la statistique pour §e" + target.name)
                        player.sendMessage(Survie.SURVIE.prefix + "§cAttention : cette fonctionnalité possède encore quelques problèmes...")
                    }

                } else {
                    player.sendMessage(Survie.SURVIE.prefix + "§cUsage : /statadmin <joueur> <Statistique> <<Material: | EntityType: >Paramètre> <valeur>")
                    return false
                }

            }

        } else {
            player.sendMessage(Survie.SURVIE.prefix + "§cUsage : /statadmin <joueur> <Statistique> <valeur>")
            player.sendMessage(Survie.SURVIE.prefix + "§cUsage : /statadmin <joueur> <Statistique> <Paramètre> <valeur>")
            return false
        }

        return true
    }

}