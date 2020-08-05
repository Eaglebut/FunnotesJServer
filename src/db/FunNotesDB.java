package db;

import org.json.JSONArray;
import org.json.JSONObject;
import other.Settings;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

public class FunNotesDB {

    private static FunNotesDB INSTANCE;

    private Connection connection = null;

    private String user;
    private String password;
    private String databaseName;
    private final JSONObject commands;

    private static final String POSTGRES_DRIVER = "org.postgresql.Driver";
    private static final String CREATE_DATABASE_JSON = "create_database";

    private static final String DATABASE_OPENED_MESSAGE = "Opened database successfully";
    private static final String DATABASE_NOT_OPENED_MESSAGE = "Database not opened";

    private static final long MSECOND = 1000;

    public static final int COMPLETED = 0;
    public static final int FAILED = 1;

    public static class DatabaseIds {
        public static final String ID = "id";
        public static final String USER_ID = "user_id";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";

        public static final String NAME = "name";
        public static final String SURNAME = "surname";
        public static final String REGISTRATION_TIME = "registration_time";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }

    public static class GetUserConstants {
        private static final String JSON = "get_user";
        private static final int EMAIL = 1;
        private static final int PASSWORD = 3;
    }

    public static class InsertUserConstants {
        private static final String JSON = "insert_user";
        private static final int NAME = 1;
        private static final int SURNAME = 3;
        private static final int EMAIL = 5;
        private static final int PASSWORD = 7;
    }

    public static class DeleteUserConstants {
        private static final String JSON = "delete_user";
        private static final int ID = 1;
    }

    public static class UpdateUserConstants {
        private static final String JSON = "update_user";
        private static final int NAME = 1;
        private static final int SURNAME = 3;
        private static final int EMAIL = 5;
        private static final int PASSWORD = 7;
        private static final int ID = 9;
    }

    public static class GetEventConstants {
        private static final String JSON = "get_event";
        private static final int USER_ID = 1;
        private static final int ID = 3;
    }

    public static class InsertEventConstants {
        private static final String JSON = "insert_event";
        private static final int USER_ID = 1;
        private static final int START_TIME = 3;
        private static final int END_TIME = 5;
        private static final int TITLE = 7;
        private static final int DESCRIPTION = 9;
    }

    public static class GetUserEventsConstants {
        private static final String JSON = "get_user_events";
        private static final int USER_ID = 1;
    }

    public static class UpdateEventConstants {
        private static final String JSON = "update_event";
        private static final int START_TIME = 1;
        private static final int END_TIME = 3;
        private static final int TITLE = 5;
        private static final int DESCRIPTION = 7;
        private static final int ID = 11;
        private static final int USER_ID = 9;
    }

    public static class DeleteEventConstants {
        private static final String JSON = "delete_event";
        private static final int ID = 3;
        private static final int USER_ID = 1;
    }

    private FunNotesDB(String user, String password, String databaseName) throws SQLException, FileNotFoundException {
        setUser(user);
        setPassword(password);
        setDatabaseName(databaseName);
        commands = Settings.loadCommandsFromFile();
    }


    public static FunNotesDB getInstance(String user, String password, String databaseName) throws FileNotFoundException, SQLException {
        if (INSTANCE == null) {
            INSTANCE = new FunNotesDB(user, password, databaseName);
            INSTANCE.connect();
        }
        return INSTANCE;
    }


    public void setUser(String user) throws SQLException {
        if (connection != null)
            if (!connection.isClosed()) {
                connection.close();
            }
        this.user = user;

    }

    public void setPassword(String password) throws SQLException {
        if (connection != null)
            if (!connection.isClosed()) {
                connection.close();
            }
        this.password = password;
    }

    public void setDatabaseName(String databaseName) throws SQLException {
        if (connection != null)
            if (!connection.isClosed()) {
                connection.close();
            }
        this.databaseName = databaseName;
    }

