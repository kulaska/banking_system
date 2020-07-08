package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHandler {
    private Connection connection;
    private final String tableName = "card";

    SQLiteDataSource dataSource = new SQLiteDataSource();

    public DBHandler(String dbName) throws SQLException {
        connect(dbName);
        createNewTable();
    }

    private void connect(String dbname) {
        String url = "jdbc:sqlite:" + dbname;
        dataSource.setUrl(url);

        try {
            connection = dataSource.getConnection();
            if (connection.isValid(5));
                System.out.println("Connection Established");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createNewTable() throws SQLException {
        Statement statement = dataSource.getConnection().createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(\n"
                + "	id INTEGER ,\n"
                + "	number TEXT,\n"
                + "	pin TEXT,\n"
                + " balance INTEGER DEFAULT 0"
                + ");";

        statement.execute(sql);
    }

    public void createNewEntry(String number, String pin, int balance, long id) throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "INSERT INTO card\n" +
                "VALUES (" +  id + " " + number + " " + pin + " " + balance +  ")";

        statement.execute(sql);
    }

    public void createNewEntry(String number, String pin, long id) throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "INSERT INTO card (id, number, pin)\n" +
                "VALUES (" +  id + ", " + number + ", " + pin +  ")";

        statement.execute(sql);
    }

    public void deleteEntry(String cardNumber) throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "DELETE FROM " + tableName + "\n" +
                "WHERE number = " + cardNumber;

        statement.execute(sql);
    }

    public CreditCard findCard(String cardNumber) throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "SELECT id, pin, balance FROM " + tableName + "\n"
                + "WHERE number = " + cardNumber;

        ResultSet result = statement.executeQuery(sql);

        String id = "", pin = "";
        Long balance = 0L;

        while (result.next()) {
            id = result.getString("id");
            pin = result.getString("pin");
            balance = result.getLong("balance");
        }
        if (id != "")
            return new CreditCard(cardNumber, pin, id, balance);
        else
            return null;
    }

    public void changeBalance(Long newBalance, String cardNumber) throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "UPDATE " + tableName + "\n"
                + "SET balance = " + newBalance + "\n"
                + "WHERE number = " + cardNumber;

        statement.execute(sql);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
