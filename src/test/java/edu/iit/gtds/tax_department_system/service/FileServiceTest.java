package edu.iit.gtds.tax_department_system.service;

import edu.iit.gtds.tax_department_system.model.Transaction;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileServiceTest {

    // Get the instance of the file service
    private final FileService fileService = new FileService();

    @Test
    void testReadFile_validCSV_returnsTransactions() throws Exception {

        // Create a temporary csv file writing dummy data
        File tempFile = File.createTempFile("test_transactions", ".csv");
        FileWriter writer = new FileWriter(tempFile);
        writer.write("1000,Lemon_01,20,10,18,2,36,98\n");
        writer.write("1001,Cake124,15.5,5,14.5,3,43.5,201\n");
        writer.close();

        // Read the file using the service method
        List<Transaction> transactions = fileService.readFile(tempFile);

        // Output should not be null and two transactions must be present
        assertNotNull(transactions);
        assertEquals(2, transactions.size());

        // Assert first line extracted data with expected outputs
        Transaction t1 = transactions.getFirst();
        assertEquals("1000", t1.getBillId());
        assertEquals("Lemon_01", t1.getItemCode());
        assertEquals(20.0, t1.getInternalPrice());
        assertEquals(10.0, t1.getDiscount());
        assertEquals(18.0, t1.getSalePrice());
        assertEquals(2, t1.getQuantity());
        assertEquals(36.0, t1.getLineTotal());
        assertEquals(98, t1.getChecksum());

        // Assert second line extracted data with expected outputs
        Transaction t2 = transactions.get(1);
        assertEquals("1001", t2.getBillId());
        assertEquals("Cake124", t2.getItemCode());
        assertEquals(15.5, t2.getInternalPrice());
        assertEquals(5.0, t2.getDiscount());
        assertEquals(14.5, t2.getSalePrice());
        assertEquals(3, t2.getQuantity());
        assertEquals(43.5, t2.getLineTotal());
        assertEquals(201, t2.getChecksum());

        // Clean the temporary file
        tempFile.delete();
    }

    @Test
    void testReadFile_fileNotFound_returnsNull() {

        // Create a none existing file
        File nonExistentFile = new File("non_existent.csv");

        // Read the file using the service method
        List<Transaction> result = fileService.readFile(nonExistentFile);

        // Assert if the result is none
        assertEquals(new ArrayList<>(), result);
    }
}
