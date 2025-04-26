package edu.iit.gtds.tax_department_system.service;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.model.TransactionList;
import edu.iit.gtds.tax_department_system.util.ChecksumUtils;
import javafx.collections.ObservableList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TransactionTableService {

    public List<String[]> validateTransactions(ObservableList<Transaction> transactions) {
        ArrayList<String[]> errorTransactionList = new ArrayList<>();

        for (Transaction transaction : transactions) {
            Boolean isValid = true; // Sets is valid record

            if (!isValidItemCode(transaction.getItemCode())) {
                // Check if item code is valid
                errorTransactionList.add(new String[]{transaction.getLineNo().toString(), "Invalid Item Code"});
                isValid = false;
            } else if (transaction.getInternalPrice() < 0) {
                // Internal price cannot be negative
                errorTransactionList.add(new String[]{transaction.getLineNo().toString(), "Negative Internal Price"});
                isValid = false;
            } else if (transaction.getDiscount() < 0) {
                // Discount cannot be negative
                errorTransactionList.add(new String[]{transaction.getLineNo().toString(), "Negative Discount"});
                isValid = false;
            } else if (transaction.getSalePrice() < 0) {
                // Sale price must be non-negative
                errorTransactionList.add(new String[]{transaction.getLineNo().toString(), "Negative Sale Price"});
                isValid = false;
            } else if (transaction.getQuantity() < 0) {
                // Quantity must be non-negative
                errorTransactionList.add(new String[]{transaction.getLineNo().toString(), "Negative Quantity"});
                isValid = false;
            } else if ((ChecksumUtils.getChecksum(transaction) != transaction.getChecksum())) {
                // Checksum validation using helper utility
                errorTransactionList.add(new String[]{transaction.getLineNo().toString(), "Invalid Checksum"});
                isValid = false;
            }

            // Set the final status of the transaction
            transaction.setIsValid(isValid ? "Valid" : "Invalid");
        }

        return errorTransactionList;
    }

    private boolean isValidItemCode(String itemCode) {
        return itemCode != null && Pattern.matches("^\\w+$", itemCode);
    }

    public void updateFile() {
        TransactionList transactionList = TransactionList.getInstance();

        ObservableList<Transaction> transactions = transactionList.getTransactions();

        // If list is empty or null, no need to write
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("No transactions to update.");
            return;
        }

        // Write each transaction to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionList.getPath()))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toString());  // toString gives the comma seperated record
                writer.newLine();  // Add line break between entries
            }
        } catch (IOException e) {
            System.out.println("Error updating the file: " + e.getMessage());
        }
    }

}
