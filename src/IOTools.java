import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class IOTools {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final String INT_FORMAT      = "^[+-]?\\d+$";
    private static final String POS_INT_FORMAT  = "^+?[1-9]\\d*$";
    private static final String FLOAT_FORMAT
        = "^[+-]?(\\d+\\.?\\d*|.?\\d+)([eE][+-]?\\d+)?$";
    private static final String YES_NO_FORMAT   = "^(n|N|no|No|y|Y|yes|Yes)$";
    private static final String YES_FORMAT      = "^(y|Y|yes|Yes)$";

    /**
     * Prompts the user for an integer in a certain range with a given prompt.
     * Asks again if the response is not an integer or within the range.
     * @param prompt The prompt to start the user with.
     * @param lowerBound The lower bound of valid integers, inclusive.
     * @param upperBound The upper bound of valid integers, exclusive.
     * @return A valid integer given the specifications, chosen by the user.
     * @throws IOException if input is invalid
     */
    public static int promptInteger(String prompt, int lowerBound, int upperBound) throws IOException {
        System.out.print(prompt);
        String response;
        int number;

        while (!(response = readLine().trim()).matches(INT_FORMAT) ||
                (number = Integer.parseInt(response)) < lowerBound ||
                number > upperBound) {
            System.out.print("\tThat is not a valid integer. Please "
                             + "choose an integer between " + lowerBound + " and "
                             + upperBound + ": ");
        }

        return Integer.parseInt(response);
    }

    /**
     * Prompts the user for an integer with a given prompt.
     * Asks again if the response is not an integer.
     * @param prompt The prompt to start the user with.
     * @return A valid integer, chosen by the user.
     * @throws IOException if input is invalid
     */
    public static int promptInteger(String prompt) throws IOException {
        System.out.print(prompt);
        String response;

        while (!(response = readLine().trim()).matches(INT_FORMAT)) {
            System.out.print("\tThat is not a valid input. Please enter "
                + "an integer: ");
        }

        return Integer.parseInt(response);
    }

    /**
     * Prompts the user for a positive integer with a given prompt.
     * Asks again if the response is not a positive integer.
     * @param prompt The prompt to start the user with.
     * @return A valid integer, chosen by the user.
     * @throws IOException if input is invalid
     */
    public static int promptPositiveInteger(String prompt) throws IOException {
        System.out.print(prompt);
        String response;

        while (!(response = readLine().trim()).matches(POS_INT_FORMAT)) {
            System.out.print("\tThat is not a valid input. Please enter "
                + "a positive integer: ");
        }

        return Integer.parseInt(response);
    }

    /**
     * Prompts the user for a double with a given prompt.
     * Asks again if the response is not a valid value.
     * @param prompt The prompt to start the user with.
     * @return A valid double, chosen by the user.
     * @throws IOException if input is invalid
     */
    public static double promptDouble(String prompt) throws IOException {
        System.out.print(prompt);
        String response = null;

        while (!(response = readLine().trim()).matches(FLOAT_FORMAT)) {
            System.out.print("\tThat is not a valid decimal value. "
                             + "Enter a decimal value: ");
        }

        return Double.parseDouble(response);
    }

    /**
     * Prompts the user for a yes or no value, specified by y/n (not cap-sensitive).
     * @param initialPrompt The prompt to ask the user at first.
     * @return True if the user responses yes, otherwise false.
     * @throws IOException if input is invalid
     */
    public static boolean promptYesNo(String initialPrompt) throws IOException {
        System.out.print(initialPrompt);
        String response = readLine().trim();
        //Repeat if selection was not yes/no
        while (!response.matches(YES_NO_FORMAT)) {
            System.out.print("\tInvalid response. Please enter only 'y' or 'n': ");
            response = readLine().trim();
        }

        return response.matches(YES_FORMAT);
    }

    /**
     * Prompts the user for a string.
     * @return the string entered by the user
     * @throws IOException if input is invalid
     */
    public static String promptLine(String prompt) throws IOException {
        System.out.print(prompt);
        return readLine();
    }

    /**
     * Reads a line from the input stream and echoes the result.
     * @return the input entered
     * @throws IOException if input is invalid
     */
    public static String readLine() throws IOException {
        String result = reader.readLine();
        System.out.println(result);
        return result;
    }

}
