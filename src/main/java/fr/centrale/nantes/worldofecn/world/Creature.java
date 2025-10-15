/* --------------------------------------------------------------------------------
 * WoE
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */
package fr.centrale.nantes.worldofecn.world;

import java.sql.Connection;

/**
 * Playable creature
 * @author ECN
 */
public abstract class Creature extends ElementDeJeu {
    
    private int pourcentAttaque;
    private int degatsAttaque;
    private int pourcentEsquive;
    private int absorbe;
    private int pVieMax;
    private int pVie;

    /**
     * Constructor for Create
     * @param world
     */
    public Creature(World world) {
        super(world);
    }

    public Creature(int pourcentAttaque, int degatsAttaque, int pourcentEsquive, int absorbe, int pVieMax, int pVie, World world) {
        super(world);
        this.pourcentAttaque = pourcentAttaque;
        this.degatsAttaque = degatsAttaque;
        this.pourcentEsquive = pourcentEsquive;
        this.absorbe = absorbe;
        this.pVieMax = pVieMax;
        this.pVie = pVie;
    }

    /**
     * Get PourcentAttaque
     * @return
     */
    public int getPourcentAttaque() {
        return pourcentAttaque;
    }

    /**
     * Set PourcentAttaque
     * @param pourcentAttaque
     */
    public void setPourcentAttaque(int pourcentAttaque) {
        this.pourcentAttaque = pourcentAttaque;
    }

    /**
     * Get DegatsAttaque
     * @return
     */
    public int getDegatsAttaque() {
        return degatsAttaque;
    }

    /**
     * Set DegatsAttaque
     * @param degatsAttaque
     */
    public void setDegatsAttaque(int degatsAttaque) {
        this.degatsAttaque = degatsAttaque;
    }

    /**
     * Get PourcentEsquive
     * @return
     */
    public int getPourcentEsquive() {
        return pourcentEsquive;
    }

    /**
     * Set PourcentEsquive
     * @param pourcentEsquive
     */
    public void setPourcentEsquive(int pourcentEsquive) {
        this.pourcentEsquive = pourcentEsquive;
    }

    /**
     * Get Absorbe Degats
     * @return
     */
    public int getAbsorbe() {
        return absorbe;
    }

    /**
     * Set Absorbe Degats
     * @param absorbe
     */
    public void setAbsorbe(int absorbe) {
        this.absorbe = absorbe;
    }

    /**
     * Get PVieMax
     * @return
     */
    public int getPVieMax() {
        return pVieMax;
    }

    /**
     * Set PVieMax
     * @param pVieMax
     */
    public void setPVieMax(int pVieMax) {
        this.pVieMax = pVieMax;
        if (this.getPVie() > pVieMax) {
            this.pVie = pVieMax;
        }
    }

    /**
     * Get tPVie
     * @return
     */
    public int getPVie() {
        return pVie;
    }

    /**
     * Set tPVie
     * @param pVie
     */
    public void setPVie(int pVie) {
        this.pVie = pVie;
        if (this.getPVieMax() < pVie) {
            this.pVie = this.getPVieMax();
        }
    }
    
    
    
    /**
     *
     * @param x
     * @param y
     */
    public void tryToMoveTo(int x, int y) {
        
    }
}
