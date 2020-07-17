package bzh.strawberry.survie.shop.commands.admin;

import bzh.strawberry.api.command.AbstractCommand;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.manager.SurviePlayer;
import bzh.strawberry.survie.shop.manager.data.ShopItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/*
 * This file (AdminShopCommand) is part of a project Survie.
 * It was created on 17/07/2020 19:20 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class AdminShopCommand extends AbstractCommand {

    public AdminShopCommand(Plugin plugin) {
        super(plugin, "survie.moderation");
        plugin.getLogger().info("    -> Enregistrement de la commande " + getClass().getName());
    }

    @Override
    protected boolean onCommand(CommandSender commandSender, String s, String[] strings) {
        Player player = (Player) commandSender;
        SurviePlayer surviePlayer = Survie.SURVIE.getSurviePlayer(player.getUniqueId());
        if (strings.length >= 8 && strings[0].equals("add")) {
            if (player.getGameMode() == GameMode.CREATIVE && player.getItemInHand().getType() != Material.AIR) {
                Material material = player.getItemInHand().getType();
                if (Survie.SURVIE.getShopManager().getShopItem(material) == null) {
                    try {
                        surviePlayer.setShopItem(new ShopItem(material, Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3]), Double.parseDouble(strings[4]), Double.parseDouble(strings[5]), Double.parseDouble(strings[6]), null, null, strings[7]));
                        player.sendMessage("§6§m----------------------------");
                        commandSender.sendMessage("§eAjout d'un item au shop §6" + material);
                        commandSender.sendMessage("§eClique gauche pour définir la pancarte d'achat");
                        commandSender.sendMessage("§eEnsuite clique gauche pour définir la pancarte de vente");
                        player.sendMessage("§6§m----------------------------");
                    } catch (NumberFormatException e) {
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue ! '" + e.getMessage() + "'");
                    }
                } else
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cCet item est déjà dans le shop ! /" + s + " remove");
            } else
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous devez être en Créatif et avoir l'item a ajouter dans la main !");
        } else if (strings.length >= 1 && strings[0].equals("remove")) {
            if (player.getGameMode() == GameMode.CREATIVE && player.getItemInHand().getType() != Material.AIR) {
                if (Survie.SURVIE.getShopManager().getShopItem(player.getItemInHand().getType()) != null) {
                    Survie.SURVIE.getShopManager().removeShopItem(Survie.SURVIE.getShopManager().getShopItem(player.getItemInHand().getType()));
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cSuppression d'un item du shop !");
                }
            }
        } else if (strings.length >= 1 && strings[0].equals("reload")) {
            Survie.SURVIE.getShopManager().reload();
            player.sendMessage(Survie.SURVIE.getPrefix() + "§cRechargement du shop !");
        } else if (strings.length >= 1 && strings[0].equals("cancel")) {
            if (surviePlayer.getShopItem() != null) {
                surviePlayer.setShopItem(null);
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cAjout annulé !");
            }
        } else {
            commandSender.sendMessage("§6§m----------------------------");
            commandSender.sendMessage("§e/" + s + " add <prixAchat> <minAchat> <maxAchat> <prixVente> <minVente> <maxVente> <nom> §8» §7Ajoute un item au shop");
            commandSender.sendMessage("§e/" + s + " remove §8» §7Supprime un item au shop");
            commandSender.sendMessage("§e/" + s + " cancel §8» §7Annuler un ajout");
            commandSender.sendMessage("§e/" + s + " reload §8» §7Recharge le shop");
            commandSender.sendMessage("§6§m----------------------------");
        }
        return true;
    }
}
