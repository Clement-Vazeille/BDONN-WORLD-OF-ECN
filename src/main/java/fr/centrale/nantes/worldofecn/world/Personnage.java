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
public class Personnage extends Creature {

private static final String RACEHUMAIN = "Humain";
private static final String RACENAIN = "Nain";
private static final String RACEELFE = "Elfe";
private static final String RACEGOBELIN = "Gobelin";
private static final String RACETROLL = "Troll";

private static final String METIERGUERRIER = "Guerrier";
private static final String METIERARCHER = "Archer";
private static final String METIERARBALETRIER = "Arbaletrier";
private static final String METIERMAGE = "Mage";
private static final String METIERPRETRE = "Pretre";
private static final String METIERPALADIN = "Paladin";
private static final String METIERVOLEUR = "Voleur";
private static final String METIERPAYSAN = "Paysan";

private String race;
    private String metier;
    private String nom;
    private String genre;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    private int pourcentParade;
    private int valeurParade;
    private int pMagieMax;
    private int pMagie;
    private int portee;

    private int nbFleches;

    private static List<String> racesList;
    private static List<String> metiersList;

    /**
     *
     */
    public static void init() {
        racesList = new ArrayList<String>();
        racesList.add(RACEHUMAIN);
        racesList.add(RACENAIN);
        racesList.add(RACEELFE);
        racesList.add(RACEGOBELIN);
        racesList.add(RACETROLL);

        metiersList = new ArrayList<String>();
        metiersList.add(METIERGUERRIER);
        metiersList.add(METIERARCHER);
        metiersList.add(METIERARBALETRIER);
        metiersList.add(METIERMAGE);
        metiersList.add(METIERPRETRE);
        metiersList.add(METIERPALADIN);
        metiersList.add(METIERVOLEUR);
        metiersList.add(METIERPAYSAN);
    }
    
    public Personnage(String nom, String genre, String race, String metier,
                      int coordx, int coordy, int pvmax, int pvactuels, int degatsattaque,
                      int pourcentageattaque, int pourcentageparade, int valeurparade,
                      int pmmax, int pmactuels, int porteeattaque, int nombrefleches, World world) {
        super(pourcentageattaque, degatsattaque, pourcentageparade, valeurparade, pvmax, pvactuels, world);
        this.nom = nom;
        this.genre = genre;
        this.race = race;
        this.metier = metier;
        super.setPosition(new Point2D(coordx,coordy));
        this.pMagieMax = pmmax;
        this.pMagie = pmactuels;
        this.portee = porteeattaque;
        this.nbFleches = nombrefleches;
    }
    
