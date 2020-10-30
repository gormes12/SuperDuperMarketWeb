package my.project.bank;

import dto.BankTransactionsDTO;
import dto.ZoneDTO;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class BankAccount {


    private double cashMoney;
    private final List<BankTransaction> actionHistory = new LinkedList<>();

    public synchronized void depositedMoney(LocalDate date, double amount){
        actionHistory.add(new BankTransaction
                (BankTransaction.eActionType.Deposited, date, amount, cashMoney, cashMoney+amount));
        cashMoney += amount;
    }

    public synchronized void receivingPayment(LocalDate date, double amount){
        actionHistory.add(new BankTransaction
                (BankTransaction.eActionType.ReceivingPayment, date, amount, cashMoney, cashMoney+amount));
        cashMoney += amount;
    }

    public synchronized double withdrawalMoney(LocalDate date, double amount){
        actionHistory.add(new BankTransaction
                (BankTransaction.eActionType.Withdrawal, date, amount, cashMoney, cashMoney-amount));
        cashMoney -= amount;
        return amount;
    }

    public double getCreditBalance(){
        return cashMoney;
    }

    public int getBankTransactionsVersion() {
        return actionHistory.size();
    }

    public synchronized List<BankTransactionsDTO> getTransactionsEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > actionHistory.size()) {
            fromIndex = 0;
        }

        List<BankTransactionsDTO> bankTransactionsList= new LinkedList<>();
        for (BankTransaction transaction : actionHistory.subList(fromIndex, actionHistory.size())){
            bankTransactionsList.add(transaction.createBankTransactionsDTO());
        }

        return bankTransactionsList;
    }
}
