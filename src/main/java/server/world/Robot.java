package server.world;

import java.util.ArrayList;

/**
 * Represents a robot with a name, position, direction, shields, shots, and status.
 */
public class Robot {
    private final String name;
    private Position position;
    private Direction direction;
    private int shield;
    private int shots;
    private int maxshield;
    private int maxshots;
    private String status;
    private final World world;
    private int hits;
    private int distance;
    private RobotTypes type;


    /**
     * Constructor to initialize a robot with the given name and starting position.
     * The robot starts with full shield and shots based on its type, and a status of NORMAL.
     *
     * @param name  The name of the robot.
     * @param world The world the robot belongs to.
     * @param position     The position of the robot's initial position.
     */
    public Robot(String name, World world, Position position, ArrayList<Integer> robotInfo) {
        this.name = name;
        this.world = world;
        this.position = position;
        this.maxshield = robotInfo.get(1);
        this.maxshots = robotInfo.getFirst();
        repairShield();
        reloadShots();
        this.distance = robotInfo.getLast();
        this.direction = Direction.NORTH;  // Start facing North
        this.status = "NORMAL";

    }

    /**
     * @return the robot's name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the position of the robot.
     */
    public Position getPosition() {
        return position;
    }


    /**
     * @return the direction the robot is currently facing.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return the current shield value of the robot.
     */
    public int getShield() { return shield; }

    /**
     * @return the number of shots remaining for the robot.
     */
    public int getShots() { return shots; }

    /**
     * @return the number of the max shots for the robot.
     */
    public int getMaxshots() { return maxshots; }

    /**
     * @return the current status of the robot e.g., NORMAL, RELOAD, DEAD.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return the distance the robot can shoot based on its type.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Sets the robot's position.
     *
     * @param position The new position.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Sets the number of shots available to the robot.
     *
     * @param shots Number of shots.
     */
    public void setShots(int shots){this.shots = shots;}

    /**
     * Reloads the robot's shots up to the minimum of its type's capacity and the world's limit.
     */
    public void reloadShots(){this.shots = Math.min(maxshots, world.getShots());}

    /**
     * Repairs the robot's shield up to the minimum of its type's capacity and the world's limit.
     */
    public void repairShield(){this.shield = Math.min(maxshield, world.getShields());}

    /**
     * Sets the robot's status.
     *
     * @param status The new status string.
     */
    public void setStatus(String status){this.status = status;}

    /**
     * Updates the robot's direction.
     *
     * @param direction New direction to face.
     */
    public void setDirection(Direction direction){
        this.direction = direction;
    }

    /**
     * Kills the robot by marking it as DEAD, setting shield and shots to 0, and removing it from the world.
     */
    public void setDead() {
        this.status = "DEAD";
        this.shield = 0;
        this.shots = 0;
    }

    /**
     * Checks if the robot is dead.
     *
     * @return true if the robot is dead, false otherwise.
     */
    public boolean isDead() {
        return "DEAD".equalsIgnoreCase(this.status);
    }

    /**
     * Applies a hit to the robot. Reduces shield, or kills the robot if shield is depleted.
     */
    public void takeHit(int hits) {
        shield -= hits;

        if (shield < 0) {
            setDead();
        }
    }

}
