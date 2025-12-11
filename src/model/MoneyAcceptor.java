package model;

public class MoneyAcceptor implements Payment {

    private int amount;

    public MoneyAcceptor(int initialAmount) {
        this.amount = initialAmount;
    }

    @Override
    public int getBalance() {
        return amount;
    }

    @Override
    public void addPayment(int amount) {
        this.amount += amount;
    }

    @Override
    public boolean deduct(int amount) {
        if (this.amount >= amount) {
            this.amount -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Монетоприемник";
    }
}
