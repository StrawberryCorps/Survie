package bzh.strawberry.survie.manager;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.util.SymbolUtils;
import bzh.strawberry.chest.api.StrawChest;
import bzh.strawberry.chest.api.utils.JsonItemStack;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.callback.Callback;
import bzh.strawberry.survie.claim.manager.data.Claim;
import bzh.strawberry.survie.jobs.manager.JobData;
import bzh.strawberry.survie.manager.request.TeleportRequest;
import bzh.strawberry.survie.shop.manager.data.ShopItem;
import bzh.strawberry.survie.utils.CurrencyFormat;
import bzh.strawberry.survie.utils.TimeUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;


/*
 * This file (SurviePlayer) is part of a project Survie.
 * It was created on 06/07/2020 11:13 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class SurviePlayer {

    private final Player player;
    private int survieID;
    private double balance;
    private final HashMap<String, Location> homes;
    private boolean tpToggle;
    private Location derniereMort;
    private final ArrayList<String> bienvenue;
    private Timestamp healTime;
    private Timestamp feedTime;
    private Timestamp repairTime;
    private String displayName;
    private String msgCo;
    private String msgDeco;
    private Timestamp pubTime;
    private SurviePlayer lastBienvenue;
    private boolean socialSpy;
    private boolean infoBlock;
    private int elid;

    private JobData jobData;

    private TeleportRequest teleportRequest;

    private boolean invsee = false;

    // SHOP ADMIN
    private ShopItem shopItem;

    public SurviePlayer(Player player) {
        this.player = player;
        this.homes = new HashMap<>();
        this.bienvenue = new ArrayList<>();
        this.socialSpy = false;
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUniqueID() {
        return player.getUniqueId();
    }

    public void loadAccount(Callback callback) {
        // On charge le compte de façon asynchrone afin de pas bloquer la connexion
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            // On essaye de voir si des lignes en BDD existent sinon on insert et on lui définie les valeurs par défaut
            Map<Integer, ItemStack> mapItemAClaim = new HashMap<>();

            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                // TODO
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM survie_player WHERE `elid` = '" + elid + "'");
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    // Le compte existe
                    survieID = resultSet.getInt("id");
                    tpToggle = resultSet.getBoolean("tpToggle");
                    healTime = resultSet.getTimestamp("healTime");
                    feedTime = resultSet.getTimestamp("feedTime");
                    repairTime = resultSet.getTimestamp("repairTime");
                    displayName = resultSet.getString("nickname");

                    if (displayName == null)
                        displayName = player.getDisplayName();
                    if (resultSet.getString("job") != null) {
                        String[] job = resultSet.getString("job").split(";");
                        jobData = new JobData(job[0], Double.parseDouble(job[1]), Double.parseDouble(job[2]), Integer.parseInt(job[3]), getPlayer());
                    }
                    msgCo = resultSet.getString("customjoin");
                    msgDeco = resultSet.getString("customleave");
                    pubTime = resultSet.getTimestamp("pubTime");

                    resultSet.close();
                    preparedStatement.close();

                    // On charge la list des Homes du joueurs
                    preparedStatement = connection.prepareStatement("SELECT * FROM survie_homes WHERE `player_id` = ?");
                    preparedStatement.setInt(1, survieID);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String[] location = resultSet.getString("location").split(";");
                        this.homes.put(resultSet.getString("home_name"), new Location(Survie.SURVIE.getServer().getWorld(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]), Double.parseDouble(location[3]), Float.parseFloat(location[4]), Float.parseFloat(location[5])));
                    }
                    resultSet.close();
                    preparedStatement.close();

                    //On charge les messages de bienvenue du joueur
                    preparedStatement = connection.prepareStatement("SELECT * FROM survie_bienvenue WHERE `player_id` = ?");
                    preparedStatement.setInt(1, survieID);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        this.bienvenue.add(resultSet.getString("msg"));
                    }
                    resultSet.close();
                    preparedStatement.close();

                    if (this.bienvenue.size() == 0) {
                        //Il n'a pas de msg de bvn
                        preparedStatement = connection.prepareStatement("SELECT * FROM survie_bienvenue WHERE `player_id` = ?");
                        preparedStatement.setInt(1, 0);
                        resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            this.bienvenue.add(resultSet.getString("msg"));
                        }
                        resultSet.close();
                        preparedStatement.close();
                    }

                    preparedStatement = connection.prepareStatement("SELECT * FROM survie_loterie_gagnant WHERE `uuid` = ?");
                    preparedStatement.setString(1, getUniqueID().toString());
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        if (!resultSet.getBoolean("hasRead")) {
                            getPlayer().sendMessage("§8[§3§lLoterie§8] §3Félicitations vous avez remporté le tirage du §b" + new SimpleDateFormat("EEEE d MMMM", new Locale("fr", "FR")).format(resultSet.getTimestamp("date")) + " §3! Vous remportez la somme de §b" + new CurrencyFormat().format(resultSet.getDouble("amount")) + " Ecu");
                            Survie.SURVIE.getAccountManager().giveMoney(getUniqueID(), resultSet.getDouble("amount"));
                            preparedStatement = connection.prepareStatement("UPDATE survie_loterie_gagnant SET hasRead = ? WHERE `id` = ?");
                            preparedStatement.setBoolean(1, true);
                            preparedStatement.setInt(2, resultSet.getInt("id"));
                            preparedStatement.executeUpdate();
                        }
                    }

                    preparedStatement = connection.prepareStatement("SELECT * FROM survie_cles WHERE `player_id` = ?");
                    preparedStatement.setInt(1, survieID);
                    resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {

                        ItemStack item = JsonItemStack.fromJson(resultSet.getString("item"));
                        if (item.getType() == Material.AIR) {
                            try {
                                PreparedStatement ps = connection.prepareStatement("DELETE FROM survie_cles WHERE `id` = ?");
                                ps.setInt(1, resultSet.getInt("id"));
                                ps.executeUpdate();
                            } catch (Exception e) {
                                player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue..." + e.getMessage());
                            }
                        }

                        mapItemAClaim.put(resultSet.getInt("id"), item);

                    }

                    resultSet.close();
                    preparedStatement.close();
                } else {
                    // Le compte existe pas on le créer
                    resultSet.close();
                    preparedStatement.close();
                    preparedStatement = connection.prepareStatement("INSERT INTO survie_player (`elid`) VALUES ('" + 1 /* @TODO */ + "')", Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.executeUpdate();
                    ResultSet resultSet1 = preparedStatement.getGeneratedKeys();
                    if (resultSet1.next())
                        survieID = resultSet1.getInt(1);
                    tpToggle = true;
                    displayName = player.getDisplayName();
                    resultSet1.close();
                    preparedStatement.close();

                    //Il n'a pas de msg de bvn
                    preparedStatement = connection.prepareStatement("SELECT * FROM survie_bienvenue WHERE `player_id` = ?");
                    preparedStatement.setInt(1, 0);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        this.bienvenue.add(resultSet.getString("msg"));
                    }
                    resultSet.close();
                    preparedStatement.close();
                }
                if (!mapItemAClaim.isEmpty())
                    player.sendMessage(StrawChest.getInstance().getPrefix() + "§7Vous avez des items en attente ! §e/cle");
                Survie.SURVIE.getItemToClaimManager().addPlayer(this.player, mapItemAClaim, "survie_cles", survieID);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, () -> player.kickPlayer("§cUne erreur est survenue (" + e.getMessage() + "). Si le problème persiste contactez un administrateur."));
            } finally {
                callback.done();
            }
        });
    }

    public void saveAccount(boolean sync) {
        // On sauvegarde les données de façon asynchrone afin de ne pas bloquer la deconnexion
        if (sync) {
            save();
        } else {
            Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, this::save);
        }
    }

    private void save() {
        try {
            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE survie_player SET `last_connexion`=?,`played_time`=?, `tpToggle`=?, `job`= ?, `healTime` = ?, `feedTime` = ?, `repairTime` = ?, `pubTime` = ?, `titre` = ?, `namemc` = ?, `parrainage` = ?, `parrainage_command` = ? WHERE `elid` = '" + this.elid + "'");
            preparedStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
            preparedStatement.setLong(2, getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 20); // Le temps est en secondes !
            preparedStatement.setBoolean(3, tpToggle);
            //enregistrement des timestamp des commandes à cooldown

            if (getJobData() != null)
                preparedStatement.setString(4, jobData.getName() + ";" + jobData.getTotalXP() + ";" + jobData.getHourXP() + ";" + jobData.getLevel());
            else
                preparedStatement.setNull(4, Types.VARCHAR);

            if (this.healTime != null)
                preparedStatement.setTimestamp(5, this.healTime);
            else
                preparedStatement.setNull(5, Types.TIMESTAMP);
            if (this.feedTime != null)
                preparedStatement.setTimestamp(6, this.feedTime);
            else
                preparedStatement.setNull(6, Types.TIMESTAMP);
            if (this.repairTime != null)
                preparedStatement.setTimestamp(7, this.repairTime);
            else
                preparedStatement.setNull(7, Types.TIMESTAMP);

            if (this.pubTime != null)
                preparedStatement.setTimestamp(8, this.pubTime);
            else
                preparedStatement.setNull(8, Types.TIMESTAMP);

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();

            System.out.println("[Survie] Compte de " + getPlayer().getName() + ":" + getSurvieID() + " sauvegardé");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getSurvieID() {
        return survieID;
    }

    public HashMap<String, Location> getHomes() {
        return homes;
    }

    public void addHome(String name, Location location) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                if (getHomes().containsKey(name)) {
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUn home est déjà défini avec ce nom !");
                } else {
                    Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO survie_homes (`player_id`, `home_name`, `location`) VALUES (?, ?, ?)");
                    preparedStatement.setInt(1, getSurvieID());
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch());

                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                    connection.close();
                    getHomes().put(name, location);
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§3Le home §9" + name + " §3a été créé !");
                }
            } catch (SQLException e) {
                getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de l'enregistrement de votre home !");
            }
        });
    }

    public void removeHome(String name) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                if (getHomes().containsKey(name)) {
                    Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `survie_homes` WHERE `player_id` = ? AND `home_name` = ?");
                    preparedStatement.setInt(1, getSurvieID());
                    preparedStatement.setString(2, name);

                    preparedStatement.execute();

                    preparedStatement.close();
                    connection.close();
                    homes.remove(name);
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§3Le home §9" + name + " §3a été supprimé !");
                } else
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez aucun home avec ce nom !");
            } catch (SQLException e) {
                getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de l'enregistrement de votre home !");
            }
        });
    }

    public boolean isInvsee() {
        return invsee;
    }

    public void setInvsee(boolean invsee) {
        this.invsee = invsee;
    }

    public void setNickname(final String nick) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            if (nick == null) {
                displayName = player.getDisplayName();
                try {
                    Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_player` SET `nickname` = ? WHERE `survie_player`.`id` = ?");
                    preparedStatement.setString(1, null);
                    preparedStatement.setInt(2, getSurvieID());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException e) {
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de la suppression de votre surnom ! (" + e.getMessage() + ")");
                }
            } else {
                String nickf = nick.replace('&', '§');
                String copieNick = nickf.replaceAll("§.", "");
                SurviePlayer copie = Survie.SURVIE.getSurviePlayer(Bukkit.getOfflinePlayer(copieNick).getUniqueId());

                if (copieNick.length() > 16)
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cLa taille maximale de votre surnom doit être 16 !");
                else if (!copieNick.matches("^[a-zA-Z0-9]+$")) //Uniquement des alpha num
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cVotre surnom doit contenir que des caractères alpha-numériques ! Actuel : '" + copieNick + "'");
                else if (copie != null && copie.getPlayer().getUniqueId() != getPlayer().getUniqueId()) {
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUn joueur existe avec ce pseudo !");
                } else {
                    displayName = nickf;
                    try {
                        Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_player` SET `nickname` = ? WHERE `survie_player`.`id` = ?");
                        preparedStatement.setString(1, nickf);
                        preparedStatement.setInt(2, getSurvieID());

                        preparedStatement.executeUpdate();

                        preparedStatement.close();
                        getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§7Votre surnom est désormais " + nickf);
                    } catch (SQLException e) {
                        getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de l'enregistrement de votre surnom ! (" + e.getMessage() + ")");
                    }
                }
            }
        });
    }

    public boolean isTpToggle() {
        return tpToggle;
    }

    public void setTpToggle(boolean tpToggle) {
        this.tpToggle = tpToggle;
    }

    public TeleportRequest getTeleportRequest() {
        return teleportRequest;
    }

    public void setTeleportRequest(TeleportRequest teleportRequest) {
        this.teleportRequest = teleportRequest;
    }

    public void addBienvenue(String msg) {
        try {
            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO survie_bienvenue (`player_id`, `msg`) VALUES (?, ?)");
            preparedStatement.setInt(1, getSurvieID());
            preparedStatement.setString(2, msg);

            preparedStatement.execute();

            preparedStatement.close();
            connection.close();
            getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§3Vous venez d'ajouter un message de bienvenue §e/b");
            this.bienvenue.add(msg);
        } catch (SQLException e) {
            getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de l'enregistrement de votre message de bienvenue !");
        }
    }

    public void removeBienvenue(int indice) {
        int idMessage = 0;

        if (!(0 <= indice && indice < this.bienvenue.size())) {
            player.sendMessage(Survie.SURVIE.getPrefix() + "§cCe message n'existe pas ! " + SymbolUtils.DEATH);
            return;
        }

        //on prend le message et le supprime de l'arraylist
        String message = this.bienvenue.remove(indice);

        try {
            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM survie_bienvenue WHERE `player_id` = ? AND `msg` = ?");
            preparedStatement.setInt(1, survieID);
            preparedStatement.setString(2, message);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                idMessage = resultSet.getInt("id");
            }
            resultSet.close();
            preparedStatement.close();

            if (idMessage != 0) {
                preparedStatement = connection.prepareStatement("DELETE FROM `survie_bienvenue` WHERE `id` = ?");
                preparedStatement.setInt(1, idMessage);
                preparedStatement.execute();

                preparedStatement.close();
                getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§3Le message de bienvenue a bien été supprimé !");
            }
            connection.close();
        } catch (SQLException e) {
            getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de la suppression de votre message de bienvenue !");
        }
    }

    public String[] getBienvenues() {
        String[] forceUnTabDeString = {};
        return this.bienvenue.toArray(forceUnTabDeString);
    }

    public void setDerniereMort(Location loc) {
        this.derniereMort = loc;
    }

    public void backCommande() {

        if (this.derniereMort != null) {
            Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, () -> getPlayer().teleport(this.derniereMort));
            getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§7Téléportation sur le lieu de votre mort");
        } else {
            getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cAucun lieu de mort connu ! " + SymbolUtils.DEATH);
        }

    }

    public void healCommande() {

        if (this.healTime != null) {

            Timestamp cpy = new Timestamp(this.healTime.getTime());

            if (player.hasPermission("survie.roi")) {
            } else if (player.hasPermission("survie.prince")) {
                cpy.setTime(cpy.getTime() + (3600 * 1000));     //1h
                if (cpy.getTime() - System.currentTimeMillis() > 0) {
                    this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour vous soigner.");
                    return;
                }
            } else if (player.hasPermission("survie.marquis")) {
                cpy.setTime(cpy.getTime() + (3600 * 1000 * 2)); //2h
                if (cpy.getTime() - System.currentTimeMillis() > 0) {
                    this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour vous soigner.");
                    return;
                }
            }

        }

        this.healTime = new Timestamp(System.currentTimeMillis());
        this.player.setHealth(this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
        this.player.setFoodLevel(20);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        this.player.setFireTicks(0);
        this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous venez d'être soigné.");

    }

    public void feedCommande() {

        if (this.feedTime != null) {

            Timestamp cpy = new Timestamp(this.feedTime.getTime());

            if (player.hasPermission("survie.roi")) {
            } else if (player.hasPermission("survie.prince")) {
                cpy.setTime(cpy.getTime() + (1800 * 1000));     //30 min
                if (cpy.getTime() - System.currentTimeMillis() > 0) {
                    this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour vous soigner.");
                    return;
                }
            } else if (player.hasPermission("survie.marquis")) {
                cpy.setTime(cpy.getTime() + (3600 * 1000));     //1h
                if (cpy.getTime() - System.currentTimeMillis() > 0) {
                    this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour vous soigner.");
                    return;
                }
            } else if (player.hasPermission("survie.comte") || !player.hasPermission("survie.comtepar")) {
                cpy.setTime(cpy.getTime() + (3600 * 1000 * 8));     //8h
                if (cpy.getTime() - System.currentTimeMillis() > 0) {
                    this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour vous soigner.");
                    return;
                }
            } else {
                cpy.setTime(cpy.getTime() + (3600 * 1000 * 24));     //24h
                if (cpy.getTime() - System.currentTimeMillis() > 0) {
                    this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour vous soigner.");
                    return;
                }
            }
        } else {
            this.feedTime = new Timestamp(System.currentTimeMillis());
        }

        this.player.setFoodLevel(20);
        this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous venez d'être rassasié");
        this.feedTime.setTime(System.currentTimeMillis());

    }

    public void repairCommande() {

        if (this.repairTime == null) {
            this.repairTime = new Timestamp(System.currentTimeMillis() - (3600 * 1000 * 48)); //enleve 48h
        }

        Timestamp cpy = new Timestamp(this.repairTime.getTime());
        ArrayList<ItemStack> listeItem = new ArrayList<ItemStack>();

        if (player.hasPermission("survie.roi")) {
            cpy.setTime(cpy.getTime() + (3600 * 1000));     //1h
            if (cpy.getTime() - System.currentTimeMillis() > 0) {
                this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour effectuer cette commande.");
                return;
            } else {
                //tout
                for (int i = 0; i <= 35; i++) {
                    listeItem.add(player.getInventory().getItem(i));
                }
                listeItem.add(player.getInventory().getHelmet());
                listeItem.add(player.getInventory().getChestplate());
                listeItem.add(player.getInventory().getLeggings());
                listeItem.add(player.getInventory().getBoots());
                listeItem.add(player.getInventory().getItemInOffHand());
            }
        } else if (player.hasPermission("survie.prince")) {
            cpy.setTime(cpy.getTime() + (3600 * 1000 * 3));     //3h
            if (cpy.getTime() - System.currentTimeMillis() > 0) {
                this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour effectuer cette commande.");
                return;
            } else {
                //tout sauf armure
                for (int i = 0; i <= 35; i++) {
                    listeItem.add(player.getInventory().getItem(i));
                }
            }
        } else if (player.hasPermission("survie.marquis")) {
            cpy.setTime(cpy.getTime() + (3600 * 1000));     //6h
            if (cpy.getTime() - System.currentTimeMillis() > 0) {
                this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour effectuer cette commande.");
                return;
            } else {
                //barre inventaire rapide
                for (int i = 0; i <= 8; i++) {
                    listeItem.add(player.getInventory().getItem(i));
                }
            }
        } else if (player.hasPermission("survie.comte") || player.hasPermission("survie.comtepar")) {
            cpy.setTime(cpy.getTime() + (3600 * 1000 * 8));     //8h
            if (cpy.getTime() - System.currentTimeMillis() > 0) {
                this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour effectuer cette commande.");
                return;
            } else {
                //la hot bar uniquement
                for (int i = 0; i <= 8; i++) {
                    listeItem.add(player.getInventory().getItem(i));
                }
            }
        } else {
            cpy.setTime(cpy.getTime() + (3600 * 1000 * 24));     //24h
            if (cpy.getTime() - System.currentTimeMillis() > 0) {
                this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour effectuer cette commande.");
                return;
            } else {
                //Item en main
                listeItem.add(player.getInventory().getItemInMainHand());
            }
        }

        reparerListe(listeItem);
        this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Réparation effectuée");
        this.repairTime.setTime(System.currentTimeMillis());
    }

    private void reparerListe(ArrayList<ItemStack> liste) {
        for (ItemStack item : liste) {
            if (item != null) {
                if (!item.getType().isBlock() && item.getDurability() != 0) {
                    item.setDurability((short) 0);
                }
            }
        }
    }

    public void addJob(JobData jobData) {
        this.jobData = jobData;
        try {
            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_player` SET `job` = ? WHERE `survie_player`.`id` = ?");
            preparedStatement.setString(1, jobData.getName() + ";" + jobData.getTotalXP() + ";" + jobData.getHourXP() + ";" + jobData.getLevel());
            preparedStatement.setInt(2, getSurvieID());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            player.sendMessage(Survie.SURVIE.getPrefix() + "§3Vous êtes désormais §9" + jobData.getName());
        } catch (SQLException e) {
            player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue ! (" + e.getMessage() + ")");
        }
        player.closeInventory();
    }

    public JobData getJobData() {
        return jobData;
    }

    public String getDisplayName() {
        return displayName;
    }

    // SHOP ADMIN

    public ShopItem getShopItem() {
        return shopItem;
    }

    public void setShopItem(ShopItem shopItem) {
        this.shopItem = shopItem;
    }

    public void ajoutMessageCo(final String msg, boolean connexion) {
        //connexion = true
        //deco = false

        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            if (msg == null) {
                displayName = player.getDisplayName();
                if ((connexion && this.msgCo != null) || (!connexion && this.msgDeco != null)) {
                    try {
                        Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                        PreparedStatement preparedStatement;
                        if (connexion)
                            preparedStatement = connection.prepareStatement("UPDATE `survie_player` SET `customjoin` = ? WHERE `survie_player`.`id` = ?");
                        else
                            preparedStatement = connection.prepareStatement("UPDATE `survie_player` SET `customleave` = ? WHERE `survie_player`.`id` = ?");

                        preparedStatement.setString(1, null);
                        preparedStatement.setInt(2, getSurvieID());
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        connection.close();
                        getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§8Vous avez supprimé votre message de " + (connexion ? "connexion" : "déconnexion"));

                    } catch (SQLException e) {
                        getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de la suppression de votre message ! (" + e.getMessage() + ")");
                    }
                    if (connexion)
                        this.msgCo = null;
                    else
                        this.msgDeco = null;
                } else {
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez pas de message de " + (connexion ? "connexion" : "déconnexion") + SymbolUtils.DEATH);
                }
            } else {
                String msgf = msg.replace('&', '§');
                String copieMsg = msgf.replaceAll("§.", "");

                if (copieMsg.length() > 200)
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cLa taille maximale de votre message doit être 200 !");
                else {
                    try {
                        Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                        PreparedStatement preparedStatement;
                        if (connexion) {
                            preparedStatement = connection.prepareStatement("UPDATE `survie_player` SET `customjoin` = ? WHERE `survie_player`.`id` = ?");
                            this.msgCo = msgf;
                        } else {
                            preparedStatement = connection.prepareStatement("UPDATE `survie_player` SET `customleave` = ? WHERE `survie_player`.`id` = ?");
                            this.msgDeco = msgf;
                        }

                        preparedStatement.setString(1, msgf);
                        preparedStatement.setInt(2, getSurvieID());

                        preparedStatement.executeUpdate();

                        preparedStatement.close();
                        connection.close();
                        getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§7Votre message de " + (connexion ? "connexion" : "déconnexion") + " est désormais " + flagsMsgCo(msgf));
                    } catch (SQLException e) {
                        getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de l'enregistrement de votre message ! (" + e.getMessage() + ")");
                    }
                }
            }
        });

    }

    public String getMsgCo() {
        return flagsMsgCo(this.msgCo);
    }

    public String getMsgDeco() {
        return flagsMsgCo(this.msgDeco);
    }

    private String flagsMsgCo(String ret) {
        if (ret != null) {
            ret = ret.replaceAll("\\(money\\)", new CurrencyFormat().format(this.balance));
            ret = ret.replaceAll("\\(time\\)", String.valueOf(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20 / 3600));
            ret = ret.replaceAll("\\(kill\\)", String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)));
            ret = ret.replaceAll("\\(pseudo\\)", this.displayName);
            ret = ret.replaceAll("\\(mort\\)", String.valueOf(player.getStatistic(Statistic.DEATHS)));
            ret = ret.replaceAll("\\(compteur\\)", String.valueOf(player.getStatistic(Statistic.LEAVE_GAME)));
            ret = ret.replaceAll("\\(monstres\\)", String.valueOf(player.getStatistic(Statistic.MOB_KILLS)));
        }
        return ret;
    }

    public void afficherPub(final String message) {

        if (this.pubTime != null) {

            Timestamp cpy = new Timestamp(this.pubTime.getTime());

            if (player.hasPermission("survie.ROI")) {
                cpy.setTime(cpy.getTime() + (3600 * 1000));     //1h
                if (cpy.getTime() - System.currentTimeMillis() > 0) {
                    this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour diffuser ce message.");
                    return;
                }
            } else if (player.hasPermission("survie.PRINCE")) {
                cpy.setTime(cpy.getTime() + (3600 * 1000 * 4)); //4h
                if (cpy.getTime() - System.currentTimeMillis() > 0) {
                    this.player.sendMessage(Survie.SURVIE.getPrefix() + "§7Vous devez encore attendre " + TimeUtils.getDuration((cpy.getTime() - System.currentTimeMillis()) / 1000) + " pour diffuser ce message.");
                    return;
                }
            }

        }

        this.pubTime = new Timestamp(System.currentTimeMillis());
        Bukkit.broadcastMessage("§3➢ §9[" + player.getName() + "]§a " + message);
    }

    public void setLastBienvenue(SurviePlayer pl) {
        this.lastBienvenue = pl;
    }

    public SurviePlayer getLastBienvenue() {
        return this.lastBienvenue;
    }

    public boolean teleport = false;

    public void randomTeleport(World world) {
        this.teleport = true;
        int i = 0;
        while (i < 20 && teleport) {

            int x, z;

            int limite = (int) ((world.getWorldBorder().getSize() - 50) / 2);

            x = new Random().nextInt(limite) * (new Random().nextBoolean() ? -1 : 1);
            z = new Random().nextInt(limite) * (new Random().nextBoolean() ? -1 : 1);


            double y = 255;
            y = world.getHighestBlockYAt(x, z);
            Location location = new Location(world, x + 0.5, y, z + 0.5);
            Location TPy = new Location(location.getWorld(), location.getX(), location.getY() - 1.0, location.getZ());
            if (!location.getWorld().getBlockAt(location).getType().equals(Material.LAVA) || !TPy.getWorld().getBlockAt(TPy).getType().equals(Material.LAVA) || !TPy.getWorld().getBlockAt(TPy).getType().equals(Material.WATER) || !TPy.getWorld().getBlockAt(TPy).getType().equals(Material.AIR)) {
                this.teleport = false;

                if (!location.getChunk().isLoaded())
                    location.getChunk().load();

                Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, () -> getPlayer().teleport(location));
                player.sendMessage(Survie.SURVIE.getPrefix() + "§7Téléportation au monde " + world.getName());
            }
            i++;
        }
        if (i == 20) {
            this.teleport = false;
            getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de la téléportation " + SymbolUtils.DEATH);
        }
    }

    public Timestamp getRepairTime() {
        return this.repairTime;
    }

    public void changerHome(String name, Location nouvelle) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                if (getHomes().containsKey(name)) {
                    Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_homes` SET `location`= ? WHERE `player_id` = ? AND `home_name` = ?");
                    preparedStatement.setString(1, nouvelle.getWorld().getName() + ";" + nouvelle.getX() + ";" + nouvelle.getY() + ";" + nouvelle.getZ() + ";" + nouvelle.getYaw() + ";" + nouvelle.getPitch());
                    preparedStatement.setInt(2, getSurvieID());
                    preparedStatement.setString(3, name);

                    preparedStatement.execute();

                    preparedStatement.close();
                    connection.close();
                    homes.remove(name);
                    homes.put(name, nouvelle);
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§3Le home §9" + name + " §3a été changé !");
                } else
                    getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cVous n'avez aucun home avec ce nom !");
            } catch (SQLException e) {
                getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de l'enregistrement de votre home !");
            }
        });
    }

    public boolean getSocialSpy() {
        return this.socialSpy;
    }

    public void setSocialSpy(boolean nv) {
        this.socialSpy = nv;
    }

    public void setInfoBlock(boolean b) {
        this.infoBlock = b;
    }

    public boolean getInfoBlock() {
        return this.infoBlock;
    }

    public void tpVersZoneClaimable() {

        World world = Bukkit.getWorld("Survie");
        this.teleport = true;
        boolean peutClaim = true;
        int i = 0;

        while (i < 20 && teleport) {

            int x, z;

            int limite = (int) ((world.getWorldBorder().getSize() - 50) / 2);

            x = new Random().nextInt(limite) * (new Random().nextBoolean() ? -1 : 1);
            z = new Random().nextInt(limite) * (new Random().nextBoolean() ? -1 : 1);

            double y = world.getHighestBlockYAt(x, z);
            Location location = new Location(world, x + 0.5, y, z + 0.5);
            Location TPy = new Location(location.getWorld(), location.getX(), location.getY() - 1.0, location.getZ());
            if (!location.getWorld().getBlockAt(location).getType().equals(Material.LAVA) || !TPy.getWorld().getBlockAt(TPy).getType().equals(Material.LAVA) || !TPy.getWorld().getBlockAt(TPy).getType().equals(Material.WATER) || !TPy.getWorld().getBlockAt(TPy).getType().equals(Material.AIR)) {

                for (Claim c : Survie.SURVIE.getClaimManager().getClaims()) {
                    peutClaim = !c.getCuboid().isInWithMarge(location, 200.0);
                    if (!peutClaim)
                        break;

                }

                this.teleport = false;

                if (!location.getChunk().isLoaded())
                    location.getChunk().load();

                Survie.SURVIE.getServer().getScheduler().runTask(Survie.SURVIE, () -> getPlayer().teleport(location));
                player.sendMessage(Survie.SURVIE.getPrefix() + "§7Téléportation au monde " + world.getName());
            }
            i++;
        }
        if (i == 20) {
            this.teleport = false;
            getPlayer().sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de la téléportation " + SymbolUtils.DEATH);
        }
    }
}