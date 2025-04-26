package edu.iit.gtds.tax_department_system.service;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.model.TransactionList;
import javafx.collections.ObservableList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ProfitTableService {

    public Double calculateTaxRate(Double finalProfit, Double taxRate) {
        if (taxRate < 0) {
            try {
                throw new InvalidTaxRateException("Tax rate cannot be negative");
            } catch (InvalidTaxRateException e) {
                throw new RuntimeException(e);
            }
        }
        double finalTax = (finalProfit) * (taxRate / 100);
        finalTax = finalTax < 0 ? 0 : finalTax;
        return finalTax;
    }

    public void updateFile() {
        TransactionList transactionList = TransactionList.getInstance();

        ObservableList<Transaction> transactions = transactionList.getTransactions();

        if (transactions == null || transactions.isEmpty()) {
            System.out.println("No transactions to update.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionList.getPath()))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating the file: " + e.getMessage());
        }
    }

    private static class InvalidTaxRateException extends Exception {
        public InvalidTaxRateException(String message) {
            super(message);
        }
    }
}
