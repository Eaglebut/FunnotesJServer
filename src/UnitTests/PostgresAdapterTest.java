package UnitTests;

import db.Event;
import db.PostgresAdapter;
import db.User;
import org.junit.Assert;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;

class PostgresAdapterTest {

    PostgresAdapter adapter;
    User user;


    @org.junit.jupiter.api.BeforeEach
    void setUp() throws FileNotFoundException, SQLException {
        user = new User("unit@test.com", "unittest", "Unit", "Test");
        adapter = new PostgresAdapter("postgres", "0671211664Q", "test1");
        adapter.connect();
        adapter.insertUser(user);
    }

    @org.junit.jupiter.api.AfterEach
    void afterTest() throws SQLException {
        adapter.deleteUser(user);
        adapter.close();
    }

    @org.junit.jupiter.api.Test
    void insertUser() {
        User expected = user;
        user = adapter.getUser(user);
        Assert.assertEquals(user, expected);
    }

    @org.junit.jupiter.api.Test
    void updateUser() {

        User expected = new User(user.getId(), "unit@test.com", "unit1test", "Unit-one", "Test-one");
        adapter.updateUser(user, expected);
        user = adapter.getUser(expected);
        expected.setId(user.getId());
        Assert.assertEquals(expected.toString(), user.toString());
    }

    @org.junit.jupiter.api.Test
    void insertEvent() {
        Event expected = new Event("Test title", "Test description");
        System.out.println(expected);
        adapter.insertEvent(user, expected);
        user.setEvents(adapter.getUserEvents(user));
        expected.setId(user.getEvent(0).getId());
        Assert.assertEquals(expected.toString(), user.getEvent(0).toString());
    }

    @org.junit.jupiter.api.Test
    void getUserEvents() {
        Event event1 = new Event(user.getId(), "Test title 1", "Test description 1");
        Event event = new Event(user.getId(), "Test title", "Test description");
        adapter.insertEvent(user, event);
        adapter.insertEvent(user, event1);

        ArrayList<Event> got = adapter.getUserEvents(user);
        ArrayList<Event> expected = new ArrayList<>();
        event.setId(got.get(0).getId());
        event1.setId(got.get(1).getId());
        expected.add(event);
        expected.add(event1);

        Assert.assertEquals(expected.toString(), got.toString());
    }

    @org.junit.jupiter.api.Test
    void getEvent() {
        Event event = new Event("Test title", "Test description");
        adapter.insertEvent(user, event);
        user.setEvents(adapter.getUserEvents(user));
        Event got = adapter.getEvent(user, user.getEvent(0));
        event.setId(got.getId());
        Assert.assertEquals(event.toString(), got.toString());
    }

    @org.junit.jupiter.api.Test
    void updateEvent() {
        Event event = new Event(user.getId(), "Test title", "Test description");
        adapter.insertEvent(user, event);
        event = new Event(user.getId(), "Test title 1", "Test description 1");
        user.setEvents(adapter.getUserEvents(user));
        event.setId(user.getEvent(0).getId());
        adapter.updateEvent(user, event);
        Assert.assertEquals(event.toString(), adapter.getEvent(user, event).toString());
    }

    @org.junit.jupiter.api.Test
    void deleteEvent() {
        Event event = new Event(user.getId(), "Test title", "Test description");
        adapter.insertEvent(user, event);
        user.setEvents(adapter.getUserEvents(user));
        System.out.println(adapter.deleteEvent(user, user.getEvent(0)));
        user.setEvents(adapter.getUserEvents(user));
        Assert.assertEquals(user.getEvents(), new ArrayList<>());
    }

    @org.junit.jupiter.api.Test
    void deleteUser() {
        adapter.deleteUser(user);
        assertNull(adapter.getUser(user));
        adapter.insertUser(user);
    }
}