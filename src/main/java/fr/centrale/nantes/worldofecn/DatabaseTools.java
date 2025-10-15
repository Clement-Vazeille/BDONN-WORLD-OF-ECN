/* --------------------------------------------------------------------------------
 * WoE Tools
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */
package fr.centrale.nantes.worldofecn;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import fr.centrale.nantes.worldofecn.world.World;

/**
 * Manage database connectio, saves and retreive informations
 * @author ECN
 */
public class DatabaseTools {

    private String login;
    private String password;
    private String url;
    private Connection connection;

    /**
     * Load infos
     */
    public DatabaseTools() {
        try {
            // Get Properties file
            ResourceBundle properties = ResourceBundle.getBundle(DatabaseTools.class.getPackage().getName() + ".database");

            // USE config parameters
            login = properties.getString("login");
            password = properties.getString("password");
            String server = properties.getString("server");
            String database = properties.getString("database");
            url = "jdbc:postgresql://" + server + "/" + database;

            // Mount driver
            Driver driver = DriverManager.getDriver(url);
            if (driver == null) {
                Class.forName("org.postgresql.Driver");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            // If driver is not found, cancel url
            url = null;
        }
        this.connection = null;
    }

    /**
     * Get connection to the database
     */
    public void connect() {
        System.out.println("connection de " + login + " mdp " + password + " url " + url);
        if ((this.connection == null) && (url != null) && (! url.isEmpty())) {
            try {
                this.connection = DriverManager.getConnection(url, login, password);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Disconnect from database
     */
    public void disconnect() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * get Player ID
     * @param nomJoueur : le login du joueur
     * @param password : le mot de passe du joueur
     * @return
     */
    public Integer getPlayerID(String nomJoueur, String password) {
        // TO BE DEFINED
        // retreive player ID according to his/her name (unique) and password from database
        // may return null if player is not found, the database ID if found.
        String query = "Select idjoueur FROM Joueur WHERE nomjoueur=? AND motdepasse=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,nomJoueur);
            stmt.setString(2,password);

            ResultSet rs = stmt.executeQuery();

            Integer resultID;
            if (rs.next()) {
                resultID = Integer.valueOf(rs.getString("idjoueur"));
                return resultID;
            }
            stmt.close();
        }
        catch (SQLException ex) {
                Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        return null;
    }

    /**
     * save world as sauvegarde in database
     * @param idJoueur : l'ID du joueur dans la BD
     * @param nomPartie : le nom de la partie
     * @param nomSauvegarde : le nom de la sauvegarde
     * @param monde: le monde à enregistrer
     */
    public void saveWorld(Integer idJoueur, String nomPartie, String nomSauvegarde, World monde) {
        // TO BE DEFINED
        
        // Create a new "partie" in database if it does not exists and link it to the player
        // Save partie's infos in the sauvegarde (height, width, ...) if necessary
        int idPartie = -1;        
        try {
            this.connect();
            
            //find sauvegarde for the partie if exist
            String query = "SELECT idpartie FROM partie WHERE nompartie=?";
            PreparedStatement stmt = this.connection.prepareStatement( query );
            stmt.setString(1,nomPartie);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {idPartie = rs.getInt("idpartie");}
            stmt.close();
            
            if (idPartie == -1) {
                
                //find a free id for new partie
                query = "SELECT MAX(idpartie) AS idmax FROM partie";
                stmt = this.connection.prepareStatement( query );
                rs = stmt.executeQuery();
                if (rs.next()) {idPartie = rs.getInt("idmax") + 1;}
                else {idPartie = 1;}
                stmt.close();
                
                //create a new partie
                query = "INSERT INTO partie (idpartie, nompartie, taillex, tailley, idjoueur) VALUES (?,?,?,?,?)";
                stmt = this.connection.prepareStatement( query );
                stmt.setInt(1,idPartie);
                stmt.setString(2,nomPartie);
                stmt.setInt(3,monde.getWidth());
                stmt.setInt(4,monde.getHeight());
                stmt.setInt(5,idJoueur);
                stmt.executeUpdate();
                stmt.close();
            }
            this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Create a new sauvegarde if it does not exist for the partie
        // Update sauvegarde infos for the partie
        int idSauvegarde = -1;
        try {
            this.connect();
            
            //find sauvegarde for the partie if exist
            String query = "SELECT idsauvegarde FROM sauvegarde WHERE idpartie=? AND nomsauvegarde=?";
            PreparedStatement stmt = this.connection.prepareStatement( query );
            stmt.setInt(1,idPartie);
            stmt.setString(2,nomSauvegarde);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {idSauvegarde = rs.getInt("idsauvegarde");}
            stmt.close();
            
            System.out.println("\n\n" + idSauvegarde + "\n\n");
            
            if (idSauvegarde == -1) {
                
                //find a free id for new sauvegarde
                query = "SELECT MAX(idsauvegarde) AS idmax FROM sauvegarde";
                stmt = this.connection.prepareStatement( query );
                rs = stmt.executeQuery();
                if (rs.next()) {idSauvegarde = rs.getInt("idmax") + 1;}
                else {idSauvegarde = 1;}
                stmt.close();
                
                //create a new sauvegarde
                query = "INSERT INTO sauvegarde (idsauvegarde, idpartie, nomsauvegarde) VALUES (?,?,?)";
                stmt = this.connection.prepareStatement( query );
                stmt.setInt(1,idSauvegarde);
                stmt.setInt(2,idPartie);
                stmt.setString(3,nomSauvegarde);
                stmt.executeUpdate();
                stmt.close();
            }
            else {
                // Remove existing elements de jeu for the sauvegarde
                String deletePersonnage = "DELETE FROM personnage WHERE idmonde IN (SELECT idmonde FROM monde WHERE idsauvegarde = ?)";
                String deleteMonstre    = "DELETE FROM monstre WHERE idmonde IN (SELECT idmonde FROM monde WHERE idsauvegarde = ?)";
                String deleteObjet      = "DELETE FROM objet WHERE idmonde IN (SELECT idmonde FROM monde WHERE idsauvegarde = ?)";
                String deleteMonde      = "DELETE FROM monde WHERE idsauvegarde = ?";
                
                PreparedStatement stmt1 = this.connection.prepareStatement(deletePersonnage);
                stmt1.setInt(1,idSauvegarde);
                stmt1.executeUpdate();
                stmt1.close();
                
                PreparedStatement stmt2 = this.connection.prepareStatement(deleteMonstre);
                stmt2.setInt(1,idSauvegarde);
                stmt2.executeUpdate();
                stmt2.close();
                
                PreparedStatement stmt3 = this.connection.prepareStatement(deleteObjet);
                stmt3.setInt(1,idSauvegarde);
                stmt3.executeUpdate();
                stmt3.close();
                
                PreparedStatement stmt4 = this.connection.prepareStatement(deleteMonde);
                stmt4.setInt(1,idSauvegarde);
                stmt4.executeUpdate();
                stmt4.close();
                
            }
            this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Save world's elementdejeu in database
        //monde.saveToDatabase(this.connection, nomPartie, nomSauvegarde);
        
        // Save player infos and the player's creature infos for this partie
        //monde.savePlayerInfo();
    }

    /**
     * get world sauvegarde from database
     * @param idJoueur
     * @param nomPartie
     * @param nomSauvegarde
     * @return monde
     */
    public World readWorld(Integer idJoueur, String nomPartie, String nomSauvegarde) {
        World monde = new World();
        // TO BE DEFINED
        
        // Retreive partie infos for the player
        // Retreive sauvegarde infos for the partie

        // Retreive world infos
        // Generate object world according to the infos
        
        // Retreive element de jeu from sauvegarde
        // Generate approprite objects
        // Link objects to the world
        
        // Associate player with the player's creature

        // Return created world
        return monde;
    }


    /**
     * remove world sauvegarde from database
     * @param idJoueur
     * @param nomPartie
     * @param nomSauvegarde
     */
    public void removeWorld(Integer idJoueur, String nomPartie, String nomSauvegarde) {
        World monde = new World();
        // TO BE DEFINED
        
        // Retreive partie infos for the player
        // Retreive sauvegarde infos for the partie

        // remove elements de jeu linked to the sauvegarde
        // remove sauvegarde
        // remove if partie has no mode sauvegarde, remove partie
    }
}
