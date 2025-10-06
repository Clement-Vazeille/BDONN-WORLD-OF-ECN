/* --------------------------------------------------------------------------------
 * WoE
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */
package fr.centrale.nantes.worldofecn.world;

/**
 *
 * @author ECN
 */
public class Joueur {
    private String nom;
    private String login;
    private String password;
    
    private Personnage personnage;
    
    /**
     * Constructor for player
     * @param nom
     */
    public Joueur(String nom) {
        this(nom, null, null);
    }

    /**
     * Constructor for player
     * @param nom
     * @param login
     * @param password
     */
    public Joueur(String nom, String login, String password) {
        this.nom = nom;
        this.login = login;
        this.password = password;
    }

    /**
     * Get player name
     * @return
     */
    public String getNom() {
        return nom;
    }

    /**
     * Set player name
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Get player login
     * @return
     */
    public String getLogin() {
        return login;
    }

    /**
     * Set player login
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Get player password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set player password
     * @param password
     */
    public void setPassword(String password) {
        // Never do this. Password must be encrypted
        this.password = password;
    }
    
    /**
     *
     * @param password
     * @return
     */
    public boolean comparePassword(String password) {
        if ((this.getPassword() != null) && (password != null)) {
            // Should compare encrypted values
            return this.getPassword().equals(password);
        }
        return false;
    }

    /**
     * Get player's creature
     * @return
     */
    public Personnage getPersonnage() {
        return personnage;
    }

    /**
     * Set players creature
     * @param personnage
     */
    public void setPersonnage(Personnage personnage) {
        this.personnage = personnage;
    }
    
    
}
