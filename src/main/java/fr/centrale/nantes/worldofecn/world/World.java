/* --------------------------------------------------------------------------------
 * WoE
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */
package fr.centrale.nantes.worldofecn.world;

import fr.centrale.nantes.worldofecn.DatabaseTools;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ECN
 */
public class World {

    private static final int MAXPEOPLE = 20;
    private static final int MAXMONSTERS = 10;
    private static final int MAXOBJECTS = 20;

    private Integer width;
    private Integer height;
    
    private int roundNo;
    private List<ElementDeJeu> listElements;
    private Joueur player;
    private List<Point2D> positions;
    private List<ElementDeJeu> roundElements;

    /**
     * Default constructor
     */
    public World() {
        this(20, 20);
    }

    /**
     * Constructor for specific world size
     *
     * @param width : world width
     * @param height : world height
     */
    public World(int width, int height) {
        this.setHeightWidth(height, width);
        init();
        generate();
        
        this.roundNo = 0;
        this.roundElements = null;
    }

    /**
     * Initialize elements
     */
    private void init() {
        this.listElements = new LinkedList<ElementDeJeu>();
        this.player = new Joueur("Player");
        this.positions = new ArrayList<Point2D>();
    }

    /**
     *
     * @return
     */
    public Integer getWidth() {
        return width;
    }

    /**
     *
     * @param width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     *
     * @return
     */
    public Integer getHeight() {
        return height;
    }

    /**
     *
     * @param height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     *
     * @param height
     * @param width
     */
    public final void setHeightWidth(Integer height, Integer width) {
        this.setHeight(height);
        this.setWidth(width);
    }

    /**
     * Check element can be created
     *
     * @param element
     * @return
     */
    private ElementDeJeu check(ElementDeJeu element) {
        return element;
    }

    /**
     * Generate personnages
     */
    private void generatePersonnages(int nbElements) {
        Random rand = new Random();
        for (int i = 0; i < nbElements; i++) {
            int race = rand.nextInt(Personnage.getNbRaces());
            String raceStr = Personnage.intToRace(race);

            int metier = rand.nextInt(Personnage.getNbMetiers());
            String metierStr = Personnage.intToMetier(metier);

            Personnage item = new Personnage(this);
            item.setRace(raceStr);
            item.setMetier(metierStr);

            // Add to list
            this.listElements.add(item);
            this.positions.add(item.getPosition());
        }
    }

    /**
     * Generate Monsters
     */
    private void generateMonsters(int nbElements) {
        Random rand = new Random();

        // Generate monsters
        for (int i = 0; i < nbElements; i++) {
            int race = rand.nextInt(Monstre.getNbRaces());
            String raceStr = Monstre.intToRace(race);

            Monstre item = new Monstre(this);
            this.listElements.add(item);
            this.positions.add(item.getPosition());
        }
    }

    /**
     * Generate Objects
     */
    private void generateObjects(int nbElements) {
        Random rand = new Random();

        // Generate objects
        for (int i = 0; i < nbElements; i++) {
            int type = rand.nextInt(Objet.getNbTypes());
            String typeStr = Objet.intToType(type);

            Objet item = new Objet(this);
            item.setType(typeStr);

            // Add to list
            this.listElements.add(item);
            this.positions.add(item.getPosition());
        }
    }

    /**
     * Generate Player
     */
    private void generatePlayer(int itemType) {
        Random rand = new Random();

        int race = rand.nextInt(Personnage.getNbRaces());
        String raceStr = Personnage.intToRace(race);

        int metier = rand.nextInt(Personnage.getNbMetiers());
        String metierStr = Personnage.intToMetier(metier);

        Personnage item = new Personnage(this);
        item.setRace(raceStr);
        item.setMetier(metierStr);
        
        // Add to list
        this.listElements.add(item);
        
        player.setPersonnage(item);
    }

