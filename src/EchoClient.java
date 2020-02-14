import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a client that connects to an EchoServer and sends requests.
 */
public class EchoClient {
    private static void saveToFile(String fileName, String fileText) {
        // file writing made possible thanks to https://stackoverflow.com/a/2885224
        try {
            PrintWriter fileWriter = new PrintWriter(fileName, "UTF-8");
            fileWriter.println(fileText);
            fileWriter.close();
        } catch (IOException e) {
            System.out.print("ERROR: Could not save to file");
            e.printStackTrace(); // show verbose error details
        }
    }

    public static void main(String[] args) {
        // You may choose to change these values for testing your code
        String server = "127.0.0.1"; // 127.0.0.1 is localhost on most systems
        int port = 8080;
        String requestPath= "/this/is/a/test";

        // ZANE MOD: raw request option
        String rawRequest = "";
        String request;
        String response;

        // ZANE MOD: file saving option
        String saveFileName = null;
        boolean shouldSaveToFile = false;

        Socket socket;
        ObjectOutputStream output;
        ObjectInputStream input;

        String[] opts = {"-h", "--help", "-r", "--request", "-p", "--port", "-s", "--server", "--raw", "--save"};


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

        // check for server arg
        if (argsList.contains("--server")) {
            server = argsList.get(argsList.indexOf("-server")+1);
        }
        else if (argsList.contains("-s")) {
            server = argsList.get(argsList.indexOf("-s")+1);
        }

        // check for port arg
        if (argsList.contains("--port")) {
            port = Integer.parseInt(argsList.get(argsList.indexOf("-port")+1));
        }
        else if (argsList.contains("-p")) {
            port = Integer.parseInt(argsList.get(argsList.indexOf("-p")+1));
        }

        // check for get arg
        if (argsList.contains("--request")) {
            requestPath = argsList.get(argsList.indexOf("--request")+1);
        }
        else if (argsList.contains("-r")) {
            requestPath = argsList.get(argsList.indexOf("-r")+1);
        }

        // check for save arg (saves response to a file)
        if (argsList.contains("--save")) {
            shouldSaveToFile = true;
            saveFileName = argsList.get(argsList.indexOf("--save")+1);
        }

        // check for raw arg (whether or not to make raw request)
        if (argsList.contains("--raw")) {
            String rawArg = argsList.get(argsList.indexOf("--raw")+1).toLowerCase();
            if (rawArg.equals("y") || rawArg.equals("yes")) {
                rawRequest = requestPath;
            }
        }

        String isRaw = rawRequest.equals("") ? "" : "(RAW) "; // shows (RAW) before displaying request
        String rawQuotationMark = rawRequest.equals("") ? "" : "\""; // puts quotation marks around raw requests
        // args are valid and form a request
        System.out.println("Client requesting " + isRaw + rawQuotationMark + requestPath + rawQuotationMark);


        //////////////////////////////////////////////
        // Connect to server
        //////////////////////////////////////////////

        // loop until connected to server
        while (true) {
            try {
                socket = new Socket(server, port);
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                break;
            } catch (IOException e) {
                System.out.println("Could not connect to server. Do you have the correct host name and port? \nTrying again...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }


        //////////////////////////////////////////////
        // Send and receive messages
        //////////////////////////////////////////////

        try {
            // TODO: STUDENT WORK
            // create the request from the path extracted from args
            // ZANE MOD: raw request option now supported
            if (rawRequest.equals("")) {
                // REGULAR REQUEST (NO raw request)
                request = String.format("GET %s", requestPath);
            } else {
                // RAW REQUEST SELECTED
                request = rawRequest;
            }

            // END STUDENT WORK

            // send request
            output.writeObject(request);

            // print response
            response = (String)input.readObject();

            // close socket and print response before exiting
            socket.close();
            if (shouldSaveToFile) {
                // SAVES RESPONSE TO FILE (--save ARGUMENT)
                System.out.println("Saving response to file...");
                saveToFile(saveFileName, response);
                System.out.printf("Response saved to \"%s\"\n", saveFileName);
            } else {
                // DEFAULT BEHAVIOR (JUST SPITS OUT RESPONSE IN TERMINAL)
                System.out.println("Server response: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private static void printHelpMessage() {
        System.out.println(
                "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +
                "\nEchoClient \n\tRepresents a client that connects to an EchoServer and sends requests." +
                "\n\nOptions:" +
                        "\n\t-s, --server : Specify the server to contact." +
                        "\n\t-p, --port : Specify the server's port for establishing a connection." +
                        "\n\t-r, --request : Specify the resource you wish to request." +
                        "\n\t-h, --help : Print this message." +
                "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
        );
    }
}
