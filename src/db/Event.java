package db;

import org.json.JSONObject;

import java.sql.Time;
import java.util.Set;

public class Event {
    private int id;
    private int userId;
    private Time startTime;
    private Time endTime;
    private String title;
    private String description;


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
        this.setId(id);
        this.setUserId(userId);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setTitle(title);
        this.setDescription(description);
    }

    public Event(int id) {
        this.setId(id);
    }

    public Event(int id, int userId) {
        this.setId(id);
        this.setUserId(userId);
    }

    public Event(int id, int userId, String title, String description) {
        this.setId(id);
        this.setUserId(userId);
        this.setStartTime(new Time(System.currentTimeMillis()));
        this.setEndTime(getStartTime());
        this.setTitle(title);
        this.setDescription(description);
    }

    public Event(int userId, String title, String description) {
        this.setUserId(userId);
        this.setStartTime(new Time(System.currentTimeMillis()));
        this.setEndTime(getStartTime());
        this.setTitle(title);
        this.setDescription(description);
    }

    public Event(Time startTime, Time endTime, String title, String description) {
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setTitle(title);
        this.setDescription(description);

    }

    public Event(String title, String description) {
        setTitle(title);
        setDescription(description);
        this.setStartTime(new Time(System.currentTimeMillis()));
        this.setEndTime(getStartTime());

    }

    private void getFromJSONObject(JSONObject jsonEvent) {
        Set<String> keys = jsonEvent.keySet();
        if (keys.contains("id")) {
            setId(jsonEvent.getInt("id"));
        }
        if (keys.contains("userId")) {
            setUserId(jsonEvent.getInt("userId"));
        }
        if (keys.contains("userId")) {
            setStartTime(new Time(jsonEvent.getInt("startTime")));
        }
        if(keys.contains("userId")){
            setEndTime(new Time(jsonEvent.getInt("endTime")));
        }
        if(keys.contains("userId")){
            setTitle(jsonEvent.getString("title"));
        }
        if(keys.contains("userId")){
            setDescription(jsonEvent.getString("description"));
        }
    }


    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject jsonEvent = new JSONObject();
        jsonEvent.put("id", getId());
        long time = getStartTime().getTime();
        jsonEvent.put("startTime", time);
        time = getEndTime().getTime();
        jsonEvent.put("endTime", time);
        jsonEvent.put("title", getTitle());
        jsonEvent.put("description", getDescription());
        return jsonEvent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
