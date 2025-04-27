package edu.iit.gtds.tax_department_system.util;

import edu.iit.gtds.tax_department_system.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChecksumUtilsTest {

    // Declare some Transaction objects that will be used in the test cases
    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;

    @BeforeEach
    void setUp() {
        // Initialize some sample transactions with dummy data
        transaction1 = new Transaction(1, "BILL123", "ITEM123", 10.0, 5.0, 20.0, 10, 200.0, 12345);
        transaction2 = new Transaction(2, "BILL124", "ITEM124", 15.0, 10.0, 30.0, 15, 450.0, 12346);
        transaction3 = new Transaction(3, "BILL125", "ITEM125", 20.0, 5.0, 25.0, 5, 125.0, 12347);
    }

    // Test for valid transaction with known checksum
    @Test
    void testChecksum_validTransaction1() {
        // Calculate checksum for transaction1
        assertEquals(32, ChecksumUtils.getChecksum(transaction1));
    }

    // Test for valid transaction with known checksum
    @Test
    void testChecksum_validTransaction2() {
        // Calculate checksum for transaction2
        assertEquals(33, ChecksumUtils.getChecksum(transaction2));
    }

    // Test for valid transaction with known checksum
    @Test
    void testChecksum_validTransaction3() {
        // Calculate checksum for transaction3
        assertEquals(31, ChecksumUtils.getChecksum(transaction3));
    }

    // Test transaction with empty fields
    @Test
    void testChecksum_emptyTransaction() {
        // Test with an empty transaction (all fields are empty)
        Transaction emptyTransaction = new Transaction(0, "", "", 0.0, 0.0, 0.0, 0, 0.0, 0);
        assertEquals(13, ChecksumUtils.getChecksum(emptyTransaction));
    }

    // Test transaction with mixed data types
    @Test
    void testChecksum_mixedData() {
        // Test with a transaction having mixed types of data (letters, digits, special characters)
        Transaction mixedDataTransaction = new Transaction(5, "BILL_001", "ITEM_001", 30.0, 10.0, 50.0, 20, 1000.0, 12349);
        assertEquals(34, ChecksumUtils.getChecksum(mixedDataTransaction));
    }
}
