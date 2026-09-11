package server.servercommands;

import server.world.Position;
import server.world.World;

/**
 * Command that dumps the current state of the world.
 * This includes a visual representation of the map grid with robots and obstacles,
 * as well as any other relevant world information.
 */
public class DumpCommand extends ServerCommand {

    /**
     * Executes the dump command, which outputs the current state of the world.
     * Displays a grid representing robots (@), obstacles (#), and empty spaces (.),
     * followed by additional world state details.
     *
     * @return true after successfully dumping the state.
     */
    @Override
    public boolean execute() {
        // Calls the WorldState method to print or return the current state
        World world = getWorld();

        for (int i = world.getTopLeft().getX(); i <= world.getBottomRight().getX(); i ++){
            for (int j = world.getTopLeft().getY(); j >= world.getBottomRight().getY(); j--){
                switch (world.checkType(new Position(j, i))){
                    case "r":
                        System.out.print(" @ ");
                        break;
                    case "o":
                        System.out.print(" # ");
                        break;
                    default:
                        System.out.print(" . ");
                }
            }
            System.out.println();
        }
        System.out.println("key:\n- @ = robot\n- # = obstacle\n- . = empty block\n");

        world.WorldState();
        return true;
    }

    /**
     * Constructor for creating a DumpCommand with a specified world.
     *
     * @param world The world whose state will be dumped.
     */
    public DumpCommand(World world) {
        super("dump", world);
    }
}