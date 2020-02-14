import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class TestMethods {
    private static boolean bodyHasOnlyBackslashedSpaces(String requestBody) {
        boolean valueToReturn = true;
        for (int i = 1; i < requestBody.length(); i++) {
//            System.out.println(requestBody.charAt(i - 1) + "" + requestBody.charAt(i));
            if (requestBody.charAt(i) == ' ') {
                if (requestBody.charAt(i - 1) != '\\') {
                    System.out.println(requestBody.charAt(i - 1) + "" + requestBody.charAt(i));
                    valueToReturn = false;
                    break;
                }
            }
        }
        return valueToReturn;
    }

    private static void saveToFile(String fileName, String fileText) {
        // file writing made possible thanks to https://stackoverflow.com/a/2885224
        try {
            PrintWriter fileWriter = new PrintWriter(fileName, "UTF-8");
            fileWriter.print(fileText);
            fileWriter.close();
        } catch (IOException e) {
            System.out.print("ERROR: Could not save to file");
            e.printStackTrace(); // show verbose error details
        }
    }

    public static void main(String[] args) {
        System.out.println(bodyHasOnlyBackslashedSpaces("Hello\\ there\\ friends.")); // true
        System.out.println(bodyHasOnlyBackslashedSpaces("Hello there\\ friends.")); // false
        System.out.println(bodyHasOnlyBackslashedSpaces("Hello\\ there friends.")); // false
        System.out.println(bodyHasOnlyBackslashedSpaces("Hello there friends.")); // false
        System.out.println(bodyHasOnlyBackslashedSpaces("/billieeili\\ sh")); // SHOULD BE true
        // splitting string test
        System.out.println(Arrays.deepToString("Let's split this string for kicks...".split(" ", 2)));
        saveToFile("hamlet.txt", "To be, or not to be, that is the question:\nWhether 'tis nobler in the mind to suffer\nThe slings and arrows of outrageous fortune,\nOr to take arms against a sea of troubles\nAnd by opposing end them.");
    }
}
