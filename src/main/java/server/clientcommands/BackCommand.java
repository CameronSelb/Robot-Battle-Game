package server.clientcommands;

import org.json.JSONArray;
import server.world.Position;
import server.world.Robot;
import server.world.World;

/**
 * Handles the "back" command to move a robot backward by a specified number of steps.
 * The robot moves in the opposite direction it is currently facing.
 */
public  class BackCommand  extends Command{

    /**
     * Constructs a BackCommand for a specific robot with given arguments.
     *
     * @param robotName The name of the robot to move.
     * @param args A JSONArray containing the number of steps to move backward. Expected to have at least one integer.
     */
    public BackCommand(String robotName, JSONArray args){
        super("back", robotName, args);
    }

    /**
     * Executes the back command by moving the robot backward within the world.
     * If the robot encounters an obstacle, another robot, or the edge of the world,
     * movement stops and an error or status message is returned.
     *
     * @param world The World instance representing the environment where the robot operates.
     * @return A status string indicating the result of the move:
     *         - "OK Done" if the robot successfully moved all steps backward,
     *         - "OK Obstructed Moved X steps blocked by:TYPE",
     *         - "ERROR Blocked by TYPE at [x,y]" if blocked immediately,
     *         - "Error#robot not found" if the robot does not exist in the world.
     */
    @Override
public String execute(World world) {
    Robot robot = world.getARobot(getRobotName());
    if (robot == null) return "Error#robot not found";
    Position newPosition = new Position(robot.getPosition().getX(), robot.getPosition().getY());
    int steps = -getArguments().getInt(0);
    Position lastValidPosition = newPosition;
    String objectType = "";

    for (int step = -1; step >= steps; step--) {
        Position nextPosition = calculateNextPosition(robot, newPosition, step);

        if (!world.isValidPosition(nextPosition)) {
            robot.setPosition(lastValidPosition);

            objectType = getObjectType(world, nextPosition, objectType);

            if (step == -1) {
                return "ERROR#Blocked by " + objectType + " of world at [" + nextPosition.getX() + "," + nextPosition.getY() + "]";
            }
            return "OK#Obstructed#Moved " + (step - 1) + " steps blocked by:" + objectType;
        }
        lastValidPosition = nextPosition;
    }
    robot.setPosition(lastValidPosition);
    return "OK#Done";
}

private Position calculateNextPosition(Robot robot, Position currentPosition, int step) {
    return switch (robot.getDirection()) {
        case NORTH -> new Position(currentPosition.getX(), currentPosition.getY() + step);
        case EAST -> new Position(currentPosition.getX() + step, currentPosition.getY());
        case SOUTH -> new Position(currentPosition.getX(), currentPosition.getY() - step);
        case WEST -> new Position(currentPosition.getX() - step, currentPosition.getY());
    };
}

private String getObjectType(World world, Position position, String currentType) {
    return switch (world.checkType(position)) {
        case "e" -> "EDGE";
        case "r" -> "ROBOT";
        case "o" -> "OBSTACLE";
        default -> currentType;
    };
}
}