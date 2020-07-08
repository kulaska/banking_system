package banking;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<CreditCard> creditCards = new ArrayList<>();

    private static AccountHandler accountHandler = new AccountHandler();

    private static DBHandler dbHandler;

    public static void main(String[] args) throws SQLException {
        String dbName;

        if (args[0].equals("-fileName")) {
            dbName = args[1];
            dbHandler = new DBHandler(dbName);
        }
        else {
            throw new IllegalArgumentException("Unknown argument type");
        }

        Menu.mainMenu();
        dbHandler.closeConnection();
    }

    public static void programExit() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    public static AccountHandler getAccountHandler() {
        return accountHandler;
    }

    public static List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public static DBHandler getDbHandler() { return dbHandler; }
}
