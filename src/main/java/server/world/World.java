package server.world;

import server.configuration.Config;
import server.obstacles.LakeObstacle;
import server.obstacles.MountainObstacle;
import server.obstacles.Obstacle;
import server.obstacles.PitObstacle;
import server.servercommands.RobotsCommand;
import server.servercommands.ServerCommand;

import java.util.*;

/**
 * Represents the world where robots move and interact.
 * It keeps track of the map size, robots, and obstacles.
 */
public class World {
    private final Position topLeft;
    private final Position bottomRight;
    private final int width;
    private final int height;
    private final int shields;
    private final int shots;
    private final int reloadTime;
    private final int repairTime;
    private final List<Robot> robots;
    private final List<Obstacle> obstacles;
    private final int lookDistance;
    private final int setMineTime;

    /**
     * Creates a new world with all the configs and empty robot and obstacle lists.
     */
    public World(Config config) {
        this.width = config.getWidth();
        this.height = config.getHeight();
        this.robots = new ArrayList<>();
        this.obstacles = new ArrayList<>();

        this.topLeft = new Position(-(width / 2), (height / 2));
        this.bottomRight = new Position((width / 2), -(height / 2));
        initObstacles(config.getNumberOfObstacles());

        this.shields = config.getShields();
        this.shots = config.getShots();
        this.repairTime = config.getRepairTime();
        this.reloadTime = config.getReloadTime();
        this.setMineTime = config.getMineActivation();
        this.lookDistance = config.getLookDistance();
    }

    /**
     * Randomly initializes obstacles in the world.
     */
    private void initObstacles(int numberObs){

        Random random = new Random();
        List<String> obsType = new ArrayList<>(Arrays.asList("pit", "lake", "mount"));

        int i = 1;
        while (i <= numberObs){
            int type = random.nextInt(3);
            Position position = new Position(random.nextInt(topLeft.getX(), bottomRight.getX()),
                    random.nextInt(bottomRight.getY(), topLeft.getY()));

            if (!isValidPosition(position)){
                continue;
            }

            if (obsType.get(type).equals("pit")){
                addObstacle(new PitObstacle(position));
            } else if (obsType.get(type).equals("lake")) {
                addObstacle(new LakeObstacle(position));
            }else {
                addObstacle(new MountainObstacle(position));
            }

            i++;
        }
    }

    public List<Obstacle> getObstacles(){return obstacles;}

    public int getWidth(){return width;}

    public int getHeight(){return height;}

    /**
     * @return The bottom right position.
     */
    public Position getTopLeft() {
        return topLeft;
    }

    /**
     * @return The bottom right position.
     */
    public Position getBottomRight() {
        return bottomRight;
    }

    /**
     * @return The reload time in seconds.
     */
    public int getReloadTime(){return reloadTime;}

    /**
     * @return The repair time in seconds.
     */
    public int getRepairTime(){return repairTime;}

    /**
     * @return List of robots currently in the world.
     */
    public List<Robot> getRobots() {
        return robots;
    }

    /**
     * @return The time to set a mine.
     */
    public int getSetMineTime(){ return setMineTime; }

    /**
     * Gets a specific obstacle at the given position.
     *
     * @param p the position of the obstacle being found
     * @return The obstacle at that location or null.
     */
    public Obstacle getAObstacle(Position p){
        for (Obstacle o : obstacles){
            if (p.equals(o.getPosition())){
                return o;
            }
        }
        return null;
    }

    /**
     * Gets a robot by its name, if it's not dead.
     *
     * @param name Name of the robot.
     * @return The robot if found, otherwise null.
     */
    public Robot getARobot(String name){
        for (Robot r : robots){
            if (r.getName().equals(name)){
                return r;
            }
        }
        return  null;
    }

    /**
     * Gets a robot at the specified coordinates.
     *
     * @param p position of the robot being found
     * @return The robot at that location or null.
     */
    public Robot getARobot(Position p){
        for (Robot r : robots){
            if (p.equals(r.getPosition())){
                return r;
            }
        }
        return  null;
    }

