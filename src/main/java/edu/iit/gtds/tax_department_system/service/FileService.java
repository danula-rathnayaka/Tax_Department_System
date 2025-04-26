package edu.iit.gtds.tax_department_system.service;

import edu.iit.gtds.tax_department_system.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileService {

    // Method to read the file and return data
    public ObservableList<Transaction> readFile(File file) {
        try {
            // Read the given file
            Scanner scanner = new Scanner(file);
            ObservableList<Transaction> transactions = FXCollections.observableArrayList();

            Integer lineNumber = 0;

            // Loop until a next line is found
            while (scanner.hasNextLine()) {

                // Split line data for each , since csv file is used to store data
                String[] data = scanner.nextLine().split(",");

                // Add a new transaction with the extracted data

                transactions.add(
                        new Transaction(
                                lineNumber++,
                                data[0],
                                data[1],
                                Double.parseDouble(data[2]),
                                Double.parseDouble(data[3]),
                                Double.parseDouble(data[4]),
                                Integer.parseInt(data[5]),
                                Double.parseDouble(data[6]),
                                Integer.parseInt(data[7])
                        )
                );
            }

            // Return a list of transactions extracted
            return transactions;
        } catch (FileNotFoundException e) {

            // If error return a empty list
            return FXCollections.observableArrayList();
        }
    }

}