    /**
     *
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
     * @return
     */
    public static int getNbMetiers() {
        if (metiersList != null) {
            return metiersList.size();
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
     * @param metier
     * @return
     */
    public static String intToMetier(int metier) {
        if (metier < metiersList.size()) {
            return metiersList.get(metier);
        }
        return "";
    }

    /**
     *
     * @param world
     */
    public Personnage(World world) {
        super(world);
        this.race = UNDEFINED;
        this.metier = UNDEFINED;
        this.nbFleches = 0;
        this.nom = "Pierre";
        this.genre = "H";
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
        if ((this.race == null) || (this.race.equals(ElementDeJeu.UNDEFINED))) {
            this.race = race;
            setRaceCaracteristiques();
        }
    }

    /**
     *
     * @return
     */
    public String getMetier() {
        return metier;
    }

    /**
     *
     * @param metier
     */
    public void setMetier(String metier) {
        if ((this.metier == null) || (this.metier.equals(ElementDeJeu.UNDEFINED))) {
            this.metier = metier;
            ensureCompatibility();
            addMetierCaracteristiques();
        }
    }

    /**
     *
     * @return
     */
    public int getPourcentParade() {
        return pourcentParade;
    }

    /**
     *
     * @param pourcentParade
     */
    public void setPourcentParade(int pourcentParade) {
        this.pourcentParade = pourcentParade;
    }

    /**
     *
     * @return
     */
    public int getValeurParade() {
        return valeurParade;
    }

    /**
     *
     * @param valeurParade
     */
    public void setValeurParade(int valeurParade) {
        this.valeurParade = valeurParade;
    }

    /**
     *
     * @return
     */
    public int getPMagieMax() {
        return pMagieMax;
    }

    /**
     *
     * @param pMagieMax
     */
    public void setPMagieMax(int pMagieMax) {
        this.pMagieMax = pMagieMax;
    }

    /**
     *
     * @return
     */
    public int getPMagie() {
        return pMagie;
    }

    /**
     *
     * @param pMagie
     */
    public void setPMagie(int pMagie) {
        this.pMagie = pMagie;
    }

    /**
     *
     * @return
     */
    public int getPortee() {
        return portee;
    }

    /**
     *
     * @param portee
     */
    public void setPortee(int portee) {
        this.portee = portee;
    }

    /**
     *
     * @return
     */
    public int getNbFleches() {
        return nbFleches;
    }

    /**
     *
     * @param nbFleches
     */
    public void setNbFleches(int nbFleches) {
        this.nbFleches = nbFleches;
    }

    /**
     * Ensure compatibility between race and metier
     */
    private void ensureCompatibility() {
        switch (this.getMetier()) {
            case METIERGUERRIER:
                // Pas de restruction
                break;
            case METIERARCHER:
                switch (this.getRace()) {
                    case RACENAIN:
                        this.metier = METIERARBALETRIER;
                        break;
                    case RACETROLL:
                        this.metier = METIERGUERRIER;
                        break;
                }
                break;
            case METIERARBALETRIER:
                switch (this.getRace()) {
                    case RACEELFE:
                    case RACEGOBELIN:
                        this.metier = METIERARCHER;
                        break;
                }
                break;
            case METIERMAGE:
                switch (this.getRace()) {
                    case RACENAIN:
                    case RACEGOBELIN:
                    case RACETROLL:
                        this.metier = METIERPRETRE;
                        break;
                }
                break;
            case METIERPRETRE:
                // Aucune restriction
                break;
            case METIERPALADIN:
                switch (this.getRace()) {
                    case RACEHUMAIN:
                        break;
                    default:
                        this.metier = METIERGUERRIER;
                        break;
                }
                break;
            case METIERVOLEUR:
                switch (this.getRace()) {
                    case RACETROLL:
                        this.metier = METIERGUERRIER;
                        break;
                }
                break;
            case METIERPAYSAN:
                // Aucune restriction
                break;
        }
    }
    
    private void setRaceCaracteristiques() {
        switch (this.getRace()) {
            case RACEHUMAIN :
                this.setPourcentAttaque(30);
                this.setDegatsAttaque(2);
                this.setPourcentParade(10);
                this.setValeurParade(0);
                this.setPourcentEsquive(10);
                this.setAbsorbe(0);
                this.setPVieMax(20);
                this.setPMagieMax(20);
                this.setPortee(1);
                break;
            case RACENAIN :
                this.setPourcentAttaque(40);
                this.setDegatsAttaque(3);
                this.setPourcentParade(10);
                this.setValeurParade(1);
                this.setPourcentEsquive(0);
                this.setAbsorbe(1);
                this.setPVieMax(25);
                this.setPMagieMax(10);
                this.setPortee(1);
                break;
            case RACEELFE :
                this.setPourcentAttaque(25);
                this.setDegatsAttaque(2);
                this.setPourcentParade(15);
                this.setValeurParade(0);
                this.setPourcentEsquive(20);
                this.setAbsorbe(0);
                this.setPVieMax(30);
                this.setPMagieMax(30);
                this.setPortee(1);
                break;
            case RACEGOBELIN :
                this.setPourcentAttaque(30);
                this.setDegatsAttaque(2);
                this.setPourcentParade(10);
                this.setValeurParade(0);
                this.setPourcentEsquive(10);
                this.setAbsorbe(0);
                this.setPVieMax(30);
                this.setPMagieMax(10);
                this.setPortee(1);
                break;
            case RACETROLL :
                this.setPourcentAttaque(40);
                this.setDegatsAttaque(4);
                this.setPourcentParade(0);
                this.setValeurParade(0);
                this.setPourcentEsquive(0);
                this.setAbsorbe(2);
                this.setPVieMax(30);
                this.setPMagieMax(0);
                this.setPortee(1);
                break;
            default : // UNDEFINED
                this.setPourcentAttaque(0);
                this.setDegatsAttaque(0);
                this.setPourcentParade(0);
                this.setValeurParade(0);
                this.setPourcentEsquive(0);
                this.setAbsorbe(0);
                this.setPVieMax(0);
                this.setPMagieMax(0);
                this.setPortee(1);
                break;
        }
        this.setPVie(this.getPVieMax());
        this.setPMagie(this.getPMagieMax());
    }

    private void addMetierCaracteristiques() {
        switch (this.getMetier()) {
            case METIERGUERRIER :
                this.setPourcentAttaque(this.getPourcentAttaque() + 10);
                this.setDegatsAttaque(this.getDegatsAttaque() + 2);
                this.setPourcentParade(this.getPourcentParade() + 20);
                this.setValeurParade(this.getValeurParade() + 5);
                this.setPVieMax(this.getPVieMax() + 5);
                break;
            case METIERARCHER :
                this.setPourcentAttaque(this.getPourcentAttaque() + 10);
                this.setDegatsAttaque(this.getDegatsAttaque() + 1);
                this.setPortee(3);
                break;
            case METIERARBALETRIER :
                this.setPourcentAttaque(this.getPourcentAttaque() + 10);
                this.setDegatsAttaque(this.getDegatsAttaque() + 3);
                this.setPortee(3);
                break;
            case METIERMAGE :
                this.setPortee(4);
                break;
            case METIERPRETRE :
                this.setPortee(3);
                break;
            case METIERPALADIN :
                this.setPourcentAttaque(this.getPourcentAttaque() + 5);
                this.setDegatsAttaque(this.getDegatsAttaque() + 2);
                this.setPourcentParade(this.getPourcentParade() + 20);
                this.setValeurParade(this.getValeurParade() + 5);
                this.setPVieMax(this.getPVieMax() + 5);
                this.setPortee(2);
                break;
            case METIERVOLEUR :
                this.setPourcentAttaque(this.getPourcentAttaque() + 15);
                this.setDegatsAttaque(this.getDegatsAttaque() + 1);
                this.setPVieMax(this.getPVieMax() + 5);
                break;
            case METIERPAYSAN :
                this.setAbsorbe(this.getAbsorbe()+ 5);
                this.setPVieMax(this.getPVieMax() + 5);
                break;
            default : // UNDEFINED
                break;
        }
        this.setPVie(this.getPVieMax());
        this.setPMagie(this.getPMagieMax());
    }

    @Override
    public Integer saveToDatabase(Connection connection, Integer worldID) {
        Integer id = -1;

            String queryFindPersonnageID = "Select COUNT(*) AS nbpersonnage FROM personnage";
            String queryInsert = "INSERT INTO personnage VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            try{
            PreparedStatement stmtFindPersonnageID = connection.prepareStatement(queryFindPersonnageID);
            PreparedStatement stmtInsert = connection.prepareStatement(queryInsert);
            
            ResultSet rsMonstreID = stmtFindPersonnageID.executeQuery();
            if (rsMonstreID.next()) {
                id = Integer.parseInt(rsMonstreID.getString("nbpersonnage"))+1;
            }
            else{
                return id;
            }
            
            stmtInsert.setInt(1,id);
            stmtInsert.setInt(2,worldID); 
            stmtInsert.setString(3,this.getNom());
            stmtInsert.setString(4,this.getGenre());
            stmtInsert.setString(5,this.getRace());
            stmtInsert.setString(6,this.getMetier());
            stmtInsert.setInt(7,this.getPosition().getX());
            stmtInsert.setInt(8,this.getPosition().getY());
            stmtInsert.setInt(9,this.getPVieMax());
            stmtInsert.setInt(10,this.getPVie());
            stmtInsert.setInt(11,this.getDegatsAttaque());
            stmtInsert.setInt(12,this.getPourcentAttaque());
            stmtInsert.setInt(13,this.getPourcentParade());
            stmtInsert.setInt(14,this.getValeurParade());
            stmtInsert.setInt(15,this.getPMagieMax());
            stmtInsert.setInt(16,this.getPMagie());
            stmtInsert.setInt(17,this.getPortee());
            stmtInsert.setInt(18,this.getNbFleches());
            stmtInsert.executeUpdate();
            
            
            stmtFindPersonnageID.close();
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
