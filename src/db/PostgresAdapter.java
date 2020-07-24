package db;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class PostgresAdapter {


    private Connection connection = null;

    private String user;
    private String password;
    private String databaseName;
    private JSONObject commands;

    private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/";
    private static final String POSTGRES_DRIVER = "org.postgresql.Driver";
    private static final String SETTINGS_PATH = "settings";
    private static final String COMMAND_FILE = "SQLCommands.json";
    private static final String CREATE_DATABASE_JSON = "create_database";

    public static final int COMPLETED = 0;
    public static final int FAILED = 1;

    public static class GetUserConstants {
        private static final String GET_USER_JSON = "get_user";
        private static final int EMAIL = 1;
        private static final int PASSWORD = 3;
    }

    public static class InsertUserConstants {
        private static final String INSERT_USER_JSON = "insert_user";
        private static final int NAME = 1;
        private static final int SURNAME = 3;
        private static final int EMAIL = 5;
        private static final int PASSWORD = 7;
    }

    public static class DeleteUserConstants {
        private static final String DELETE_USER_JSON = "delete_user";
        private static final int ID = 1;
    }

    public static class UpdateUserConstants {
        private static final String UPDATE_USER_JSON = "update_user";
        private static final int NAME = 1;
        private static final int SURNAME = 3;
        private static final int EMAIL = 5;
        private static final int PASSWORD = 7;
        private static final int ID = 9;
    }


    public PostgresAdapter(String user, String password, String databaseName) throws SQLException, FileNotFoundException {
        setUser(user);
        setPassword(password);
        setDatabaseName(databaseName);
        loadCommandsFromFile();
    }

    public PostgresAdapter() {
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

    public void connect() {
        try {
            Class.forName(POSTGRES_DRIVER);
            connection = DriverManager
                    .getConnection( POSTGRES_URL + databaseName, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Database not opened");
        }


        System.out.println("Opened database successfully");
    }

    private Statement createStatement() throws SQLException {
        if (!connection.isClosed()) {
            return connection.createStatement();
        } else {
            throw new SQLException("database not opened");
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


    public void loadCommandsFromFile() throws FileNotFoundException {
        String commandsString;
        StringBuilder commandsStringBuilder = new StringBuilder();
        File commands = new File(SETTINGS_PATH, COMMAND_FILE);
        Scanner scanner;
        if (commands.exists() && commands.canRead()) {
            scanner = new Scanner(commands);
            while (scanner.hasNextLine()) {
                commandsStringBuilder.append(scanner.nextLine());
            }
            commandsString = commandsStringBuilder.toString();
            this.commands = new JSONObject(commandsString);
        } else throw new FileNotFoundException("Command file not found");
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

    public User getUser(String email, String password) throws SQLException {
        Statement statement = createStatement();
        JSONArray sqlArray = commands.getJSONArray(GetUserConstants.GET_USER_JSON);
        sqlArray.put(GetUserConstants.EMAIL, email);
        sqlArray.put(GetUserConstants.PASSWORD, password);

        String queryString = getSQLString(sqlArray);
        ResultSet set = statement.executeQuery(queryString);
        User user = new User();
        while (set.next()) {
            user.id = set.getInt("id");
            user.email = (String) set.getObject("email");
            user.password = set.getString("password");
            user.name = set.getString("name");
            user.surname = set.getString("surname");
        }
        statement.close();
        /*if(user.id == 0){
            return null;
        }*/
        return user;
    }

    public int insertUser(User user) {
        JSONArray sqlArray = commands.getJSONArray(InsertUserConstants.INSERT_USER_JSON);
        sqlArray.put(InsertUserConstants.NAME, user.name);
        sqlArray.put(InsertUserConstants.SURNAME, user.surname);
        sqlArray.put(InsertUserConstants.EMAIL, user.email);
        sqlArray.put(InsertUserConstants.PASSWORD, user.password);
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
            exception.printStackTrace();
            return FAILED;
        }
    }


    public int insertUser(String name, String surname, String email, String password) {
        User user = new User();
        user.name = name;
        user.surname = surname;
        user.email = email;
        user.password = password;
        return insertUser(user);
    }

    public int deleteUser(User user) {
        if (user.id == 0) {
            return FAILED;
        }
        JSONArray sqlArray = commands.getJSONArray(DeleteUserConstants.DELETE_USER_JSON);
        sqlArray.put(DeleteUserConstants.ID, String.valueOf(user.id));
        return executeStatement(getSQLString(sqlArray));
    }

    public int deleteUser(int id) {
        User user = new User();
        user.id = id;
        return deleteUser(user);
    }

    public int updateUser(User user) {
        JSONArray sqlArray = commands.getJSONArray(UpdateUserConstants.UPDATE_USER_JSON);
        sqlArray.put(UpdateUserConstants.NAME, user.name);
        sqlArray.put(UpdateUserConstants.SURNAME, user.surname);
        sqlArray.put(UpdateUserConstants.EMAIL, user.email);
        sqlArray.put(UpdateUserConstants.PASSWORD, user.password);
        sqlArray.put(UpdateUserConstants.ID, String.valueOf(user.id));
        return executeStatement(getSQLString(sqlArray));
    }

    public int updateUser(int id, String email, String password, String name, String surname) {
        User user = new User();
        user.email = email;
        user.password = password;
        user.name = name;
        user.surname = surname;
        user.id = id;
        return updateUser(user);
    }


    public static void main(String[] args) throws FileNotFoundException, SQLException {
        PostgresAdapter adapter = new PostgresAdapter("postgres", "0671211664Q", "test1");
        adapter.connect();
        User user = new User();
        user.name = "Test";
        user.surname = "Test";
        user.email = "test@test.test";
        user.password = "password";
        System.out.println(adapter.insertUser(user));
        user = adapter.getUser(user.email, user.password);
        System.out.println(user);
        user.name = "Testt";
        System.out.println(adapter.updateUser(user));
        System.out.println(adapter.getUser(user.email, user.password));
        adapter.deleteUser(user);
    }
}
