package server.obstacles;

import server.world.Position;

public class Mines extends Obstacle{

    public Mines(Position position){super("mine", position);}

    @Override
    public boolean isTransparent() {
        return true;
    }

    @Override
    public boolean isPassable() {
        return true;
    }
}
