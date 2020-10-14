package ui;

// Interface allowing for the construction of lambdas assisting input filtering
public interface DoubleEvaluator {

    // REQUIRES: arg is not NaN
    // EFFECTS: Return true if the input is deemed acceptable, false otherwise
    boolean evaluate(double arg);

}
