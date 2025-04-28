package edu.iit.gtds.tax_department_system.service;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.util.ChecksumUtils;

import java.util.ArrayList;
import java.util.List;

public class EditTransactionService {


    // Validates and updates the transaction with new data
    public List<String> validateAndUpdate(Transaction transaction,
                                          String itemCode,
                                          String internalPriceStr,
                                          String discountStr,
                                          String salePriceStr,
                                          String quantityStr,
                                          String checksumStr) {

        List<String> errors = new ArrayList<>();  // List to collect error messages

        // Validate the item code (must be alphanumeric or underscores)
        if (!itemCode.matches("^\\w+$")) {
            errors.add("Item code is invalid. Use only letters, numbers, or underscores.");
        }

        // Parse and validate the internal price (must be a positive number)
        Double internalPrice = parsePositiveDouble(internalPriceStr);
        if (internalPrice == null) {
            errors.add("Internal Price must be a valid non-negative number.");
        }

        // Parse and validate the discount (must be between a positive double)
        Double discount = parsePositiveDouble(discountStr);
        if (discount == null) {
            errors.add("Discount must be a valid non-negative number.");
        }

        // Parse and validate the sale price (must be a positive number)
        Double salePrice = parsePositiveDouble(salePriceStr);
        if (salePrice == null) {
            errors.add("Sale Price must be a valid non-negative number.");
        }

        // Parse and validate the quantity (must be a positive integer)
        Integer quantity = parsePositiveInt(quantityStr);
        if (quantity == null) {
            errors.add("Quantity must be a positive integer.");
        }

        // Parse and validate the checksum (must be a positive integer)
        Integer checksum = parsePositiveInt(checksumStr);
        if (checksum == null) {
            errors.add("Checksum must be a positive integer.");
        }

        // If no errors, update transaction
        if (errors.isEmpty()) {
            transaction.setItemCode(itemCode);
            transaction.setInternalPrice(internalPrice);
            transaction.setDiscount(discount);
            transaction.setSalePrice(salePrice);
            transaction.setQuantity(quantity);
            transaction.setLineTotal((salePrice - discount) * quantity);

            // Update checksum only if it is different
            transaction.setChecksum(!transaction.getChecksum().equals(checksum) ? checksum : ChecksumUtils.getChecksum(transaction));
        }

        return errors;  // Return any validation errors
    }

    private Double parsePositiveDouble(String value) {
        try {
            double num = Double.parseDouble(value);
            return num >= 0 ? num : null;  // Return null if negative
        } catch (NumberFormatException e) {
            return null;  // Return null if not a valid number
        }
    }

    private Integer parsePositiveInt(String value) {
        try {
            int num = Integer.parseInt(value);
            return num > 0 ? num : null;  // Return null if negative or zero
        } catch (NumberFormatException e) {
            return null;  // Return null if not a valid number
        }
    }
}
