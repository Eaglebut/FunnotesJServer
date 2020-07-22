package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresAdapter {


    private Connection connection = null;
    private Statement statement = null;

    private String user;
    private String password;
    private String databaseName;

    private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/";
    private static final String POSTGRES_DRIVER = "org.postgresql.Driver";


    public PostgresAdapter(String user, String password, String databaseName) throws SQLException {
        setUser(user);
        setPassword(password);
        setDatabaseName(databaseName);
    }

    public PostgresAdapter(){}

    public void setUser(String user) throws SQLException {
        if (!connection.isClosed())
        {
            connection.close();
        }
        this.user = user;

    }

    public void setPassword(String password) throws SQLException {
        if (!connection.isClosed())
        {
            connection.close();
        }
        this.password = password;
    }

    public void setDatabaseName(String databaseName) throws SQLException {
        if (!connection.isClosed())
        {
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
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.out.println("Database not opened");
        }


        System.out.println("Opened database successfully");
    }


    public static void main(String[] args) {


    }
}
