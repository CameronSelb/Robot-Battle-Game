package webApi;

import io.javalin.Javalin;
import server.Server;
import server.obstacles.Obstacle;
import server.world.Robot;
import server.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobotWorldApi {
    private final Server server;                // the main game server that runs everything
    private final World world;                  // represents the "game world" (robots, obstacles, size, etc.)

    // constructor: when we create RobotWorldApi, we give it a server
    // from that server, it also connects to the database and gets the world
    public RobotWorldApi(Server server) {
        this.server = server;        this.world = server.getWorld();
    }

    // this method starts the web API on the given port
    // Javalin is used to create lightweight web servers
    public void start(int port) {
        Javalin app = Javalin.create().start(port);
        // endpoint for world state
        // this is a GET request: e.g. GET /world/state
        // it will return the current world’s state (robots + obstacles + size)
        app.get("/world/state", ctx -> {
            try {
                ctx.json(worldStateToJson()); // return the simplified JSON version of the world state
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", "Could not fetch world state"));
            }
        });
    }

    // converts the full world into a JSON-friendly map
    // this lets us send it back to the user through the API
    private Map<String, Object> worldToJson(World world) {
        Map<String, Object> json = new HashMap<>();
        if (world == null) return json;

        json.put("width", world.getWidth());
        json.put("height", world.getHeight());
        json.put("shields", world.getShields());
        json.put("shots", world.getShots());
        json.put("reloadTime", world.getReloadTime());
        json.put("repairTime", world.getRepairTime());
        json.put("lookDistance", world.getLookDistance());
        json.put("setMineTime", world.getSetMineTime());
        json.put("robots", getRobotsAsJson(world));
        json.put("obstacles", getObstaclesAsJson(world));
        return json;
    }

    // gets all robots in the world and converts them to JSON maps
    private List<Map<String, Object>> getRobotsAsJson(World world) {
        List<Map<String, Object>> robots = new ArrayList<>();
        if (world.getRobots() == null) return robots;

        for (Robot robot : world.getRobots()) {        // loop through each robot
            Map<String, Object> robotJson = new HashMap<>();
            robotJson.put("name", robot.getName());    // robot name
            robotJson.put("position", robot.getPosition()); // robot position (x,y)
            robots.add(robotJson);
        }
        return robots;
    }

    // gets all obstacles in the world and converts them to JSON maps
    private List<Map<String, Object>> getObstaclesAsJson(World world) {
        List<Map<String, Object>> obstacles = new ArrayList<>();
        if (world.getObstacles() == null) return obstacles;

        for (Obstacle obstacle : world.getObstacles()) {   // loop through each obstacle
            Map<String, Object> obstacleJson = new HashMap<>();
            obstacleJson.put("type", obstacle.getType());  // obstacle type (e.g., square, pit, etc.)
            obstacleJson.put("position", obstacle.getPosition()); // position of the obstacle
            obstacles.add(obstacleJson);
        }
        return obstacles;
    }

    // creates a smaller/simpler version of the world JSON
    // instead of full details, it only shows dimensions, robots, and obstacles
    private Map<String, Object> worldStateToJson() {
        Map<String, Object> state = new HashMap<>();
        if (world != null) {
            state.put("dimensions", world.getWidth() + "x" + world.getHeight()); // world size in "WxH" format
            state.put("robots", getRobotsAsJson(world));       // list
            state.put("obstacles", getObstaclesAsJson(world)); // list of obstacles
        }
        return state;
    }
}
