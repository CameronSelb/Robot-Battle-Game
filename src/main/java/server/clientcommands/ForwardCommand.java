package server.clientcommands;

import org.json.JSONArray;
import server.world.Direction;
import server.world.Position;
import server.world.Robot;
import server.world.World;

/**
 * Handles the "forward" command to move a robot forward
 * in the direction it is currently facing by a specified number of steps.
 * Movement stops if an obstacle, edge, or another robot blocks the path.
 */
public class ForwardCommand extends Command{

    /**
     * Constructs a ForwardCommand for the specified robot.
     *
     * @param robotName The name of the robot using the forward command.
     * @param args A JSONArray containing the number of steps to move forward.
     */
    public ForwardCommand(String robotName, JSONArray args){
        super("forward", robotName, args);

    }

    /**
     * Executes the forward movement for the robot.
     * The robot attempts to move forward step by step. If blocked by an obstacle,
     * edge, or another robot, it stops and reports the obstruction.
     *
     * @param world The world in which the robot is operating.
     * @return A response string indicating the outcome:
     *         - "OK#Done" if the robot successfully moved all steps.
     *         - "OK#Obstructed#Moved X steps blocked by:TYPE" if blocked.
     *         - "ERROR#Blocked by TYPE of world at [x,y]" if immediately blocked.
     *         - "Error#robot not found" if the robot does not exist in the world.
     */
    @Override
public String execute(World world) {
    Robot robot = world.getARobot(getRobotName());
    if (robot == null) return "Error#robot not found";
    Direction direction = robot.getDirection();
    String obstuction = "Obstructed";
    Position newPosition = new Position(robot.getPosition().getX(),robot.getPosition().getY());
    int steps = getArguments().getInt(0);
    Position lastValidPosition = newPosition;
    String objectType = "";

    for (int step = 1; step <= steps; step++) {
        Position nextPosition = calculateNextPosition(newPosition, direction, step);
        if (!world.isValidPosition(nextPosition)) {
            robot.setPosition(lastValidPosition);
            objectType = getObjectType(world, nextPosition, objectType);
            if (objectType.equals("EDGE")) {
                obstuction = "At the " + direction + " edge";
            }
            return "OK#"+ obstuction;
        }
        lastValidPosition = nextPosition;
    }
    robot.setPosition(lastValidPosition);
    return "OK#Done";
}

private Position calculateNextPosition(Position currentPosition, Direction direction, int step) {
    return switch (direction) {
        case NORTH -> new Position(currentPosition.getX(), currentPosition.getY() + step);
        case EAST  -> new Position(currentPosition.getX() + step, currentPosition.getY());
        case SOUTH -> new Position(currentPosition.getX(), currentPosition.getY() - step);
        case WEST  -> new Position(currentPosition.getX() - step, currentPosition.getY());
    };
}

private String getObjectType(World world, Position position, String defaultType) {
    return switch (world.checkType(position)) {
        case "e" -> "EDGE";
        case "r" -> "ROBOT";
        case "o" -> "OBSTACLE";
        default -> defaultType;
    };
}
}
