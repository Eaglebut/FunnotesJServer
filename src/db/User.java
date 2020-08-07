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


    private static final String EVENTS = "events";


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
        return getEmail() != null && getPassword() != null && !getEmail().isEmpty() && !getPassword().isEmpty();
    }


    public User(String json) {
        getFromJSON(new JSONObject(json));
    }

    public User(JSONObject jsonUser) {
        getFromJSON(jsonUser);
    }

    private void getFromJSON(JSONObject jsonUser) {
        Set<String> keys = jsonUser.keySet();
        if (keys.contains(FunNotesDB.DatabaseIds.ID)) {
            setId(jsonUser.getInt(FunNotesDB.DatabaseIds.ID));
        }
        if (keys.contains(FunNotesDB.DatabaseIds.EMAIL)) {
            setEmail(jsonUser.getString(FunNotesDB.DatabaseIds.EMAIL));
        }
        if (keys.contains(FunNotesDB.DatabaseIds.PASSWORD)) {
            setPassword(jsonUser.getString(FunNotesDB.DatabaseIds.PASSWORD));
        }
        if (keys.contains(FunNotesDB.DatabaseIds.NAME)) {
            setName(jsonUser.getString(FunNotesDB.DatabaseIds.NAME));
        }
        if (keys.contains(FunNotesDB.DatabaseIds.SURNAME)) {
            setSurname(jsonUser.getString(FunNotesDB.DatabaseIds.SURNAME));
        }
        if (keys.contains(EVENTS)) {
            JSONArray jsonEvents = jsonUser.getJSONArray(EVENTS);
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
        jsonEvent.put(FunNotesDB.DatabaseIds.ID, getId());
        jsonEvent.put(FunNotesDB.DatabaseIds.EMAIL, getEmail());
        jsonEvent.put(FunNotesDB.DatabaseIds.PASSWORD, getPassword());
        jsonEvent.put(FunNotesDB.DatabaseIds.NAME, getName());
        jsonEvent.put(FunNotesDB.DatabaseIds.SURNAME, getSurname());
        JSONArray jsonEvents = new JSONArray();
        if (getEvents() != null) {
            for (Event event : getEvents()) {
                jsonEvents.put(event.toJSON());
            }
            jsonEvent.put(EVENTS, jsonEvents);
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

    public void add(Event event) {
        if (events == null) {
            events = new ArrayList<>();
        }
        events.add(event);
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
