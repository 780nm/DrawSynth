package persistence;

import exceptions.GeneratorException;
import org.json.JSONObject;

public interface Generator {

    void generate(JSONObject json);

}
