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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ECN
 */
public class Monstre extends Creature {
    
    private static final String RACELOUP = "Loup";
    private static final String RACEOURS = "Ours";
    private static final String RACEBUFFLE = "Buffle";
    private static final String RACEJAGUAR = "Jaguar";
    private static final String RACEVACHE = "Vache";
    private static final String RACELAPIN = "Lapin";

    private String race;

    private static List<String> racesList;

    /**
     *
     */
    public static void init() {
        racesList = new ArrayList<String>();
        racesList.add(RACELOUP);
        racesList.add(RACEOURS);
        racesList.add(RACEBUFFLE);
        racesList.add(RACEJAGUAR);
        racesList.add(RACEVACHE);
        racesList.add(RACELAPIN);
    }

    public Monstre(String typemonstre, int coordx, int coordy,
                   int pvmax, int pvactuels, int degatsattaque, int pourcentageattaque,
                   int pourcentageparade, int valeurparade, World world) {
        super(pourcentageattaque, degatsattaque, pourcentageparade, valeurparade, pvmax, pvactuels, world);
        this.race = typemonstre;
        super.setPosition(new Point2D(coordx,coordy));
    }
    
    /**
     * Get nb races
     * @return
     */
    public static int getNbRaces() {
        if (racesList != null) {
            return racesList.size();
        }
        return 0;
    }

    /**
     *
     * @param race
     * @return
     */
    public static String intToRace(int race) {
        if (race < racesList.size()) {
            return racesList.get(race);
        }
        return "";
    }

    /**
     *
     * @param world
     */
    public Monstre(World world) {
        super(world);
        race = "race non determinee";
    }

    /**
     * 
     * @return 
     */
    public String getRace() {
        return race;
    }

    /**
     * 
     * @param race 
     */
    public void setRace(String race) {
        this.race = race;
    }
    
    /**
     *
     */
    public void setRaceCaracteristiques() {
        switch (this.getRace()) {
            case RACELOUP :
                this.setPourcentAttaque(30);
                this.setDegatsAttaque(3);
                this.setPourcentEsquive(20);
                this.setAbsorbe(2);
                this.setPVieMax(20);
                break;
            case RACEOURS :
                this.setPourcentAttaque(30);
                this.setDegatsAttaque(5);
                this.setPourcentEsquive(5);
                this.setAbsorbe(3);
                this.setPVieMax(40);
                break;
            case RACEBUFFLE :
                this.setPourcentAttaque(20);
                this.setDegatsAttaque(5);
                this.setPourcentEsquive(0);
                this.setAbsorbe(2);
                this.setPVieMax(30);
                break;
            case RACEJAGUAR :
                this.setPourcentAttaque(20);
                this.setDegatsAttaque(2);
                this.setPourcentEsquive(20);
                this.setAbsorbe(1);
                this.setPVieMax(20);
                break;
            case RACEVACHE :
                this.setPourcentAttaque(20);
                this.setDegatsAttaque(4);
                this.setPourcentEsquive(0);
                this.setAbsorbe(2);
                this.setPVieMax(20);
                break;
            case RACELAPIN :
                this.setPourcentAttaque(1);
                this.setDegatsAttaque(0);
                this.setPourcentEsquive(40);
                this.setAbsorbe(0);
                this.setPVieMax(5);
                break;
            default : // UNDEFINED
                this.setPourcentAttaque(0);
                this.setDegatsAttaque(0);
                this.setPourcentEsquive(0);
                this.setAbsorbe(0);
                this.setPVieMax(0);
                break;
        }
        this.setPVie(this.getPVieMax());
    }


    @Override
    public Integer saveToDatabase(Connection connection, Integer worldID) {
        Integer id = -1;

            String queryFindMonstreID = "Select COUNT(*) AS nbmonstre FROM monstre";
            String queryInsert = "INSERT INTO monstre VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            
            try{
            PreparedStatement stmtFindMonstreID = connection.prepareStatement(queryFindMonstreID);
            PreparedStatement stmtInsert = connection.prepareStatement(queryInsert);
            
            ResultSet rsMonstreID = stmtFindMonstreID.executeQuery();
            if (rsMonstreID.next()) {
                id = Integer.parseInt(rsMonstreID.getString("nbmonstre"))+1;
            }
            else{
                return id;
            }
            
            stmtInsert.setInt(1,id);
            stmtInsert.setInt(2,worldID); 
            stmtInsert.setString(3,this.getRace());
            stmtInsert.setInt(4,this.getPosition().getX());
            stmtInsert.setInt(5,this.getPosition().getY());
            stmtInsert.setInt(6,this.getPVieMax());
            stmtInsert.setInt(7,this.getPVie());
            stmtInsert.setInt(8,this.getDegatsAttaque());
            stmtInsert.setInt(9,this.getPourcentAttaque());
            stmtInsert.setInt(10,this.getPourcentEsquive());
            stmtInsert.setInt(11,this.getAbsorbe());
            stmtInsert.executeUpdate();
            
            
            stmtFindMonstreID.close();
            stmtInsert.close();
            
        }
        catch (SQLException ex) {
                Logger.getLogger(DatabaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        return id;
    }

    @Override
    public void getFromDatabase(Connection connection, Integer id) {
    }

    @Override
    public void removeFromDatabase(Connection connection, Integer id) {
    }
    
}
