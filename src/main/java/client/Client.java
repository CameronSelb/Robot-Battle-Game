package client;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * The Client class connects to a robot server and allows a user to send commands
 * to control a robot in the game world.
 */
public class Client {

    /** The socket used to connect to the server. */
    private Socket socket;

    /** Writer used to send data to the server. */
    private BufferedWriter bufferedWriter;

    /** Reader used to receive data from the server. */
    private BufferedReader bufferedReader;

    /** Validates and formats commands into JSON format. */
    private RequestHandler requestHandler;

    /** Handles parsing and displaying server responses. */
    private ResponseHandler responseHandler;

    /**
     * Creates a client and connects it to the server,
     * and utility classes for command validation and response handling.
     *
     * @param socket the socket used for communication
     * @param robotName the name of the robot this client controls
     */
    public Client (Socket socket, String robotName){
        try {
            this.socket = socket;
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            requestHandler = new RequestHandler(robotName);
            responseHandler = new ResponseHandler();

        } catch (IOException e){
            closeClient(socket, bufferedReader, bufferedWriter);
        }

    }

    /**
     * Starts an input loop where the user can type commands, which are
     * validated, formatted, and sent to the server.
     * Server responses are printed via the ResponseHandler.
     */
    public void sendCommands() {
        try {
            boolean launched = false;
            Scanner scanner = new Scanner(System.in);
            requestHandler.handleCommand("types");

            while (socket.isConnected()) {
                System.out.print("\n> ");
                String messageToSendToServer = scanner.nextLine();
                String commandName = messageToSendToServer.toLowerCase().split(" ")[0];
                System.out.println();
                String jsonCommand = requestHandler.handleCommand(messageToSendToServer);

                if (jsonCommand.isEmpty()){continue;}

                if (jsonCommand.equals("off")){
                    closeClient(socket, bufferedReader,bufferedWriter);
                    break;
                }

                if (commandName.equals("launch") && launched) {
                    System.out.println("Error: You have already launched a robot");
                    continue;
                }

                String response = sendACommand(jsonCommand);

                if (response == null){
                    closeClient(socket, bufferedReader,bufferedWriter);
                    break;
                }


                JSONObject responseJSON = new JSONObject(response);

                if (commandName.equals("launch") && responseJSON.getString("result").equals("ERROR")) {requestHandler.setRobotName("");}

                if (commandName.equals("launch")){
                launched = true;
                }

                responseHandler.decodeResponse(commandName,responseJSON);
            }
        } catch (IOException e){
            closeClient(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * Sends a preformatted JSON command to the server and waits for a response.
     *
     * @param commandToSend the JSON formatted command string
     * @return the server's response as a string
     * @throws IOException if IO operations fail
     */
    public String sendACommand(String commandToSend) throws IOException{

        bufferedWriter.write(commandToSend);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        // Waiting on response from the server
        return bufferedReader.readLine();
    }



    /**
     * Closes all streams and the socket, and terminates the client application.
     *
     * @param socket the client socket
     * @param bufferedReader the input stream
     * @param bufferedWriter the output stream
     */
    public void closeClient(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            System.out.println("Disconnected from Server!");
            System.exit(0);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Entry point for the client application.
     * Prompts the user to launch a robot before sending any other commands.
     * Valid robot types are listed via the "types" command.
     *
     * @param args command line arguments
     * @throws IOException if the socket fails to connect
     */
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Connected to server ... ");
        System.out.println("Use launch command first e.g. launch <type> <name>\"");
        Client client = new Client(socket, "");
        client.sendCommands();
    }

}
