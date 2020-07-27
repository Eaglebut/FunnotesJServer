import db.PostgresAdapter;
import db.User;

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
            adapter = new PostgresAdapter("postgres", "0671211664Q", "test1");
            adapter.connect();
        } catch (SQLException | FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getContentType() == null || !request.getContentType().contains("json")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        Scanner scanner = new Scanner(request.getReader());
        StringBuilder requestBodyString = new StringBuilder();
        while (scanner.hasNextLine())
            requestBodyString.append(scanner.nextLine());
        User user = new User(requestBodyString.toString());
        user = adapter.getUser(user);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        response.getWriter().write(user.toString());
    }
}
