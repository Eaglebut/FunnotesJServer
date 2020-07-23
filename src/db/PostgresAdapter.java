package db;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class PostgresAdapter {


    private Connection connection = null;
    private Statement statement = null;

    private String user;
    private String password;
    private String databaseName;
    private JSONObject commands;

    private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/";
    private static final String POSTGRES_DRIVER = "org.postgresql.Driver";
    private static final String SETTINGS_PATH = "settings";
    private static final String COMMAND_FILE = "SQLCommands.json";
    private static final String CREATE_DATABASE_JSON = "create_database";
    private static final String GET_USER_JSON = "get_user";
    private static final int FIRST_VARIABLE = 1;
    private static final int SECOND_VARIABLE = 3;


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

    private void createStatement() throws SQLException {
        if (!connection.isClosed()) {
            statement = connection.createStatement();
        } else {
            throw new SQLException("database not opened");
        }
    }


    public void createDatabase() throws SQLException {
        createStatement();
        if (!statement.isClosed()) {
            connection.setAutoCommit(false);
            JSONArray commands = this.commands.getJSONArray(CREATE_DATABASE_JSON);
            for (int i = 0; i < commands.length(); i++) {
                statement.executeUpdate(commands.getString(i));
            }
            statement.close();
            connection.commit();
            connection.setAutoCommit(true);
        }
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

    public User getUser(String email, String password) throws SQLException {
        createStatement();
        JSONArray sqlArray = commands.getJSONArray(GET_USER_JSON);
        sqlArray.put(FIRST_VARIABLE, email);
        sqlArray.put(SECOND_VARIABLE, password);

        StringBuilder queryString = new StringBuilder();

        for (int i = 0; i < sqlArray.length(); i++) {
            queryString.append(sqlArray.getString(i));
        }
        System.out.println(queryString);
        ResultSet set = statement.executeQuery(queryString.toString());
        User user = new User();
        while (set.next()) {
            user.id = set.getInt("id");
            user.email = (String) set.getObject("email");
            user.password = set.getString("password");
        }
        System.out.println(user);
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException, SQLException {
        PostgresAdapter adapter = new PostgresAdapter("postgres", "0671211664Q", "test1");
        adapter.connect();
        adapter.getUser("awdd@awda.com", "test");
    }
}
