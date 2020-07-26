package db;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

public class Event {
    private int id;
    private int userId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String title;
    private String description;

    private static class Constants {
        private static final String ID = "id";
        private static final String USER_ID = "userId";
        private static final String START_TIME = "startTime";
        private static final String END_TIME = "endTime";
        private static final String TITLE = "title";
        private static final String DESCRIPTION = "description";
        private static final int MSECOND = 1000;
    }

    public Event(String json) {
        JSONObject jsonEvent = new JSONObject(json);
        getFromJSONObject(jsonEvent);
    }

    public Event(JSONObject jsonEvent) {
        getFromJSONObject(jsonEvent);
    }

    public Event() {
    }

    public Event(int id, int userId, Timestamp startTime, Timestamp endTime, String title, String description) {
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
        this.setStartTime(new Timestamp(System.currentTimeMillis()));
        this.setEndTime(getStartTime());
        this.setTitle(title);
        this.setDescription(description);
    }

    public Event(int userId, String title, String description) {
        this.setUserId(userId);
        this.setStartTime(new Timestamp(System.currentTimeMillis()));
        this.setEndTime(getStartTime());
        this.setTitle(title);
        this.setDescription(description);
    }

    public Event(Timestamp startTime, Timestamp endTime, String title, String description) {
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setTitle(title);
        this.setDescription(description);

    }

    public Event(String title, String description) {
        setTitle(title);
        setDescription(description);
        this.setStartTime(new Timestamp(System.currentTimeMillis()));
        this.setEndTime(getStartTime());

    }

    private void getFromJSONObject(JSONObject jsonEvent) {
        Set<String> keys = jsonEvent.keySet();
        if (keys.contains(Constants.ID)) {
            setId(jsonEvent.getInt(Constants.ID));
        }
        if (keys.contains(Constants.USER_ID)) {
            setUserId(jsonEvent.getInt(Constants.USER_ID));
        }
        if (keys.contains(Constants.START_TIME)) {
            setStartTime(new Timestamp(jsonEvent.getInt(Constants.START_TIME)));
        }
        if (keys.contains(Constants.END_TIME)) {
            setEndTime(new Timestamp(jsonEvent.getInt(Constants.END_TIME)));
        }
        if (keys.contains(Constants.TITLE)) {
            setTitle(jsonEvent.getString(Constants.TITLE));
        }
        if (keys.contains(Constants.DESCRIPTION)) {
            setDescription(jsonEvent.getString(Constants.DESCRIPTION));
        }
    }


    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject jsonEvent = new JSONObject();
        jsonEvent.put(Constants.ID, getId());
        jsonEvent.put(Constants.START_TIME, getStartTime().getTime() / Constants.MSECOND);
        jsonEvent.put(Constants.END_TIME, getEndTime().getTime() / Constants.MSECOND);
        jsonEvent.put(Constants.TITLE, getTitle());
        jsonEvent.put(Constants.DESCRIPTION, getDescription());
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
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

    public boolean equals(Event event) {
        return userId == event.userId &&
                Objects.equals(title, event.title) &&
                Objects.equals(description, event.description);
    }

}
