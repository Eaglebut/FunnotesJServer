package servlets;

import db.Event;
import db.PostgresAdapter;
import db.User;
import org.json.JSONException;
import org.json.JSONObject;
import other.Settings;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/event")
public class EventServlet extends HttpServlet {

    PostgresAdapter adapter;

    @Override
    public void init() {
        try {
            adapter = new PostgresAdapter(Settings.USER, "0671211664Q", "funnotes");
            adapter.connect();
        } catch (SQLException | FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private User getUserAndEvent(HttpServletRequest request, HttpServletResponse response) {
        String bodyString = UserServlet.getBody(request, response);
        if (bodyString == null) {
            return null;
        }
        JSONObject bodyObject = new JSONObject(bodyString);
        User user;
        Event event;
        try {
            user = new User(bodyObject.getJSONObject("user"));
            event = new Event(bodyObject.getJSONObject("event"));
        } catch (JSONException | NullPointerException exception) {
            System.out.println(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        if (!user.isValid()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        ArrayList<Event> events = new ArrayList<>();
        events.add(event);
        user.setEvents(events);
        return user;
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

        User user = getUserAndEvent(request, response);
        if (user == null) {
            return;
        }
        if (!user.getEvent(0).canInsert()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int status = adapter.insertEvent(user, user.getEvent(0));
        if (status != PostgresAdapter.COMPLETED) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = getUserAndEvent(request, response);
        if (user == null) {
            return;
        }
        if (user.getEvent(0).getId() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Event event = adapter.getEvent(user, user.getEvent(0));
        if (event == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType(request.getContentType());
        response.getWriter().write(event.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        User user = getUserAndEvent(request, response);
        if (user == null) {
            return;
        }
        int status = adapter.deleteEvent(user, user.getEvent(0));
        if (status != PostgresAdapter.COMPLETED) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        User user = getUserAndEvent(request, response);
        if (user == null) {
            return;
        }
        if (user.getEvent(0).getId() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int status = adapter.updateEvent(user, user.getEvent(0));
        if (status != PostgresAdapter.COMPLETED) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
