package server.obstacles;

import server.world.Position;

/**
 * Base class for all obstacles in the world.
 * Provides common properties and methods for obstacle position and type.
 */
public abstract class Obstacle {
    private final Position position;
    private final String type;

    /**
     * Creates an obstacle at the given position with the specified type.
     *
     * @param type The type/name of the obstacle.
     * @param position    The position of the obstacle.
     */
    public Obstacle(String type, Position position){
        this.type = type;
        this.position = position;
    }

    /**
     * Returns the position of the obstacle.
     *
     * @return Position type.
     */
    public Position getPosition(){return position;}

    /**
     * Returns the type of the obstacle.
     *
     * @return Obstacle type as a string.
     */
    public String getType(){return type;}

    /**
     * Checks if the obstacle is transparent (can be seen through).
     *
     * @return true if transparent, false otherwise.
     */
    public abstract boolean isTransparent();

    /**
     * Checks if the obstacle is passable (can be moved through).
     *
     * @return true if passable, false otherwise.
     */
    public abstract boolean isPassable();

    /**
     * Returns a string representation of the obstacle's position.
     *
     * @return A string in the format: "Obstacle at (x, y)".
     */
    @Override
    public String toString(){
        return "Obstacle at (" + position.getX() + ", " + position.getY() + ")";
    }
}
