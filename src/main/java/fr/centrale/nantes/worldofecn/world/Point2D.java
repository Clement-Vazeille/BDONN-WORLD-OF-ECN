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
public class Point2D {

    private int x;
    private int y;
    
    private ElementDeJeu elementdejeu;

    /**
     * Custructor for empty point
     */
    public Point2D() {
        this((ElementDeJeu)null);
    }

    /**
     * Defaut constructor for a point for an ElmeentDeJeu
     * @param elementdejeu 
     */
    public Point2D(ElementDeJeu elementdejeu) {
        this(0, 0, elementdejeu);
    }

    /**
     * Constructor with the 2 coordinates
     *
     * @param x
     * @param y
     */
    public Point2D(int x, int y) {
        this(x, y , (ElementDeJeu)null);
    }

    /**
     * Constructor with the 2 coordinates for an ElmeentDeJeu
     *
     * @param x
     * @param y
     * @param elementdejeu
     */
    public Point2D(int x, int y, ElementDeJeu elementdejeu) {
        this.x = x;
        this.y = y;
        this.elementdejeu = elementdejeu;
    }

    /**
     * Copy point
     *
     * @param point
     */
    public Point2D(Point2D point) {
        this(point.x, point.y);
    }

    /**
     * Get X value
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Set X value
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get Y value
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * Set Y value
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Get elementdejeu
     * @return 
     */
    public ElementDeJeu getElementDeJeu() {
        return this.elementdejeu;
    }

    /**
     * Move to position
     *
     * @param x
     * @param y
     */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Move in this direction
     *
     * @param dx : -1, 0, 1
     * @param dy : -1, 0, 1
     */
    public void directionTo(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * ensure the point is in the world
     * 
     * @param world
     */
    public void ensureInWorld(World world) {
        if (world != null) {
            if (this.x < 0) {
                this.x = 0;
            } else if (this.x >= world.getWidth()) {
                this.x = world.getWidth() - 1;
            }
            if (this.y < 0) {
                this.y = 0;
            } else if (this.y >= world.getHeight()) {
                this.y = world.getHeight() - 1;
            }
        }
    }

    /**
     * Check if the position is already used
     * @param world
     * @return 
     */
    public boolean isAlreadyUsed(World world) {
        if ((world != null) && (world.getPositions() != null)) {
            // Point2D overrides compareTo, so equals used Point2D compareTO method, and so do contains
            return world.getPositions().contains(this);
        }
        return false;
    }

    /**
     * Compare 2 points
     *
     * @param object
     * @return
     */
    public int compareTo(Object object) {
        if (object == null) {
            return 1;
        } else if (!(object instanceof Point2D)) {
            return 1;
        }
        Point2D objectPoint = (Point2D) object;
        if (this.getY() != objectPoint.getY()) {
            return this.getY() - objectPoint.getY();
        } else {
            return this.getX() - objectPoint.getX();
        }
    }

}
