package servlets;

import db.FunNotesDB;
import db.User;
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

    FunNotesDB db;

    @Override
    public void init() {
        try {
            db = FunNotesDB.getInstance(Settings.USER, "0671211664Q", "funnotes");
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
        User user = new User(request.getParameter("email"), request.getParameter("password"));
        if (!user.isValid()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        return user;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        User oldUser = getUser(request, response);
        String body = getBody(request, response);
        if (body == null || oldUser == null || !oldUser.isValid()) {
            return;
        }
        User newUser = new User(body);
        int status;
        if (oldUser.isValid()) {
            status = db.updateUser(oldUser, newUser);
            if (status == FunNotesDB.COMPLETED) {
                return;
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        String body = getBody(request, response);
        if (body == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        User user = new User(body);
        if (user.getName() == null || user.getSurname() == null || !user.isValid()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int status = db.insertUser(user);
        if (status == FunNotesDB.FAILED) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else if (status == FunNotesDB.COMPLETED) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = getUser(request, response);
        if (user == null) {
            return;
        }
        user = db.getUser(user);
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
        int status = db.deleteUser(user);
        if (status == FunNotesDB.FAILED) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
