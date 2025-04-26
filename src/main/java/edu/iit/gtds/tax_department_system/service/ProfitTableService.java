package edu.iit.gtds.tax_department_system.service;

public class ProfitTableService {

    public Double calculateTaxRate(Double finalProfit, Double taxRate) {
        if (taxRate < 0) {
            try {
                throw new InvalidTaxRateException("Tax rate cannot be negative");
            } catch (InvalidTaxRateException e) {
                throw new RuntimeException(e);
            }
        }
        double finalTax = (finalProfit) * (taxRate / 100);
        finalTax = finalTax < 0 ? 0 : finalTax;
        return finalTax;
    }

    private static class InvalidTaxRateException extends Exception {
        public InvalidTaxRateException(String message) {
            super(message);
        }
    }
}