    /**
     * @return Maximum look distance for robots.
     */
    public int getLookDistance() {return lookDistance;}

    /**
     * @return Number of shots available.
     */
    public int getShots() {return shots;}

    /**
     * @return Shield level available.
     */
    public int getShields() {return  shields;}

    /**
     * Checks if the robot name is unique in the world.
     *
     * @param RobotName The name to check.
     * @return True if the name is not used, false otherwise.
     */
    public boolean isValidRobotName(String RobotName){
        for (Robot r : robots) {
            if (r.getName().equals(RobotName.toLowerCase().trim())){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a position is valid (within bounds and not blocked).
     *
     * @param p the position given, which holds the x and y value.
     * @return True if the position is valid, false otherwise.
     */
    public boolean isValidPosition(Position p) {
        if (!p.isIn(topLeft, bottomRight)) {
            return false; // Outside world bounds
        }

        if (!isObstacle(p)){return false;}

        return isRobot(p);
    }

    public boolean isObstacle(Position p){
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPosition().equals(p)) {
                return obstacle.isPassable(); // Position occupied by an obstacle
            }
        }
        return true;
    }

    public boolean isRobot(Position p){
        for (Robot robot : robots) {
            if (robot.getPosition().equals(p)) {
                return false; // Position occupied by a robot
            }
        }
        return true;
    }

    /**
     * Removes a robot from the world.
     *
     * @param robot The robot to remove.
     */
    public void removeRobot(Robot robot) {
        System.out.println("Robot " + robot.getName() + " removed from the world.");
        robots.remove(robot);
    }

    /**
     * Adds a robot to the world at a valid position.
     *
     * @param robot The robot to add.
     */
    public void addRobot(Robot robot) {
        if (isValidPosition(robot.getPosition())){
            robots.add(robot);
            System.out.println("Robot " + robot.getName() + " added at position (" +
                    robot.getPosition().getX() + ", " + robot.getPosition().getY() + ")");
        } else {
            System.out.println("Invalid position for robot " + robot.getName() + ". Position outside world bounds.");
        }
    }

    /**
     * Adds an obstacle to the world if the position is valid.
     *
     * @param obstacle The obstacle to add.
     */
    public void addObstacle(Obstacle obstacle) {
        if (isValidPosition(obstacle.getPosition())) {
            obstacles.add(obstacle);
        } else {
            System.out.println("Invalid position for obstacle. Position outside world bounds.");
        }
    }

    /**
     * Checks if there is at least one valid open space in the world.
     *
     * @return True if an open space is available, false otherwise.
     */
    public boolean checkForOpenSpaces(){
        for (int i = topLeft.getX(); i <= bottomRight.getX(); i ++){
            for (int j = topLeft.getY(); j >= bottomRight.getY(); j--){
                if (isValidPosition(new Position(j,i))){return true;}
            }
        }
        return false;
    }

    /**
     * Checks the type of entity at a position.
     *
     * @param position the position given.
     * @return "r" for robot, "o" for obstacle, "e" for edge, "" for empty/valid.
     */
    public String checkType(Position position){

        for (Obstacle ob : obstacles){
            if (ob.getPosition().equals(position)){return "o";}
        }

        if (!position.isIn(topLeft, bottomRight)){
            return "e";
        }

        if (!isRobot(position)){
            return "r";
        }
        return "";
    }

    /**
     * Prints the current state of the world, including robots and obstacles.
     */
    public void WorldState() {
        System.out.println("World State: " + width + "x" + height);
        ServerCommand robots = new RobotsCommand(this);
        robots.execute();
        System.out.println("Obstacles:");
        for (Obstacle obstacle : obstacles) {
            System.out.println("- " + obstacle.getType() + " at (" + obstacle.getPosition().getX() +
                    ", " + obstacle.getPosition().getY() + ")");
        }
    }

    public void restoreWorld(String worldName) {
    }
}
