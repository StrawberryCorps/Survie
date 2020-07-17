package bzh.strawberry.survie.jobs.manager;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.survie.Survie;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.*;
import java.text.DecimalFormat;

/*
 * This file (JobData) is part of a project Survie.
 * It was created on 17/07/2020 09:58 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class JobData {

    private final String name;
    private double totalXP;
    private double hourXP;
    private int level;
    private final Player player;

    public JobData(String name, double totalXP, double hourXP, int level, Player player) {
        this.name = name;
        this.totalXP = totalXP;
        this.hourXP = hourXP;
        this.level = level;
        this.player = player;
    }

    public String getName() {
        return name;
    }

    public double getTotalXP() {
        return totalXP;
    }

    public double getHourXP() {
        return hourXP;
    }

    public int getLevel() {
        return level;
    }

    public void addHourXP(double hourXP) {
        this.hourXP += hourXP;
        this.totalXP += hourXP;
        if (this.level > 31) {
            double required = (4.5 * Math.pow(this.level, 2) - 162.5 * this.level + 2220);
            if (this.totalXP >= required) {
                this.level++;
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
            }
        } else if (this.level > 16) {
            double required = (2.5 * Math.pow(this.level, 2) - 40.5 * this.level + 360);
            if (this.totalXP >= required) {
                this.level++;
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
            }
        } else {
            double required = Math.pow(this.level, 2) + 6 * this.level;
            if (this.totalXP >= required) {
                this.level++;
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
            }
        }
    }

    public void sendPaie(Player player) {
        // &3&m--------&r &b&lFiche de paie &3&m--------
        // &9- &3Pseudo: &bEclixal -*- &9- &3ID: &b#487
        // &9- &3Expériences durant l'heure: &b54545544
        // &9- &3Taxe Etat (10%): &b157 Ecu
        // &9- &3Taxe claim (80%): &b87787 Ecu
        // &9- &3Salaire: §31 Ecu
        // &3&m--------&r &b&lFiche de paie &3&m--------

        double salaire = 1521.22 + (1521.22 * (getHourXP() / 1000)) + (1521.22 * ((double) getLevel() / 100));

        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `survie_paies` (`survie_player`, `brut_salary`, `net_salary`, `amount_claim`, `amount_etat`) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, Survie.SURVIE.getSurviePlayer(player.getUniqueId()).getSurvieID());
                preparedStatement.setDouble(2, salaire);
                if (Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())) != null) {
                    preparedStatement.setDouble(3, salaire - salaire * Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxeEtat() / 100 - salaire * Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxe());
                    preparedStatement.setDouble(4, salaire - salaire * Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxe());
                    preparedStatement.setDouble(5, salaire * Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxeEtat() / 100);
                    Survie.SURVIE.getLotterieManager().addCagnotte(salaire * Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxeEtat() / 100);
                } else {
                    preparedStatement.setDouble(3, salaire - salaire * 0.11 - salaire * 0);
                    preparedStatement.setDouble(4, 0);
                    preparedStatement.setDouble(5, salaire * 0.11);
                    Survie.SURVIE.getLotterieManager().addCagnotte(salaire * 0.11);
                }
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                int pId = -1;
                if (resultSet.next())
                    pId = resultSet.getInt(1);

                DecimalFormat decimalFormat = new DecimalFormat("##.00");

                player.sendMessage("§3§m--------§r §b§lFiche de paie §3§m--------");
                player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Pseudo: §b" + player.getName() + " §8-*- §9" + SymbolUtils.SQUARE + " §3ID: §b#" + pId);
                if (getHourXP() == 0.0)
                    player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Expériences durant l'heure: §b0.0");
                else
                    player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Expériences durant l'heure: §b" + decimalFormat.format(getHourXP()));
                if (Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())) != null) {
                    player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Taxe Etat (" + Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxeEtat() + "%): §b" + decimalFormat.format(salaire * Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxeEtat() / 100) + " Ecu");
                } else {
                    player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Taxe Etat (11%): §b" + decimalFormat.format(salaire * 0.11) + " Ecu");
                }
                if (Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())) != null) {
                    player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Taxe claim (" + (int) (Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxe() * 100) + "%): §b" + decimalFormat.format(salaire * Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxe()) + " Ecu");
                    Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).addBank(salaire * Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxe());
                    player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Salaire: §b" + decimalFormat.format((salaire - salaire * 0.11 - salaire * Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxe())) + " Ecu");
                } else
                    player.sendMessage("§9" + SymbolUtils.SQUARE + " §3Salaire: §b" + decimalFormat.format((salaire - salaire * 0.11)) + " Ecu");

                player.sendMessage("§3§m--------§r §b§lFiche de paie §3§m--------");

                if (Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())) != null)
                    Survie.SURVIE.getAccountManager().giveMoney(player.getUniqueId(), salaire - salaire * 0.11 - salaire * Survie.SURVIE.getClaimManager().getClaim(Survie.SURVIE.getSurviePlayer(player.getUniqueId())).getTaxe());
                else Survie.SURVIE.getAccountManager().giveMoney(player.getUniqueId(), salaire - salaire * 0.11);
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue (" + e.getMessage() + ")");
            } finally {
                this.hourXP = 0;
            }
        });
    }

    public String remainingBeforeLevelUp() {
        double requis = 0d;
        DecimalFormat formatteur = new DecimalFormat("#9.00");

        if (this.level > 31)
            requis = (4.5 * Math.pow(this.level, 2) - 162.5 * this.level + 2220);
        else if (this.level > 16)
            requis = (2.5 * Math.pow(this.level, 2) - 40.5 * this.level + 360);
        else
            requis = Math.pow(this.level, 2) + 6 * this.level;

        return formatteur.format(requis - this.totalXP);
    }
}
