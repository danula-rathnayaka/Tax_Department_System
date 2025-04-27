package edu.iit.gtds.tax_department_system.util;

import edu.iit.gtds.tax_department_system.model.Transaction;

public class ChecksumUtils {

    // Prevents the creation of an object from this utility class since all methods are static.
    private ChecksumUtils() {
    }

    public static int getChecksum(Transaction transaction) {
        String data = transaction.getBillId()
                + transaction.getItemCode()
                + transaction.getInternalPrice()
                + transaction.getDiscount()
                + transaction.getSalePrice()
                + transaction.getQuantity()
                + transaction.getLineTotal();

        int upperCase = 0;
        int lowerCase = 0;
        int numberAndDots = 0;

        for (char c : data.toCharArray()) {
            if (Character.isUpperCase(c)) {
                upperCase++;
            } else if (Character.isLowerCase(c)) {
                lowerCase++;
            } else if (Character.isDigit(c) || c == '.') {
                numberAndDots++;
            }
        }

        return upperCase + lowerCase + numberAndDots;
    }
}
