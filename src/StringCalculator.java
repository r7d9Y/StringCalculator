/**
 * The StringCalculator is a simple calculator that can evaluate mathematical expressions
 * entered as strings. It supports basic arithmetic operations, trigonometric functions,
 * logarithms, factorials, and more
 *
 * @version 2.3
 * @author Rand7Y9Z@gmail.com
 * @since 2023
 * <p>
 * please note: javadoc is partially written by GPT for a more understandable code
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Scanner;

public class StringCalculator {

    public static String programName = "StringCalculator";
    public static double version = 2.6;
    private static Scanner in = new Scanner(System.in);


    public static void main(String[] args) {

        System.out.println("\n" + programName + " " + version + " (2023) \nFor more info write: show -info" +
                "\n-------------------------------------------------------------------------------------------------------");
        while (true) {
            System.out.print("\nEnter an input String:");
            String input = in.nextLine();

            if (input.contains("show")) {
                for (int i = 0; i < commands.length; i++) {
                    String output = "";

                    if (input.contains(commands[i])) {
                        output = infos[i];
                        if (i == 0) output = "The current mode is " + (isDEG ? "DEG" : "RAD");
                    }
                    System.out.println(output);
                }
            } else if (input.equalsIgnoreCase("Rad")) {
                isDEG = false;
            } else if (input.equalsIgnoreCase("Deg")) {
                isDEG = true;
            } else if (input.equalsIgnoreCase("end") || input.equalsIgnoreCase("close")) {
                System.out.println("Program closed");
                break;
            } else {
                System.out.println(doCalculate(input));
            }
        }


    }

    public static String[] commands = new String[]{"-mode", "-commands", "-info", "-trigonometric_functions", "-contact"};
    public static String[] infos = new String[]{
            "",
            "'show -commands'\n'show -info'\n'show -trigonometric_functions'\n'show -mode'\n'show -contact'\n'end'\n'deg' & 'rad'",
            """
    This program allows you to enter a String and it calculates the mathematical value of it
    For a list of possible commands, type 'show - commands', to close the programm type 'end'
    Here is a list of symbols/characters it can Handle:
     
    1, 2, 3, 4, 5, 6, 7, 8, 9, 0, ., π ,
    ( ), | |,
    +, -, *, / (or: ÷), %, ^, !, x√(),∛, ∜\s
    ⁰ ¹ ² ³ ⁴ ⁵ ⁶ ⁷ ⁸ ⁹ (potentiate symbols)
    sin(), cos(), tan(), logx()
    E, e\s
    ⅖ ¾ ⅗ ⅜ ⅘ ⅚ ⅝ ⅞ ( these symbols get converted to divisions afterwards)
    
    Please note that if you want to use a root besides the square root,
    you have to type the needed root before the root symbol e.g. 3√(27) would be the cubic root of 27
    If you want to know more about the use of trigonometric functions, please enter 'show -trigonometric_functions'
    """,
            """
    If you want to change from deg to rad (or reverse), type 'rad' or 'deg'. The default setting is DEG.
    Anything to gets to be calculated by one of the trigonometric functions has to be within the brackets e.g. sin(60)
    """,
            "Rand7Y9Z@gmail.com"
    };

    /**
     * The current mode indicating whether trigonometric functions operate in degrees or radians
     */
    public static boolean isDEG = true;

    /**
     * The time in ms the programm has been running
     */
    private static long timeItRuns = 0;

    private static final String[] listOfErrors = new String[]{"Error: Calculation", "Error: Brackets", "Error: Overflow",
            "Error: Input exceeds limit", "Error: Calculation timed out", "Error: Input can't be calculated",
            "Error: factorial needs numbers", "Error: Division by 0", "Error: No closing '|' found", "Error: no number to potentiate found"};

    /**
     * Calculates the expression given as input
     *
     * @param inp The input expression as a String
     * @return The result of the calculation as a String or an error message (the possible ones are found in String[] listOfErrors,
     * they should be self explaining)
     */
    public static String doCalculate(String inp) {
        try {
            long time = System.currentTimeMillis();

            inp = remove(inp, '_');
            inp = remove(inp, ' ');
            inp = remove(inp, '"');
            //inp = replace(inp,',',".");

            inp = doReplaceThings(inp);
            if (inp.equals(listOfErrors[9])) return listOfErrors[9];

            if (countInstances(inp, '(') != countInstances(inp, ')')) return listOfErrors[1];

            if (countInstances(inp, '|') % 2 == 1) return listOfErrors[8];

            char[] input = inp.toCharArray();

            for (int i = 0; i < input.length; i++) {
                if (input[i] == '!' && (i == 0 || !contains(input[i - 1], "0123456789".toCharArray())))
                    return listOfErrors[6];
            }

            input = addMultiplier(input);

            input = clearPiAndE(input);

            input = doRoots(input);

            input = doLogarithm(input);

            input = doCosSinTan(input);

            input = clearBracket(input);

            input = absolute(input);

            input = clearPlusAndMinus(input);

            input = factorial(input);

            input = potentiate(input);

            input = multiplyDivideAndModulo(input);
            if (String.valueOf(input).equals(listOfErrors[7])) return listOfErrors[7];

            input = clearPlusAndMinus(input);

            input = addAndSubtract(input);

            timeItRuns = System.currentTimeMillis() - time;

            if (String.valueOf(input).contains("Infinity")) return listOfErrors[3];

            inp = String.valueOf(roundDouble(Double.parseDouble(String.valueOf(input)), 10));

            if (inp.charAt(0) == '+') inp = inp.substring(1);

            if (Double.isNaN(Double.parseDouble(inp))) return listOfErrors[0];

            return (((int) Double.parseDouble(inp)) == Double.parseDouble(inp)) ? String.valueOf(((int) Double.parseDouble(inp))) : inp;

        } catch (Exception e) {
            return listOfErrors[5];
        }
    }

    /**
     * Replaces special characters in a given string with their corresponding mathematical expressions,
     * and converts potential characters to a format compatible with the potentiate operator (^)
     *
     * @param s The input string to process
     * @return The modified string with replaced characters and converted potential format
     */
    private static String doReplaceThings(String s) {

        char[] searchChars = {'∛', '∜', '×', '÷', '–', '–', '⅖', '¾', '⅗', '⅜', '⅘', '⅚', '⅝', '⅞'};
        String[] replacements = {"3√", "4√", "*", "/", "-", "-", String.valueOf((double) 2 / 3), String.valueOf((double) 3 / 4),
                String.valueOf((double) 3 / 5), String.valueOf((double) 3 / 8), String.valueOf((double) 4 / 5), String.valueOf((double) 5 / 6),
                String.valueOf((double) 5 / 8), String.valueOf((double) 7 / 8)};

        for (int j = 0; j < searchChars.length; j++) {
            s = replace(s, searchChars[j], replacements[j]);
        }

        char[] potentiateDigits = new char[]{'⁰', '¹', '²', '³', '⁴', '⁵', '⁶', '⁷', '⁸', '⁹'};

        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < potentiateDigits.length; j++) {
                if (s.charAt(i) == potentiateDigits[j]) {
                    if (i == 0) return listOfErrors[9];
                    s = s.substring(0, i) + "^" + j + s.substring(i + 1);
                }
            }
        }

        return s;
    }


    /**
     * Handles the expression within brackets
     *
     * @param s The expression within the brackets as a String
     * @return The result of calculating the expression within the brackets as a String
     */
    private static String bracketHandler(String s) {
        if (s.length() == 2) return "";

        return doCalculate(s.substring(1, s.length() - 1));
    }


    /**
     * Adds multiplication symbols if necessary
     *
     * @param input The input expression as a char arraylist
     * @return The input expression with added multiplication symbols if necessary as a char arraylist
     */
    private static char[] addMultiplier(char... input) {
        for (int i = 0; i < input.length; i++) {
            if (contains(input[i], "0123456789".toCharArray()) &&
                    (i < input.length - 1 && contains(input[i + 1], "(πlsct".toCharArray())) &&
                    (input.length == 2 || !contains(input[max(0, 1, 2, i - 3)], "log".toCharArray()))) {
                StringBuilder sb = new StringBuilder();
                input = sb.append(Arrays.copyOfRange(input, 0, i + 1)).append("*").append(Arrays.copyOfRange(input, i + 1, input.length)).toString().toCharArray();
                i = 0;
            }
            if (contains(input[i], "0123456789".toCharArray()) && (i - 1 >= 0 && contains(input[i - 1], ")π".toCharArray()))) {
                StringBuilder sb = new StringBuilder();
                input = sb.append(Arrays.copyOfRange(input, 0, i)).append("*").append(Arrays.copyOfRange(input, i, input.length)).toString().toCharArray();
                i = 0;
            }
            if (i > 0 && (input[i] == '(' && input[i - 1] == ')')) {
                StringBuilder sb = new StringBuilder();
                input = sb.append(Arrays.copyOfRange(input, 0, i)).append("*").append(Arrays.copyOfRange(input, i, input.length)).toString().toCharArray();
                i = 0;
            }
        }
        return input;
    }


    /**
     * Removes 'π' and 'e' and replaces them with their numerical values
     *
     * @param input The input expression as a char array
     * @return The input expression with numerical values for 'π' and 'e' as a char array
     */
    private static char[] clearPiAndE(char... input) {
        for (int i = 0; i < input.length && String.valueOf(input).contains("π"); i++) {
            if (input[i] == 'π') {
                StringBuilder sb = new StringBuilder();
                input = sb.append(Arrays.copyOfRange(input, 0, max(i, 0)))
                        .append("3.141592653589793").append(Arrays.copyOfRange(input, i + 1, input.length)).toString().toCharArray();
            }
        }

        for (int i = 0, j = 0; i < input.length && j < 1000 && (Arrays.toString(input).contains("E") || Arrays.toString(input).contains("e")); i++, j++) {
            if (input[i] == 'e' || input[i] == 'E') {
                StringBuilder sb = new StringBuilder();
                input = ("0123456789".contains(String.valueOf(input[i == 0 ? 0 : i - 1])) ? sb.append(Arrays.copyOfRange(input, 0, i))
                        .append("*10^" + ("0123456789-".contains(String.valueOf(input[i + (i + 1 < input.length ? 0 : 1)])) ? "1" : ""))
                        .append(Arrays.copyOfRange(input, i + 1, input.length)) : sb.append(Arrays.copyOfRange(input, 0, i))
                        .append("10^" + ("0123456789-".contains(String.valueOf(input[i + (i + 1 < input.length ? 0 : 1)])) ? "" : "1"))
                        .append(Arrays.copyOfRange(input, i + 1, input.length))).toString().toCharArray();
            }
        }

        return input;
    }


    /**
     * Performs calculations for roots
     *
     * @param input The input expression as a char array
     * @return The input expression with calculated roots as a char array
     */
    private static char[] doRoots(char... input) {
        for (int i = 0; i < input.length && String.valueOf(input).contains("√"); i++) {
            if (input[i] == '√') {
                if (i == 0 || !"0123456789".contains(String.valueOf(input[i - 1]))) {
                    StringBuilder sb = new StringBuilder();
                    input = sb.append(Arrays.copyOfRange(input, 0, i))
                            .append("2").append(Arrays.copyOfRange(input, i, input.length))
                            .toString().toCharArray();
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
                input = sb.append(Arrays.copyOfRange(input, 0, i - k + 1))
                        .append(String.valueOf(root(Double.parseDouble(bracketHandler(s.toString())), Double.parseDouble(reverse(t.toString()))))
                                .toCharArray())
                        .append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
            }
        }

        return input;
    }


    /**
     * Performs calculations for logarithms
     *
     * @param input The input expression as a char array
     * @return The input expression with calculated logarithms as a char array
     */
    private static char[] doLogarithm(char... input) {
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
                input = sb.append(Arrays.copyOfRange(input, 0, i))
                        .append(String.valueOf(log(Double.parseDouble(s.toString()), Double.parseDouble(bracketHandler(t.toString()))))
                                .toCharArray())
                        .append(Arrays.copyOfRange(input, i + k, input.length)).toString().toCharArray();
            }
        }

        return input;
    }


    /**
     * Performs calculations for sine, cosine, and tangent
     *
     * @param input The input expression as a char array
     * @return The input expression with calculated trigonometric functions as a char array
     */
    private static char[] doCosSinTan(char... input) {
        for (int i = 0; i < input.length; i++) {
            if (input[i] == 's' || input[i] == 'c' || input[i] == 't') {
                StringBuilder s = new StringBuilder().append('(');
                int j = 4;
                for (; countInstances(s.toString(), ')') != countInstances(s.toString(), '('); j++) {
                    s.append(input[i + j]);
                }

                StringBuilder sb = new StringBuilder();
                switch (input[i]) {
                    case 's' -> input = sb.append(Arrays.copyOfRange(input, 0, i))
                            .append(String.valueOf(sinus(Double.parseDouble(bracketHandler(s.toString())), isDEG)).toCharArray())
                            .append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                    case 'c' -> input = sb.append(Arrays.copyOfRange(input, 0, i))
                            .append(String.valueOf(cosinus(Double.parseDouble(bracketHandler(s.toString())), isDEG))
                                    .toCharArray()).append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                    case 't' -> input = sb.append(Arrays.copyOfRange(input, 0, i))
                            .append(String.valueOf(tangens(Double.parseDouble(bracketHandler(s.toString())), isDEG)).toCharArray())
                            .append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                }
            }
        }

        return input;
    }


    /**
     * Handles brackets in the expression
     *
     * @param input The input expression as a char array
     * @return The input expression with handled brackets as a char array
     */
    private static char[] clearBracket(char... input) {
        for (int i = 0; i < input.length; i++) {
            if (input[i] == '(') {
                StringBuilder s = new StringBuilder().append('(');
                int j = 1;
                for (; countInstances(s.toString(), ')') != countInstances(s.toString(), '('); j++) {
                    s.append(input[i + j]);
                }

                StringBuilder sb = new StringBuilder();
                input = sb.append(Arrays.copyOfRange(input, 0, i)).append(bracketHandler(s.toString()).toCharArray())
                        .append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
            }
        }

        return input;
    }


    /**
     * Calculates the absolute value
     *
     * @param input The input expression as a char array
     * @return The input expression with calculated absolute value as a char array
     */
    private static char[] absolute(char... input) {
        for (int i = 0; i < input.length && String.valueOf(input).contains("|"); i++) {
            if (input[i] == '|') {
                StringBuilder s = new StringBuilder();
                int j = 1;
                for (; input[i + j] != '|'; j++) {
                    s.append(input[i + j]);
                }

                StringBuilder sb = new StringBuilder();
                input = sb.append(Arrays.copyOfRange(input, 0, i))
                        .append(String.valueOf(abs(Double.parseDouble(doCalculate(s.toString())))).toCharArray())
                        .append(Arrays.copyOfRange(input, i + j + 1, input.length)).toString().toCharArray();
            }
        }
        return input;
    }


    /**
     * Calculates factorials
     *
     * @param input The input expression as a char array
     * @return The input expression with calculated factorials as a char array
     */
    private static char[] factorial(char... input) {
        for (int i = 0; i < input.length && String.valueOf(input).contains("!"); i++) {
            if (input[i] == '!') {
                StringBuilder s = new StringBuilder();
                int j = 0;
                for (; i - j - 1 >= 0 && contains((input[i - j - 1]), "1234567890.".toCharArray()); j++) {
                    s.append(input[i - j - 1]);
                }
                StringBuilder sb = new StringBuilder();
                input = sb.append(Arrays.copyOfRange(input, 0, i <= 1 ? 0 : i - 1))
                        .append(String.valueOf(factorial(Double.parseDouble(reverse(s.toString())))).toCharArray())
                        .append(Arrays.copyOfRange(input, i + 1, input.length)).toString().toCharArray();
            }
        }

        return input;
    }


    /**
     * Performs exponentiation
     *
     * @param input The input expression as a char array
     * @return The input expression with calculated exponentiation as a char array
     */
    private static char[] potentiate(char... input) {
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
                input = sb.append(Arrays.copyOfRange(input, 0, i - k + 1))
                        .append(String.valueOf(Math.pow(Double.parseDouble(reverse(t.toString())), Double.parseDouble(s.toString()))).toCharArray())
                        .append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                i = 0;
            }
        }

        return input;
    }


    /**
     * Performs multiplication, division, and modulo operations
     *
     * @param input The input expression as a char array
     * @return The input expression with calculated multiplication, division, and modulo operations as a char array
     */
    private static char[] multiplyDivideAndModulo(char... input) {
        for (int i = 0; i < input.length; i++) {
            if (input[i] == '*' || input[i] == '/' || input[i] == '%') {

                StringBuilder s = new StringBuilder();
                int j = 1;
                for (; ((i + j < input.length && "0123456789.Ee".contains(String.valueOf(input[i + j]))) ||
                        (j == 1 && input[i + 1] == '-')) || ((input[i + j - 1] == 'E' || input[i + j - 1] == 'e') && input[i + j] == '-'); j++) {
                    s.append(input[i + j]);
                }

                StringBuilder t = new StringBuilder();
                int k = 1;
                for (; i - k >= 0 && "0123456789.Ee".contains(String.valueOf(input[i - k])); k++) {
                    t.append(input[i - k]);
                }
                if (i - k >= 0 && input[i - k] == '-') {
                    t.append('-');
                    k++;
                }

                StringBuilder sb = new StringBuilder();
                switch (input[i]) {
                    case '*' -> input = sb.append(Arrays.copyOfRange(input, 0, i - k + 1))
                            .append(String.valueOf(Double.parseDouble(reverse(t.toString())) * Double.parseDouble(s.toString())).toCharArray())
                            .append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                    case '/' -> {
                        if (Double.parseDouble(s.toString()) == 0) {
                            return listOfErrors[7].toCharArray();
                        }
                        input = sb.append(Arrays.copyOfRange(input, 0, i - k + 1))
                                .append(String.valueOf(Double.parseDouble(reverse(t.toString())) / Double.parseDouble(s.toString())).toCharArray())
                                .append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                    }
                    case '%' -> input = sb.append(Arrays.copyOfRange(input, 0, i - k + 1))
                            .append(String.valueOf(Double.parseDouble(reverse(t.toString())) % Double.parseDouble(s.toString())).toCharArray()).
                            append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                }
                i = 0;
            }

        }

        return input;
    }


    /**
     * Removes consecutive plus and minus signs
     *
     * @param input The input expression as a char array
     * @return The input expression without consecutive plus and minus signs as a char array
     */
    private static char[] clearPlusAndMinus(char... input) {
        for (int j = 0; j < input.length - 1; j++) {
            if ((input[j] == '-' && input[j + 1] == '-') || (input[j] == '+' && input[j + 1] == '+') || (input[j] == '+' &&
                    input[j + 1] == '-') || (input[j] == '-' && input[j + 1] == '+')) {
                StringBuilder sb = new StringBuilder();
                input = sb.append(Arrays.copyOfRange(input, 0, j)).append(((input[j] == '-' && input[j + 1] == '-') ||
                        (input[j] == '+' && input[j + 1] == '+')) ? '+' : '-').append(Arrays.copyOfRange(input, j + 2, input.length)).toString().toCharArray();
                j = 0;
            }
        }
        return input;
    }


    /**
     * Performs addition and subtraction
     *
     * @param input The input expression as a char array
     * @return The input expression with calculated addition and subtraction as a char array
     */
    private static char[] addAndSubtract(char... input) {
        for (int i = 0; i < input.length && (String.valueOf(input).contains("+") || String.valueOf(input).contains("-")); i++) {
            if ((input[i] == '+' || (input[i] == '-' && i >= 1 && (input[i - 1] != 'E' && input[i - 1] != 'e'))) && i != 0) {
                StringBuilder s = new StringBuilder();
                int j = 1;
                for (; i + j < input.length && ("0123456789.Ee".contains(String.valueOf(input[i + j])) ||
                        ((input[i + j - 1] == 'E' || input[i + j - 1] == 'e') && input[i + j] == '-')); j++) {
                    s.append(input[i + j]);
                }

                StringBuilder t = new StringBuilder();
                int k = 1;
                for (; i - k >= 0 && ("0123456789.Ee".contains(String.valueOf(input[i - k])) ||
                        (input[i - k] == '-' && i - k >= 1 && (input[i - k - 1] == 'E' || input[i - k - 1] == 'e'))); k++) {
                    t.append(input[i - k]);
                }

                if (i - k == 0) t.append(input[0] == '+' ? '+' : '-');


                StringBuilder sb = new StringBuilder();
                input = sb.append(Arrays.copyOfRange(input, 0, i - t.length())).append(String.valueOf(input[i] == '+' ?
                                Double.parseDouble(reverse(t.toString())) + Double.parseDouble(s.toString()) :
                                Double.parseDouble(reverse(t.toString())) - Double.parseDouble(s.toString())).toCharArray())
                        .append(Arrays.copyOfRange(input, i + j, input.length)).toString().toCharArray();
                i = 0;
            }
        }

        return input;
    }


    /**
     * Calculates the sine of an angle in degrees or radians
     *
     * @param deg   The angle in degrees
     * @param isDEG If true, the angle is in degrees; if false, the angle is in radians
     * @return The sine of the angle
     */
    private static double sinus(double deg, boolean isDEG) {
        if (deg % 180 == 0) return 0;

        return isDEG ? Math.sin(toRad(deg)) : Math.sin(deg);
    }

    /**
     * Calculates the cosine of an angle in degrees or radians
     *
     * @param deg   The angle in degrees
     * @param isDEG If true, the angle is in degrees; if false, the angle is in radians
     * @return The cosine of the angle
     */
    private static double cosinus(double deg, boolean isDEG) {
        return isDEG ? Math.cos(toRad(deg)) : Math.cos(deg);
    }

    /**
     * Calculates the tangent of an angle in degrees or radians
     *
     * @param deg   The angle in degrees
     * @param isDEG If true, the angle is in degrees; if false, the angle is in radians
     * @return The tangent of the angle
     */
    private static double tangens(double deg, boolean isDEG) {
        if (deg % 180 == 0) return 0;

        return isDEG ? Math.tan(toRad(deg)) : Math.tan(deg);
    }

    /**
     * Calculates the factorial of a number
     *
     * @param f The input number
     * @return The factorial of the input number
     */
    private static double factorial(double f) {
        if (f >= 11) return Double.POSITIVE_INFINITY;

        double ret = 1;
        for (double i = 1; i <= f; i++) {
            ret = ret * i;
        }
        return ret;
    }

    /**
     * Calculates the n-th root of a base number
     *
     * @param base The base number
     * @param n    The root degree
     * @return The n-th root of the base number
     */
    private static double root(double base, double n) {
        return Math.pow(base, 1.0 / n);
    }

    /**
     * Calculates the logarithm of a number with a specified base
     *
     * @param base The logarithmic base.
     * @param n    The number to calculate the logarithm for
     * @return The logarithm of the number with the specified base
     */
    private static double log(double base, double n) {
        return Math.log(n) / Math.log(base);
    }

    /**
     * Returns the absolute value of a number
     *
     * @param i The input number
     * @return The absolute value of the input number
     */
    private static double abs(double i) {
        return i < 0 ? i * -1 : i;
    }

    /**
     * Converts an angle from radians to degrees
     *
     * @param i The angle in radians
     * @return The angle in degrees
     */
    private static double toDeg(double i) {
        return i * 180 / 3.141592653589793;
    }

    /**
     * Converts an angle from degrees to radians
     *
     * @param i The angle in degrees
     * @return The angle in radians
     */
    private static double toRad(double i) {
        return i * 3.141592653589793 / 180;
    }

    /**
     * Finds the maximum value in an array of integers
     *
     * @param a The array of integers
     * @return The maximum value in the array
     */
    private static int max(int... a) {
        if (a.length == 0) return 0;

        int max = a[0];
        for (int j : a) {
            if (j > max) max = j;
        }

        return max;
    }

    /**
     * this methode rounds double values
     * it is used in order to avoid floating point errors
     *
     * @param value         is the given value
     * @param decimalPlaces is the number of decimal places there will be
     * @return is value rounded to x (decimal places)
     */
    private static double roundDouble(double value, int decimalPlaces) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
        bigDecimal = bigDecimal.setScale(decimalPlaces, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();
    }

    /**
     * Counts the number of occurrences of a character in a string
     *
     * @param s      The input string
     * @param search The character to count
     * @return The number of occurrences of the character in the string
     */
    private static int countInstances(String s, char search) {
        int ret = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == search) ret++;
        }
        return ret;
    }

    /**
     * Reverses a given string
     *
     * @param s The input string
     * @return The reversed string
     */
    private static String reverse(String s) {
        StringBuilder r = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) {
            r.append(s.charAt(i));
        }
        return r.toString();
    }

    /**
     * Removes all occurrences of a specified character from a string
     *
     * @param s The input string
     * @param c The character to be removed
     * @return The string with all occurrences of the character removed
     */
    private static String remove(String s, char c) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) r.append(s.charAt(i));
        }
        return r.toString();
    }

    /**
     * Checks if a character is present in an array of characters
     *
     * @param a The array of characters
     * @param c The character to search for
     * @return True if the character is present, false otherwise
     */
    private static boolean contains(char c, char... a) {
        for (char value : a) {
            if (value == c) return true;
        }
        return false;
    }

    /**
     * Replaces all occurrences of a specified character in a given string with a replacement string
     *
     * @param s           The input string in which the specified character will be replaced
     * @param exclude     The character to be replaced in the input string
     * @param replacement The string to replace the specified character with
     * @return A new string with all occurrences of the specified character replaced by the replacement string
     */
    private static String replace(String s, char exclude, String replacement) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            r.append((s.charAt(i) == exclude) ? replacement : s.charAt(i));
        }
        return r.toString();
    }


}