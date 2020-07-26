package db;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private ArrayList<Event> events;

    public User() {
    }

    public User(int id, String email, String password, String name, String surname, ArrayList<Event> events) {
        this.setId(id);
        this.setEmail(email);
        this.setPassword(password);
        this.setName(name);
        this.setSurname(surname);
        this.setEvents(events);
    }

    public User(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public User(String email, String password, String name, String surname) {
        this.setEmail(email);
        this.setPassword(password);
        this.setName(name);
        this.setSurname(surname);
    }

    public User(int id) {
        this.setId(id);
    }

    public boolean isValid() {
        return getEmail() != null && getPassword() != null;
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
            setId(jsonUser.getInt("id"));
        }
        if(keys.contains("email")){
            setEmail(jsonUser.getString("email"));
        }
        if(keys.contains("password")){
            setPassword(jsonUser.getString("password"));
        }
        if(keys.contains("name")){
            setName(jsonUser.getString("name"));
        }
        if(keys.contains("surname")){
            setSurname(jsonUser.getString("surname"));
        }
        if(keys.contains("surname")){
            setSurname(jsonUser.getString("surname"));
        }
        if(keys.contains("events")){
            JSONArray jsonEvents = jsonUser.getJSONArray("events");
            setEvents(new ArrayList<>());
            for (int i = 0; i < jsonEvents.length(); i++){
                this.events.add(new Event(jsonEvents.getJSONObject(i)));
            }
        }
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject jsonEvent = new JSONObject();
        jsonEvent.put("id", getId());
        jsonEvent.put("email", getEmail());
        jsonEvent.put("password", getPassword());
        jsonEvent.put("name", getName());
        jsonEvent.put("surname", getSurname());
        JSONArray jsonEvents = new JSONArray();
        if (getEvents() != null) {
            for (Event event : getEvents()) {
                jsonEvents.put(event.toJSON());
            }
            jsonEvent.put("events", jsonEvents);
        }
        return jsonEvent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public Event getEvent(int index) {
        return events.get(index);
    }
}
