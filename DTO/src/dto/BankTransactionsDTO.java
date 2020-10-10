package dto;

import java.time.LocalDate;

public class BankTransactionsDTO {
    private final String type;
    private final String date;
    private final double amount;
    private final double creditBalanceBefore;
    private final double creditBalanceAfter;

    public BankTransactionsDTO(String type, LocalDate date, double amount,
                           double creditBalanceBefore, double creditBalanceAfter){
        this.type = type;
        this.date = date.toString();
        this.amount = amount;
        this.creditBalanceBefore = creditBalanceBefore;
        this.creditBalanceAfter = creditBalanceAfter;
    }
}
