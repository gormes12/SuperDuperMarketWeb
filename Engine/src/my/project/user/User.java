package my.project.user;

import my.project.bank.BankAccount;

import java.time.LocalDate;

public abstract class User {

    public enum eUserType{
        StoreOwner,
        Customer
    }

    protected final int ID;
    protected final String username;
    private final String userType;
    protected final BankAccount bankAccount;

    public User(int id, String username, eUserType userType) {
        this.ID = id;
        this.username = username;
        this.userType = userType.toString();
        bankAccount = new BankAccount();
    }

    public int getID() {
        return ID;
    }

    public String getUsername(){
        return username;
    }

    public abstract eUserType getUserType();

    public double withdrawalMoney(LocalDate date, double amount){
        return bankAccount.withdrawalMoney(date, amount);
    }

    public void depositedMoney(LocalDate date, double amount){
        bankAccount.depositedMoney(date, amount);
    }

    public void receivingPayment(LocalDate date, double amount){
        bankAccount.receivingPayment(date, amount);
    }

    public double getCreditBalance(){
        return bankAccount.getCreditBalance();
    }
}
