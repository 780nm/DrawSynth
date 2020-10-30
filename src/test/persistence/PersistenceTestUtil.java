package persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PersistenceTestUtil {

    public static boolean evalFileEquality(String file1, String file2) throws IOException {
        BufferedReader sample = new BufferedReader(new FileReader(file1));
        BufferedReader test = new BufferedReader(new FileReader(file2));
        String line1 = sample.readLine();
        String line2 = test.readLine();


        while (line1 != null || line2 != null) {
            if (line1 == null || !line1.equals(line2)) {
                return false;
            }
            line1 = sample.readLine();
            line2 = test.readLine();
        }

        return true;
    }

}

