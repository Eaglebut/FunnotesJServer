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

@WebServlet("/user_all")
public class AllUserServlet extends HttpServlet {

    FunNotesDB db;

    @Override
    public void init() {
        try {
            db = FunNotesDB.getInstance(Settings.USER, "0671211664Q", "funnotes");
        } catch (SQLException | FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("json");
        System.out.println("get_all from " + request.getRemoteAddr());
        User user = UserServlet.getUser(request, response);
        if (user == null) {
            System.out.println(response.getStatus());
            return;
        }
        user = db.getUser(user);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            System.out.println(response.getStatus());
            return;
        }
        user.setEvents(db.getUserEvents(user));

        response.getWriter().write(user.toString());
        System.out.println(response.getStatus());
    }

}
