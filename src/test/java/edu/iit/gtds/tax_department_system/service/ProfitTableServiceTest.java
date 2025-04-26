package edu.iit.gtds.tax_department_system.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProfitTableServiceTest {

    ProfitTableService calculator = new ProfitTableService();

    @Test
    void testCalculateTaxRate_validInputs() {
        Double result = calculator.calculateTaxRate(10000.0, 15.0);
        assertEquals(1500.0, result);
    }

    @Test
    void testCalculateTaxRate_zeroTaxRate() {
        Double result = calculator.calculateTaxRate(5000.0, 0.0);
        assertEquals(0.0, result);
    }

    @Test
    void testCalculateTaxRate_zeroProfit() {
        Double result = calculator.calculateTaxRate(0.0, 20.0);
        assertEquals(0.0, result);
    }

    @Test
    void testCalculateTaxRate_negativeTaxRate_throwsRuntimeException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            calculator.calculateTaxRate(10000.0, -5.0);
        });

        Throwable cause = exception.getCause();
        assertNotNull(cause);
        assertEquals("Tax rate cannot be negative", cause.getMessage());
    }

}
