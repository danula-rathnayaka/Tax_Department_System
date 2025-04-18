package edu.iit.gtds.tax_department_system.service;

import edu.iit.gtds.tax_department_system.model.Transaction;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class TransactionTableService {

    public ArrayList<String[]> validateTransactions(ObservableList<Transaction> transactions) {
        ArrayList<String[]> errorTransactionList = new ArrayList<>();

        for (Transaction transaction : transactions) {
            String billId = transaction.getBillId();
            String itemCode = transaction.getItemCode();

            if (!isValidItemCode(itemCode)) {
                errorTransactionList.add(new String[]{billId, itemCode, "itemCode"});
            } else if (transaction.getInternalPrice() < 0) {
                errorTransactionList.add(new String[]{billId, itemCode, "internalPrice"});
            } else if (transaction.getDiscount() < 0) {
                errorTransactionList.add(new String[]{billId, itemCode, "discount"});
            } else if (transaction.getSalePrice() < 0) {
                errorTransactionList.add(new String[]{billId, itemCode, "salePrice"});
            } else if (transaction.getQuantity() < 0) {
                errorTransactionList.add(new String[]{billId, itemCode, "quantity"});
            } else if (transaction.getLineTotal() < 0) {
                errorTransactionList.add(new String[]{billId, itemCode, "lineTotal"});
            } else if (!isChecksumValid(transaction)) {
                errorTransactionList.add(new String[]{billId, itemCode, "checksum"});
            }
        }

        return errorTransactionList;
    }

    private boolean isValidItemCode(String itemCode) {
        return itemCode != null && Pattern.matches("^\\w+$", itemCode);
    }

    private boolean isChecksumValid(Transaction t) {
        String data = t.getBillId()
                + t.getItemCode()
                + t.getInternalPrice()
                + t.getDiscount()
                + t.getSalePrice()
                + t.getQuantity()
                + t.getLineTotal();

        return generateChecksum(data) == t.getChecksum();
    }

    private int generateChecksum(String data) {
        int sum = 0;
        for (int i = 0; i < data.length(); i++) {
            sum += (i + 1) * data.charAt(i);
        }
        return (~sum + 1) & 0xFF;
    }
}
