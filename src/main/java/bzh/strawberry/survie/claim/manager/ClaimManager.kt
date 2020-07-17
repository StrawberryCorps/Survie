package bzh.strawberry.survie.claim.manager

import bzh.strawberry.api.StrawAPI
import bzh.strawberry.api.world.Cuboid
import bzh.strawberry.survie.Survie
import bzh.strawberry.survie.claim.manager.data.Claim
import bzh.strawberry.survie.claim.manager.data.ClaimMember
import bzh.strawberry.survie.claim.manager.rank.ClaimRank
import bzh.strawberry.survie.manager.SurviePlayer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.*
import kotlin.collections.ArrayList

/*
 * This file (ClaimManager.kt) is part of a project Survie.
 * It was created on 09/07/2020 20:04 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
class ClaimManager {

    private var claims = ArrayList<Claim>()

    init {
        this.loadClaim()
    }

    private fun loadClaim() {
        try {
            val connecion = StrawAPI.getAPI().dataFactory.dataSource.connection
            val preparedStatement: PreparedStatement = connecion.prepareStatement("SELECT C.id, C.owner, C.world, C.cuboid, C.created_at, C.updated_at, C.name, C.taxe, C.bank, C.taxe_etat, C.taille, C.welcome, C.farewell, C.warp ,A.uuid FROM survie_claims C, survie_player P, elenoxauthenticator A WHERE C.owner = P.id AND P.elid = A.id")
            val resultSet: ResultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                val cuboid = resultSet.getString("cuboid").split(";")
                val claim = Claim(resultSet.getInt("id"), UUID.fromString(resultSet.getString("uuid")),
                        Cuboid(Location(Survie.SURVIE.server.getWorld("Survie"), cuboid[0].toDouble(), cuboid[1].toDouble(), cuboid[2].toDouble()),
                                Location(Survie.SURVIE.server.getWorld("Survie"), cuboid[3].toDouble(), cuboid[4].toDouble(), cuboid[5].toDouble())),
                        resultSet.getInt("taxe"),
                        resultSet.getDouble("bank"),
                        resultSet.getInt("taxe_etat"),
                        resultSet.getInt("taille"),
                        resultSet.getString("welcome"),
                        resultSet.getString("farewell"))

                val locWarp = resultSet.getString("warp")
                if (locWarp != null) {
                    val tab = locWarp.split(";")
                    claim.setWarp(Location(Bukkit.getWorld("Survie"), tab[0].toDouble(), tab[1].toDouble(), tab[2].toDouble(), tab[3].toFloat(), tab[4].toFloat()), false)
                } else
                    claim.setWarp(null, false)

                val preparedStatementData: PreparedStatement = connecion.prepareStatement("SELECT * FROM survie_claims_data WHERE claim_id = ?")
                preparedStatementData.setInt(1, claim.claimId)
                val resultSetData: ResultSet = preparedStatementData.executeQuery()
                if (resultSetData.next()) {
                    val pos = resultSetData.getString("home").split(";")
                    claim.home = Location(claim.cuboid.center.world, pos[0].toDouble(), pos[1].toDouble(), pos[2].toDouble(), pos[3].toFloat(), pos[4].toFloat())
                    claim.name = resultSetData.getString("name")
                    claim.isLock = resultSetData.getBoolean("locked")
                }
                resultSetData.close()
                preparedStatementData.close()

                val preparedStatementMember: PreparedStatement = connecion.prepareStatement("SELECT M.role, A.uuid FROM survie_claims_member M, elenoxauthenticator A, survie_player P, survie_claims C WHERE C.id = M.claimID AND P.elid = A.id AND M.member = P.id AND C.id = ?")
                preparedStatementMember.setInt(1, claim.claimId)
                val resultSetMember: ResultSet = preparedStatementMember.executeQuery()
                while (resultSetMember.next())
                    claim.addClaimMember(ClaimMember(UUID.fromString(resultSetMember.getString("uuid")), ClaimRank.valueOf(resultSetMember.getString("role"))), false)
                resultSetMember.close()
                preparedStatementMember.close()

                val preparedStatementBan: PreparedStatement = connecion.prepareStatement("SELECT A.uuid FROM survie_claims_bans M, elenoxauthenticator A, survie_player P, survie_claims C WHERE C.id = M.claim_id AND P.elid = A.id AND M.player_id = P.id AND C.id = ?")
                preparedStatementBan.setInt(1, claim.claimId)
                val resultSetBan: ResultSet = preparedStatementBan.executeQuery()
                while (resultSetBan.next())
                    claim.addBan(UUID.fromString(resultSetBan.getString("uuid")), false)
                resultSetBan.close()
                preparedStatementBan.close()

                claims.add(claim)

                Survie.SURVIE.server.logger.info("    -> Ajout claim : $claim")
            }
            resultSet.close()
            preparedStatement.close()
            connecion.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            Survie.SURVIE.server.shutdown()
        }
    }

    fun getClaims(): ArrayList<Claim> = this.claims

    fun getClaim(surviePlayer: SurviePlayer) = this.claims.firstOrNull { claim -> (claim.isOnClaim(surviePlayer.uniqueID) && !claim.coopMembers.contains(surviePlayer.uniqueID)) }

    fun getClaim(location: Location) = this.claims.firstOrNull { claim -> claim.cuboid.isIn(location) }

    fun getClaim(uuid: UUID) = this.claims.firstOrNull { claim -> claim.owner == uuid }

    fun getClaim(player: OfflinePlayer) = this.claims.firstOrNull { claim -> claim.isOnClaim(player.uniqueId) }

    fun addClaim(surviePlayer: SurviePlayer, cuboid: Cuboid) {
        Survie.SURVIE.server.scheduler.runTaskAsynchronously(Survie.SURVIE, kotlin.run {
            {
                try {
                    val connection = StrawAPI.getAPI().dataFactory.dataSource.connection
                    val preparedStatement = connection.prepareStatement("INSERT INTO survie_claims (`owner`, `world`, `cuboid`) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
                    preparedStatement.setInt(1, surviePlayer.survieID)
                    preparedStatement.setString(2, cuboid.pos1.world!!.name)
                    preparedStatement.setString(3, cuboid.pos1.x.toString() + ";" + cuboid.pos1.y.toString() + ";" + cuboid.pos1.z.toString() + ";" + cuboid.pos2.x.toString() + ";" + cuboid.pos2.y.toString() + ";" + cuboid.pos2.z.toString())
                    preparedStatement.executeUpdate()
                    val resultSet = preparedStatement.generatedKeys
                    if (resultSet.next()) {
                        val id = resultSet.getInt(1)
                        val claim = Claim(id, surviePlayer.uniqueID, cuboid, 7, 0.0, 11, 0, null, null)
                        claim.home = surviePlayer.player.location
                        claims.add(claim)
                        Survie.SURVIE.server.logger.info("    -> Ajout claim : $claim")
                    }
                    resultSet.close()
                    preparedStatement.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                    surviePlayer.player.sendMessage(Survie.SURVIE.prefix + "§cUne erreur est survenue lors de la création de votre claim (" + e.message + ")")
                }
            }
        })
    }

    fun isCoop(uuid: UUID): Boolean {
        var ret = false

        this.claims.stream().forEach { c: Claim ->
            if (!ret) {
                ret = c.coopMembers.containsKey(uuid)
            }
        }

        return ret
    }

    fun getClaimsWithWarp() = this.claims.filter { c -> c.warp != null }
}