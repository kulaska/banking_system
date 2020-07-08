package banking;

public class CreditCard {

    private Long balance = 0L;
    private String cardNumber;
    private String pin;
    private String id;

    public CreditCard(String cardNumber, String pin, String id) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.id = id;
    }

    public CreditCard(String cardNumber, String pin, String id, Long balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.id = id;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public Long getBalance() {
        return balance;
    }
}