    private void connect() {
        try {
            Class.forName(POSTGRES_DRIVER);
            connection = DriverManager
                    .getConnection(Settings.POSTGRES_URL + databaseName, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println(DATABASE_NOT_OPENED_MESSAGE);
            return;
        }


        System.out.println(DATABASE_OPENED_MESSAGE);
    }

    private Statement createStatement() throws SQLException {
        if (!connection.isClosed()) {
            return connection.createStatement();
        } else {
            throw new SQLException(DATABASE_NOT_OPENED_MESSAGE);
        }
    }

    public void createDatabase() throws SQLException {
        Statement statement = createStatement();
            connection.setAutoCommit(false);
            JSONArray commands = this.commands.getJSONArray(CREATE_DATABASE_JSON);
            for (int i = 0; i < commands.length(); i++) {
                statement.executeUpdate(commands.getString(i));
            }
            statement.close();
            connection.commit();
            connection.setAutoCommit(true);
        }


    public boolean isConnected() throws SQLException {
        return connection.isValid(Connection.TRANSACTION_NONE);
    }

    private String getSQLString(JSONArray sqlArray) {
        StringBuilder queryString = new StringBuilder();

        for (int i = 0; i < sqlArray.length(); i++) {
            queryString.append(sqlArray.getString(i));
        }
        return queryString.toString();
    }

    public User getUser(String email, String password) {
        if (email == null && password == null) {
            return null;
        }
        JSONArray sqlArray = commands.getJSONArray(GetUserConstants.JSON);
        sqlArray.put(GetUserConstants.EMAIL, email);
        sqlArray.put(GetUserConstants.PASSWORD, password);

        Statement statement;
        ResultSet set;
        User user = new User();
        try {
            statement = createStatement();
            set = statement.executeQuery(getSQLString(sqlArray));
            while (set.next()) {
                user.setId(set.getInt(DatabaseIds.ID));
                user.setEmail((String) set.getObject(DatabaseIds.EMAIL));
                user.setPassword(set.getString(DatabaseIds.PASSWORD));
                user.setName(set.getString(DatabaseIds.NAME));
                user.setSurname(set.getString(DatabaseIds.SURNAME));
            }
            statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
        if (user.isValid())
            return user;
        else return null;
    }

    public User getUser(User user) {
        return getUser(user.getEmail(), user.getPassword());
    }

    public int insertUser(User user) {
        if (user == null || !user.isValid()) {
            return FAILED;
        }
        JSONArray sqlArray = commands.getJSONArray(InsertUserConstants.JSON);
        sqlArray.put(InsertUserConstants.NAME, user.getName());
        sqlArray.put(InsertUserConstants.SURNAME, user.getSurname());
        sqlArray.put(InsertUserConstants.EMAIL, user.getEmail());
        sqlArray.put(InsertUserConstants.PASSWORD, user.getPassword());
        return executeStatement(getSQLString(sqlArray));
    }

    private int executeStatement(String queryString) {
        try {
            Statement statement = createStatement();
            statement.executeUpdate(queryString);
            statement.close();
            return COMPLETED;
        } catch (SQLException exception) {
            //TODO Обрабатывать разные ошибки
            System.out.println(exception.getMessage());
            return FAILED;
        }
    }

    public int insertUser(String name, String surname, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        return insertUser(user);
    }

    public int deleteUser(User user) {
        if (user == null || !user.isValid()) {
            return FAILED;
        }
        user = getUser(user);
        if (user == null || !user.isValid()) {
            return FAILED;
        }
        JSONArray sqlArray = commands.getJSONArray(DeleteUserConstants.JSON);
        sqlArray.put(DeleteUserConstants.ID, String.valueOf(user.getId()));
        return executeStatement(getSQLString(sqlArray));
    }

    public int deleteUser(String email, String password) {
        return deleteUser(new User(email, password));
    }

    public int updateUser(User user, User newUser) {
        if (user == null || !user.isValid()) {
            return FAILED;
        }
        user = getUser(user);
        if (!user.isValid()) {
            return FAILED;
        }
        JSONArray sqlArray = commands.getJSONArray(UpdateUserConstants.JSON);
        sqlArray.put(UpdateUserConstants.NAME, newUser.getName() != null ? newUser.getName() : user.getName());
        sqlArray.put(UpdateUserConstants.SURNAME, newUser.getSurname() != null ? newUser.getSurname() : user.getSurname());
        sqlArray.put(UpdateUserConstants.EMAIL, newUser.getEmail() != null ? newUser.getEmail() : user.getEmail());
        sqlArray.put(UpdateUserConstants.PASSWORD, newUser.getPassword() != null ? newUser.getPassword() : user.getPassword());
        sqlArray.put(UpdateUserConstants.ID, String.valueOf(user.getId()));
        return executeStatement(getSQLString(sqlArray));
    }

    public int updateUser(String email, String password, String newEmail, String newPassword, String newName, String newSurname) {
        return updateUser(new User(email, password), new User(newEmail, newPassword, newName, newSurname));
    }

    public int updateUser(User user, String newEmail, String newPassword, String newName, String newSurname) {
        return updateUser(user, new User(newEmail, newPassword, newName, newSurname));
    }

    public int updateUser(String email, String password, User newUser) {
        return updateUser(new User(email, password), newUser);
    }

    public Event getEvent(User user, int id) {
        if (user == null || !user.isValid()) {
            return null;
        }
        user = getUser(user);

        if (user == null || !user.isValid()) {
            return null;
        }

        JSONArray sqlArray = commands.getJSONArray(GetEventConstants.JSON);

        sqlArray.put(GetEventConstants.USER_ID, String.valueOf(user.getId()));
        sqlArray.put(GetEventConstants.ID, String.valueOf(id));

        Statement statement;
        ResultSet set;
        Event event = new Event();
        try {
            statement = createStatement();
            set = statement.executeQuery(getSQLString(sqlArray));
            while (set.next()) {
                setEvent(set, event);
            }
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        if (event.getId() == 0) {
            return null;
        }

        return event;
    }

    private static void setEvent(ResultSet set, Event event) throws SQLException {
        event.setId(set.getInt(DatabaseIds.ID));
        event.setUserId(set.getInt(DatabaseIds.USER_ID));
        event.setStartTime(set.getTimestamp(DatabaseIds.START_TIME));
        event.setEndTime(set.getTimestamp(DatabaseIds.END_TIME));
        event.setTitle(set.getString(DatabaseIds.TITLE));
        event.setDescription(set.getString(DatabaseIds.DESCRIPTION));
    }

    public Event getEvent(User user, Event event) {
        return getEvent(user.getEmail(), user.getPassword(), event.getId());
    }

    public Event getEvent(String email, String password, Event event) {
        return getEvent(email, password, event.getId());
    }

    public Event getEvent(String email, String password, int id) {
        return getEvent(new User(email, password), id);
    }

    public int insertEvent(User user, Event event) {
        if (user == null || !user.isValid()) {
            return FAILED;
        }
        user = getUser(user);

        if (!user.isValid()) {
            return FAILED;
        }

        JSONArray sqlArray = commands.getJSONArray(InsertEventConstants.JSON);
        sqlArray.put(InsertEventConstants.DESCRIPTION, event.getDescription());
        sqlArray.put(InsertEventConstants.END_TIME, String.valueOf(event.getEndTime().getTime() / MSECOND));
        sqlArray.put(InsertEventConstants.START_TIME, String.valueOf(event.getStartTime().getTime() / MSECOND));
        sqlArray.put(InsertEventConstants.TITLE, event.getTitle());
        sqlArray.put(InsertEventConstants.USER_ID, String.valueOf(user.getId()));
        return executeStatement(getSQLString(sqlArray));
    }

    public int insertEvent(String email, String password, Event event) {
        return insertEvent(new User(email, password), event);
    }

    public int insertEvent(User user, Timestamp start_time, Timestamp end_time, String title, String description) {
        return insertEvent(user,
                new Event(start_time, end_time, title, description));
    }

    public int insertEvent(String email, String password, Timestamp start_time, Timestamp end_time, String title, String description) {
        return insertEvent(new User(email, password), new Event(start_time, end_time, title, description));
    }


    public ArrayList<Event> getUserEvents(User user) {
        if (user == null || !user.isValid()) {
            return null;
        }

        user = getUser(user);

        if (!user.isValid()) {
            return null;
        }

        JSONArray sqlArray = commands.getJSONArray(GetUserEventsConstants.JSON);
        sqlArray.put(GetUserEventsConstants.USER_ID, String.valueOf(user.getId()));

        ArrayList<Event> events = new ArrayList<>();
        Statement statement;
        ResultSet set;
        Event event;
        try {
            statement = createStatement();
            set = statement.executeQuery(getSQLString(sqlArray));
            while (set.next()) {
                event = new Event();
                setEvent(set, event);
                events.add(event);
            }
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return events;
    }

    public ArrayList<Event> getUserEvents(String email, String password) {
        return getUserEvents(new User(email, password));
    }

    public int updateEvent(User user, Event event) {
        if (user == null || !user.isValid()) {
            return FAILED;
        }

        user = getUser(user);
        Event oldEvent = getEvent(user, event);
        if (!user.isValid() || oldEvent == null) {
            return FAILED;
        }

        JSONArray sqlArray = commands.getJSONArray(UpdateEventConstants.JSON);
        sqlArray.put(UpdateEventConstants.ID,
                String.valueOf(event.getId()));
        sqlArray.put(UpdateEventConstants.USER_ID,
                String.valueOf(user.getId()));
        sqlArray.put(UpdateEventConstants.TITLE,
                event.getTitle() != null ? event.getTitle() : oldEvent.getTitle());
        sqlArray.put(UpdateEventConstants.DESCRIPTION,
                event.getDescription() != null ? event.getDescription() : oldEvent.getDescription());
        sqlArray.put(UpdateEventConstants.START_TIME,
                String.valueOf((event.getStartTime() != null ? event.getStartTime().getTime() : oldEvent.getStartTime().getTime()) / MSECOND));
        sqlArray.put(UpdateEventConstants.END_TIME,
                String.valueOf((event.getEndTime() != null ? event.getEndTime().getTime() : oldEvent.getEndTime().getTime()) / MSECOND));
        return executeStatement(getSQLString(sqlArray));
    }

    public int updateEvent(String email, String password, Event event) {
        return updateEvent(new User(email, password), event);
    }

    public int updateEvent(String email, String password, Timestamp start_time, Timestamp end_time, String title, String description) {
        return updateEvent(new User(email, password),
                new Event(start_time, end_time, title, description));
    }

    public int updateEvent(User user, Timestamp start_time, Timestamp end_time, String title, String description) {
        return updateEvent(user,
                new Event(start_time, end_time, title, description));
    }

    public int deleteEvent(User user, Event event) {
        if (user == null || !user.isValid()) {
            return FAILED;
        }
        user = getUser(user);
        if (!user.isValid()) {
            return FAILED;
        }
        JSONArray sqlArray = commands.getJSONArray(DeleteEventConstants.JSON);
        sqlArray.put(DeleteEventConstants.ID, String.valueOf(event.getId()));
        sqlArray.put(DeleteEventConstants.USER_ID, String.valueOf(user.getId()));
        return executeStatement(getSQLString(sqlArray));
    }

    public int deleteEvent(String email, String password, Event event) {
        return deleteEvent(new User(email, password), event);
    }

    public int deleteEvent(String email, String password, int id) {
        return deleteEvent(new User(email, password), new Event(id));
    }

    public int deleteEvent(User user, int id) {
        return deleteEvent(user, new Event(id));
    }

    public void close() throws SQLException {
        connection.close();
    }
}
