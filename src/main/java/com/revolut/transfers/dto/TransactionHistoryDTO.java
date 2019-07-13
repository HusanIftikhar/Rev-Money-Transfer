package com.revolut.transfers.dto;

import com.revolut.transfers.enums.TransferType;
import com.revolut.transfers.model.Transaction;
import org.javamoney.moneta.Money;

import javax.money.format.MonetaryFormats;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TransactionHistoryDTO {

    private String dateTime;
    private String transactionType;
    private String transactionAmount;

    public TransactionHistoryDTO() {
    }

    public TransactionHistoryDTO(Transaction transaction){

        this.dateTime = transaction.getTransactionTime().format(DateTimeFormatter.ISO_DATE_TIME);
        this.transactionType = transaction.getTransferType().name();
        this.transactionAmount =  MonetaryFormats.getAmountFormat(Locale.US).format(transaction.getTransferAmount());

    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
