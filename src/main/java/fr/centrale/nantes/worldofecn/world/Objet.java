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
 * @author kwyhr
 */
public class Objet extends ElementDeJeu {

    private static final String OBJETPOTIONDESOIN = "Potion de Soin";
    private static final String OBJETPOTIONDUGUERRIER = "Potion du Guerrier";
    private static final String OBJETPOTIONDEMAGIE = "Potion de Magie";
    private static final String OBJETPOTIONDEMAGE = "Potion de Mage";
    private static final String OBJETEPEEDEMONIAQUE = "Epée démoniaque";
    private static final String OBJETARCENCHANTE = "Arc enchanté";
    private static final String OBJETARBRE = "Arbre";
    private static final String OBJETROCHER = "Rocher";
    private static final String OBJETNUAGETOXIQUE = "Nuage toxique";

    private String type;

    private static List<String> typesList;

    /**
     *
     */
    public static void init() {
        typesList = new ArrayList<String>();
        typesList.add(OBJETPOTIONDESOIN);
        typesList.add(OBJETPOTIONDUGUERRIER);
        typesList.add(OBJETPOTIONDEMAGIE);
        typesList.add(OBJETPOTIONDEMAGE);
        typesList.add(OBJETEPEEDEMONIAQUE);
        typesList.add(OBJETARCENCHANTE);
        typesList.add(OBJETARBRE);
        typesList.add(OBJETROCHER);
        typesList.add(OBJETNUAGETOXIQUE);
    }

    /**
     *
     * @return
     */
    public static int getNbTypes() {
        if (typesList != null) {
            return typesList.size();
        }
        return 0;
    }

    /**
     *
     * @param type
     * @return
     */
    public static String intToType(int type) {
        if (type < typesList.size()) {
            return typesList.get(type);
        }
        return "";
    }

    /**
     *
     * @param world
     */
    public Objet(World world) {
        super(world);
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Can any creature walk on this object
     * @return 
     */
    public boolean canWalkOn() {
        switch (this.getType()) {
            case OBJETARBRE :
            case OBJETROCHER :
                return false;
            default :
                return true;
        }
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
