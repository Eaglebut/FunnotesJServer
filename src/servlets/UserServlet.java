package servlets;

import db.PostgresAdapter;
import db.User;
import org.json.JSONObject;
import other.Settings;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

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

    public static String getBody(HttpServletRequest request, HttpServletResponse response) {
        if (request.getContentType() == null || !request.getContentType().contains("json")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        Scanner scanner;
        try {
            scanner = new Scanner(request.getReader());
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        StringBuilder requestBodyString = new StringBuilder();
        while (scanner.hasNextLine())
            requestBodyString.append(scanner.nextLine());
        return requestBodyString.toString();
    }


    public static User getUser(HttpServletRequest request, HttpServletResponse response) {
        String body = getBody(request, response);
        if (body == null) {
            return null;
        }
        User user = new User(body);
        if (!user.isValid()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        return user;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String body = getBody(request, response);
        if (body == null) {
            return;
        }
        JSONObject bodyJSON = new JSONObject(body);
        User oldUser = new User(bodyJSON.getJSONObject("user"));
        User newUser = new User(bodyJSON.getJSONObject("new_user"));
        int status;
        if (oldUser.isValid()) {
            status = adapter.updateUser(oldUser, newUser);
            if (status == PostgresAdapter.COMPLETED) {
                return;
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        User user = getUser(request, response);
        if (user == null) {
            return;
        }
        if (user.getName() == null || user.getSurname() == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int status = adapter.insertUser(user);
        if (status == PostgresAdapter.FAILED) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else if (status == PostgresAdapter.COMPLETED) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = getUser(request, response);
        if (user == null) {
            return;
        }
        user = adapter.getUser(user);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        response.setContentType(request.getContentType());
        response.getWriter().write(user.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        User user = getUser(request, response);
        if (user == null) {
            return;
        }
        int status = adapter.deleteUser(user);
        if (status == PostgresAdapter.FAILED) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
