package bzh.strawberry.survie.playershop.listeners;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.claim.manager.data.Claim;
import bzh.strawberry.survie.gui.ConfirmGUI;
import bzh.strawberry.survie.playershop.data.ShopPlayerData;
import bzh.strawberry.survie.playershop.data.ShopPlayerEmplacement;
import bzh.strawberry.survie.utils.AnvilUtil;
import bzh.strawberry.survie.utils.CurrencyFormat;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/*
 * This file (PlayerInteractBuy) is part of a project Survie.
 * It was created on 17/07/2020 19:18 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class PlayerInteractBuy implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getClickedBlock().getType().equals(Material.STONE_BUTTON)) {
                if (Survie.SURVIE.getShopPlayerManager().getEmplacement(event.getClickedBlock().getLocation()) != null) {
                    ShopPlayerEmplacement emplacement = Survie.SURVIE.getShopPlayerManager().getEmplacement(event.getClickedBlock().getLocation());
                    if (Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())) != null) {
                        Claim claim = Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId()));
                        if (Survie.SURVIE.getShopPlayerManager().isLibre(emplacement)) {
                            StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("Confirmation location",
                                    new ItemStackBuilder(Material.DIAMOND, 1, "§aConfirmer l'achat", Arrays.asList("§9» §3Durée: §b5 jours", "§9» §3Prix: §b" + new CurrencyFormat().format(emplacement.getPrice()) + " Ecu")), null, (abstractGui) -> {
                                if (claim.getBank() >= emplacement.getPrice()) {
                                    if (Survie.SURVIE.getShopPlayerManager().hasAShopPlayer(claim)) {
                                        claim.removeBank(emplacement.getPrice());
                                        player.sendMessage(Survie.SURVIE.getPrefix() + "§7Transaction en cours...");
                                        Survie.SURVIE.getShopPlayerManager().initializeShopPlayer(player, emplacement);
                                    } else {
                                        player.sendMessage(Survie.SURVIE.getPrefix() + "§cVotre claim à déjà une parcelle de vente " + SymbolUtils.DEATH);
                                    }
                                    player.closeInventory();
                                } else {
                                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVotre claim n'a pas assez d'Ecu " + SymbolUtils.DEATH);
                                    player.closeInventory();
                                }
                            }, player), player);
                        } else {
                            player.sendMessage(Survie.SURVIE.getPrefix() + "§cCette emplacement n'est pas libre " + SymbolUtils.DEATH);
                        }
                    } else {
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous devez être dans un claim " + SymbolUtils.DEATH);
                    }
                }
            }

            if (event.getClickedBlock().getType().equals(Material.OAK_WALL_SIGN)) {
                if (!Survie.SURVIE.getShopPlayerManager().hasAShopPlayer(Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())))) {
                    ShopPlayerData shopPlayerData = Survie.SURVIE.getShopPlayerManager().getShopPlayer(Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())));
                    if (shopPlayerData != null) {
                        if (shopPlayerData.getEmplacement().isLocationSign(event.getClickedBlock().getLocation())) {
                            // On check si un item est déjà défini ou non
                            Sign sign = (Sign) event.getClickedBlock().getState();
                            if (sign.getLine(1).equals("§cAucun " + SymbolUtils.DEATH)) {
                                if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("Ajout d'un item a vendre",
                                            new ItemStackBuilder(player.getInventory().getItemInMainHand().getType(), player.getInventory().getItemInMainHand().getAmount(), "§3Confirmation", Arrays.asList("§9» §3Quantité: §b" + player.getInventory().getItemInMainHand().getAmount(), "§9» §3Prix: §bProchaine étape")), null, (abstractGui -> {
                                        AnvilUtil price = new AnvilUtil(player, e -> {
                                            if (e.getSlot() == AnvilUtil.AnvilSlot.OUTPUT && e.hasText()) {
                                                try {
                                                    e.setWillClose(true);
                                                    double montant = Double.parseDouble(e.getText());
                                                    if (montant <= 0)
                                                        throw new NumberFormatException("Le montant doit être positif");


                                                } catch (NumberFormatException err) {
                                                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cLe prix de vente doit être un nombre et supérieur a 0 Ecu !");
                                                }
                                            }
                                        });
                                        price.setSlot(AnvilUtil.AnvilSlot.INPUT_LEFT, new ItemStack(Material.PAPER));
                                        price.setSlotName(AnvilUtil.AnvilSlot.INPUT_LEFT, "0");
                                        price.setTitle("Prix de vente");
                                        price.open();
                                    }), player), player);
                                } else {
                                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous devez avoir un item dans votre main pour définir l'article a vendre " + SymbolUtils.DEATH);
                                }
                            } else {
                                // On ouvre le menu pour le remplissage
                                player.sendMessage("UPDATE");
                            }
                        }
                    }
                }
            }
        }
    }

}
