/**
 * The StringCalculator class is a simple calculator that can evaluate mathematical expressions
 * entered as strings. It supports basic arithmetic operations, trigonometric functions,
 * logarithms, factorials, and more.
 *
 * @version 2.2
 * @since 2023
 * @author Rand7Y9Z@gmail.com
 */

import java.util.Arrays;
import java.util.Scanner;

public class StringCalculator {
    /** The name of the calculator program. */
    public static String programName = "StringCalculator";

    /** The version number of the calculator program. */
    public static double version = 2.2;

    /** The Scanner object for user input. */
    private static Scanner in = new Scanner(System.in);



    public static void main(String[] args) {

        System.out.println("\n" + programName + " " + version + " (2023) \nFor more info write: show -info\n------------------------------------------------------------------------------------------------------------");
        while (true) {
            System.out.print("\nEnter an input String:");
            String input = in.nextLine();

            if (input.contains("show")) {
                for (int i = 0; i < commands.length; i++) {
                    String output = "";

                    if (input.contains(commands[i])) {
                        output = infos[i];
                        if (i == 0) {
                            output = isDEG ? "The current mode is DEG" : "The current mode is RAD";
                        }
                    }
                    System.out.println(output);
                }
            } else if (input.equalsIgnoreCase("Rad")) {
                isDEG = false;
            } else if (input.equalsIgnoreCase("Deg")) {
                isDEG = true;
            } else if (input.equalsIgnoreCase("end")) {
                System.out.println("Program closed");
                break;
            } else {
                System.out.println(doCalculate(input));
            }
        }


    }

    /** An array of command strings for providing information about the program. */
    public static String[] commands = new String[]{"-mode", "-commands", "-info", "-trigonometric_functions", "-contact"};
    public static String[] infos = new String[]{"", "'show -commands'\n'show -info'\n'show -trigonometric_functions'\n'show -mode'\n'show -contact'\n'end'\n'deg' & 'rad'", "\nThis program allows you to enter a String and it calculates the mathematical value of it.\nFor a list of possible commands, type 'show - commands', to close the programm type 'end'. \nHere is a list of symbols/characters it can Handle:\n \n1, 2, 3, 4, 5, 6, 7, 8, 9, 0, ., π ,\n( ), | |,\n+, -, *, /, %, !, √(), ^,\nsin(), cos(), tan(), logx()\nE, e \n\nPlease note that if you want to use a root besides the square root, you have to type the needed root before the root symbol e.g. 3√(27) would be the cubic root of 27. \nIf you want to know more about the use of trigonometric functions, please enter 'show -trigonometric_functions'", "\nIf you want to change from deg to rad (or reverse), type 'rad' or 'deg'. The default setting is DEG.\nAnything to gets to be calculated by one of the trigonometric functions has to be within the brackets e.g. sin(60)", "Rand7Y9@gmail.com"};


    /** The current mode indicating whether trigonometric functions operate in degrees or radians. */
    public static boolean isDEG = true;

    /** The time in milliseconds the calculator has been running. */
    public static long timeItRuns = 0;

