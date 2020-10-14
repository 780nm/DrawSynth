package ui;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

//Handles console input collection and filtering
public class InputHandler {

    private Scanner input;

    // EFFECTS: Constructs a new InputHandler
    public InputHandler() {
        input = new Scanner(System.in);
    }

    // REQUIRES: eval, msg and errorMsg are not null
    // EFFECTS: polls user for input until an integer answer matching the eval rule is input
    public int getSafeInt(DoubleEvaluator eval, String msg, String errorMsg) {
        double temp = Double.NaN;
        while (true) {
            try {
                System.out.print(msg);
                temp = input.nextDouble();
            } catch (InputMismatchException exception) {
                System.err.println("Error processing input: " + exception);
            } finally {
                input.nextLine();
                if (eval.evaluate(temp) && temp % 1 == 0) {
                    return (int) temp;
                } else {
                    System.out.println(errorMsg);
                }
            }
        }
    }

    // REQUIRES: eval, msg and errorMsg are not null
    // EFFECTS: polls user for input until a double answer matching the eval rule is input
    public double getSafeDouble(DoubleEvaluator eval, String msg, String errorMsg) {
        while (true) {
            double temp = Double.NaN;
            try {
                System.out.print(msg);
                temp = input.nextDouble();
            } catch (InputMismatchException exception) {
                System.err.println("Error processing input: " + exception);
            } finally {
                input.nextLine();
                if (eval.evaluate(temp)) {
                    return temp;
                } else {
                    System.out.println(errorMsg);
                }
            }
        }
    }

    // REQUIRES: acceptableAnswers, msg and errorMsg are not null or empty
    // EFFECTS: polls user for input until a string answer matching one of the acceptableAnswers is input
    public String getSafeString(String[] acceptableAnswers, String msg, String errorMsg) {
        while (true) {
            System.out.print(msg);
            final String temp = input.nextLine();
            if (Arrays.stream(acceptableAnswers).anyMatch(temp::equalsIgnoreCase)) {
                return temp;
            } else {
                System.out.println(errorMsg);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Closes the InputHandler
    public void close() {
        input.close();
    }

}