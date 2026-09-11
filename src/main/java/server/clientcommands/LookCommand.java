package server.clientcommands;

import org.json.JSONArray;
import org.json.JSONObject;
import server.world.Direction;
import server.world.Position;
import server.world.Robot;
import server.world.World;
import server.obstacles.Obstacle;

import java.util.*;

/**
 * Handles the "look" command, allowing a robot to scan in all four cardinal directions
 * (NORTH, SOUTH, EAST, WEST) up to the configured look distance.
 * Reports obstacles, robots, and world edges within sight range.
 */
public class LookCommand extends Command{

    /**
     * Executes the look command.
     * The robot scans in all four directions (NORTH, SOUTH, EAST, WEST) for visible objects.
     *
     * @param world The game world in which the robot is operating.
     * @return A string starting with "OK#" followed by a JSONArray of detected objects
     *         including their type, direction, and distance.
     */
    @Override
    public String execute(World world) {
        JSONArray result = new JSONArray();
        Robot robot = world.getARobot(getRobotName());

        JSONArray northLook = look(robot.getPosition(),Direction.NORTH,world.getLookDistance(), world);
        JSONArray southLook = look(robot.getPosition(),Direction.SOUTH,world.getLookDistance(), world);
        JSONArray westLook = look(robot.getPosition(),Direction.WEST,world.getLookDistance(), world);
        JSONArray eastLook = look(robot.getPosition(),Direction.EAST,world.getLookDistance(), world);
        List<JSONArray> arrs = new ArrayList<>(Arrays.asList(northLook, southLook, westLook,eastLook));
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }

        return "OK#" + result;
    }

    /**
     * Looks in a specific direction from a starting position for a certain distance.
     * Detects and returns information about robots, obstacles, and edges.
     *
     * @param position Position of the robot.
     * @param facing   Direction in which to look.
     * @param distance Maximum number of steps to scan.
     * @param world    The world to check for objects and boundaries.
     * @return A JSONArray of visible objects in the specified direction.
     */
    public JSONArray look(Position position, Direction facing, int distance, World world){
        JSONArray objects = new JSONArray();

        for (int i = 1; i <= distance; i++){
            JSONObject object = createObjectForDistance(position, facing, i, world);
            if (object == null) {
                continue;
            }
            objects.put(object);

            if (shouldStopLooking(object, world)){
                break;
            }
        }

        return objects;
    }

    private boolean shouldStopLooking(JSONObject object, World world) {
        String type = object.getString("type");

        if (type.equals("EDGE")){return true;}

        if (type.equals("OBSTACLE")){
            return handleObstacle(object, world);
        }
        return false;
    }

    private boolean handleObstacle(JSONObject object, World world) {
        String type = object.getString("type");
        Obstacle ob = world.getAObstacle(new Position(object.getInt("x"), object.getInt("y")));
        object.put("type", type + ": " + ob.getType());
        return !ob.isTransparent();
    }

    private JSONObject createObjectForDistance(Position position, Direction facing, int distance, World world) {
        JSONObject object = new JSONObject();
        String type = "";
        Position newPosition;

        List<Integer> coords = move(facing, position.getX(), position.getY(), distance);
        newPosition = new Position(coords.get(0), coords.get(1));

        if (world.checkType(newPosition).isEmpty()){return null;}

        object.put("direction", facing);
        object.put("x", newPosition.getX());
        object.put("y", newPosition.getY());
        type = switch (world.checkType(newPosition)) {
            case "r" -> "ROBOT";
            case "o" -> "OBSTACLE";
            case "e" -> "EDGE";
            default -> type;
        };

        object.put("type", type);
        object.put("distance", distance);
        return object;
    }

    /**
     * Calculates a new coordinate after moving a certain number of steps in a given direction.
     *
     * @param facing The direction to move in.
     * @param x      Starting x-coordinate.
     * @param y      Starting y-coordinate.
     * @param step   Number of steps to move.
     * @return A list containing the new x and y coordinates.
     */
    public List<Integer> move(Direction facing, int x, int y, int step){
        List<Integer> coords = new ArrayList<>();

        switch (facing) {
            case NORTH:
                y = y + step;
                break;
            case EAST:
                x = x + step;
                break;
            case SOUTH:
                y = y - step;
                break;
            case WEST:
                x = x - step;
                break;
        }

        coords.add(x);
        coords.add(y);

        return coords;
    }

    /**
     * Constructs a new LookCommand for the given robot name and arguments.
     *
     * @param robotName The name of the robot using the look command.
     * @param args      The arguments passed to the command.
     */
    public LookCommand(String robotName, JSONArray args) {
        super("look", robotName, args);
    }
}
