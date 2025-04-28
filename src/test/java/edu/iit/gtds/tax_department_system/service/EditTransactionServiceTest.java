package edu.iit.gtds.tax_department_system.service;

import edu.iit.gtds.tax_department_system.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EditTransactionServiceTest {

    private EditTransactionService service; // Service to be tested
    private Transaction transaction; // The transaction object that will be validated and updated

    @BeforeEach
    void setUp() {
        service = new EditTransactionService();

        // Initialize the transaction with dummy values for testing
        transaction = new Transaction(1, "BILL123", "ITEM123", 10.50, 5.0, 20.00, 10, 200.00, 12345);
    }

    // Test case for valid data where no errors should be encountered
    @Test
    void testValidateAndUpdate_noErrors() {
        // Valid input values for the transaction
        String itemCode = "ITEM123";
        String internalPriceStr = "10.50";
        String discountStr = "15";
        String salePriceStr = "20.00";
        String quantityStr = "5";
        String checksumStr = "12345";

        // Validate and update the transaction
        List<String> errors = service.validateAndUpdate(transaction, itemCode, internalPriceStr, discountStr, salePriceStr, quantityStr, checksumStr);

        // There should be no errors for valid inputs
        assertTrue(errors.isEmpty());

        // Assert that the transaction details were updated correctly
        assertEquals(itemCode, transaction.getItemCode());
        assertEquals(Double.valueOf(internalPriceStr), transaction.getInternalPrice());
        assertEquals(Double.valueOf(discountStr), transaction.getDiscount());
        assertEquals(Double.valueOf(salePriceStr), transaction.getSalePrice());
        assertEquals(Integer.valueOf(quantityStr), transaction.getQuantity());
        assertEquals((Double.parseDouble(salePriceStr) - Double.parseDouble(discountStr)) * Integer.parseInt(quantityStr), transaction.getLineTotal());
    }


    // Test case for invalid item code (contains special character)
    @Test
    void testValidateAndUpdate_invalidItemCode() {
        String itemCode = "ITEM#123"; // Invalid because of special character
        String internalPriceStr = "10.50";
        String discountStr = "15";
        String salePriceStr = "20.00";
        String quantityStr = "5";
        String checksumStr = "12345";

        // Validate and update the transaction
        List<String> errors = service.validateAndUpdate(transaction, itemCode, internalPriceStr, discountStr, salePriceStr, quantityStr, checksumStr);

        // Check that the validation error for item code appears
        assertTrue(errors.contains("Item code is invalid. Use only letters, numbers, or underscores."));
    }

    // Test case for invalid internal price (should be a non-negative number)
    @Test
    void testValidateAndUpdate_invalidInternalPrice() {
        String itemCode = "ITEM123";
        String internalPriceStr = "-10"; // Invalid negative price
        String discountStr = "15";
        String salePriceStr = "20.00";
        String quantityStr = "5";
        String checksumStr = "12345";

        // Validate and update the transaction
        List<String> errors = service.validateAndUpdate(transaction, itemCode, internalPriceStr, discountStr, salePriceStr, quantityStr, checksumStr);

        // Check that the validation error for internal price appears
        assertTrue(errors.contains("Internal Price must be a valid non-negative number."));
    }

    // Test case for invalid discount
    @Test
    void testValidateAndUpdate_invalidDiscount() {
        String itemCode = "ITEM123";
        String internalPriceStr = "10.50";
        String discountStr = "-10"; // Invalid negative discount
        String salePriceStr = "20.00";
        String quantityStr = "5";
        String checksumStr = "12345";

        // Validate and update the transaction
        List<String> errors = service.validateAndUpdate(transaction, itemCode, internalPriceStr, discountStr, salePriceStr, quantityStr, checksumStr);

        // Check that the validation error for discount appears
        assertTrue(errors.contains("Discount must be a valid non-negative number."));
    }

    // Test case for invalid sale price (should be a non-negative number)
    @Test
    void testValidateAndUpdate_invalidSalePrice() {
        String itemCode = "ITEM123";
        String internalPriceStr = "10.50";
        String discountStr = "15";
        String salePriceStr = "-20"; // Invalid negative sale price
        String quantityStr = "5";
        String checksumStr = "12345";

        // Validate and update the transaction
        List<String> errors = service.validateAndUpdate(transaction, itemCode, internalPriceStr, discountStr, salePriceStr, quantityStr, checksumStr);

        // Check that the validation error for sale price appears
        assertTrue(errors.contains("Sale Price must be a valid non-negative number."));
    }

    // Test case for invalid quantity (should be a positive integer)
    @Test
    void testValidateAndUpdate_invalidQuantity() {
        String itemCode = "ITEM123";
        String internalPriceStr = "10.50";
        String discountStr = "15";
        String salePriceStr = "20.00";
        String quantityStr = "-5"; // Invalid negative quantity
        String checksumStr = "12345";

        // Validate and update the transaction
        List<String> errors = service.validateAndUpdate(transaction, itemCode, internalPriceStr, discountStr, salePriceStr, quantityStr, checksumStr);

        // Check that the validation error for quantity appears
        assertTrue(errors.contains("Quantity must be a positive integer."));
    }

    // Test case for invalid checksum (should be a positive integer)
    @Test
    void testValidateAndUpdate_invalidChecksum() {
        String itemCode = "ITEM123";
        String internalPriceStr = "10.50";
        String discountStr = "15";
        String salePriceStr = "20.00";
        String quantityStr = "5";
        String checksumStr = "123456"; // Invalid negative checksum

        List<String> errors = service.validateAndUpdate(transaction, itemCode, internalPriceStr, discountStr, salePriceStr, quantityStr, checksumStr);

        // Check if checksum was updated correctly
        assertTrue(errors.isEmpty());
        assertEquals(Integer.valueOf(checksumStr), transaction.getChecksum());
    }

    // Test case to verify the checksum is updated correctly
    @Test
    void testValidateAndUpdate_updateChecksum() {
        String itemCode = "ITEM123";
        String internalPriceStr = "10.50";
        String discountStr = "16";
        String salePriceStr = "20.00";
        String quantityStr = "5";
        String checksumStr = "12345";

        // Set the initial checksum
        transaction.setChecksum(12345);

        List<String> errors = service.validateAndUpdate(transaction, itemCode, internalPriceStr, discountStr, salePriceStr, quantityStr, checksumStr);

        // Check if checksum was updated correctly
        assertTrue(errors.isEmpty());
        assertEquals(31, transaction.getChecksum());
    }
}
