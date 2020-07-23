package db;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;

public class User {
    public int id;
    public String email;
    public String password;
    public String name;
    public String surname;
    public Event[] events;

    public User() {
    }

    public User(String json) {
        getFromJSON(new JSONObject(json));
    }

    public User(JSONObject jsonUser) {
        getFromJSON(jsonUser);
    }

    private void getFromJSON(JSONObject jsonUser) {
        Set<String> keys = jsonUser.keySet();
        if(keys.contains("id")){
            id = jsonUser.getInt("id");
        }
        if(keys.contains("email")){
            email = jsonUser.getString("email");
        }
        if(keys.contains("password")){
            password = jsonUser.getString("password");
        }
        if(keys.contains("name")){
            name = jsonUser.getString("name");
        }
        if(keys.contains("surname")){
            surname = jsonUser.getString("surname");
        }
        if(keys.contains("surname")){
            surname = jsonUser.getString("surname");
        }
        if(keys.contains("events")){
            JSONArray jsonEvents = jsonUser.getJSONArray("events");
            events = new Event[jsonEvents.length()];
            for (int i = 0; i < jsonEvents.length(); i++){
                events[i] = new Event(jsonEvents.getJSONObject(i));
            }
        }
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject jsonEvent = new JSONObject();
        jsonEvent.put("id", id);
        jsonEvent.put("email", email);
        jsonEvent.put("password", password);
        jsonEvent.put("name", name);
        jsonEvent.put("surname", surname);
        JSONArray jsonEvents = new JSONArray();
        if (events != null) {
            for (Event event : events) {
                jsonEvents.put(event.toJSON());
            }
            jsonEvent.put("events", jsonEvents);
        }
        return jsonEvent;
    }
}