    public static String doCalculate(String inp) {
        try {
            long time = System.currentTimeMillis();

            inp = remove(inp, '_');
            inp = remove(inp, ' ');
            inp = remove(inp, '"');

            if (countInstances(inp, '(') != countInstances(inp, ')')) {
                return listOfErrors[1];
            }
            if (countInstances(inp, '|') % 2 == 1) {
                return listOfErrors[8];
            }

            char[] input = inp.toCharArray();

            for (int i = 0; i < input.length; i++) {
                if (input[i] == '!' && (i == 0 || !contains("0123456789".toCharArray(), input[i - 1]))) {
                    return listOfErrors[6];
                }
            }

            for (int i = 0; i < input.length; i++) {
                if (contains("0123456789".toCharArray(), input[i]) && (i < input.length - 1 && contains("(πlsct".toCharArray(), input[i + 1])) && (!contains("log".toCharArray(), input[max(new int[]{0, 1, 2, i - 3})]))) {
                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, i + 1)).append("*").append(Arrays.copyOfRange(input, i + 1, input.length)).toString().toCharArray();
                    i = 0;
                }
                if (contains("0123456789".toCharArray(), input[i]) && (i - 1 >= 0 && contains(")π".toCharArray(), input[i - 1]))) {
                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, i)).append("*").append(Arrays.copyOfRange(input, i, input.length)).toString().toCharArray();
                    i = 0;
                }
            }

            for (int i = 0; i < input.length && inp.contains("π"); i++) {
                if (input[i] == 'π') {
                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, max(new int[]{i, 0}))).append("3.141592653589793").append(Arrays.copyOfRange(input, i + 1, input.length)).toString().toCharArray();
                }
            }

            for (int i = 0, j = 0; i < input.length && j < 1000 && (Arrays.toString(input).contains("E") || Arrays.toString(input).contains("e")); i++, j++) {
                if (input[i] == 'e' || input[i] == 'E') {
                    StringBuilder sb = new StringBuilder();
                    input = ("0123456789".contains(String.valueOf(input[i == 0 ? 0 : i - 1])) ? sb.append(Arrays.copyOfRange(input, 0, i)).append("*10^" + ("0123456789-".contains(String.valueOf(input[i+(i+1<input.length?0:1)])) ? "" : "1")).append(Arrays.copyOfRange(input, i + 1, input.length)) : sb.append(Arrays.copyOfRange(input, 0, i)).append("10^" + ("0123456789-".contains(String.valueOf(input[i+(i+1<input.length?0:1)])) ? "" : "1")).append(Arrays.copyOfRange(input, i + 1, input.length))).toString().toCharArray();
                }
            }

            for (int i = 0; i < input.length && String.valueOf(input).contains("√"); i++) {
                if (input[i] == '√') {
                    if (i == 0 || !"0123456789".contains(String.valueOf(input[i - 1]))) {
                        StringBuilder sb = new StringBuilder();
                        input = sb.append(Arrays.copyOfRange(input, 0, i)).append("2").append(Arrays.copyOfRange(input, i, input.length)).toString().toCharArray();
                        i++;
                    }

                    StringBuilder s = new StringBuilder().append('(');
                    int j = 2;
                    for (; countInstances(s.toString(), ')') != countInstances(s.toString(), '('); j++) {
                        s.append(input[i + j]);
                    }

                    StringBuilder t = new StringBuilder();
                    int k = 1;
                    for (; i - k >= 0 && "0123456789.".contains(String.valueOf(input[i - k])); k++) {
                        t.append(input[i - k]);
                    }

                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, i - k + 1)).append(String.valueOf(root(Double.parseDouble(bracketHandler(s.toString())), Double.parseDouble(reverse(t.toString())))).toCharArray()).append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                }
            }

            for (int i = 0; i < input.length - 2; i++) {
                if (input[i] == 'l' && input[i + 1] == 'o' && input[i + 2] == 'g') {

                    StringBuilder s = new StringBuilder();
                    int j = 3;
                    for (; "-0123456789.Ee".contains(String.valueOf(input[i + j])); j++) {
                        s.append(input[i + j]);
                    }

                    StringBuilder t = new StringBuilder().append('(');
                    int k = 1 + j;
                    for (; countInstances(t.toString(), ')') != countInstances(t.toString(), '('); k++) {
                        t.append(input[i + k]);
                    }

                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, i)).append(String.valueOf(log(Double.parseDouble(s.toString()), Double.parseDouble(bracketHandler(t.toString())))).toCharArray()).append(Arrays.copyOfRange(input, i + k, input.length)).toString().toCharArray();
                }
            }

            for (int i = 0; i < input.length; i++) {
                if (input[i] == 's' || input[i] == 'c' || input[i] == 't') {
                    StringBuilder s = new StringBuilder().append('(');
                    int j = 4;
                    for (; countInstances(s.toString(), ')') != countInstances(s.toString(), '('); j++) {
                        s.append(input[i + j]);
                    }

                    StringBuilder sb = new StringBuilder();
                    switch (input[i]) {
                        case 's' ->
                                input = sb.append(Arrays.copyOfRange(input, 0, i)).append(String.valueOf(sinus(Double.parseDouble(bracketHandler(s.toString())), isDEG)).toCharArray()).append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                        case 'c' ->
                                input = sb.append(Arrays.copyOfRange(input, 0, i)).append(String.valueOf(cosinus(Double.parseDouble(bracketHandler(s.toString())), isDEG)).toCharArray()).append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                        case 't' ->
                                input = sb.append(Arrays.copyOfRange(input, 0, i)).append(String.valueOf(tangens(Double.parseDouble(bracketHandler(s.toString())), isDEG)).toCharArray()).append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                    }
                }
            }


            for (int i = 0; i < input.length; i++) {
                if (input[i] == '(') {
                    StringBuilder s = new StringBuilder().append('(');
                    int j = 1;
                    for (; countInstances(s.toString(), ')') != countInstances(s.toString(), '('); j++) {
                        s.append(input[i + j]);
                    }

                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, i)).append(bracketHandler(s.toString()).toCharArray()).append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                }
            }

            for (int i = 0; i < input.length && String.valueOf(input).contains("|"); i++) {
                if (input[i] == '|') {
                    StringBuilder s = new StringBuilder();
                    int j = 1;
                    for (; input[i + j] != '|'; j++) {
                        s.append(input[i + j]);
                    }

                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, i)).append(String.valueOf(abs(Double.parseDouble(doCalculate(s.toString())))).toCharArray()).append(Arrays.copyOfRange(input, i + j + 1, input.length)).toString().toCharArray();
                }
            }

            for (int j = 0; j < input.length - 1; j++) {
                if ((input[j] == '-' && input[j + 1] == '-') || (input[j] == '+' && input[j + 1] == '+') || (input[j] == '+' && input[j + 1] == '-') || (input[j] == '-' && input[j + 1] == '+')) {
                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, j)).append(((input[j] == '-' && input[j + 1] == '-') || (input[j] == '+' && input[j + 1] == '+')) ? '+' : '-').append(Arrays.copyOfRange(input, j + 2, input.length)).toString().toCharArray();
                    j = 0;
                }
            }

            for (int i = 0; i < input.length && String.valueOf(input).contains("!"); i++) {
                if (input[i] == '!') {
                    StringBuilder s = new StringBuilder();
                    int j = 0;
                    for (; i - j - 1 >= 0 && contains("1234567890.".toCharArray(), (input[i - j - 1])); j++) {
                        s.append(input[i - j - 1]);
                    }
                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, i <= 1 ? 0 : i - 1)).append(String.valueOf(factorial(Double.parseDouble(reverse(s.toString())))).toCharArray()).append(Arrays.copyOfRange(input, i + 1, input.length)).toString().toCharArray();
                }
            }

            for (int i = 0; i < input.length && String.valueOf(input).contains("^"); i++) {
                if (input[i] == '^') {

                    StringBuilder s = new StringBuilder();
                    int j = 1;
                    for (; (i + j < input.length && "0123456789.".contains(String.valueOf(input[i + j]))) || (j == 1 && input[i + 1] == '-'); j++) {
                        s.append(input[i + j]);
                    }

                    StringBuilder t = new StringBuilder();
                    int k = 1;
                    for (; i - k >= 0 && "0123456789.".contains(String.valueOf(input[i - k])); k++) {
                        t.append(input[i - k]);
                    }
                    if (i - k >= 0 && input[i - k] == '-') {
                        t.append('-');
                    }

                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, i - k + 1)).append(String.valueOf(Math.pow(Double.parseDouble(reverse(t.toString())), Double.parseDouble(s.toString()))).toCharArray()).append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                    i = 0;
                }
            }

            for (int i = 0; i < input.length; i++) {
                if (input[i] == '*' || input[i] == '/' || input[i] == '%') {

                    StringBuilder s = new StringBuilder();
                    int j = 1;
                    for (; ((i + j < input.length && "0123456789.Ee".contains(String.valueOf(input[i + j]))) || (j == 1 && input[i + 1] == '-')) || ((input[i + j - 1] == 'E' || input[i + j - 1] == 'e') && input[i + j] == '-'); j++) {
                        s.append(input[i + j]);
                    }

                    StringBuilder t = new StringBuilder();
                    int k = 1;
                    for (; i - k >= 0 && "0123456789.Ee".contains(String.valueOf(input[i - k])); k++) {
                        t.append(input[i - k]);
                    }
                    if (i - k >= 0 && input[i - k] == '-') {
                        t.append('-');
                    }

                    StringBuilder sb = new StringBuilder();
                    switch (input[i]) {
                        case '*' ->
                                input = sb.append(Arrays.copyOfRange(input, 0, i - k + 1)).append(String.valueOf(Double.parseDouble(reverse(t.toString())) * Double.parseDouble(s.toString())).toCharArray()).append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                        case '/' -> {
                            if (Double.parseDouble(s.toString()) == 0) {
                                return listOfErrors[7];
                            }
                            input = sb.append(Arrays.copyOfRange(input, 0, i - k + 1)).append(String.valueOf(Double.parseDouble(reverse(t.toString())) / Double.parseDouble(s.toString())).toCharArray()).append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                        }
                        case '%' ->
                                input = sb.append(Arrays.copyOfRange(input, 0, i - k + 1)).append(String.valueOf(Double.parseDouble(reverse(t.toString())) % Double.parseDouble(s.toString())).toCharArray()).append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                    }
                    i = 0;
                }

            }

            for (int j = 0; j < input.length - 1; j++) {
                if ((input[j] == '-' && input[j + 1] == '-') || (input[j] == '+' && input[j + 1] == '+') || (input[j] == '+' && input[j + 1] == '-') || (input[j] == '-' && input[j + 1] == '+')) {
                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, j)).append(((input[j] == '-' && input[j + 1] == '-') || (input[j] == '+' && input[j + 1] == '+')) ? '+' : '-').append(Arrays.copyOfRange(input, j + 2, input.length)).toString().toCharArray();
                    j = 0;
                }
            }

            for (int i = 0; i < input.length && (String.valueOf(input).contains("+") || String.valueOf(input).contains("-")); i++) {
                if ((input[i] == '+' || (input[i] == '-' && i >= 1 && (input[i - 1] != 'E' && input[i - 1] != 'e'))) && i != 0) {
                    StringBuilder s = new StringBuilder();
                    int j = 1;
                    for (; i + j < input.length && ("0123456789.Ee".contains(String.valueOf(input[i + j])) || ((input[i + j - 1] == 'E' || input[i + j - 1] == 'e') && input[i + j] == '-')); j++) {
                        s.append(input[i + j]);
                    }

                    StringBuilder t = new StringBuilder();
                    int k = 1;
                    for (; i - k >= 0 && ("0123456789.Ee".contains(String.valueOf(input[i - k])) || (input[i - k] == '-' && i - k >= 1 && (input[i - k - 1] == 'E' || input[i - k - 1] == 'e')) ); k++) {
                        t.append(input[i - k]);
                    }
                    if (i - k == 0) {
                        t.append(input[0] == '+' ? '+' : '-');
                    }

                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, i - t.length())).append(String.valueOf(input[i] == '+' ? Double.parseDouble(reverse(t.toString())) + Double.parseDouble(s.toString()) : Double.parseDouble(reverse(t.toString())) - Double.parseDouble(s.toString())).toCharArray()).append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                    i = 0;
                }

            }

            timeItRuns = System.currentTimeMillis() - time;

            inp = String.valueOf(input);
            if (inp.contains("Infinity")) {
                return listOfErrors[3];
            }
            if (inp.charAt(0) == '+') {
                inp = inp.substring(1);
            }
            if (Double.isNaN(Double.parseDouble(inp))) {
                return listOfErrors[0];
            }
            return (((int) Double.parseDouble(inp)) == Double.parseDouble(inp)) ? String.valueOf(((int) Double.parseDouble(inp))) : inp;
        } catch (Exception e) {
            return listOfErrors[5];
        }
    }

    public static String bracketHandler(String s) {
        if (s.length() == 2) {
            return "";
        }
        return doCalculate(s.substring(1, s.length() - 1));
    }

    public static String[] listOfErrors = new String[]{"Error: Calculation", "Error: Brackets", "Error: Overflow", "Error: Input exceeds limit", "Error: Calculation timed out", "Error: Input can't be calculated", "Error: factorial needs numbers", "Error: Division by 0", "Error: No closing '|' found"};

    /**
     * Calculates the sine of an angle in degrees or radians.
     *
     * @param deg     The angle in degrees.
     * @param isDEG   If true, the angle is in degrees; if false, the angle is in radians.
     * @return        The sine of the angle.
     */
    public static double sinus(double deg, boolean isDEG) {
        if (deg % 180 == 0) {
            return 0;
        }
        return isDEG ? Math.sin(toRad(deg)) : Math.sin(deg);
    }

    /**
     * Calculates the cosine of an angle in degrees or radians.
     *
     * @param deg     The angle in degrees.
     * @param isDEG   If true, the angle is in degrees; if false, the angle is in radians.
     * @return        The cosine of the angle.
     */
    public static double cosinus(double deg, boolean isDEG) {
        return isDEG ? Math.cos(toRad(deg)) : Math.cos(deg);
    }

    /**
     * Calculates the tangent of an angle in degrees or radians.
     *
     * @param deg     The angle in degrees.
     * @param isDEG   If true, the angle is in degrees; if false, the angle is in radians.
     * @return        The tangent of the angle.
     */
    public static double tangens(double deg, boolean isDEG) {
        if (deg % 180 == 0) {
            return 0;
        }
        return isDEG ? Math.tan(toRad(deg)) : Math.tan(deg);
    }

    /**
     * Calculates the factorial of a number.
     *
     * @param f       The input number.
     * @return        The factorial of the input number.
     */
    public static double factorial(double f) {
        if (f >= 11) {
            return Double.POSITIVE_INFINITY;
        }
        double ret = 1;
        for (double i = 1; i <= f; i++) {
            ret = ret * i;
        }
        return ret;
    }

    /**
     * Calculates the n-th root of a base number.
     *
     * @param base    The base number.
     * @param n       The root degree.
     * @return        The n-th root of the base number.
     */
    public static double root(double base, double n) {
        return Math.pow(base, 1.0 / n);
    }

    /**
     * Calculates the logarithm of a number with a specified base.
     *
     * @param base    The logarithmic base.
     * @param n       The number to calculate the logarithm for.
     * @return        The logarithm of the number with the specified base.
     */
    public static double log(double base, double n) {
        return Math.log(n) / Math.log(base);
    }

    /**
     * Returns the absolute value of a number.
     *
     * @param i       The input number.
     * @return        The absolute value of the input number.
     */
    public static double abs(double i) {
        return i < 0 ? i * -1 : i;
    }

    /**
     * Converts an angle from radians to degrees.
     *
     * @param i       The angle in radians.
     * @return        The angle in degrees.
     */
    public static double toDeg(double i) {
        return i * 180 / 3.141592653589793;
    }

    /**
     * Converts an angle from degrees to radians.
     *
     * @param i       The angle in degrees.
     * @return        The angle in radians.
     */
    public static double toRad(double i) {
        return i * 3.141592653589793 / 180;
    }

    /**
     * Finds the maximum value in an array of integers.
     *
     * @param a       The array of integers.
     * @return        The maximum value in the array.
     */
    public static int max(int[] a) {
        if (a.length == 0) {
            return 0;
        }
        int max = a[0];
        for (int j : a) {
            if (j > max) {
                max = j;
            }
        }
        return max;
    }

    /**
     * Counts the number of occurrences of a character in a string.
     *
     * @param s       The input string.
     * @param search  The character to count.
     * @return        The number of occurrences of the character in the string.
     */
    public static int countInstances(String s, char search) {
        int ret = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == search) {
                ret++;
            }
        }
        return ret;
    }

    /**
     * Reverses a given string.
     *
     * @param s       The input string.
     * @return        The reversed string.
     */
    public static String reverse(String s) {
        StringBuilder r = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) {
            r.append(s.charAt(i));
        }
        return r.toString();
    }

    /**
     * Removes all occurrences of a specified character from a string.
     *
     * @param s       The input string.
     * @param c       The character to be removed.
     * @return        The string with all occurrences of the character removed.
     */
    public static String remove(String s, char c) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) {
                r.append(s.charAt(i));
            }
        }
        return r.toString();
    }

    /**
     * Checks if a character is present in an array of characters.
     *
     * @param a       The array of characters.
     * @param c       The character to search for.
     * @return        True if the character is present, false otherwise.
     */
    public static boolean contains(char[] a, char c) {
        for (char value : a) {
            if (value == c) {
                return true;
            }
        }
        return false;
    }

}