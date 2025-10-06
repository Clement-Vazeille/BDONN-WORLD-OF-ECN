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
    
    private float pourcentAttaque;
    private float degatsAttaque;
    private float pourcentEsquive;
    private float absorbe;
    private float pVieMax;
    private float pVie;

    /**
     * Constructor for Create
     * @param world
     */
    public Creature(World world) {
        super(world);
    }

    /**
     * Get PourcentAttaque
     * @return
     */
    public float getPourcentAttaque() {
        return pourcentAttaque;
    }

    /**
     * Set PourcentAttaque
     * @param pourcentAttaque
     */
    public void setPourcentAttaque(float pourcentAttaque) {
        this.pourcentAttaque = pourcentAttaque;
    }

    /**
     * Get DegatsAttaque
     * @return
     */
    public float getDegatsAttaque() {
        return degatsAttaque;
    }

    /**
     * Set DegatsAttaque
     * @param degatsAttaque
     */
    public void setDegatsAttaque(float degatsAttaque) {
        this.degatsAttaque = degatsAttaque;
    }

    /**
     * Get PourcentEsquive
     * @return
     */
    public float getPourcentEsquive() {
        return pourcentEsquive;
    }

    /**
     * Set PourcentEsquive
     * @param pourcentEsquive
     */
    public void setPourcentEsquive(float pourcentEsquive) {
        this.pourcentEsquive = pourcentEsquive;
    }

    /**
     * Get Absorbe Degats
     * @return
     */
    public float getAbsorbe() {
        return absorbe;
    }

    /**
     * Set Absorbe Degats
     * @param absorbe
     */
    public void setAbsorbe(float absorbe) {
        this.absorbe = absorbe;
    }

    /**
     * Get PVieMax
     * @return
     */
    public float getPVieMax() {
        return pVieMax;
    }

    /**
     * Set PVieMax
     * @param pVieMax
     */
    public void setPVieMax(float pVieMax) {
        this.pVieMax = pVieMax;
        if (this.getPVie() > pVieMax) {
            this.pVie = pVieMax;
        }
    }

    /**
     * Get tPVie
     * @return
     */
    public float getPVie() {
        return pVie;
    }

    /**
     * Set tPVie
     * @param pVie
     */
    public void setPVie(float pVie) {
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
