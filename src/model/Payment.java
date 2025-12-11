package model;

public interface Payment {
    int getBalance();
    void addPayment(int amount);
    boolean deduct(int amount);
    String getName();
}
