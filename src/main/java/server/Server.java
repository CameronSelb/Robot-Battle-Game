package server;

import za.co.wethinkcode.flow.Recorder;
import server.configuration.CLIArgumentHandler;
import server.configuration.Config;
import server.configuration.ConfigLoader;
import server.servercommands.ServerCommand;
import server.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Represents the server that manages client connections and executes server commands.
 */
public class Server {

    /** The ServerSocket that listens for client connections. */
    private final ServerSocket serverSocket;

    /** The config file that holds all the server & world info. */
    private final Config config;

    /** The world instance that holds the game state. */
    private  static  World world;

    /**
     * Constructor to initialize the server with a ServerSocket and a World instance.
     *
     * @param serverSocket the ServerSocket used to accept client connections
     * @param world the World object shared among all clients
     */
    public Server(ServerSocket serverSocket, World world, Config config) {
        this.serverSocket = serverSocket;
        this.world = world;
        this.config = config;
    }

    public  World getWorld() {
        return  this.world;
    }

    /**
     * Starts the server, accepting client connections and starting new threads to handle them.
     * Keeps accepting clients until the server socket is closed.
     */
    public void startServer() {
        try {
            while (!serverSocket.isClosed()){
                System.out.println("Waiting on client to connect");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(socket, world);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e){
            System.out.println("Server Shutdown.");
        }
    }

    /**
     * Listens for server commands entered by the user and processes them.
     * This runs in a separate thread to avoid blocking the main server loop.
     */
    public void listenForCommand(Server server) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String commandForServer;
                while (!serverSocket.isClosed()){
                    Scanner scanner = new Scanner(System.in);
                    commandForServer = scanner.nextLine();
                    System.out.println();
                    ServerCommand command = ServerCommand.create(commandForServer, world, server);
                    if (command == null){
                        System.out.println("ERROR : Invalid command!");
                        continue;
                    }
                    command.execute();

                    // Close the server when "quit" command is entered
                    if (command.getName().equals("quit")) {
                        closeServerSocket();
                    }
                }
            }
        }).start();
    }

    public void setWorld(World world){this.world = world;}

    public Config getConfig(){return config;}

    /**
     * Closes the server socket, shuts down the server.
     */
    public void closeServerSocket() {
        try {
            if (serverSocket != null){
                serverSocket.close();
            }
            System.exit(0);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method to run the server.
     * Binds the server to localhost on port 1234, then starts listening for client and console commands.
     *
     * @param args command line arguments
     * @throws IOException if there is an error during server socket creation
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        CLIArgumentHandler handler = new CLIArgumentHandler();

        Config config = ConfigLoader.loadConfig();

        for (int i = 0; i < args.length; i+=2){
            String key = args[i];
            String value = args[i+1];

            handler.checkCLIArguments(config, key, value);
        }

        if (config == null) { System.out.println("Error: loading the config file."); return;}
        serverSocket.bind(new InetSocketAddress("localhost", config.getPort()));
        World world = new World(config);

        for (int i = 0; i < args.length; i+=2){
            if (args[i].equals("-o") || args[i].equals("--obstacle")){handler.handleObstacle(world, args[i+1]);}
        }

        System.out.println("Server Started.\nPort: " + config.getPort() + "\nSize: " + config.getHeight() + "x" + config.getWidth());
        Server server = new Server(serverSocket, world, config);
        server.listenForCommand(server);
        server.startServer();

    }

    // The following initialisation is REQUIRED for `flow` monitoring.
    // DO NOT REMOVE OR MODIFY THIS CODE.
    static {
        new Recorder().logRun();
    }

}



