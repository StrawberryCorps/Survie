package bzh.strawberry.survie.claim.gui;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.gui.AbstractInterface;
import bzh.strawberry.api.util.ItemStackBuilder;
import bzh.strawberry.api.util.SkullItemBuilder;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.claim.manager.data.Claim;
import bzh.strawberry.survie.gui.ConfirmGUI;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/*
 * This file (ClaimAgrandissementGUI) is part of a project Survie.
 * It was created on 09/07/2020 20:02 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class ClaimAgrandissementGUI extends AbstractInterface {

    private final SurviePlayer surviePlayer;
    private final Claim claim;

    public ClaimAgrandissementGUI(SurviePlayer surviePlayer, Claim claim) {
        super("Claim » Agrandissement", 45, surviePlayer.getPlayer());
        this.surviePlayer = surviePlayer;
        this.claim = claim;
    }

    public void show(Player player) {
        this.inventory.clear();
        int[] orangeCase = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 16, 17, 18, 26, 27, 28, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43};
        for (int aBlueCase : orangeCase)
            this.addItem(new ItemStackBuilder(Material.ORANGE_STAINED_GLASS_PANE, 1, ""), aBlueCase, null);

        if (player.hasPermission("survie.agr125")) {
            if (claim.getTaille() >= 1) {
                this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 125x125", "§e» §7Impôts Etat 11% -> 14%"), "http://textures.minecraft.net/texture/36d1fabdf3e342671bd9f95f687fe263f439ddc2f1c9ea8ff15b13f1e7e48b9", "§3Agrandissement 125x125"), 20, null);
            } else {
                this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 125x125", "§e» §7Impôts Etat 11% -> 14%", "", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic: §3Activer"), "http://textures.minecraft.net/texture/f6c5ecac942c77b95ab4620df5b85e38064c974f9c5c576b843622806a4557", "§3Agrandissement 125x125"), 20, "125");
            }
        } else {
            this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 125x125", "§e» §7Impôts Etat 11% -> 14%", "", "§8" + SymbolUtils.HAND_RIGHT + " §7Prix: §31 000 000 Ecu", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic: §3Acheter"), "http://textures.minecraft.net/texture/f6c5ecac942c77b95ab4620df5b85e38064c974f9c5c576b843622806a4557", "§3Agrandissement 125x125"), 20, "125");
        }

        if (player.hasPermission("survie.agr150")) {
            if (claim.getTaille() >= 2) {
                this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 150x150", "§e» §7Impôts Etat 14% -> 17%"), "http://textures.minecraft.net/texture/36d1fabdf3e342671bd9f95f687fe263f439ddc2f1c9ea8ff15b13f1e7e48b9", "§3Agrandissement 150x150"), 21, null);
            } else {
                this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 150x150", "§e» §7Impôts Etat 14% -> 17%", "", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic: §3Activer"), "http://textures.minecraft.net/texture/126b772329cf32f8643c4928626b6a325233ff61aa9c7725873a4bd66db3d692", "§3Agrandissement 150x150"), 21, "150");
            }
        } else {
            this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 150x150", "§e» §7Impôts Etat 14% -> 17%", "", "§8" + SymbolUtils.HAND_RIGHT + " §7Prix: §32 000 000 Ecu", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic: §3Acheter"), "http://textures.minecraft.net/texture/126b772329cf32f8643c4928626b6a325233ff61aa9c7725873a4bd66db3d692", "§3Agrandissement 150x150"), 21, "150");
        }

        if (player.hasPermission("survie.agr175")) {
            if (claim.getTaille() >= 3) {
                this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 175x175", "§e» §7Impôts Etat 17% -> 20%"), "http://textures.minecraft.net/texture/36d1fabdf3e342671bd9f95f687fe263f439ddc2f1c9ea8ff15b13f1e7e48b9", "§3Agrandissement 175x175"), 23, null);
            } else {
                this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 175x175", "§e» §7Impôts Etat 17% -> 20%", "", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic: §3Activer"), "http://textures.minecraft.net/texture/54bf893fc6defad218f7836efefbe636f1c2cc1bb650c82fccd99f2c1ee6", "§3Agrandissement 175x175"), 23, "175");
            }
        } else {
            this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 175x175", "§e» §7Impôts Etat 17% -> 20%", "", "§8" + SymbolUtils.HAND_RIGHT + " §7Prix: §35 000 000 Ecu", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic: §3Acheter"), "http://textures.minecraft.net/texture/54bf893fc6defad218f7836efefbe636f1c2cc1bb650c82fccd99f2c1ee6", "§3Agrandissement 175x175"), 23, "175");
        }

        if (player.hasPermission("survie.agr200")) {
            if (claim.getTaille() >= 4) {
                this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 200x200", "§e» §7Impôts Etat 20% -> 23%"), "http://textures.minecraft.net/texture/36d1fabdf3e342671bd9f95f687fe263f439ddc2f1c9ea8ff15b13f1e7e48b9", "§3Agrandissement 200x200"), 24, null);
            } else {
                this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 200x200", "§e» §7Impôts Etat 20% -> 23%", "", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic: §3Activer"), "http://textures.minecraft.net/texture/9631597dce4e4051e8d5a543641966ab54fbf25a0ed6047f11e6140d88bf48f", "§3Agrandissement 200x200"), 24, "200");
            }
        } else {
            this.addItem(new SkullItemBuilder(Arrays.asList("", "§e» §7Claim 200x200", "§e» §7Impôts Etat 20% -> 23%", "", "§8" + SymbolUtils.HAND_RIGHT + " §7Prix: §37 000 000 Ecu", "§8" + SymbolUtils.HAND_RIGHT + " §7Clic: §3Acheter"), "http://textures.minecraft.net/texture/9631597dce4e4051e8d5a543641966ab54fbf25a0ed6047f11e6140d88bf48f", "§3Agrandissement 200x200"), 24, "200");
        }

        this.addItem(new ItemStackBuilder(Material.DARK_OAK_DOOR, 1, "§3Retour", null), 44, "retour");
    }

    @Override
    public void onInventoryClose(Player player) {
    }

    @Override
    public void onInventoryClick(Player player, ItemStack itemStack, ClickType clickType, String action) {
        if (action.isEmpty()) return;
        if (action.equals("retour")) {
            StrawAPI.getAPI().getInterfaceManager().openInterface(new ClaimGestionGUI(surviePlayer, claim), player);
        } else if (action.equals("125")) {
            if (claim.getTaille() == 0) {
                if (player.hasPermission("survie.agr125")) {
                    if (claim.getTaille() < 1) {
                        claim.agrandirClaim(1);
                        player.closeInventory();
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§3Agrandissement du claim 125x125 !");
                    } else {
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne extension plus grande est déjà active !");
                        player.closeInventory();
                    }
                } else {
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("Confirmation", itemStack,
                            this, abstractGui -> {
                        if (Survie.SURVIE.getAccountManager().getBalance(player.getUniqueId()) >= 1000000) {
                            Survie.SURVIE.getAccountManager().takeMoney(player.getUniqueId(), 1000000);
                            claim.agrandirClaim(1);
                            player.sendMessage(Survie.SURVIE.getPrefix() + "§3Agrandissement du claim 125x125 !");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " add survie.agr125");
                        } else {
                            player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas assez d'argent");
                        }
                        player.closeInventory();
                    }, player), player);
                }
            } else {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous avez déjà activé cet agrandissement " + SymbolUtils.DEATH);
            }
        } else if (action.equals("150")) {
            if (player.hasPermission("survie.agr125") && claim.getTaille() == 1) {
                if (player.hasPermission("survie.agr150")) {
                    if (claim.getTaille() < 2) {
                        claim.agrandirClaim(2);
                        player.closeInventory();
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§3Agrandissement du claim 150x150 !");
                    } else {
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne extension plus grande est déjà active !");
                        player.closeInventory();
                    }
                } else {
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("Confirmation", itemStack,
                            this, abstractGui -> {
                        if (Survie.SURVIE.getAccountManager().getBalance(player.getUniqueId()) >= 2000000) {
                            Survie.SURVIE.getAccountManager().takeMoney(player.getUniqueId(), 2000000);
                            claim.agrandirClaim(2);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " add survie.agr150");
                            player.sendMessage(Survie.SURVIE.getPrefix() + "§3Agrandissement du claim 150x150 !");
                        } else {
                            player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas assez d'argent");
                        }
                        player.closeInventory();
                    }, player), player);

                }
            } else {
                player.closeInventory();
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous devez avoir l'agrandissement précédent " + SymbolUtils.DEATH);
            }
        } else if (action.equals("175")) {
            if (player.hasPermission("survie.agr125") && player.hasPermission("survie.agr150") && claim.getTaille() == 2) {
                if (player.hasPermission("survie.agr175")) {
                    if (claim.getTaille() < 3) {
                        claim.agrandirClaim(3);
                        player.closeInventory();
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§3Agrandissement du claim 175x175 !");
                    } else {
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne extension plus grande est déjà active !");
                        player.closeInventory();
                    }
                } else {
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("Confirmation", itemStack,
                            this, abstractGui -> {
                        if (Survie.SURVIE.getAccountManager().getBalance(player.getUniqueId()) >= 5000000) {
                            Survie.SURVIE.getAccountManager().takeMoney(player.getUniqueId(), 5000000);
                            claim.agrandirClaim(3);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " add survie.agr175");
                            player.sendMessage(Survie.SURVIE.getPrefix() + "§3Agrandissement du claim 175x175 !");
                        } else {
                            player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas assez d'argent");
                        }
                        player.closeInventory();
                    }, player), player);

                }
            } else {
                player.closeInventory();
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous devez avoir l'agrandissement précédent " + SymbolUtils.DEATH);
            }
        } else if (action.equals("200")) {
            if (player.hasPermission("survie.agr125") && player.hasPermission("survie.agr150") && player.hasPermission("survie.agr175") && claim.getTaille() == 3) {
                if (player.hasPermission("survie.agr200")) {
                    if (claim.getTaille() < 4) {
                        claim.agrandirClaim(4);
                        player.closeInventory();
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§3Agrandissement du claim 200x200 !");
                    } else {
                        player.sendMessage(Survie.SURVIE.getPrefix() + "§cVotre claim a déjà la taille maximum !");
                        player.closeInventory();
                    }
                } else {
                    StrawAPI.getAPI().getInterfaceManager().openInterface(new ConfirmGUI("Confirmation", itemStack,
                            this, abstractGui -> {
                        if (Survie.SURVIE.getAccountManager().getBalance(player.getUniqueId()) >= 7000000) {
                            Survie.SURVIE.getAccountManager().takeMoney(player.getUniqueId(), 7000000);
                            claim.agrandirClaim(4);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " add survie.agr200");
                            player.sendMessage(Survie.SURVIE.getPrefix() + "§3Agrandissement du claim 200x200 !");
                        } else {
                            player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas assez d'argent");
                        }
                        player.closeInventory();
                    }, player), player);

                }
            } else {
                player.closeInventory();
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cVous devez avoir l'agrandissement précédent " + SymbolUtils.DEATH);
            }
        }
    }
}
