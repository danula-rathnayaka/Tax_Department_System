package edu.iit.gtds.tax_department_system.util;

import edu.iit.gtds.tax_department_system.model.Transaction;

public class ChecksumUtils {

    // Prevents the creation of an object from this utility class since all methods are static.
    private ChecksumUtils() {
    }

    public static int getChecksum(Transaction transaction) {

        // Concatenate transaction fields into a single string for checksum calculation
        String data = transaction.getBillId()
                + transaction.getItemCode()
                + transaction.getInternalPrice()
                + transaction.getDiscount()
                + transaction.getSalePrice()
                + transaction.getQuantity()
                + transaction.getLineTotal();

        // Variables to track different character types in the data
        int upperCase = 0;
        int lowerCase = 0;
        int numberAndDots = 0;

        // Loop through each character in the concatenated data string
        for (char c : data.toCharArray()) {
            if (Character.isUpperCase(c)) {
                // If the character is an uppercase letter
                upperCase++;
            } else if (Character.isLowerCase(c)) {
                // If the character is a lowercase letter
                lowerCase++;
            } else if (Character.isDigit(c) || c == '.') {
                // If the character is a digit or a dot
                numberAndDots++;
            }
        }

        // Return the sum of counts for uppercase, lowercase, digits, and dots as the checksum
        return upperCase + lowerCase + numberAndDots;
    }
}
