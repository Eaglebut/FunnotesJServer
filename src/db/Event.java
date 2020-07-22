package db;

import org.json.JSONObject;

import java.util.Date;
import java.util.Set;

public class Event {
    public int id;
    public int userId;
    public Date startTime;
    public Date endTime;
    public String title;
    public String description;


    public Event(String json){
        JSONObject jsonEvent = new JSONObject(json);
        getFromJSONObject(jsonEvent);
    }

    public Event(JSONObject jsonEvent){
        getFromJSONObject(jsonEvent);
    }

    public Event(){}

    private void getFromJSONObject(JSONObject jsonEvent){
        Set<String> keys = jsonEvent.keySet();
        if(keys.contains("id")){
            id = jsonEvent.getInt("id");
        }
        if(keys.contains("userId")){
            userId = jsonEvent.getInt("userId");
        }
        if(keys.contains("userId")){
            startTime = new Date(jsonEvent.getInt("startTime"));
        }
        if(keys.contains("userId")){
            endTime = new Date(jsonEvent.getInt("endTime"));
        }
        if(keys.contains("userId")){
            title = jsonEvent.getString("title");
        }
        if(keys.contains("userId")){
            description = jsonEvent.getString("description");
        }
    }


    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON(){
        JSONObject jsonEvent = new JSONObject();
        jsonEvent.put("id", id);
        jsonEvent.put("startTime", startTime);
        jsonEvent.put("endTime", endTime);
        jsonEvent.put("title", title);
        jsonEvent.put("description", description);
        return jsonEvent;
    }
}
