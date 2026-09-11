package server.clientcommands;

import org.json.JSONArray;
import server.obstacles.Mines;
import server.obstacles.Obstacle;
import server.world.Robot;
import server.world.World;

import java.util.List;

public class MineCommand extends Command{

    public MineCommand(String robotName, JSONArray args){super("mine", robotName, args);}


    @Override
    public String execute(World world) {
        Robot robot = world.getARobot(getRobotName());
        Command move = new ForwardCommand(getRobotName(), new JSONArray(List.of(1)));
        int time = world.getSetMineTime() * 1000;

        if (robot.getMaxshots() != 0) {return "OK#This robot can't drop mines!";}
        try {
            Obstacle mine = new Mines(robot.getPosition());
            robot.setStatus("SETMINE");
            String result = move.execute(world);
            Thread.sleep(time);
            if (result.contains("Obstructed") || result.contains("edge")){
                robot.takeHit(3);
                return "OK#Mine";
            }
            world.addObstacle(mine);
        }catch (InterruptedException e){
            return "ERROR#Interrupted";
        }

        return "OK#Done";
    }
}
