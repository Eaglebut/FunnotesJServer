package db;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private ArrayList<Event> events;

    private static class Constants {
        private static final String ID = "id";
        private static final String NAME = "name";
        private static final String SURNAME = "surname";
        private static final String REGISTRATION_TIME = "registrationTime";
        private static final String EMAIL = "email";
        private static final String PASSWORD = "password";
        private static final String EVENTS = "events";
    }

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

    public User(int id, String email, String password, String name, String surname) {
        this.setId(id);
        this.setEmail(email);
        this.setPassword(password);
        this.setName(name);
        this.setSurname(surname);
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
        if (keys.contains(Constants.ID)) {
            setId(jsonUser.getInt(Constants.ID));
        }
        if (keys.contains(Constants.EMAIL)) {
            setEmail(jsonUser.getString(Constants.EMAIL));
        }
        if (keys.contains(Constants.PASSWORD)) {
            setPassword(jsonUser.getString(Constants.PASSWORD));
        }
        if (keys.contains(Constants.NAME)) {
            setName(jsonUser.getString(Constants.NAME));
        }
        if (keys.contains(Constants.SURNAME)) {
            setSurname(jsonUser.getString(Constants.SURNAME));
        }
        if (keys.contains(Constants.EVENTS)) {
            JSONArray jsonEvents = jsonUser.getJSONArray(Constants.EVENTS);
            setEvents(new ArrayList<>());
            for (int i = 0; i < jsonEvents.length(); i++) {
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
        jsonEvent.put(Constants.ID, getId());
        jsonEvent.put(Constants.EMAIL, getEmail());
        jsonEvent.put(Constants.PASSWORD, getPassword());
        jsonEvent.put(Constants.NAME, getName());
        jsonEvent.put(Constants.PASSWORD, getSurname());
        JSONArray jsonEvents = new JSONArray();
        if (getEvents() != null) {
            for (Event event : getEvents()) {
                jsonEvents.put(event.toJSON());
            }
            jsonEvent.put(Constants.EVENTS, jsonEvents);
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
        if (events != null)
            return events.get(index);
        else return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email) &&
                password.equals(user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(events, user.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
