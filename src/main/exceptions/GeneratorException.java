package exceptions;

// Represents an exception during generation of model from saved configuration.
public class GeneratorException extends RuntimeException {

    public GeneratorException(String cause) {
        super(cause);
    }

}
