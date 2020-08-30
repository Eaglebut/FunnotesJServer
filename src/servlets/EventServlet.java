package servlets;

import db.Event;
import db.FunNotesDB;
import db.User;
import other.Settings;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@WebServlet("/event")
public class EventServlet extends HttpServlet {

    FunNotesDB db;

    @Override
    public void init() {
        try {
            db = FunNotesDB.getInstance(Settings.USER, "0671211664Q", "funnotes");
        } catch (SQLException | FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private User getUserAndEventId(HttpServletRequest request, HttpServletResponse response) {

        User user = UserServlet.getUser(request, response);
        Event event = new Event(Integer.parseInt(request.getParameter("id")));
        if (event.getId() == 0 || user == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        user.add(event);
        return user;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("json");
        System.out.println("get event " + request.getRemoteAddr());
        User user = getUserAndEventId(request, response);
        if (user == null) {
            System.out.println(response.getStatus());
            return;
        }
        if (user.getEvent(0).getId() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(response.getStatus());
            return;
        }
        Event event = db.getEvent(user, user.getEvent(0));
        if (event == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            System.out.println(response.getStatus());
            return;
        }
        response.setContentType(request.getContentType());
        response.getWriter().write(event.toString());
        System.out.println(response.getStatus());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        System.out.println("delete event " + request.getRemoteAddr());
        User user = getUserAndEventId(request, response);
        if (user == null) {
            System.out.println(response.getStatus());
            return;
        }
        int status = db.deleteEvent(user, user.getEvent(0));
        if (status != FunNotesDB.COMPLETED) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        System.out.println(response.getStatus());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        System.out.println("post event " + request.getRemoteAddr());
        User user = UserServlet.getUser(request, response);
        String body = UserServlet.getBody(request, response);
        if (user == null || body == null) {
            System.out.println(response.getStatus());
            return;
        }
        Event event = new Event(body);
        if (!event.canInsert()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(response.getStatus());
            return;
        }
        int status;
        if (event.getId() == 0) {
            status = db.insertEvent(user, event);
        } else {
            status = db.updateEvent(user, event);
        }
        System.out.println(event);
        if (status != FunNotesDB.COMPLETED) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        System.out.println(response.getStatus());
    }
}
