/* --------------------------------------------------------------------------------
 * WoE
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */

package fr.centrale.nantes.worldofecn.world;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
                this.setPourcentAttaque(30.0f);
                this.setDegatsAttaque(3.0f);
                this.setPourcentEsquive(20.0f);
                this.setAbsorbe(2.0f);
                this.setPVieMax(20.0f);
                break;
            case RACEOURS :
                this.setPourcentAttaque(30.0f);
                this.setDegatsAttaque(5.0f);
                this.setPourcentEsquive(5.0f);
                this.setAbsorbe(3.0f);
                this.setPVieMax(40.0f);
                break;
            case RACEBUFFLE :
                this.setPourcentAttaque(20.0f);
                this.setDegatsAttaque(5.0f);
                this.setPourcentEsquive(0.0f);
                this.setAbsorbe(2.0f);
                this.setPVieMax(30.0f);
                break;
            case RACEJAGUAR :
                this.setPourcentAttaque(20.0f);
                this.setDegatsAttaque(2.0f);
                this.setPourcentEsquive(20.0f);
                this.setAbsorbe(1.0f);
                this.setPVieMax(20.0f);
                break;
            case RACEVACHE :
                this.setPourcentAttaque(20.0f);
                this.setDegatsAttaque(4.0f);
                this.setPourcentEsquive(0.0f);
                this.setAbsorbe(2.0f);
                this.setPVieMax(20.0f);
                break;
            case RACELAPIN :
                this.setPourcentAttaque(1.0f);
                this.setDegatsAttaque(0.1f);
                this.setPourcentEsquive(40.0f);
                this.setAbsorbe(0.5f);
                this.setPVieMax(5.0f);
                break;
            default : // UNDEFINED
                this.setPourcentAttaque(0.0f);
                this.setDegatsAttaque(0.0f);
                this.setPourcentEsquive(0.0f);
                this.setAbsorbe(0.0f);
                this.setPVieMax(0.0f);
                break;
        }
        this.setPVie(this.getPVieMax());
    }


    @Override
    public Integer saveToDatabase(Connection connection) {
        Integer id = -1;

        return id;
    }

    @Override
    public void getFromDatabase(Connection connection, Integer id) {
    }

    @Override
    public void removeFromDatabase(Connection connection, Integer id) {
    }
    
}
