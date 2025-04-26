package edu.iit.gtds.tax_department_system.util;

import edu.iit.gtds.tax_department_system.model.Transaction;

public class ChecksumUtils {

    // Prevents the creation of an object from this utility class since all methods are static.
    private ChecksumUtils(){}

    public static int getChecksum(Transaction transaction) {
        String data = transaction.getBillId()
                + transaction.getItemCode()
                + transaction.getInternalPrice()
                + transaction.getDiscount()
                + transaction.getSalePrice()
                + transaction.getQuantity()
                + transaction.getLineTotal();

        int sum = 0;
        for (int i = 0; i < data.length(); i++) {
            sum += (i + 1) * data.charAt(i);
        }

        return (~sum + 1) & 0xFF;
    }

}
