package edu.iit.gtds.tax_department_system.util;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.model.TransactionList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileHandleUtilsTest {

    private ObservableList<Transaction> transactions;
    private Path tempFilePath;

    @BeforeEach
    void setUp() {
        // Create a list of sample transactions
        transactions = FXCollections.observableArrayList(
                new Transaction(0,"BILL001", "ITEM001", 10.0, 5.0, 20.0, 2, 50.0, 12345),
                new Transaction(1,"BILL002", "ITEM002", 15.0, 10.0, 30.0, 3, 75.0, 67890)
        );

        // Set up a temporary file path for testing
        tempFilePath = Paths.get("test_transactions.txt");

        // Mock TransactionList to return sample transactions and the path to the temp file
        TransactionList transactionList = TransactionList.getInstance();
        transactionList.setTransactions(transactions);
        transactionList.setPath(tempFilePath.toString());
    }

    @Test
    void testUpdateFile_validTransactions() throws IOException {
        // Act: Call the method to update the file
        FileHandleUtils.updateFile();

        // Verify the content in the temporary file
        List<String> fileContent = Files.readAllLines(tempFilePath);

        // Expected strings for each transaction
        String expectedLine1 = "BILL001,ITEM001,10.0,5.0,20.0,2,50.0,12345,";
        String expectedLine2 = "BILL002,ITEM002,15.0,10.0,30.0,3,75.0,67890,";

        // Assert: Check if the file contains the expected values
        assertEquals(2, fileContent.size());  // Two transactions in the list
        assertTrue(fileContent.contains(expectedLine1));
        assertTrue(fileContent.contains(expectedLine2));
    }

    @Test
    void testUpdateFile_noTransactions() throws IOException {
        // Arrange: Set an empty list of transactions
        transactions.clear();

        // Act: Call the method to update the file
        FileHandleUtils.updateFile();

        // Assert: Ensure the file is empty (no transactions written)
        List<String> fileContent = Files.readAllLines(tempFilePath);
        assertTrue(fileContent.isEmpty());
    }


    @AfterEach
    void tearDown() throws IOException {
        // Clean up by deleting the temporary file after each test
        Files.deleteIfExists(tempFilePath);
    }
}
