package my.project.bank;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class BankAccount {


    private double cashMoney;
    private final List<BankAction> actionHistory = new LinkedList<>();

    public void depositedMoney(LocalDate date, double amount){
        actionHistory.add(new BankAction
                (BankAction.eActionType.Deposited, date, amount, cashMoney, cashMoney+amount));
        cashMoney += amount;
    }

    public void receivingPayment(LocalDate date, double amount){
        actionHistory.add(new BankAction
                (BankAction.eActionType.ReceivingPayment, date, amount, cashMoney, cashMoney+amount));
        cashMoney += amount;
    }

    public double withdrawalMoney(LocalDate date, double amount){
        actionHistory.add(new BankAction
                (BankAction.eActionType.Withdrawal, date, amount, cashMoney, cashMoney-amount));
        cashMoney -= amount;
        return amount;
    }

    public double getCreditBalance(){
        return cashMoney;
    }
}
