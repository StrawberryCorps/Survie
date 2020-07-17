package bzh.strawberry.survie.listeners.block

import bzh.strawberry.api.util.SymbolUtils
import bzh.strawberry.survie.Survie
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

/*
 * This file (BlockBreak.kt) is part of a project Survie.
 * It was created on 17/07/2020 10:02 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class BlockBreak(plugin: Plugin) : Listener {

    init {
        plugin.logger.info("    -> Enregistrement du listener " + javaClass.name)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        // On empeche la construction sur le spawn
        if (event.block.world.name.equals("world", ignoreCase = true) && !event.player.isOp && event.player.gameMode != GameMode.CREATIVE)
            event.isCancelled = true

        // On empeche la destruction dans le monde Survie
        if (event.block.world.name == "Survie" && !event.player.isOp && event.player.gameMode != GameMode.CREATIVE) {
            val claim = Survie.SURVIE.claimManager.getClaim(event.block.location)
            if (claim == null || (!claim.isOnClaim(event.player.uniqueId)))
                event.isCancelled = true
        }

        if (Survie.SURVIE.getSurviePlayer(event.player.uniqueId).infoBlock) {
            Survie.SURVIE.getSurviePlayer(event.player.uniqueId).infoBlock = false
            if (event.block.getMetadata("pseudo").isEmpty()) {
                event.player.sendMessage(Survie.SURVIE.prefix + "§cPas de joueur trouvé... " + SymbolUtils.DEATH)
                return
            }

            val meta = event.block.getMetadata("pseudo").iterator().next()
            event.player.sendMessage(Survie.SURVIE.prefix + "§7Le joueur qui a posé ce bloc est : §3" + meta.value() + "§7.")
        }

        if (event.block.world.name.equals("Survie", ignoreCase = true) && !event.isCancelled && event.block.type == Material.END_PORTAL_FRAME) {

            event.isCancelled = true
            event.block.type = Material.AIR

            for (i in portal.indices)
                checkFace(i, event.block)

            if (event.player.inventory.itemInMainHand.containsEnchantment(Enchantment.SILK_TOUCH))
                event.block.world.dropItemNaturally(event.block.location, ItemStack(Material.END_PORTAL_FRAME))

        }

        if (event.block.location.distance(event.block.world.spawnLocation) < 20 && !event.player.isOp && event.block.world.name != "Survie")
            event.isCancelled = true

    }

    val portal = arrayOf(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST)

    fun checkFace(i: Int, block: Block) {
        val currentFace = block.getRelative(portal[i])
        if (currentFace.type == Material.END_PORTAL) {
            currentFace.breakNaturally()
            for (j in portal.indices)
                checkFace(j, currentFace)

        }
    }
}
