package exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExceptionsTest {

    @Test
    public void testConstructors () {
        ElementNotFoundException e1 = new ElementNotFoundException();
        NoteIntersectionException e2 = new NoteIntersectionException();

        GeneratorException e3 = new GeneratorException("Test");
        assertEquals("Test", e3.getMessage());
    }

}
