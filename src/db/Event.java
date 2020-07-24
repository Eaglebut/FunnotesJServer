package db;

import org.json.JSONObject;

import java.sql.Time;
import java.util.Set;

public class Event {
    public int id;
    public int userId;
    public Time startTime;
    public Time endTime;
    public String title;
    public String description;


    public Event(String json) {
        JSONObject jsonEvent = new JSONObject(json);
        getFromJSONObject(jsonEvent);
    }

    public Event(JSONObject jsonEvent) {
        getFromJSONObject(jsonEvent);
    }

    public Event() {
    }

    public Event(int id, int userId, Time startTime, Time endTime, String title, String description) {
        this.id = id;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
    }

    public Event(int id) {
        this.id = id;
    }

    public Event(int id, int userId) {
        this.id = id;
        this.userId = userId;
    }

    public Event(int id, int userId, String title, String description) {
        this.id = id;
        this.userId = userId;
        this.startTime = new Time(System.currentTimeMillis());
        this.endTime = startTime;
        this.title = title;
        this.description = description;
    }

    public Event(int userId, String title, String description) {
        this.userId = userId;
        this.startTime = new Time(System.currentTimeMillis());
        this.endTime = startTime;
        this.title = title;
        this.description = description;
    }

    public Event(Time startTime, Time endTime, String title, String description) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
    }

    private void getFromJSONObject(JSONObject jsonEvent) {
        Set<String> keys = jsonEvent.keySet();
        if (keys.contains("id")) {
            id = jsonEvent.getInt("id");
        }
        if (keys.contains("userId")) {
            userId = jsonEvent.getInt("userId");
        }
        if (keys.contains("userId")) {
            startTime = new Time(jsonEvent.getInt("startTime"));
        }
        if(keys.contains("userId")){
            endTime = new Time(jsonEvent.getInt("endTime"));
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