    /**
     * Generate elements randomly
     */
    private void generate() {
        Random rand = new Random();

        generatePlayer(1);

        generatePersonnages(rand.nextInt(MAXPEOPLE));
        generateMonsters(rand.nextInt(MAXMONSTERS));
        generateObjects(rand.nextInt(MAXOBJECTS));
    }

    /**
     * Set Player name
     *
     * @param name
     */
    public void setPlayer(String name) {
        this.player.setNom(name);
    }
    
    /**
     * Return used positions
     * @return 
     */
    public List<Point2D> getPositions() {
        return positions;
    }

    /**
     * Remove element from the world
     * @param elementdejeu 
     */
    public void removeFromWorld(ElementDeJeu elementdejeu) {
        if (elementdejeu != null) {
            this.positions.remove(elementdejeu.getPosition());
            this.listElements.remove(elementdejeu);
            this.roundElements.remove(elementdejeu);
        }
    }
    
    /**
     * Go to next round
     */
    public void nextRound() {
        this.roundNo++;
        this.roundElements = new LinkedList<ElementDeJeu>();
        this.roundElements.addAll(this.listElements);
    }
    
    /**
     * Returns next element who has to play
     * @return 
     */
    public ElementDeJeu nextElementInRound() {
        ElementDeJeu nextElement = this.roundElements.getFirst();
        if (nextElement != null) {
            this.roundElements.removeFirst();
        }
        return nextElement;
    }

    /**
     * Save world to database
     *
     * @param connection
     * @param gameName
     * @param saveName
     */
    public void saveToDatabase(Connection connection, String gameName, String saveName) {
        if (connection != null) {
            // Get Player ID
            
            // Save world for Player ID
            
            //creation du world (pas de la partie
            String queryFindSaveID = "Select idsauvegarde FROM Sauvegarde WHERE nomsauvegarde=?";
            String queryFindWorldID = "Select COUNT(*) AS nbmondes FROM monde";
            String queryInsert = "INSERT INTO monde VALUES (?,?,?)";
            
            Integer saveID=-1;
        try{
            PreparedStatement stmtFindSaveID = connection.prepareStatement(queryFindSaveID);
            PreparedStatement stmtFindWorldID = connection.prepareStatement(queryFindWorldID);
            PreparedStatement stmtInsert = connection.prepareStatement(queryInsert);
            
            stmtFindSaveID.setString(1,saveName);
            
            ResultSet rsSaveID = stmtFindSaveID.executeQuery();
            if (rsSaveID.next()) {
                saveID = Integer.parseInt(rsSaveID.getString("idsauvegarde"));
            }
            else{
                return;
            }
            
            ResultSet rsWorldID = stmtFindWorldID.executeQuery();
            Integer worldID;
            if (rsWorldID.next()) {
                worldID = Integer.parseInt(rsWorldID.getString("nbmondes"))+1;
            }
            else{
                return;
            }
            
            int idpersonnageJoueur=-1;
            //Save all elements from the world in the database
            for(ElementDeJeu e:listElements)
            {
                int idelem = e.saveToDatabase(connection,saveID);
                if(e == player.getPersonnage())
                {
                    idpersonnageJoueur = idelem;
                }
            }
            
            stmtInsert.setInt(1,worldID);
            stmtInsert.setInt(2,saveID);
            stmtInsert.setInt(3,idpersonnageJoueur);
            stmtInsert.executeUpdate();
            
            
            stmtFindSaveID.close();
            stmtFindWorldID.close();
            stmtInsert.close();
            
        }
        catch (SQLException ex) {
                Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        

        
        }
    }

    /**
     * Get world from database
     *
     * @param connection
     * @param gameName
     * @param saveName
     */
    public void getFromDatabase(Connection connection, String gameName, String saveName) {
        if (connection != null) {
            // Remove old data
            this.setHeightWidth(0, 0);
            init();

            // Get Player ID
            // get world for Player ID
        }
    }
}
