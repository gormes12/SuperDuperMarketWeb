package my.project.bank;

import dto.BankTransactionsDTO;

import java.time.LocalDate;

public class BankTransaction {

    public enum eActionType{
        Withdrawal,
        ReceivingPayment,
        Deposited
    }

    private final eActionType type;
    private final LocalDate date;
    private final double amount;
    private final double creditBalanceBefore;
    private final double creditBalanceAfter;

    public BankTransaction(eActionType type, LocalDate date, double amount,
                           double creditBalanceBefore, double creditBalanceAfter){
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.creditBalanceBefore = creditBalanceBefore;
        this.creditBalanceAfter = creditBalanceAfter;
    }

    public eActionType getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public double getCreditBalanceBefore() {
        return creditBalanceBefore;
    }

    public double getCreditBalanceAfter() {
        return creditBalanceAfter;
    }

    public BankTransactionsDTO createBankTransactionsDTO(){
        return new BankTransactionsDTO(type.toString(), date, amount, creditBalanceBefore, creditBalanceAfter);
    }

}
