package edu.iit.gtds.tax_department_system.service;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.model.TransactionList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProfitTableServiceTest {

    private ProfitTableService service;

    // Setup method that runs before each test case
    @BeforeEach
    void setUp() {
        // Initialize the ProfitTableService
        service = new ProfitTableService();

        // Set up some mock transactions
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(
                new Transaction(1, "BILL123", "ITEM123", 10.0, 5.0, 20.0, 10, 200.0, 12345),
                new Transaction(2, "BILL124", "ITEM124", 15.0, 10.0, 30.0, 15, 450.0, 12346),
                new Transaction(3, "BILL125", "ITEM125", 20.0, 5.0, 25.0, 5, 125.0, 12347)
        );

        // Set the mock transactions in the TransactionList singleton
        TransactionList.getInstance().setTransactions(transactions);
    }

    //  Test for calculating tax rate with positive final profit and tax rate
    @Test
    void testCalculateTaxRate_validInput() {
        Double finalProfit = 1000.0;
        Double taxRate = 10.0;

        // Calculate the tax rate
        Double result = service.calculateTaxRate(finalProfit, taxRate);

        // The expected tax rate is 1000 * 10% = 100.0
        assertNotNull(result);  // Ensure that the result is not null
        assertEquals(100.0, result);  // Verify the correct tax is calculated
    }

    //  Test for calculating tax rate with a negative tax rate
    @Test
    void testCalculateTaxRate_negativeTaxRate() {
        Double finalProfit = 1000.0;
        Double taxRate = -10.0;

        // Calculate the tax rate with a negative tax rate
        Double result = service.calculateTaxRate(finalProfit, taxRate);

        // Verify that the result is null for invalid input
        assertNull(result);
    }

    // Test case to verify the profit and loss calculations based on mock transactions
    @Test
    void testCalculateProfitLoss_validData() {
        // Call the method to calculate profit and loss
        Map<String, Double> result = service.calculateProfitLoss();

        // Assert the total profit and total loss values
        assertEquals(125.0, result.get("TotalProfit"));
        assertEquals(0.0, result.get("TotalLoss"));
    }

    // Test case to verify the profit and loss calculations when there is no profit or loss
    @Test
    void testCalculateProfitLoss_noProfitOrLoss() {
        // Set up the transaction list with no profit or loss (same price for sale and internal price)
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(
                new Transaction(1, "BILL126", "ITEM126", 10.0, 0.0, 10.0, 5, 50.0, 12348)
        );

        // Set the mock transactions in the TransactionList singleton
        TransactionList.getInstance().setTransactions(transactions);

        // Call the method to calculate profit and loss
        Map<String, Double> result = service.calculateProfitLoss();

        // Expected profit should be 0 and loss should be 0 as well
        assertEquals(0.0, result.get("TotalProfit"));
        assertEquals(0.0, result.get("TotalLoss"));
    }


    // Test case to verify the profit and loss calculations when there is a loss
    @Test
    void testCalculateProfitLoss_withLoss() {
        // Set up the transaction list with a loss (internal price higher than sale price)
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(
                new Transaction(1, "BILL127", "ITEM127", 15.0, 10.0, 10.0, 5, -25.0, 12349)
        );

        // Set the mock transactions in the TransactionList singleton
        TransactionList.getInstance().setTransactions(transactions);

        // Call the method to calculate profit and loss
        Map<String, Double> result = service.calculateProfitLoss();

        // Expected total profit should be 0 and total loss should be the absolute value of the loss
        assertEquals(0.0, result.get("TotalProfit"));
        assertEquals(75.0, result.get("TotalLoss"));
    }
}
