package bzh.strawberry.survie.commands.player

import bzh.strawberry.api.command.AbstractCommand
import bzh.strawberry.survie.Survie
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

/*
 * This file (HatCommand.kt) is part of a project Survie.
 * It was created on 17/07/2020 20:10 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class HatCommand(plugin: Plugin) : AbstractCommand(plugin, "survie.joueur") {

    init {
        plugin.logger.info("    -> Enregistrement de la commande " + javaClass.name)
    }

    override fun onCommand(commandSender: CommandSender, s: String, strings: Array<String>): Boolean {

        val player = commandSender as Player

        val itemEnMain: ItemStack = player.inventory.itemInMainHand

        if (itemEnMain.type == Material.AIR) {
            player.sendMessage(Survie.SURVIE.prefix + "§cVous n'avez pas d'objet en main")
            return false
        }

        val listeEpees: Array<Material> = arrayOf(Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.WOODEN_SWORD, Material.STONE_SWORD)
        val listePioches: Array<Material> = arrayOf(Material.DIAMOND_PICKAXE, Material.GOLDEN_PICKAXE, Material.IRON_PICKAXE, Material.WOODEN_PICKAXE, Material.STONE_PICKAXE)
        val listeHaches: Array<Material> = arrayOf(Material.DIAMOND_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.WOODEN_AXE, Material.STONE_AXE)
        val listeHoues: Array<Material> = arrayOf(Material.DIAMOND_HOE, Material.GOLDEN_HOE, Material.IRON_HOE, Material.WOODEN_HOE, Material.STONE_HOE)
        val listePelles: Array<Material> = arrayOf(Material.DIAMOND_SHOVEL, Material.GOLDEN_SHOVEL, Material.IRON_SHOVEL, Material.WOODEN_SHOVEL, Material.STONE_SHOVEL)
        val listeAutres: Array<Material> = arrayOf(Material.BOW, Material.FLINT_AND_STEEL, Material.SHEARS, Material.FISHING_ROD, Material.CARROT_ON_A_STICK, Material.SHIELD, Material.ELYTRA)

        if (listeEpees.contains<Material>(itemEnMain.type) || listePioches.contains<Material>(itemEnMain.type) || listeHaches.contains<Material>(itemEnMain.type) || listeHoues.contains<Material>(itemEnMain.type) || listePelles.contains<Material>(itemEnMain.type) || listeAutres.contains<Material>(itemEnMain.type)) {
            player.sendMessage(Survie.SURVIE.prefix + "§cVous ne pouvez pas mettre cet item sur votre tête !")
            return false
        }

        //swap les items
        val tmp = player.inventory.itemInMainHand
        player.inventory.setItemInMainHand(player.inventory.helmet)
        player.inventory.helmet = tmp

        return true
    }
}
