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
    
    public Objet(String typeobjet, int coordx, int coordy, World world) {
        super(world);
        this.type = typeobjet;
        super.setPosition(new Point2D(coordx,coordy));
        
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
    public Integer saveToDatabase(Connection connection, Integer worldID) {
        Integer id = -1;
        
        
            String queryFindObjetID = "Select COUNT(*) AS nbobjets FROM objet";
            String queryInsert = "INSERT INTO objet VALUES (?,?,?,?,?)";
            
            try{
            PreparedStatement stmtFindObjetID = connection.prepareStatement(queryFindObjetID);
            PreparedStatement stmtInsert = connection.prepareStatement(queryInsert);
            
            ResultSet rsObjetID = stmtFindObjetID.executeQuery();
            if (rsObjetID.next()) {
                id = Integer.parseInt(rsObjetID.getString("nbobjets"))+1;
            }
            else{
                return id;
            }
            
            stmtInsert.setInt(1,id);
            stmtInsert.setInt(2,worldID); 
            stmtInsert.setString(3,this.getType());
            stmtInsert.setInt(4,this.getPosition().getX());
            stmtInsert.setInt(5,this.getPosition().getY());
            stmtInsert.executeUpdate();
            
            
            stmtFindObjetID.close();
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
