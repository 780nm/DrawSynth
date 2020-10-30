package persistence;

import exceptions.ElementNotFoundException;
import exceptions.GeneratorException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

// JSON Serialization helpers
public abstract class PersistenceUtil {

    // EFFECTS: Returns a JSONObject representing the data in the given ArrayList
    public static JSONArray arrayToJson(ArrayList<? extends Persistent> objects) {
        JSONArray jsonArray = new JSONArray();

        for (Persistent object: objects) {
            jsonArray.put(object.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: Returns a JSONObject representing the data in the given Map
    public static JSONObject mapToJson(Map<?, ?> map) {
        JSONObject jsonObject = new JSONObject();
        Set<?> keys = map.keySet();

        for (Object key : keys) {
            jsonObject.put(key.toString(), map.get(key).toString());
        }

        return jsonObject;
    }

    // EFFECTS: Applies Generator to each element in the JSONArray
    public static void forEachEntry(JSONArray jsonArray, Generator gen) {
        for (Object obj : jsonArray) {
            JSONObject json = (JSONObject) obj;
            gen.generate(json);
        }
    }

    // EFFECTS: given an ArrayList of Persistent objects, retrieves the one with the given UUID.
    //          Throws ElementNotFoundException is no such object is found.
    public static <T extends Persistent> T getElementWithID(ArrayList<T> list, UUID elementID) {
        for (T element : list) {
            if (element.getUuid().compareTo(elementID) == 0) {
                return element;
            }
        }
        throw new ElementNotFoundException();
    }

}
