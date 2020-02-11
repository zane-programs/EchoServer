import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a server that accepts connections from EchoClients
 * and processes requests.
 */
public class EchoServer {
    private static String readFile(String path) {
        // file reading help thanks to https://www.w3schools.com/java/java_files_read.asp
        try {
            StringBuilder fileContents = new StringBuilder(); // file contents dumped here

            File file = new File(path);
            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                fileContents.append(line).append("\n");
            }

            return fileContents.toString().trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "File Not Found";
        }
    }

    private static String parseRequestBody(String body) {
        // this method parses VALID requests, as the main method handles the rest ;)
        String response;
        int bodyTxtExtPosition = body.indexOf(".txt");
        if (bodyTxtExtPosition != -1 && body.substring(bodyTxtExtPosition).equals(".txt")) { // temp
            response = readFile("../resources/server" + body);
        } else {
            // normal response
            response = String.format("You requested %s", body);
        }
        return response;
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        int port = 8080;
        String request = null;
        String response = null;

        Socket socket = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;

        String[] opts = {"-h", "--help", "-p", "--port"};


        //////////////////////////////////////////////
        // Parse args
        //////////////////////////////////////////////

        List<String> argsList = Arrays.asList(args);

        // check for help arg
        if (argsList.contains("--help") || argsList.contains("-h")) {
            printHelpMessage();
            return;
        }

        // check all other args have form "--command param" or "-c param"
        for (int i = 0; i < args.length; i+=2)
            if (!Arrays.asList(opts).contains(args[i]))
                throw new IllegalArgumentException("Invalid arguments, pass \"--help\" or \"-h\" for help");

        // check for port arg
        if (argsList.contains("--port")) {
            port = Integer.parseInt(argsList.get(argsList.indexOf("--port")+1));
        }
        else if (argsList.contains("-p")) {
            port = Integer.parseInt(argsList.get(argsList.indexOf("-p")+1));
        }


        //////////////////////////////////////////////
        // Serve clients
        //////////////////////////////////////////////

        // create server socket
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server listening on port " + port);

        // loop forever
        while (true) {
            try {
                // accept client connection
                assert serverSocket != null; // Added at the suggestion of the IDE (IntelliJ IDEA)
                socket = serverSocket.accept();

                // setup input and output streams
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());

                // get request from client
                request = (String)input.readObject();

                // TODO: STUDENT WORK
                // parse request message and create response
                String[] parsedRequest = request.split(" ");
                String preamble = parsedRequest[0];
                String requestBody = parsedRequest[1];
                if (parsedRequest.length == 2 && preamble.equals("GET") && requestBody.charAt(0) == '/' && requestBody.charAt(1) != '/') {
                    // valid request
                    response = parseRequestBody(requestBody);
                } else {
                    // INVALID response
                    response = "INVALID";
                }
                // END STUDENT WORK

                // send response
                output.writeObject(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void printHelpMessage() {
        System.out.println(
                "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +
                "\nEchoServer \n\tRepresents a server that accepts connections from EchoClients and \n\tprocesses requests." +
                "\n\nOptions:" +
                        "\n\t-p, --port : Specify the port on which the server listens." +
                        "\n\t-h, --help : Print this message." +
                "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
        );
    }
}
