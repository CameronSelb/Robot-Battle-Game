package server.clientcommands;

import org.json.JSONArray;
import org.json.JSONException;
import server.world.Position;
import server.world.Robot;
import server.world.RobotTypes;
import server.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Handles the "launch" command to introduce a new robot into the world.
 * A robot is placed at a random open position, if available, and is assigned a specific type.
 */
public class LaunchCommand extends Command{

    private int shots;
    private int shield;
    private int distance;

    /**
     * Constructs a LaunchCommand for the given robot name and arguments.
     *
     * @param name The name of the robot to be launched.
     * @param args A JSONArray containing one element: the robot type as a string.
     */
    public LaunchCommand(String name, JSONArray args){
        super("launch", name, args);
    }

    /**
     * Executes the launch command by attempting to place a new robot in the world:
     * - Checks for available space.
     * - Randomly finds a valid position.
     * - Validates the robot name and type.
     * - Creates and adds the robot to the world.
     *
     * @param world The game world into which the robot should be launched.
     * @return A string indicating the outcome:
     *         - "OK" if the robot was successfully launched.
     *         - "ERROR#No space left in the world" if the world is full.
     *         - "ERROR#Too many of you in this world" if the robot name is already taken.
     *         - "ERROR#Not a valid robot type" if the robot type is unrecognized.
     */
    @Override
    public String execute(World world){
        Random random = new Random();
        Position position = new Position(0,0);

        String type = getArguments().getString(0).toLowerCase();
        RobotTypes robotType;

        if (!world.checkForOpenSpaces()){
            return "ERROR#No more space in this world";
        }

        while (!world.isValidPosition(position)){
            position = new Position(random.nextInt(world.getTopLeft().getX(), world.getBottomRight().getX()),
                    random.nextInt(world.getBottomRight().getY(), world.getTopLeft().getY()));
        }


        if (!world.isValidRobotName(this.getRobotName().toLowerCase()) ){
            return "ERROR#Too many of you in this world";
        }
        switch (type){
            case "juggernaut":
                robotType = RobotTypes.JUGGERNAUT;
                setArguments(robotType.getTypeShots(), robotType.getTypeShield(),robotType.getTypeDistance());
                break;
            case "tank":
                robotType = RobotTypes.TANK;
                setArguments(robotType.getTypeShots(), robotType.getTypeShield(),robotType.getTypeDistance());
                break;
            case "soldier":
                robotType = RobotTypes.SOLDIER;
                setArguments(robotType.getTypeShots(), robotType.getTypeShield(),robotType.getTypeDistance());
                break;
            case "marksman":
                robotType = RobotTypes.MARKSMAN;
                setArguments(robotType.getTypeShots(), robotType.getTypeShield(),robotType.getTypeDistance());
                break;
            case "sniper":
                robotType = RobotTypes.SNIPER;
                setArguments(robotType.getTypeShots(), robotType.getTypeShield(),robotType.getTypeDistance());
                break;
            case "miner":
                robotType = RobotTypes.MINER;
                setArguments(robotType.getTypeShots(), robotType.getTypeShield(),robotType.getTypeDistance());
                break;
            default:
                try{
                    shots = getArguments().getInt(1);
                    shield = getArguments().getInt(2);
                    distance = world.getLookDistance();
                } catch (JSONException e) {
                    return "ERROR#Not a valid robot type";
                }
        }

        Robot robot = new Robot(this.getRobotName(), world, position, new ArrayList<>(Arrays.asList(shots, shield, distance)));
        world.addRobot(robot);
        return "OK";
    }

    private void setArguments(int shots, int shield, int distance){
        this.shots = shots;
        this.shield = shield;
        this.distance = distance;
    }
}
