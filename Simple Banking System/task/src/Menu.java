package banking;

import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    private static final String MENU_CREATE_AN_ACCOUNT = "Create an account";
    private static final String MENU_EXIT = "Exit";
    private static final String MENU_LOG_OUT = "Log out";
    private static final String MENU_LOG_IN = "Log into account";
    private static final String MENU_BALANCE = "Balance";
    private static final String MENU_ADD_INCOME = "Add income";
    private static final String MENU_DO_TRANSFER = "Do transfer";
    private static final String MENU_CLOSE_ACCOUNT = "Close account";

    private static final String MENU_MAIN = "main";
    private static final String MENU_LOGGED_IN = "logged_in";


    private static String[] mainMenuOptions = {
            MENU_EXIT,
            MENU_CREATE_AN_ACCOUNT,
            MENU_LOG_IN
    };

    private static String[] loggedInMenuOptions = {
            MENU_EXIT,
            MENU_BALANCE,
            MENU_ADD_INCOME,
            MENU_DO_TRANSFER,
            MENU_CLOSE_ACCOUNT,
            MENU_LOG_OUT
    };

    private static String currentMenu = MENU_MAIN;

    private static Scanner scanner = new Scanner(System.in);

    public static void mainMenu() throws SQLException {
        currentMenu = MENU_MAIN;

        for (int i = 1; i < mainMenuOptions.length; i++) {
            System.out.println(i + ": " + mainMenuOptions[i]);
        }

        System.out.println("0: " + mainMenuOptions[0]);

        int menuOption = scanner.nextInt();
        menuHandler(menuOption);
    }

    public static void loggedInMenu() throws SQLException {
        currentMenu = MENU_LOGGED_IN;

        for (int i = 1; i < loggedInMenuOptions.length; i++) {
            System.out.println(i + ": " + loggedInMenuOptions[i]);
        }

        System.out.println("0: " + loggedInMenuOptions[0]);

        int menuOption = scanner.nextInt();
        menuHandler(menuOption);
    }

    public static void menuHandler(int menuOptionNumber) throws SQLException {

        String[] currentMenuOptions = getMenuOptions(currentMenu);

        String menuOption = currentMenuOptions[menuOptionNumber];

        AccountHandler accountHandler = Main.getAccountHandler();

        switch (menuOption) {
            case MENU_EXIT:
                Main.programExit();
                break;
            case MENU_LOG_IN:
                System.out.println("Enter your card number:");
                String cardNumber = scanner.next();

                System.out.println("Enter your PIN:");
                String pin = scanner.next();

                accountHandler.logInToAccount(cardNumber, pin);
                break;
            case MENU_BALANCE:
                accountHandler.getBalance();
                break;
            case MENU_LOG_OUT:
                accountHandler.logOut();
                break;
            case MENU_CREATE_AN_ACCOUNT:
                accountHandler.createAccount();
                break;
            case MENU_ADD_INCOME:
                System.out.println("Enter the sum of your deposit: ");
                Long depositNumber = scanner.nextLong();

                accountHandler.addIncome(depositNumber);
                loggedInMenu();
                break;
            case MENU_DO_TRANSFER:
                handleTransfer();
                break;
            case MENU_CLOSE_ACCOUNT:
                accountHandler.closeAccount();
                mainMenu();
                break;
            default:
                break;
        }
    }

    private static String[] getMenuOptions(String menuType) {
        String[] arr;

        switch (menuType) {
            case MENU_MAIN:
                arr = mainMenuOptions;
                break;
            case MENU_LOGGED_IN:
                arr = loggedInMenuOptions;
                break;
            default:
                arr = new String[]{};
        }

        return arr;
    }

    private static void handleTransfer() throws SQLException {
        AccountHandler accountHandler = Main.getAccountHandler();

        System.out.println("Enter the number of card you want to make a transfer to: ");
        String cardNumberToTransferTo = scanner.next();

        System.out.println("Enter amount of money you are willing to transfer: ");
        Long numberToTransfer = scanner.nextLong();

        try {
            accountHandler.transferMoney(cardNumberToTransferTo, numberToTransfer);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());

            System.out.println("Please, try again");

            handleTransfer();
        } catch(Error e) {
            System.out.println(e.getMessage());
            loggedInMenu();
        }
    }
}
