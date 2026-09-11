package server.obstacles;

import server.world.Position;

/**
 * Represents a pit obstacle on the map.
 * Pits can be seen through but cannot be passed through.
 */
public class PitObstacle extends Obstacle {

    /**
     * Creates a pit obstacle at the specified position.
     *
     * @param position The position of the pit.
     */
    public PitObstacle(Position position) {
        super("pit", position);
    }

    /**
     * Indicates that pits are transparent.
     *
     * @return true, since pits are transparent.
     */
    @Override
    public boolean isTransparent() {
        return true;
    }

    /**
     * Indicates that pits are not passable.
     *
     * @return false, since pits block movement.
     */
    @Override
    public boolean isPassable() {
        return false;
    }
}
