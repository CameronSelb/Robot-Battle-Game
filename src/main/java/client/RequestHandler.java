package client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for validating and formatting client commands
 * into a json format that the server understands.
 */
public class RequestHandler {

    /** The name of the robot associated with this command validator. */
    private String robotName;

    /** A reusable JSON object used to construct messages. */
    private final JSONObject json = new JSONObject();


    /**
     * Constructs a new ValidateCommand for the specified robot name.
     *
     * @param robotName The name of the robot using commands.
     */
    public RequestHandler(String robotName){this.robotName = robotName;}


    /**
     * Handles the raw command input from the user, validates it, and
     * returns a JSON-formatted string that can be sent to the server.
     *
     * @param commandMessage The command entered by the user.
     * @return A JSON string representing the command or an empty string if invalid.
     */
    public String handleCommand(String commandMessage) {

        String[] args = commandMessage.trim().split(" ");
        JSONArray jsonArray = new JSONArray();

        for (String arg : args) {
            if (!arg.equals(args[0])){
                jsonArray.put(arg.toLowerCase());
            }
        }

        if (args[0].equals("launch") && robotName.isEmpty()){ setRobotName(args[args.length-1]); jsonArray.remove(args.length-1);}

        switch (args[0].toLowerCase()) {
            case "off" -> {
                return "off";
            }
            case "help" -> {
                showCommands();
                return "";
            }
            case "types" -> {
                showTypes();
                return "";
            }
        }

        return createJson(args[0], jsonArray);
    }

    public void setRobotName(String name){robotName = name;}

    /**
     * Displays a list of all available commands.
     */
    public void showCommands() {
        String commands = """
                Commands:
                <> - variable needed.
                -----------------------------------------
                - forward <steps> : move forward x steps.
                - back <steps> : move back x steps.
                - turn <left/right> : turn 90 degrees left or right.
                - look : shows objects north, east, south and east of you.
                - repair : fixes shield to full.
                - fire : shoots a bullet.
                - reload : reloads shots to max.
                - state : shows robots state.
                - off : disconnects from server.
                -----------------------------------------""";
        System.out.println(commands);
    }

    /**
     * Displays a list of all available robot types.
     */
    public void showTypes(){
        String commands = """
                Types:
                ---------------
                - Juggernaut
                - Tank
                - Soldier
                - Marksman
                - Sniper
                ---------------""";
        System.out.println(commands);
    }

    /**
     * Builds and returns a JSON string containing the command details.
     *
     * @param commandName The command name e.g., "forward".
     * @param args The arguments passed to the command.
     * @return A JSON formatted string with robot name, command, and arguments.
     */
    public String createJson(String commandName, JSONArray args) {
        json.put("robot", robotName);
        json.put("command", commandName);
        json.put("arguments", args);
        return json.toString();
    }
}
