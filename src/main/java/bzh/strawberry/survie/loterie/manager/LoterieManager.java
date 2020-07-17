package bzh.strawberry.survie.loterie.manager;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.loterie.data.Ticket;
import bzh.strawberry.survie.utils.CurrencyFormat;
import org.bukkit.Bukkit;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

/*
 * This file (LoterieManager) is part of a project Survie.
 * It was created on 17/07/2020 19:14 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class LoterieManager {

    private double cagnotte;
    private final List<Ticket> tickets = new ArrayList<>();

    private double priceTicket;

    public LoterieManager() {
        try {
            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `survie_loterie_conf` WHERE `id` = 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                this.cagnotte = resultSet.getDouble("cagnotte");
                this.priceTicket = resultSet.getDouble("prix");
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Calendar start = Calendar.getInstance();
            start.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            start.set(Calendar.HOUR_OF_DAY, 18);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            if (start.getTime().after(new Date()))
                start.add(Calendar.DAY_OF_WEEK, -7);

            Calendar end = Calendar.getInstance();
            end.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            end.set(Calendar.HOUR_OF_DAY, 18);
            end.set(Calendar.MINUTE, 0);
            end.set(Calendar.SECOND, 0);
            if (end.getTime().before(new Date()))
                end.add(Calendar.DAY_OF_WEEK, 7);

            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `survie_loterie` WHERE `date` BETWEEN ? AND ?");
            preparedStatement.setTimestamp(1, new Timestamp(start.getTime().getTime()));
            preparedStatement.setTimestamp(2, new Timestamp(end.getTime().getTime()));
            System.out.println(preparedStatement.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                addTicket(new Ticket(UUID.fromString(resultSet.getString("uuid")), resultSet.getTimestamp("date"), resultSet.getInt("nombre1"), resultSet.getInt("nombre2"), resultSet.getInt("nombre3")), true);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_loterie_conf` SET `cagnotte`=?,`prix`=? WHERE `id` = ?");
            preparedStatement.setDouble(1, this.cagnotte);
            preparedStatement.setDouble(2, this.priceTicket);
            preparedStatement.setInt(3, 1);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCagnotte(double value) {
        this.cagnotte += value;
    }

    public void generateResult() {
        int tentative = 0;
        boolean w = false;

        while (!w && tentative < 30) {
            int nombre1 = new Random().nextInt(5) + 1;
            int nombre2 = new Random().nextInt(5) + 1;
            int nombre3 = new Random().nextInt(5) + 1;

            List<Ticket> gagnants = this.tickets.stream().filter(ticket -> ticket.getNumber1() == nombre1 && ticket.getNumber2() == nombre2
                    && ticket.getNumber3() == nombre3).collect(Collectors.toList());

            Calendar date = Calendar.getInstance();
            date.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            if (date.getTime().before(new Date()))
                date.add(Calendar.DAY_OF_WEEK, 7);

            if (gagnants.size() > 0) {
                w = true;
                if (gagnants.size() > 1) {
                    StringBuilder winner = new StringBuilder();
                    for (Ticket ticket : gagnants)
                        winner.append(Bukkit.getOfflinePlayer(ticket.getPlayer()).getName()).append(", ");
                    Bukkit.broadcastMessage("§8[§3§lLoterie§8] §7Plusieurs gagnants cette semaine §3" + winner.substring(0, winner.length() - 1) + " §7! Ils partagent la somme de " + new CurrencyFormat().format(getCagnotte()) + " Ecu ! Prochain tirage le " + new SimpleDateFormat("EEEE d MMMM", new Locale("fr", "FR")).format(date.getTime()) + " à 18 heures");

                    for (Ticket gagnant : gagnants) {
                        try {
                            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `survie_loterie_gagnant`(`uuid`, `amount`, `hasRead`) VALUES (?, ?, ?)");
                            preparedStatement.setString(1, gagnant.getPlayer().toString());
                            preparedStatement.setDouble(2, getCagnotte() / gagnants.size());
                            preparedStatement.setBoolean(3, (Bukkit.getPlayer(gagnant.getPlayer()) != null));
                            preparedStatement.execute();

                            if (Bukkit.getPlayer(gagnant.getPlayer()) != null) {
                                Bukkit.getPlayer(gagnant.getPlayer()).sendMessage("§8[§3§lLoterie§8] §3Félicitations vous avez remporté le tirage d'aujourd'hui ! Vous remportez la somme de §b" + new CurrencyFormat().format(getCagnotte() / gagnants.size()) + " Ecu");
                                Survie.SURVIE.getAccountManager().giveMoney(gagnant.getPlayer(), getCagnotte() / gagnants.size());
                            }
                            preparedStatement.close();
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Bukkit.broadcastMessage("§8[§3§lLoterie§8] §3" + Bukkit.getOfflinePlayer(gagnants.get(0).getPlayer()).getName() + " §7est le grand gagnant de cette semaine ! Il remporte la somme de " + new CurrencyFormat().format(getCagnotte()) + " Ecu ! Prochain tirage le " + new SimpleDateFormat("EEEE d MMMM", new Locale("fr", "FR")).format(date.getTime()) + " à 18 heures");
                    try {
                        Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `survie_loterie_gagnant`(`uuid`, `amount`, `hasRead`) VALUES (?, ?, ?)");
                        preparedStatement.setString(1, gagnants.get(0).getPlayer().toString());
                        preparedStatement.setDouble(2, getCagnotte());
                        preparedStatement.setBoolean(3, (Bukkit.getPlayer(gagnants.get(0).getPlayer()) != null));
                        preparedStatement.execute();

                        if (Bukkit.getPlayer(gagnants.get(0).getPlayer()) != null) {
                            Bukkit.getPlayer(gagnants.get(0).getPlayer()).sendMessage("§8[§3§lLoterie§8] §3Félicitations vous avez remporté le tirage d'aujourd'hui ! Vous remportez la somme de §b" + new CurrencyFormat().format(getCagnotte()) + " Ecu");
                            Survie.SURVIE.getAccountManager().giveMoney(gagnants.get(0).getPlayer(), getCagnotte());
                        }
                        preparedStatement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                this.cagnotte = 0;
                this.save();
            } else {
                tentative++;
                if (tentative == 4)
                    Bukkit.broadcastMessage("§8[§3§lLoterie§8] §cAucun gagnant cette semaine ! La cagnotte de " + new CurrencyFormat().format(getCagnotte()) + " Ecu est reconduite pour la semaine prochaine ! Prochain tirage le " + new SimpleDateFormat("EEEE d MMMM", new Locale("fr", "FR")).format(date.getTime()) + " à 18 heures");
            }
        }

        this.tickets.clear();
    }

    public void addTicket(Ticket ticket, boolean bdd) {
        this.tickets.add(ticket);
        if (!bdd) addCagnotte(getPriceTicket() / 2);
    }

    public int playerTicket(UUID uuid) {
        return (int) this.tickets.stream().filter(ticket -> ticket.getPlayer().toString().equals(uuid.toString())).count();
    }

    public Ticket getPlayerTicket(UUID uuid, int n) {
        return this.tickets.stream().filter(ticket -> ticket.getPlayer().toString().equals(uuid.toString())).collect(Collectors.toList()).get(n - 1);
    }

    public Ticket getPlayerTickets(UUID uuid, int nb1, int nb2, int nb3) {
        return this.tickets.stream().filter(ticket -> ticket.getPlayer().toString().equals(uuid.toString()) && ticket.getNumber1() == nb1 && ticket.getNumber2() == nb2 && ticket.getNumber3() == nb3).findFirst().orElse(null);
    }

    public double getPriceTicket() {
        return priceTicket;
    }

    public double getCagnotte() {
        return cagnotte;
    }
}
