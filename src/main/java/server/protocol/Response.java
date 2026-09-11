package server.protocol;

import org.json.JSONArray;
import org.json.JSONObject;
import server.world.Robot;
import server.world.World;
import server.clientcommands.Command;

/**
 * Handles the creation of responses to commands executed in the world.
 */
public class Response {

    private World world;

    /**
     * Constructs a Response handler for the specified world.
     *
     * @param world The game world containing robots and state.
     */
    public Response(World world){this.world = world;}

    /**
     * Processes the execution result of a command and generates
     * a JSON response string representing the outcome.
     *
     * @param command The command to process.
     * @return A JSON formatted string response.
     */
    public String handleResponse(Command command){
        JSONObject json = new JSONObject();
        String[] outcome;

        if (command == null) {
            json.put("result", "ERROR");
            json.put("data", createErrorJson("Unsupported command"));
            return json.toString();
        }

        if (!command.getCommandName().equals("launch") && world.getARobot(command.getRobotName()) == null){
            json.put("result", "ERROR");
            json.put("data", createErrorJson("Launch a robot before using other commands"));
            return json.toString();
        }

        outcome = command.execute(world).split("#");
        Robot robot = world.getARobot(command.getRobotName());

        if (robot.getStatus().equals("DEAD")) {
            world.removeRobot(robot);
            System.out.println("removed " + robot.getName());
            json.put("result", "ERROR");
            json.put("data", createErrorJson("Your robot has died!"));
            return json.toString();
        }


        String result = outcome[0];
        String msg = "";
        if (outcome.length > 1) {
            msg = outcome[1];
        }

        // If result is an error, return error JSON
        if (!result.equals("OK")){
            json.put("result", result);
            json.put("data", createErrorJson(msg));
            return json.toString();
        }

        // Response
        json.put("result", result);

        // For "state" command, just return the robot's state
        if (command.getCommandName().equals("state")){
            json.put("state", createStateJson(robot));
            return json.toString();
        }

        // Attach data based on command type
        switch (command.getCommandName()){
            case "launch":
                json.put("data", createLaunchDataJson(robot));
                break;
            case "fire":
                json.put("data", createFireDataJson(msg));
                break;
            case "look":
                json.put("data", createLookDataJson(msg));
                break;

            default:
                json.put("data", createDataJson(msg));
        }

        // Always include the robot's state
        json.put("state", createStateJson(robot));
        // Reset robot status to normal after processing
        robot.setStatus("NORMAL");
        return json.toString();
    }

    /**
     * Creates a JSON object containing an error message.
     *
     * @param errorMsg The error message to include.
     * @return JSON object with the error message.
     */
    public JSONObject createErrorJson(String errorMsg){
        JSONObject json = new JSONObject();
        json.put("message", errorMsg);
        return json;
    }

    /**
     * Creates a JSON object with data relevant to a launched robot.
     *
     * @param robot The launched robot.
     * @return JSON object containing launch data.
     */
    public JSONObject createLaunchDataJson(Robot robot){
        JSONObject launch = new JSONObject();
        JSONArray position = new JSONArray();
        position.put(robot.getPosition().getX());
        position.put(robot.getPosition().getY());

        launch.put("position", position);
        launch.put("visibility", world.getLookDistance());
        launch.put("reload", world.getReloadTime());
        launch.put("repair", world.getRepairTime());
        launch.put("shields", world.getShields());

        return launch;
    }

    /**
     * Creates a JSON object representing the results of a "look" command.
     *
     * @param msg The JSON string containing objects seen.
     * @return JSON object with an array of objects.
     */
    public JSONObject createLookDataJson(String msg) {
        JSONObject Look = new JSONObject();
        JSONArray objects = new JSONArray(msg);
        Look.put("objects", objects);
        return Look;
    }

    /**
     * Creates a JSON object representing the results of a "fire" command.
     * If a robot is hit, updates the hit robot's state accordingly.
     *
     * @param msg The JSON string containing fire results.
     * @return JSON object representing the fire command results.
     */
    public JSONObject createFireDataJson(String msg){
        JSONObject fire = new JSONObject(msg);
        if (fire.getString("message").equals("Hit")) {
            Robot hitRobot = world.getARobot(fire.getString("robot"));
            fire.put("state", createStateJson(hitRobot));
        }
        return fire;
    }

    /**
     * Creates a generic JSON object with a message.
     *
     * @param msg The message to include.
     * @return JSON object containing the message.
     */
    public JSONObject createDataJson(String msg){
        JSONObject basic = new JSONObject();
        basic.put("message", msg);
        return basic;
    }

    /**
     * Creates a JSON object representing the robot's current state.
     *
     * @param robot The robot whose state is being represented.
     * @return JSON object containing the robot's state.
     */
    public JSONObject createStateJson(Robot robot){
        JSONObject json = new JSONObject();
        JSONArray position = new JSONArray();
        position.put(robot.getPosition().getX());
        position.put(robot.getPosition().getY());
        json.put("position", position);
        json.put("direction", robot.getDirection());
        json.put("shields", robot.getShield());
        json.put("shots", robot.getShots());
        json.put("status", robot.getStatus());

        return json;
    }
}
