package bzh.strawberry.survie.claim.manager.data;

import bzh.strawberry.api.StrawAPI;
import bzh.strawberry.api.world.Cuboid;
import bzh.strawberry.survie.Survie;
import bzh.strawberry.survie.claim.manager.rank.ClaimRank;
import bzh.strawberry.survie.manager.SurviePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/*
 * This file (Claim) is part of a project Survie.
 * It was created on 09/07/2020 10:51 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class Claim {

    private final int claimId; // ID unique du claim
    private UUID owner;
    private final List<ClaimMember> claimMembers;
    private Cuboid cuboid;
    private final World world;

    private Location home;
    private String name;
    private boolean lock;

    private final Map<UUID, Long> invitations;
    private final List<UUID> bannis;
    private final Map<UUID, UUID> coop;   //Map sous la forme : Joueur Coop, membre du claim qui a coop

    private int taxe;
    private int taxe_etat;
    private double bank;
    private int taille;

    private String bienvenue;
    private String farewell;
    private Location warp;

    public Claim(int claimId, UUID owner, Cuboid cuboid, int taxe, double bank, int taxe_etat, int taille, String bienvenue, String adieu) {
        this.claimId = claimId;
        this.owner = owner;
        this.cuboid = cuboid;
        this.world = cuboid.getPos1().getWorld();
        this.taxe = taxe;
        this.bank = bank;
        this.taille = taille;
        this.taxe_etat = taxe_etat;
        this.claimMembers = new ArrayList<>();
        this.invitations = new HashMap<>();
        this.bannis = new ArrayList<>();
        this.coop = new HashMap<>();
        this.bienvenue = bienvenue;
        this.farewell = adieu;
    }

    public int getClaimId() {
        return claimId;
    }

    public UUID getOwner() {
        return owner;
    }

    public ClaimRank getRank(SurviePlayer surviePlayer) {
        ClaimRank claimRank;
        if (owner.equals(surviePlayer.getUniqueID()))
            claimRank = ClaimRank.DUC;
        else
            claimRank = claimMembers.stream().filter(claimMember -> claimMember.getUuidMember().equals(surviePlayer.getUniqueID())).findFirst().orElse(null).getClaimRank();
        return claimRank;
    }

    public List<ClaimMember> getClaimMembers() {
        return claimMembers;
    }

    public String getMembers() {
        StringBuilder str = new StringBuilder();
        for (ClaimMember claimMember : claimMembers) {
            str.append(Bukkit.getOfflinePlayer(claimMember.getUuidMember()).getName()).append(", ");
        }
        if (str.toString().length() > 2)
            str = new StringBuilder(str.toString().substring(0, str.toString().length() - 2));
        return str.toString();
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public void addClaimMember(ClaimMember claimMember, boolean toBdd) {
        if (toBdd) {
            Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
                try {
                    Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO survie_claims_member (`member`, `claimID`, `role`) VALUES (?, ?, ?)");
                    preparedStatement.setInt(1, Survie.SURVIE.getSurviePlayer(claimMember.getUuidMember()).getSurvieID());
                    preparedStatement.setInt(2, claimId);
                    preparedStatement.setString(3, claimMember.getClaimRank().name());
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        this.claimMembers.add(claimMember);
    }

    public void removClaimMember(ClaimMember claimMember, boolean toBdd) {
        if (toBdd) {
            Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
                try {
                    Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `survie_claims_member` WHERE `member` = ? AND `claimID` = ?");
                    preparedStatement.setInt(1, Survie.SURVIE.getSurvieIdByUUID(claimMember.getUuidMember()));
                    preparedStatement.setInt(2, claimId);
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        this.claimMembers.remove(claimMember);
    }

    public void changeRole(ClaimMember claimMember, ClaimRank claimRank) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims_member` SET `role` = ? WHERE `member` = ? AND `claimID` = ?");
                preparedStatement.setString(1, claimRank.name());
                preparedStatement.setInt(2, Survie.SURVIE.getSurvieIdByUUID(claimMember.getUuidMember()));
                preparedStatement.setInt(3, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
                claimMembers.remove(claimMember);
                claimMembers.add(new ClaimMember(claimMember.getUuidMember(), claimRank));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void changeOwner(ClaimMember member) {
        removClaimMember(member, true);
        addClaimMember(new ClaimMember(this.owner, ClaimRank.MARQUIS), true);
        this.owner = member.getUuidMember();
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `owner` = ? WHERE `id` = ?");
                preparedStatement.setInt(1, Survie.SURVIE.getSurvieIdByUUID(this.owner));
                preparedStatement.setInt(2, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public ClaimMember getMember(UUID uuid) {
        ClaimMember claimMember;
        if (owner.equals(uuid))
            claimMember = new ClaimMember(uuid, ClaimRank.DUC);
        else
            claimMember = claimMembers.stream().filter(cm -> cm.getUuidMember().equals(uuid)).findFirst().orElse(null);
        return claimMember;
    }

    public boolean isOnClaim(UUID uuid) {
        boolean ret = false;
        if (uuid.equals(this.owner)) ret = true;
        else
            for (ClaimMember claimMember : claimMembers) {
                if (claimMember.getUuidMember().equals(uuid))
                    ret = true;
            }
        if (!ret)
            ret = this.coop.containsKey(uuid);
        return ret;
    }

    public String getName() {
        if (name != null)
            return name;
        return Bukkit.getOfflinePlayer(owner).getName();
    }

    public boolean hasCustomName() {
        return name != null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("REPLACE INTO survie_claims_data (claim_id, home, locked, name) VALUES (?, ?, ?, ?)");
                preparedStatement.setInt(1, claimId);
                preparedStatement.setString(2, (home.getX() + ";" + home.getY() + ";" + home.getZ() + ";" + home.getYaw() + ";" + home.getPitch()));
                preparedStatement.setBoolean(3, lock);
                preparedStatement.setString(4, name);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        this.home = home;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("REPLACE INTO survie_claims_data (claim_id, locked, home, name) VALUES (?, ?, ?, ?)");
                preparedStatement.setInt(1, claimId);
                preparedStatement.setBoolean(2, lock);
                preparedStatement.setString(3, (home.getX() + ";" + home.getY() + ";" + home.getZ() + ";" + home.getYaw() + ";" + home.getPitch()));
                preparedStatement.setString(4, name);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        this.lock = lock;
    }

    public int getMaxMember() {
        int nb = 3;
        if (Bukkit.getPlayer(owner) != null) {
            if (Bukkit.getPlayer(owner).hasPermission("survie.roi"))
                nb = 12;
            else if (Bukkit.getPlayer(owner).hasPermission("survie.prince"))
                nb = 9;
            else if (Bukkit.getPlayer(owner).hasPermission("survie.marquis"))
                nb = 7;
            else if (Bukkit.getPlayer(owner).hasPermission("survie.comte"))
                nb = 5;
        }
        return nb;
    }

    public Map<UUID, Long> getInvitations() {
        return invitations;
    }

    public void addBan(UUID target, boolean toBDD) {
        if (toBDD) {
            Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
                int id = Survie.SURVIE.getSurvieIdByUUID(target);
                //Survie.SURVIE.getServer().getLogger().info("        -> [Claim] ajout en bdd de l'id pour ban "+ id + "nick : " + Bukkit.getOfflinePlayer(target).getName());
                try {
                    Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO survie_claims_bans (player_id, claim_id) VALUES (?, ?)");
                    preparedStatement.setInt(1, id);
                    preparedStatement.setInt(2, claimId);
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        this.bannis.add(target);
    }

    public void removeBan(UUID target, boolean toBDD) {
        if (toBDD) {
            Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
                int id = Survie.SURVIE.getSurvieIdByUUID(target);
                try {
                    Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `survie_claims_bans` WHERE `player_id` = ? AND `claim_id` = ?");
                    preparedStatement.setInt(1, id);
                    preparedStatement.setInt(2, claimId);
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        this.bannis.remove(target);
    }

    public double getBank() {
        return bank;
    }

    public double getTaxe() {
        return (double) taxe / 100;
    }

    public List<UUID> getBannis() {
        return bannis;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "claimId=" + claimId +
                ", owner=" + owner +
                ", claimMembers=" + claimMembers +
                ", cuboid=" + cuboid +
                ", world=" + world +
                ", bienvenue=" + bienvenue +
                ", farewell=" + farewell +
                ", warp=" + warp +
                '}';
    }

    public void addBank(double i) {
        this.bank += i;
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `bank` = ? WHERE `id` = ?");
                preparedStatement.setDouble(1, this.bank);
                preparedStatement.setInt(2, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void removeBank(int i) {
        this.bank -= i;
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `bank` = ? WHERE `id` = ?");
                preparedStatement.setDouble(1, this.bank);
                preparedStatement.setInt(2, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void addTaxe(int i) {
        this.taxe += i;
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `taxe` = ? WHERE `id` = ?");
                preparedStatement.setInt(1, this.taxe);
                preparedStatement.setInt(2, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void removeTaxe(int i) {
        this.taxe -= i;
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `taxe` = ? WHERE `id` = ?");
                preparedStatement.setInt(1, this.taxe);
                preparedStatement.setInt(2, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public Map<UUID, UUID> getCoopMembers() {
        return this.coop;
    }

    public void coop(UUID uuid, UUID claimMember) {
        this.coop.put(uuid, claimMember);
    }

    public void uncoop(UUID uuid) {
        this.coop.remove(uuid);
    }

    public boolean isMember(UUID uuid) {
        boolean ret = false;
        if (uuid.equals(this.owner)) ret = true;
        else
            for (ClaimMember claimMember : claimMembers) {
                if (claimMember.getUuidMember().equals(uuid))
                    ret = true;
            }
        return ret;
    }

    public void setBank(double i) {
        this.bank = i;
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `bank` = ? WHERE `id` = ?");
                preparedStatement.setDouble(1, this.bank);
                preparedStatement.setInt(2, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Taille 62/75/87/100
    public void agrandirClaim(int z) {
        if (z == 1) {
            this.cuboid = Cuboid.fromCenter(this.cuboid.getCenter(), 62);
            setTaille(1);
            setTaxeEtat(14);
        } else if (z == 2) {
            this.cuboid = Cuboid.fromCenter(this.cuboid.getCenter(), 75);
            setTaille(2);
            setTaxeEtat(17);
        } else if (z == 3) {
            this.cuboid = Cuboid.fromCenter(this.cuboid.getCenter(), 87);
            setTaille(3);
            setTaxeEtat(20);
        } else if (z == 4) {
            this.cuboid = Cuboid.fromCenter(this.cuboid.getCenter(), 100);
            setTaille(4);
            setTaxeEtat(23);
        }

        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `cuboid` = ? WHERE `id` = ?");
                preparedStatement.setString(1, cuboid.getPos1().getX() + ";" + cuboid.getPos1().getY() + ";" + cuboid.getPos1().getZ() + ";" + cuboid.getPos2().getX() + ";" + cuboid.getPos2().getY() + ";" + cuboid.getPos2().getZ());
                preparedStatement.setInt(2, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public int getTaxeEtat() {
        return taxe_etat;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `taille` = ? WHERE `id` = ?");
                preparedStatement.setInt(1, this.taille);
                preparedStatement.setInt(2, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void setTaxeEtat(int taxe_etat) {
        this.taxe_etat = taxe_etat;
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `taxe_etat` = ? WHERE `id` = ?");
                preparedStatement.setInt(1, this.taxe_etat);
                preparedStatement.setInt(2, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void setBienvenue(String msg) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `welcome` = ? WHERE `id` = ?");
                if (msg != null)
                    preparedStatement.setString(1, msg);
                else
                    preparedStatement.setNull(1, Types.VARCHAR);
                preparedStatement.setInt(2, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        this.bienvenue = msg;
    }

    public String getBienvenue() {
        return this.bienvenue;
    }

    public void setFarewell(String msg) {
        Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
            try {
                Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `farewell` = ? WHERE `id` = ?");
                if (msg != null)
                    preparedStatement.setString(1, msg);
                else
                    preparedStatement.setNull(1, Types.VARCHAR);
                preparedStatement.setInt(2, claimId);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        this.farewell = msg;
    }

    public String getFarewell() {
        return this.farewell;
    }

    public void setWarp(Location warp, boolean toBDD) {

        if (toBDD) {
            Survie.SURVIE.getServer().getScheduler().runTaskAsynchronously(Survie.SURVIE, () -> {
                try {
                    Connection connection = StrawAPI.getAPI().getDataFactory().getDataSource().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `survie_claims` SET `warp` = ? WHERE `id` = ?");
                    if (warp != null)
                        preparedStatement.setString(1, (warp.getX() + ";" + warp.getY() + ";" + warp.getZ() + ";" + warp.getYaw() + ";" + warp.getPitch()));
                    else
                        preparedStatement.setNull(1, Types.VARCHAR);
                    preparedStatement.setInt(2, claimId);
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
        this.warp = warp;
    }

    public Location getWarp() {
        return this.warp;
    }

    public boolean isBanned(Player p) {
        return bannis.contains(p.getUniqueId());
    }

}
