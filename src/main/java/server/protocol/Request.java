package server.protocol;

import org.json.JSONArray;
import org.json.JSONObject;
import server.world.World;
import server.clientcommands.Command;

/**
 * Handles incoming requests by processing command data.
 */
public class Request {

    /** Constructor. */
    public Request(){}

    /**
     * Parses a JSON string representing a client request, creates the corresponding Command,
     * executes it in the context of the given World, and returns a JSON-formatted response string.
     *
     * @param json       The JSON object received from the client representing the command request.
     * @param world      The World instance in which the command will be executed.
     * @return           A JSON string representing the result of executing the command.
     */
    public String handleRequest(JSONObject json, World world){
        Command command = null;
        Response responseHandler = new Response(world);

        String robotName = json.getString("robot");
        String commandName = json.getString("command");
        JSONArray args = json.getJSONArray("arguments");

        if (commandValidation(commandName, args)) {
            command = Command.create(commandName, robotName, args);
        }

        return responseHandler.handleResponse(command);
    }


    /**
     * Validates a command based on its name and expected argument rules.
     *
     * @param commandName The command name e.g., "forward", "turn".
     * @param args A JSONArray containing the arguments to the command.
     * @return true if the command and its arguments are valid; false otherwise.
     */

    public boolean commandValidation(String commandName, JSONArray args) {
        return switch (commandName) {
            case "launch" -> validateLaunchCommand(args);
            case "forward", "back" -> validateForwardBackCommand(args);
            case "turn" -> validateTurnCommand(args);
            case "fire", "reload", "repair", "look", "state", "mine" -> args.isEmpty();
            default -> false;
        };
    }

    private boolean validateLaunchCommand(JSONArray args) {
        if (args.length() != 3 && args.length() != 2) {
            return false;
        }

        if (args.length() != 4) {
            return true;
        }

        return isInteger(args.getString(1)) && isInteger(args.getString(2));
    }

    private boolean validateForwardBackCommand(JSONArray args) {
        if (args.length() != 1) {
            return false;
        }

        return isInteger(args.getString(0));
    }

    private boolean validateTurnCommand(JSONArray args) {
        if (args.length() != 1) {
            return false;
        }

        return args.getString(0).equals("left") || args.getString(0).equals("right");
    }

    /**
     * Checks if a given string can be parsed as an integer.
     *
     * @param str The string to check.
     * @return true if the string is an integer, false otherwise.
     */
    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}


