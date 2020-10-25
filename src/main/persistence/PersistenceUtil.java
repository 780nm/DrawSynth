package persistence;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class PersistenceUtil {

    public static JSONArray arrayToJson(ArrayList<? extends Persistent> objects) {
        JSONArray jsonArray = new JSONArray();

        for (Persistent object: objects) {
            jsonArray.put(object.toJson());
        }

        return jsonArray;
    }

    public static JSONObject mapToJson(Map<?, ? extends Persistent> map) {
        JSONObject jsonObject = new JSONObject();

        Set<?> keys = map.keySet();

        for (Object key : keys) {
            jsonObject.put(key.toString(), map.get(key).toJson());
        }

        return jsonObject;
    }

}
