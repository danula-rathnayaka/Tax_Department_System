package edu.iit.gtds.tax_department_system.model;

import javafx.collections.ObservableList;

public class TransactionList {
    private static TransactionList instance;
    private ObservableList<Transaction> transactions;
    private String path;

    private TransactionList() {
    }

    public static TransactionList getInstance() {
        return instance == null ? instance = new TransactionList() : instance;
    }

    public ObservableList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ObservableList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
