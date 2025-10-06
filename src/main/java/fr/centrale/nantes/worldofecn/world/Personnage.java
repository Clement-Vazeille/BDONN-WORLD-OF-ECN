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

    private float pourcentParade;
    private float valeurParade;
    private float pMagieMax;
    private float pMagie;
    private float portee;

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
    public float getPourcentParade() {
        return pourcentParade;
    }

    /**
     *
     * @param pourcentParade
     */
    public void setPourcentParade(float pourcentParade) {
        this.pourcentParade = pourcentParade;
    }

    /**
     *
     * @return
     */
    public float getValeurParade() {
        return valeurParade;
    }

    /**
     *
     * @param valeurParade
     */
    public void setValeurParade(float valeurParade) {
        this.valeurParade = valeurParade;
    }

    /**
     *
     * @return
     */
    public float getPMagieMax() {
        return pMagieMax;
    }

    /**
     *
     * @param pMagieMax
     */
    public void setPMagieMax(float pMagieMax) {
        this.pMagieMax = pMagieMax;
    }

    /**
     *
     * @return
     */
    public float getPMagie() {
        return pMagie;
    }

    /**
     *
     * @param pMagie
     */
    public void setPMagie(float pMagie) {
        this.pMagie = pMagie;
    }

    /**
     *
     * @return
     */
    public float getPortee() {
        return portee;
    }

    /**
     *
     * @param portee
     */
    public void setPortee(float portee) {
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
                this.setPourcentAttaque(30.0f);
                this.setDegatsAttaque(2.0f);
                this.setPourcentParade(10.0f);
                this.setValeurParade(0.0f);
                this.setPourcentEsquive(10.0f);
                this.setAbsorbe(0.0f);
                this.setPVieMax(20.0f);
                this.setPMagieMax(20.0f);
                this.setPortee(1.0f);
                break;
            case RACENAIN :
                this.setPourcentAttaque(40.0f);
                this.setDegatsAttaque(3.0f);
                this.setPourcentParade(10.0f);
                this.setValeurParade(1.0f);
                this.setPourcentEsquive(0.0f);
                this.setAbsorbe(1.0f);
                this.setPVieMax(25.0f);
                this.setPMagieMax(10.0f);
                this.setPortee(1.0f);
                break;
            case RACEELFE :
                this.setPourcentAttaque(25.0f);
                this.setDegatsAttaque(2.0f);
                this.setPourcentParade(15.0f);
                this.setValeurParade(0.0f);
                this.setPourcentEsquive(20.0f);
                this.setAbsorbe(0.0f);
                this.setPVieMax(30.0f);
                this.setPMagieMax(30.0f);
                this.setPortee(1.0f);
                break;
            case RACEGOBELIN :
                this.setPourcentAttaque(30.0f);
                this.setDegatsAttaque(2.0f);
                this.setPourcentParade(10.0f);
                this.setValeurParade(0.0f);
                this.setPourcentEsquive(10.0f);
                this.setAbsorbe(0.0f);
                this.setPVieMax(30.0f);
                this.setPMagieMax(10.0f);
                this.setPortee(1.0f);
                break;
            case RACETROLL :
                this.setPourcentAttaque(40.0f);
                this.setDegatsAttaque(4.0f);
                this.setPourcentParade(0.0f);
                this.setValeurParade(0.0f);
                this.setPourcentEsquive(0.0f);
                this.setAbsorbe(2.0f);
                this.setPVieMax(30.0f);
                this.setPMagieMax(0.0f);
                this.setPortee(1.0f);
                break;
            default : // UNDEFINED
                this.setPourcentAttaque(0.0f);
                this.setDegatsAttaque(0.0f);
                this.setPourcentParade(0.0f);
                this.setValeurParade(0.0f);
                this.setPourcentEsquive(0.0f);
                this.setAbsorbe(0.0f);
                this.setPVieMax(0.0f);
                this.setPMagieMax(0.0f);
                this.setPortee(1.0f);
                break;
        }
        this.setPVie(this.getPVieMax());
        this.setPMagie(this.getPMagieMax());
    }

    private void addMetierCaracteristiques() {
        switch (this.getMetier()) {
            case METIERGUERRIER :
                this.setPourcentAttaque(this.getPourcentAttaque() + 10.0f);
                this.setDegatsAttaque(this.getDegatsAttaque() + 2.0f);
                this.setPourcentParade(this.getPourcentParade() + 20.0f);
                this.setValeurParade(this.getValeurParade() + 5.0f);
                this.setPVieMax(this.getPVieMax() + 5.0f);
                break;
            case METIERARCHER :
                this.setPourcentAttaque(this.getPourcentAttaque() + 10.0f);
                this.setDegatsAttaque(this.getDegatsAttaque() + 1.0f);
                this.setPortee(3.0f);
                break;
            case METIERARBALETRIER :
                this.setPourcentAttaque(this.getPourcentAttaque() + 10.0f);
                this.setDegatsAttaque(this.getDegatsAttaque() + 3.0f);
                this.setPortee(3.0f);
                break;
            case METIERMAGE :
                this.setPortee(4.0f);
                break;
            case METIERPRETRE :
                this.setPortee(3.0f);
                break;
            case METIERPALADIN :
                this.setPourcentAttaque(this.getPourcentAttaque() + 5.0f);
                this.setDegatsAttaque(this.getDegatsAttaque() + 2.0f);
                this.setPourcentParade(this.getPourcentParade() + 20.0f);
                this.setValeurParade(this.getValeurParade() + 5.0f);
                this.setPVieMax(this.getPVieMax() + 5.0f);
                this.setPortee(2.5f);
                break;
            case METIERVOLEUR :
                this.setPourcentAttaque(this.getPourcentAttaque() + 15.0f);
                this.setDegatsAttaque(this.getDegatsAttaque() + 1.0f);
                this.setPVieMax(this.getPVieMax() + 5.0f);
                break;
            case METIERPAYSAN :
                this.setAbsorbe(this.getAbsorbe()+ 5.0f);
                this.setPVieMax(this.getPVieMax() + 5.0f);
                break;
            default : // UNDEFINED
                break;
        }
        this.setPVie(this.getPVieMax());
        this.setPMagie(this.getPMagieMax());
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
