package edu.iit.gtds.tax_department_system.service;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.model.TransactionList;

import java.util.HashMap;
import java.util.Map;

public class ProfitTableService {

    public Double calculateTaxRate(Double finalProfit, Double taxRate) {
        // If the tax rate is negative, return null as it's invalid
        if (taxRate < 0) {
            return null;
        }

        // Calculate the final tax (final profit * tax rate / 100)
        double finalTax = (finalProfit) * (taxRate / 100);

        // If the final tax is negative, set it to zero (no negative tax)
        finalTax = finalTax < 0 ? 0 : finalTax;

        // Return the calculated tax
        return finalTax;
    }

    public Map<String, Double> calculateProfitLoss() {
        // Initialize variables to keep track of total profit and total loss
        double totalProfit = 0;
        double totalLoss = 0;

        // Loop through all transactions and calculate profit or loss for each
        for (Transaction transaction : TransactionList.getInstance().getTransactions()) {

            // Calculate the profit for the transaction
            double profit = ((transaction.getSalePrice() - transaction.getDiscount()) * transaction.getQuantity()) - (transaction.getInternalPrice() * transaction.getQuantity());
            transaction.setProfit(profit);

            // If profit is positive or zero, add to totalProfit
            if (profit >= 0) {
                totalProfit += profit;
            } else {
                // If profit is negative (loss), add the absolute value to totalLoss
                totalLoss += Math.abs(profit);
            }
        }

        // Create a map to store the results
        Map<String, Double> result = new HashMap<>();
        result.put("TotalProfit", totalProfit);  // Store the total profit
        result.put("TotalLoss", totalLoss);  // Store the total loss

        // Return the map containing profit and loss
        return result;
    }
}
