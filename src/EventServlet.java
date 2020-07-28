import db.Event;
import db.PostgresAdapter;
import db.User;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/event")
public class EventServlet extends HttpServlet {

    PostgresAdapter adapter;

    @Override
    public void init() {
        try {
            adapter = new PostgresAdapter("postgres", "0671211664Q", "test1");
            adapter.connect();
        } catch (SQLException | FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        String bodyString = UserServlet.getBody(request, response);
        if (bodyString == null) {
            return;
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
            return;
        }
        if (!user.isValid() || !event.canInsert()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int status = adapter.insertEvent(user, event);
        if (status != PostgresAdapter.COMPLETED) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bodyString = UserServlet.getBody(request, response);
        if (bodyString == null) {
            return;
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
            return;
        }
        if (!user.isValid() || event.getId() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        event = adapter.getEvent(user, event);
        if (event == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType(request.getContentType());
        response.getWriter().write(event.toString());
    }

}
