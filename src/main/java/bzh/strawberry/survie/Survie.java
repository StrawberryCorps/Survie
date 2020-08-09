package bzh.strawberry.survie;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.chest.api.manager.ItemToClaimManager;
import bzh.strawberry.survie.claim.commands.admin.ClaimAdminCommand;
import bzh.strawberry.survie.claim.commands.player.ClaimCommand;
import bzh.strawberry.survie.claim.commands.staff.ClaimStaffCommand;
import bzh.strawberry.survie.claim.manager.ClaimManager;
import bzh.strawberry.survie.commands.admin.*;
import bzh.strawberry.survie.commands.player.*;
import bzh.strawberry.survie.commands.staff.*;
import bzh.strawberry.survie.economy.Account;
import bzh.strawberry.survie.jobs.commands.JobsCommand;
import bzh.strawberry.survie.jobs.listeners.entity.EntityDeath;
import bzh.strawberry.survie.jobs.listeners.player.PlayerFish;
import bzh.strawberry.survie.kit.KitManager;
import bzh.strawberry.survie.listeners.block.BlockBreak;
import bzh.strawberry.survie.listeners.block.BlockPlace;
import bzh.strawberry.survie.listeners.entity.EntityDamage;
import bzh.strawberry.survie.listeners.entity.EntityExplode;
import bzh.strawberry.survie.listeners.entity.EntitySpawn;
import bzh.strawberry.survie.listeners.food.FoodLevelChange;
import bzh.strawberry.survie.listeners.player.*;
import bzh.strawberry.survie.listeners.sign.SignChange;
import bzh.strawberry.survie.listeners.weather.WeatherChange;
import bzh.strawberry.survie.loterie.commands.LoterieCommand;
import bzh.strawberry.survie.loterie.manager.LoterieManager;
import bzh.strawberry.survie.manager.SurviePlayer;
import bzh.strawberry.survie.playershop.ShopPlayerManager;
import bzh.strawberry.survie.shop.commands.admin.AdminShopCommand;
import bzh.strawberry.survie.shop.listener.AdminAdd;
import bzh.strawberry.survie.shop.listener.ShopPlayer;
import bzh.strawberry.survie.shop.manager.ShopManager;
import bzh.strawberry.survie.task.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/*
 * This file (Survie) is part of a project Survie.
 * It was created on 17/07/2020 19:46 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class Survie extends JavaPlugin {

    public static Survie SURVIE;

    public ArrayList<SurviePlayer> surviePlayers;

    private ClaimManager claimManager;
    private KitManager kitManager;
    private ShopManager shopManager;

    private AwayTask awayTask;
    private HashMap<String, Location> warps;

    private LoterieManager lotterieManager;
    private ItemToClaimManager itemToClaimManager;
    private Account accountManager;

    private ShopPlayerManager shopPlayerManager;
    public int sleepingPlayer;

    @Override
    public void onEnable() {
        SURVIE = this;
        long tick = System.currentTimeMillis();

        getLogger().info("######################## [Survie-" + getDescription().getVersion() + "] #################################");
        getLogger().info("Chargement du Survie....");

        getLogger().info("Chargement des listeners...");
        getServer().getPluginManager().registerEvents(new BlockBreak(this), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamage(this), this);
        getServer().getPluginManager().registerEvents(new EntityExplode(this), this);
        getServer().getPluginManager().registerEvents(new EntitySpawn(this), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChange(this), this);
        getServer().getPluginManager().registerEvents(new PlayerBucket(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDie(this), this);
        getServer().getPluginManager().registerEvents(new WeatherChange(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
        getServer().getPluginManager().registerEvents(new SignChange(this), this);
        getServer().getPluginManager().registerEvents(new PlayerPortal(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
        getServer().getPluginManager().registerEvents(new bzh.strawberry.survie.jobs.listeners.block.BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new bzh.strawberry.survie.jobs.listeners.block.BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new PlayerFish(), this);
        getServer().getPluginManager().registerEvents(new EntityDeath(), this);
        getServer().getPluginManager().registerEvents(new ShopPlayer(), this);
        getServer().getPluginManager().registerEvents(new AdminAdd(), this);
        getLogger().info("Chargement des listeners terminé...");

        getLogger().info("Chargement des commandes...");
        // ADMIN
        Objects.requireNonNull(getServer().getPluginCommand("sudo")).setExecutor(new SudoCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("tpall")).setExecutor(new TpAllCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("tpo")).setExecutor(new TpoCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("tpohere")).setExecutor(new TpoHereCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("claimadmin")).setExecutor(new ClaimAdminCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("shopadmin")).setExecutor(new AdminShopCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("warpadmin")).setExecutor(new AdminWarpCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("homeadmin")).setExecutor(new HomeAdminCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("statadmin")).setExecutor(new StatsAdminCommand(this));
        // STAFF
        Objects.requireNonNull(getServer().getPluginCommand("tp")).setExecutor(new TpCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("tphere")).setExecutor(new TpHereCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("tptoggle")).setExecutor(new TpToggleCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("invsee")).setExecutor(new InvseeCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("enderchest")).setExecutor(new EnderchestCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("goto")).setExecutor(new GoToCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("claimmod")).setExecutor(new ClaimStaffCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("infoblock")).setExecutor(new InfoBlockCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("near")).setExecutor(new NearCommand(this));
        // JOUEUR
        Objects.requireNonNull(getServer().getPluginCommand("spawn")).setExecutor(new SpawnCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("bienvenue")).setExecutor(new BienvenueCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("hat")).setExecutor(new HatCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("home")).setExecutor(new HomeCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("kit")).setExecutor(new KitCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("nick")).setExecutor(new NickCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("sethome")).setExecutor(new SetHomeCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("delhome")).setExecutor(new DelHomeCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("tpa")).setExecutor(new TpaCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("tpahere")).setExecutor(new TpaHereCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("tpno")).setExecutor(new TpNoCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("tpyes")).setExecutor(new TpYesCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("warp")).setExecutor(new WarpCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("jobs")).setExecutor(new JobsCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("claim")).setExecutor(new ClaimCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("craft")).setExecutor(new CraftCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("ptime")).setExecutor(new PtimeCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("temps")).setExecutor(new TempsCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("back")).setExecutor(new BackCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("heal")).setExecutor(new HealCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("feed")).setExecutor(new FeedCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("repair")).setExecutor(new RepairCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("extinguish")).setExecutor(new ExtinguishCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("experience")).setExecutor(new ExperienceCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("customjoin")).setExecutor(new CustomJoinCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("customleave")).setExecutor(new CustomLeaveCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("pub")).setExecutor(new PubCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("tprandom")).setExecutor(new TprandomCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("loterie")).setExecutor(new LoterieCommand());
        Objects.requireNonNull(getServer().getPluginCommand("trash")).setExecutor(new TrashCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("jeveuxunclaim")).setExecutor(new JeVeuxUnClaimCommand(this));
        getLogger().info("Chargement des commandes terminé...");

        getLogger().info("Chargement du monde Survie...");
        World survieWorld = getServer().createWorld(new WorldCreator("Survie"));
        survieWorld.setAutoSave(true);
        World ressourcesWorld = getServer().createWorld(new WorldCreator("Ressources"));
        ressourcesWorld.setAutoSave(true);
        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("announceAdvancements", "false");
            world.setGameRuleValue("doFireTick", "false");
            world.setDifficulty(Difficulty.HARD);
        }

        getLogger().info("Chargement du monde Survie terminé..");

        getLogger().info("Chargement des claims...");
        this.claimManager = new ClaimManager();
        getLogger().info("Chargement des claims terminé...");

        getLogger().info("Chargement du shop...");
        this.shopManager = new ShopManager();
        getLogger().info("Chargement du shop terminé...");

        getLogger().info("Chargement de l'économie...");
        this.accountManager = new Account(this);
        getLogger().info("Chargement de l'économie terminé...");

        getLogger().info("Chargement de la lotterie...");
        this.lotterieManager = new LoterieManager();
        getLogger().info("Chargement de la lotterie terminé...");

        getLogger().info("Chargement des warps...");
        this.warps = new HashMap<>();
        try {
            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM survie_warps");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String[] location = resultSet.getString("location").split(";");
                warps.put(resultSet.getString("name"), new Location(Survie.SURVIE.getServer().getWorld(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]), Double.parseDouble(location[3]), Float.parseFloat(location[4]), Float.parseFloat(location[5])));
                getLogger().info("    -> Ajout Warp: " + resultSet.getString("name"));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            getServer().shutdown();
        }
        getLogger().info("Chargement des warps terminé...");

        getLogger().info("Chargement des runnables...");
        getServer().getScheduler().runTaskTimerAsynchronously(this, new ScoreboardTask(this), 0, 20);
        getServer().getScheduler().runTaskTimerAsynchronously(this, new JobTask(this), 0, 20);
        getServer().getScheduler().runTaskAsynchronously(this, new LoterieTask(this));
        getServer().getScheduler().runTaskTimer(this, new GUIRefreshTask(this), 0, 20);
        awayTask = new AwayTask();
        getServer().getScheduler().runTaskTimer(this, awayTask, 0, 20);
        getLogger().info("Chargement des runnables terminé...");

        getLogger().info("Chargement des kits...");
        this.kitManager = new KitManager();
        getLogger().info("Chargement des kits terminé...");

        getLogger().info("Chargement des coffres...");


        getLogger().info("Chargement des coffres terminé...");

        getLogger().info("Chargement des shops joueurs...");
        this.shopPlayerManager = new ShopPlayerManager();
        getLogger().info("Chargement des shops joueurs terminé...");

        getLogger().info("Chargement du module de claim des clés...");
        this.itemToClaimManager = ItemToClaimManager.getInstance();
        getLogger().info("Chargement du module de claim des clés terminé...");
        this.surviePlayers = new ArrayList<>();
        this.sleepingPlayer = 0;

        getLogger().info("Chargement du Survie effectué en " + (System.currentTimeMillis() - tick) + " ms.");
        getLogger().info("######################## [Survie-" + getDescription().getVersion() + "] #################################");
    }

    @Override
    public void onDisable() {
        this.lotterieManager.save();
        this.surviePlayers.forEach(surviePlayer -> surviePlayer.saveAccount(true));
    }

    public ShopPlayerManager getShopPlayerManager() {
        return shopPlayerManager;
    }

    public String getPrefix() {
        return "§8[§9Survie§8]§r ";
    }

    public SurviePlayer getSurviePlayer(UUID uuid) {
        return this.surviePlayers.stream().filter(surviePlayer -> surviePlayer.getUniqueID().equals(uuid)).findFirst().orElse(null);
    }

    public AwayTask getAwayTask() {
        return awayTask;
    }

    public HashMap<String, Location> getWarps() {
        return warps;
    }

    public LoterieManager getLotterieManager() {
        return lotterieManager;
    }

    public void addWarp(String name, Location location, Player player) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                if (getWarps().containsKey(name)) {
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cUn warp est déjà défini avec ce nom !");
                } else {
                    Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO survie_warps (`name`, `location`) VALUES (?, ?)");
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch());

                    preparedStatement.executeUpdate();

                    preparedStatement.close();
                    connection.close();
                    warps.put(name, location);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§3Le warp §9" + name + " §3a été créé !");
                }
            } catch (SQLException e) {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de l'enregistrement du warp !");
            }
        });
    }

    public void removeWarp(String name, Player player) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                if (getWarps().containsKey(name)) {
                    Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `survie_warps` WHERE `name` = ?");
                    preparedStatement.setString(1, name);

                    preparedStatement.execute();

                    preparedStatement.close();
                    connection.close();
                    warps.remove(name);
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§3Le warp §9" + name + " §3a été supprimé !");
                } else
                    player.sendMessage(Survie.SURVIE.getPrefix() + "§cIl n'y a aucun warp avec ce nom !");
            } catch (SQLException e) {
                player.sendMessage(Survie.SURVIE.getPrefix() + "§cUne erreur est survenue lors de la suppression du warp !");
            }
        });
    }

    public ClaimManager getClaimManager() {
        return claimManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public int getSurvieIdByUUID(UUID uuid) {
        int ret = -1;
        try {
            Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT survie_player.id AS survie_player_id FROM survie_player WHERE uuid = ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                ret = resultSet.getInt("survie_player_id");
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public Account getAccountManager() {
        return accountManager;
    }

    public ItemToClaimManager getItemToClaimManager() {
        return this.itemToClaimManager;
    }

    public void addSleepingPlayer() {
        this.sleepingPlayer++;
        if ((this.surviePlayers.size() / 3) < this.sleepingPlayer) {
            for (World world : Bukkit.getWorlds()) {
                world.setTime(1000);
            }
            Bukkit.broadcastMessage(Survie.SURVIE.getPrefix() + "§7La nuit a été passée !");
        }

    }

    public void removeSleepingPlayer() {
        if (this.sleepingPlayer > 0)
            this.sleepingPlayer--;
    }
}
