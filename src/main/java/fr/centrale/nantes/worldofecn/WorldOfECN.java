/* --------------------------------------------------------------------------------
 * ECN Tools
 * 
 * Ecole Centrale Nantes - Septembre 2022
 * Equipe pédagogique Informatique et Mathématiques
 * JY Martin
 * -------------------------------------------------------------------------------- */

package fr.centrale.nantes.worldofecn;

import fr.centrale.nantes.worldofecn.world.*;

/**
 *
 * @author ECN
 */
public class WorldOfECN {

    /**
     * main program
     * @param args
     */
    public static void main(String[] args) {
        // Initialize data
        Personnage.init();
        Monstre.init();
        Objet.init();
        
        // Generate random world
        World world1 = new World();
        world1.setPlayer("Saegusa");
        
        // Test phase
        DatabaseTools database = new DatabaseTools();

        // Save world
        database.connect();
        Integer playerId = database.getPlayerID("Saegusa", "Mayumi");
        database.saveWorld(playerId, "Test Game 1", "Start", world1);
        
        // Retreive World
        World world2 = database.readWorld(playerId, "Test Game 1", "Start");
        // Check you retreived the values
        
        database.disconnect();
    }
}
