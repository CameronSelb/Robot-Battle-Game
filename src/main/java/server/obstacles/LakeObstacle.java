package server.obstacles;

import server.world.Position;

/**
 * Represents a lake obstacle on the map.
 * Lakes cannot be passed through, but they can be seen through (transparent).
 */
public class LakeObstacle extends Obstacle {

    /**
     * Creates a lake obstacle at the specified position.
     *
     * @param position The position of the lake.
     */
    public LakeObstacle(Position position) {
        super("lake", position);
    }

    /**
     * Indicates that lakes are transparent.
     *
     * @return true, since lakes are transparent.
     */
    @Override
    public boolean isTransparent() {
        return true;
    }

    /**
     * Indicates that lakes are not passable.
     *
     * @return false, since lakes are not passable.
     */
    @Override
    public boolean isPassable() {
        return false;
    }
}