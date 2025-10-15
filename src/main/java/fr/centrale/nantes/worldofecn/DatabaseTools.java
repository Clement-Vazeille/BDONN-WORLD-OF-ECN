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
            String query = "SELECT idpartie FROM partie WHERE idjoueur=? AND nompartie=?";
            PreparedStatement stmt = this.connection.prepareStatement( query );
            stmt.setInt(1,idJoueur);
            stmt.setString(2,nomPartie);
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
                String deletePersonnage = "DELETE FROM personnage WHERE idmonde = (SELECT idmonde FROM monde WHERE idsauvegarde = ?)";
                String deleteMonstre    = "DELETE FROM monstre WHERE idmonde = (SELECT idmonde FROM monde WHERE idsauvegarde = ?)";
                String deleteObjet      = "DELETE FROM objet WHERE idmonde = (SELECT idmonde FROM monde WHERE idsauvegarde = ?)";
                String deleteMonde      = "DELETE FROM monde WHERE idsauvegarde = ?";
                
                PreparedStatement stmtDeletePersonnage = this.connection.prepareStatement(deletePersonnage);
                stmtDeletePersonnage.setInt(1,idSauvegarde);
                stmtDeletePersonnage.executeUpdate();
                stmtDeletePersonnage.close();
                
                PreparedStatement stmtDeleteMonstre = this.connection.prepareStatement(deleteMonstre);
                stmtDeleteMonstre.setInt(1,idSauvegarde);
                stmtDeleteMonstre.executeUpdate();
                stmtDeleteMonstre.close();
                
                PreparedStatement stmtDeleteObjet = this.connection.prepareStatement(deleteObjet);
                stmtDeleteObjet.setInt(1,idSauvegarde);
                stmtDeleteObjet.executeUpdate();
                stmtDeleteObjet.close();
                
                PreparedStatement stmtDeleteMonde  = this.connection.prepareStatement(deleteMonde);
                stmtDeleteMonde.setInt(1,idSauvegarde);
                stmtDeleteMonde.executeUpdate();
                stmtDeleteMonde.close();
                
            }
            this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Save world's elementdejeu in database
        //monde.saveToDatabase(this.connection, nomPartie, nomSauvegarde);
        
        // Save player infos and the player's creature infos for this partie
        monde.saveToDatabase(this.connection, nomPartie, nomSauvegarde);
    }

    /**
     * get world sauvegarde from database
     * @param idJoueur
     * @param nomPartie
     * @param nomSauvegarde
     * @return monde
     */
    public World readWorld(Integer idJoueur, String nomPartie, String nomSauvegarde) {
        World monde;
        
        // Retreive partie infos for the player
        int idPartie=-1;
        int idSauvegarde = -1;
        try {
            this.connect();
            
            String queryfindidpartie = "SELECT idpartie FROM partie WHERE idjoueur=? AND nompartie=?";
            String queryfindidsauvegarde = "SELECT idsauvegarde FROM sauvegarde WHERE idpartie=? AND nomsauvegarde=?";
            PreparedStatement stmtidpartie = this.connection.prepareStatement(queryfindidpartie);
            PreparedStatement stmtidsauvegarde = this.connection.prepareStatement(queryfindidsauvegarde);
            stmtidpartie.setInt(1,idJoueur);
            stmtidpartie.setString(2,nomPartie);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {idPartie = rs.getInt("idpartie");}
            
            stmtidsauvegarde.setInt(1,idPartie);
            stmtidsauvegarde.setString(2,nomSauvegarde);
            
            ResultSet rssauvegarde = stmtidsauvegarde.executeQuery();
            if (rssauvegarde.next()) {idSauvegarde = rssauvegarde.getInt("idsauvegarde");}
            
            stmtidpartie.close();
            stmtidsauvegarde.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Retreive sauvegarde infos for the partie
        
        // Retreive world infos
        int idMonde = -1;
        int tailleX = -1;
        int tailleY = -1;
        try {
            this.connect();
            
            String queryfindidpartie = "SELECT idmonde FROM monde WHERE idsauvegarde=?";
            String queryfindtaillemonde = "SELECT taillex, tailley FROM partie WHERE idpartie=?";
            
            PreparedStatement stmtidmonde = this.connection.prepareStatement(queryfindidpartie);
            PreparedStatement stmttaillemonde = this.connection.prepareStatement(queryfindtaillemonde);
            stmtidmonde.setInt(1,idSauvegarde);
            ResultSet rs = stmtidmonde.executeQuery();
            if (rs.next()) {
                idMonde = rs.getInt("idmonde");
            }
            
            stmttaillemonde.setInt(1,idPartie);
            ResultSet rsTailleMonde = stmtidmonde.executeQuery();
            if (rs.next()) {
                tailleX = rsTailleMonde.getInt("taillex");
                tailleY = rsTailleMonde.getInt("tailley");
            }
            
            stmtidmonde.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Generate object world according to the infos
        monde = new World(tailleX,tailleY);
        
        // Retreive element de jeu from sauvegarde
        // Generate approprite objects
        // Link objects to the world
        try {
            this.connect();
            
            //objets
            String queryfindobjets = "SELECT typeObjet,coordx,coordy FROM monde WHERE idsmonde=?";
            String queryfindmonstres = "SELECT typemonstre,coordx,coordy,pvmax,pvactuels,degatsattaque,"
                    + "pourcentageattaque,pourcentageparade,valeurparade FROM monde WHERE idsmonde=?";
            String queryfindpersonnages = "SELECT idpersonnage,nom,genre,race,metier,coordx,coordy,pvmax,pvactuels,degatsattaque,"
                    + "pourcentageattaque,pourcentageparade,valeurparade,pmmax,pmactuels,porteeattaque,nombrefleches"
                    + " FROM monde WHERE idsmonde=?";
            
            PreparedStatement stmtobjets = this.connection.prepareStatement(queryfindobjets);
            PreparedStatement stmtmonstres = this.connection.prepareStatement(queryfindmonstres);
            PreparedStatement stmtpersonnages = this.connection.prepareStatement(queryfindpersonnages);
            
            
            stmtobjets.setInt(1,idMonde);
            stmtmonstres.setInt(1,idMonde);
            stmtpersonnages.setInt(1,idMonde);
            
            ResultSet rso = stmtobjets.executeQuery();
            while (rso.next()) {
                Objet o = new Objet(rso.getInt("idmonde"),);
                monde.addElementToList(o);
            }   
            
            ResultSet rsm = stmtmonstres.executeQuery();
            while (rso.next()) {
                Monstre m = new Monstre(rsm.getInt("idmonde"),);
                monde.addElementToList(m);
            }   
            
            ResultSet rsp = stmtpersonnages.executeQuery();
            while (rso.next()) {
                Personnage p = new Personnage(rsp.getInt("idmonde"),);
                monde.addElementToList(p);
            }   
            
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
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
        
        this.connect();
        
        // Retreive partie infos for the player
        int idPartie = -1;
        try {
            String searchIdPartie= "SELECT idpartie FROM partie WHERE idjoueur=? AND nompartie=?";
            PreparedStatement stmtSearchIdPartie = this.connection.prepareStatement( searchIdPartie );
            stmtSearchIdPartie.setInt(1,idJoueur);
            stmtSearchIdPartie.setString(2,nomPartie);
            ResultSet rs = stmtSearchIdPartie.executeQuery();
            if (rs.next()) {idPartie = rs.getInt("idpartie");}
            stmtSearchIdPartie.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Retreive sauvegarde infos for the partie
        int idSauvegarde = -1;
        try {
            String searchIdSauvegarde = "SELECT idsauvegarde FROM sauvegarde WHERE idpartie=? AND nomsauvegarde=?";
            PreparedStatement stmtSearchIdSauvegarde = this.connection.prepareStatement( searchIdSauvegarde );
            stmtSearchIdSauvegarde.setInt(1,idPartie);
            stmtSearchIdSauvegarde.setString(2,nomSauvegarde);
            ResultSet rs = stmtSearchIdSauvegarde.executeQuery();
            if (rs.next()) {idSauvegarde = rs.getInt("idsauvegarde");}
            stmtSearchIdSauvegarde.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (idSauvegarde != -1) {
            try {
                // remove sauvegarde
                String deleteSauvegarde = "DELETE FROM sauvegarde WHERE idsauvegarde = ?)";
                PreparedStatement stmtDeleteSauvegarde = this.connection.prepareStatement(deleteSauvegarde);
                stmtDeleteSauvegarde.setInt(1,idSauvegarde);
                stmtDeleteSauvegarde.executeUpdate();
                stmtDeleteSauvegarde.close();
                
                // remove elements de jeu linked to the sauvegarde
                String deletePersonnage = "DELETE FROM personnage WHERE idmonde = (SELECT idmonde FROM monde WHERE idsauvegarde = ?)";
                String deleteMonstre    = "DELETE FROM monstre WHERE idmonde = (SELECT idmonde FROM monde WHERE idsauvegarde = ?)";
                String deleteObjet      = "DELETE FROM objet WHERE idmonde = (SELECT idmonde FROM monde WHERE idsauvegarde = ?)";
                String deleteMonde      = "DELETE FROM monde WHERE idsauvegarde = ?";
                
                PreparedStatement stmtDeletePersonnage = this.connection.prepareStatement(deletePersonnage);
                stmtDeletePersonnage.setInt(1,idSauvegarde);
                stmtDeletePersonnage.executeUpdate();
                stmtDeletePersonnage.close();
                
                PreparedStatement stmtDeleteMonstre = this.connection.prepareStatement(deleteMonstre);
                stmtDeleteMonstre.setInt(1,idSauvegarde);
                stmtDeleteMonstre.executeUpdate();
                stmtDeleteMonstre.close();
                
                PreparedStatement stmtDeleteObjet = this.connection.prepareStatement(deleteObjet);
                stmtDeleteObjet.setInt(1,idSauvegarde);
                stmtDeleteObjet.executeUpdate();
                stmtDeleteObjet.close();
                
                PreparedStatement stmtDeleteMonde  = this.connection.prepareStatement(deleteMonde);
                stmtDeleteMonde.setInt(1,idSauvegarde);
                stmtDeleteMonde.executeUpdate();
                stmtDeleteMonde.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // remove if partie has no more sauvegarde, remove partie
        else {
            try {
                String deletePartie = "DELETE FROM partie WHERE idpartie = ?)";
                PreparedStatement stmtDeletePartie = this.connection.prepareStatement(deletePartie);
                stmtDeletePartie.setInt(1,idPartie);
                stmtDeletePartie.executeUpdate();
                stmtDeletePartie.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
