package banking;

import java.sql.SQLException;

public class AccountHandler {
    private CreditCard currentCard = null;
    private String currentCardNumber = "";

    private static CreditCard findCard(String cardNumber) throws SQLException {
        return Main.getDbHandler().findCard(cardNumber);
    }

    private static boolean checkForLuhn(String str) {

        int[] ints = new int[str.length()];

        for (int i = 0; i < str.length(); i++) {
            ints[i] = Integer.parseInt(str.substring(i, i + 1));
        }

        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;
            if (j > 9) {
                j = j % 10 + 1;
            }
            ints[i] = j;
        }

        int sum = 0;

        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }

        return sum % 10 == 0;
    }

    private long generateRandomNumber(int rank) {
        long leftLimit = 0L;
        long rightLimit = (long) Math.pow(10, rank);
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }

    private String generateCardNumber() {
        String randomNumber = "" + generateRandomNumber(10);

        while (randomNumber.length() < 10) {
            randomNumber = "0" + randomNumber;
        }

        return randomNumber;
    }

    private String getPin() {
        String randomNumber = "" + generateRandomNumber(4);

        while (randomNumber.length() < 4) {
            randomNumber = "0" + randomNumber;
        }

        return randomNumber;
    }

    public void logInToAccount(String cardNumber, String pin) throws SQLException {
        CreditCard card = findCard(cardNumber);

        if (card == null) {
            System.out.println("Wrong card number or PIN!");
            Menu.mainMenu();
        } else {
            String trueCardPin = card.getPin();

            if (pin.equals(trueCardPin)) {
                currentCard = card;
                currentCardNumber = cardNumber;
                System.out.println("You have successfully logged in!");
                Menu.loggedInMenu();
            } else {
                System.out.println("Wrong card number or PIN!");
                Menu.mainMenu();
            }
        }
    }

    public void createAccount() throws SQLException {
        String id = generateCardNumber();
        String cardNumber = "400000" + id;
        String pin = getPin();

        while (!checkForLuhn(cardNumber))
            cardNumber = "400000" + generateCardNumber();

        DBHandler handler = Main.getDbHandler();
        handler.createNewEntry(cardNumber, pin, Long.parseLong(id));

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pin);

        Menu.mainMenu();
    }

    public void logOut() throws SQLException {
        currentCard = null;
        Menu.mainMenu();
    }

    public void getBalance() throws SQLException {
        CreditCard currentCardUpdated = findCard(currentCardNumber);
        System.out.println("Balance: " + currentCardUpdated.getBalance());
        Menu.loggedInMenu();
    }

    private void removeFunds(String cardNumber, long currentBalance, long fundsToRemove) throws SQLException {
        Main.getDbHandler().changeBalance(currentBalance - fundsToRemove, cardNumber);
    }

    public void addIncome(String cardNumber, long incomeToAdd) throws SQLException {
        CreditCard currentCardUpdated = findCard(cardNumber);
        Long newBalance = currentCardUpdated.getBalance() + incomeToAdd;

        if (newBalance < currentCardUpdated.getBalance() || newBalance < 0)
            throw new IllegalArgumentException("Balance should not contain less money after deposit");

        Main.getDbHandler().changeBalance(newBalance, currentCardNumber);
    }

    public void addIncome(long incomeToAdd) throws SQLException {
        CreditCard currentCardUpdated = findCard(currentCardNumber);
        Long newBalance = currentCardUpdated.getBalance() + incomeToAdd;

        if (newBalance < currentCardUpdated.getBalance() || newBalance < 0)
            throw new IllegalArgumentException("Balance should not contain less money after deposit");

        Main.getDbHandler().changeBalance(newBalance, currentCardNumber);
    }

    public void transferMoney(String cardNumberToTransferTo, long sumOfMoneyToTransfer) throws SQLException {
        if (currentCardNumber == cardNumberToTransferTo)
            throw new Error("You can't transfer money to the same account!");

        if (!checkForLuhn(cardNumberToTransferTo))
            throw new IllegalArgumentException("Probably you made mistake in the card number. Please try again!");

        CreditCard currentCardUpdated = findCard(currentCardNumber);

        if (currentCardUpdated.getBalance() - sumOfMoneyToTransfer < 0)
            throw new Error("Not enough money!");

        CreditCard cardToTransferTo = findCard(cardNumberToTransferTo);

        if (cardToTransferTo == null)
            throw new Error("Such a card does not exist.");

        removeFunds(currentCardNumber, currentCardUpdated.getBalance(), sumOfMoneyToTransfer);
        addIncome(cardNumberToTransferTo, sumOfMoneyToTransfer);
    }

    public void closeAccount() throws SQLException {
        Main.getDbHandler().deleteEntry(currentCardNumber);
        logOut();
    }
}
