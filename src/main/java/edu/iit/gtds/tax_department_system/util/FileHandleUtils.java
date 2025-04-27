package edu.iit.gtds.tax_department_system.util;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.model.TransactionList;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandleUtils {

    private FileHandleUtils() {
    }

    public static void updateFile() {
        TransactionList transactionList = TransactionList.getInstance();

        ObservableList<Transaction> transactions = transactionList.getTransactions();

        // If list is null, no need to write
        if (transactions == null) {
            new Alert(Alert.AlertType.ERROR, "No transactions to update.").show();
            return;
        }

        // Write each transaction to the file
        try {
            // Create a FileWriter and BufferedWriter for writing data to the file
            FileWriter f = new FileWriter(transactionList.getPath());  // true to append data
            BufferedWriter b = new BufferedWriter(f);

            // Write each transaction to the file
            for (Transaction transaction : transactions) {
                b.write(transaction.toString());  // toString gives the comma-separated record
                b.newLine();  // Add line break between entries
            }

            // Close the BufferedWriter and FileWriter
            b.close();
            f.close();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error updating the file: " + e.getMessage()).show();
        }
    }
}
