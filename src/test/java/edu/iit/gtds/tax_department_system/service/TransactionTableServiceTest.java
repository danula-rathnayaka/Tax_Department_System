package edu.iit.gtds.tax_department_system.service;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.util.ChecksumUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionTableServiceTest {

    private TransactionTableService service;

    // Setup method to initialize the service before each test
    @BeforeEach
    void setup() {
        service = new TransactionTableService();
    }

    // Helper method to create a new transaction for testing
    private Transaction buildTransaction(
            Integer lineNo,
            String itemCode,
            Double internalPrice,
            Double discount,
            Double salePrice,
            Integer quantity
    ) {
        double lineTotal = salePrice * quantity - discount; // calculate line total
        Transaction transaction = new Transaction(
                lineNo,
                "BILL" + lineNo, // generates a unique bill ID
                itemCode,
                internalPrice,
                discount,
                salePrice,
                quantity,
                lineTotal,
                0
        );
        transaction.setChecksum(ChecksumUtils.getChecksum(transaction)); // set checksum
        return transaction;
    }

    // Test case where all transactions are valid
    @Test
    void testValidate_allValidTransaction() {
        Transaction tx = buildTransaction(1, "ITEM001", 100.0, 10.0, 90.0, 2);
        ObservableList<Transaction> list = FXCollections.observableArrayList(tx);
        List<String[]> errors = service.validateTransactions(list); // validate transactions
        assertTrue(errors.isEmpty()); // no errors should be present
        assertEquals("Valid", tx.getIsValid()); // transaction should be marked valid
    }


    // Test case where the item code is invalid
    @Test
    void testValidate_invalidItemCode() {
        Transaction tx = buildTransaction(2, "BAD CODE!", 100.0, 10.0, 90.0, 2);
        ObservableList<Transaction> list = FXCollections.observableArrayList(tx);
        List<String[]> errors = service.validateTransactions(list);
        assertEquals(1, errors.size()); // should have one error
        assertEquals("Invalid Item Code", errors.getFirst()[1]); // error message should match
        assertEquals("Invalid", tx.getIsValid()); // transaction should be marked invalid
    }

    // Test case where the internal price is negative
    @Test
    void testValidate_negativeInternalPrice() {
        Transaction tx = buildTransaction(3, "ITEM002", -50.0, 5.0, 45.0, 1);
        ObservableList<Transaction> list = FXCollections.observableArrayList(tx);
        List<String[]> errors = service.validateTransactions(list);
        assertEquals(1, errors.size()); // should have one error
        assertEquals("Negative Internal Price", errors.getFirst()[1]); // error message should match
        assertEquals("Invalid", tx.getIsValid()); // transaction should be marked invalid
    }

    // Test case where the discount is negative
    @Test
    void testValidate_negativeDiscount() {
        Transaction tx = buildTransaction(4, "ITEM003", 50.0, -5.0, 45.0, 1);
        ObservableList<Transaction> list = FXCollections.observableArrayList(tx);
        List<String[]> errors = service.validateTransactions(list);
        assertEquals(1, errors.size()); // should have one error
        assertEquals("Negative Discount", errors.getFirst()[1]); // error message should match
        assertEquals("Invalid", tx.getIsValid()); // transaction should be marked invalid
    }

    // Test case where the sales price is negative
    @Test
    void testValidate_negativeSalePrice() {
        Transaction tx = buildTransaction(5, "ITEM004", 50.0, 5.0, -1.0, 1);
        ObservableList<Transaction> list = FXCollections.observableArrayList(tx);
        List<String[]> errors = service.validateTransactions(list);
        assertEquals(1, errors.size()); // should have one error
        assertEquals("Negative Sale Price", errors.getFirst()[1]); // error message should match
        assertEquals("Invalid", tx.getIsValid()); // transaction should be marked invalid
    }

    // Test case where the quantity is negative
    @Test
    void testValidate_negativeQuantity() {
        Transaction tx = buildTransaction(6, "ITEM005", 50.0, 5.0, 45.0, -1);
        ObservableList<Transaction> list = FXCollections.observableArrayList(tx);
        List<String[]> errors = service.validateTransactions(list);
        assertEquals(1, errors.size()); // should have one error
        assertEquals("Negative Quantity", errors.getFirst()[1]); // error message should match
        assertEquals("Invalid", tx.getIsValid()); // transaction should be marked invalid
    }

    // Test case where the checksum is invalid
    @Test
    void testValidate_invalidChecksum() {
        Transaction tx = buildTransaction(7, "ITEM006", 50.0, 5.0, 45.0, 1);
        tx.setChecksum(999999); // bad checksum
        ObservableList<Transaction> list = FXCollections.observableArrayList(tx);
        List<String[]> errors = service.validateTransactions(list);
        assertEquals(1, errors.size()); // should have one error
        assertEquals("Invalid Checksum", errors.getFirst()[1]); // error message should match
        assertEquals("Invalid", tx.getIsValid()); // transaction should be marked invalid
    }
}
