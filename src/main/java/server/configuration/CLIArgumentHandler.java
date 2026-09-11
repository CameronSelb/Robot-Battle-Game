package server.configuration;

import server.world.Position;
import server.world.World;
import server.obstacles.MountainObstacle;

public class CLIArgumentHandler {

    public void checkCLIArguments(Config config , String key, String value){
        switch (key){
            case "-p", "--port" -> config.setPort(Integer.parseInt(value));

            case "-s", "--size" -> {
                config.setHeight(Integer.parseInt(value));
                config.setWidth(Integer.parseInt(value));
            }

        }
    }

    public void handleObstacle(World world, String value){
        String[] coords = value.split(",");
        world.addObstacle(new MountainObstacle(new Position(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim()))));
    }
}
