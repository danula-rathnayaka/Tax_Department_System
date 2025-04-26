package edu.iit.gtds.tax_department_system.service;

import edu.iit.gtds.tax_department_system.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class EditTransactionService {

    public List<String> validateAndUpdate(Transaction transaction,
                                          String itemCode,
                                          String internalPriceStr,
                                          String discountStr,
                                          String salePriceStr,
                                          String quantityStr,
                                          String lineTotalStr,
                                          String checksumStr) {

        List<String> errors = new ArrayList<>();

        // Validate item code
        if (!itemCode.matches("^\\w+$")) {
            errors.add("Item code is invalid. Use only letters, numbers, or underscores.");
        }

        Double internalPrice = parsePositiveDouble(internalPriceStr);
        if (internalPrice == null) {
            errors.add("Internal Price must be a valid non-negative number.");
        }

        Double discount = parseDoubleInRange(discountStr, 0, 100);
        if (discount == null) {
            errors.add("Discount must be a number between 0 and 100.");
        }

        Double salePrice = parsePositiveDouble(salePriceStr);
        if (salePrice == null) {
            errors.add("Sale Price must be a valid non-negative number.");
        }

        Integer quantity = parsePositiveInt(quantityStr);
        if (quantity == null) {
            errors.add("Quantity must be a positive integer.");
        }

        Double lineTotal = parsePositiveDouble(lineTotalStr);
        if (lineTotal == null) {
            errors.add("Line Total must be a valid non-negative number.");
        }

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
            transaction.setLineTotal(lineTotal);
            transaction.setChecksum(checksum);
        }

        return errors;
    }

    private Double parsePositiveDouble(String value) {
        try {
            double num = Double.parseDouble(value);
            return num >= 0 ? num : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDoubleInRange(String value, double min, double max) {
        try {
            double num = Double.parseDouble(value);
            return (num >= min && num <= max) ? num : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parsePositiveInt(String value) {
        try {
            int num = Integer.parseInt(value);
            return num > 0 ? num : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
