package server.obstacles;

import server.world.Position;

/**
 * Represents a mountain obstacle on the map.
 * Mountains cannot be seen through or passed through.
 */
public class MountainObstacle extends Obstacle {

    /**
     * Creates a mountain obstacle at the specified position.
     *
     * @param position The position of the mountain.
     */
    public MountainObstacle(Position position) {
        super("mountain", position);
    }

    /**
     * Indicates that mountains are not transparent.
     *
     * @return false, since mountains blocks vision.
     */
    @Override
    public boolean isTransparent() {
        return false;
    }

    /**
     * Indicates that mountains are not passable.
     *
     * @return false, since mountains block movement.
     */
    @Override
    public boolean isPassable() {
        return false;
    }
}
