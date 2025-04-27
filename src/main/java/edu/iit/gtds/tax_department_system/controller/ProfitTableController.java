package edu.iit.gtds.tax_department_system.controller;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.model.TransactionList;
import edu.iit.gtds.tax_department_system.service.ProfitTableService;
import edu.iit.gtds.tax_department_system.util.FileHandleUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ProfitTableController implements Initializable {

    // Observable list to hold transactions
    private final ObservableList<Transaction> transactions = TransactionList.getInstance().getTransactions();

    // Instance of the profit table service
    private final ProfitTableService service = new ProfitTableService();
    @FXML
    private TableColumn<Transaction, String> colBillId;
    @FXML
    private TableColumn<Transaction, Integer> colChecksum;
    @FXML
    private TableColumn<Transaction, Double> colDiscount;
    @FXML
    private TableColumn<Transaction, Double> colProfit;
    @FXML
    private TableColumn<Transaction, Double> colInternalPrice;
    @FXML
    private TableColumn<Transaction, String> colItemCode;
    @FXML
    private TableColumn<Transaction, Integer> colLineNo;
    @FXML
    private TableColumn<Transaction, Double> colLineTotal;
    @FXML
    private TableColumn<Transaction, Integer> colQuantity;
    @FXML
    private TableColumn<Transaction, Double> colSalesPrice;
    @FXML
    private TableView<Transaction> tblProfit;
    @FXML
    private Label lblFinalProfit;
    @FXML
    private Label lblLineSelected;
    @FXML
    private Label lblFinalTax;
    @FXML
    private Label lblTotalLoss;
    @FXML
    private Label lblTotalProfit;
    @FXML
    private TextField txtTaxRate;

    // Currently selected transaction
    private Transaction selectedTransaction;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Bind table columns to Transaction fields
        colLineNo.setCellValueFactory(new PropertyValueFactory<>("lineNo"));
        colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colInternalPrice.setCellValueFactory(new PropertyValueFactory<>("internalPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colSalesPrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colLineTotal.setCellValueFactory(new PropertyValueFactory<>("lineTotal"));
        colChecksum.setCellValueFactory(new PropertyValueFactory<>("checksum"));
        colProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));

        // Populate the table with transactions
        tblProfit.setItems(transactions);

        // Initialize the labels with profit and loss data
        setValuesToLabels();

        // Add listener for selecting a row in the table
        tblProfit.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Update the label with the selected line number
                lblLineSelected.setText(newSelection.getLineNo().toString());
                selectedTransaction = newSelection;
            } else {
                // If no selection, clear the label and reset selected transaction
                lblLineSelected.setText("Null");
                selectedTransaction = null;
            }
        });

    }

    private void setValuesToLabels() {
        // Fetch profit and loss values from the service
        Map<String, Double> profitsLosses = service.calculateProfitLoss();

        // Update the labels with the calculated profit and loss
        lblTotalProfit.setText(String.format("%.2f", profitsLosses.get("TotalProfit")));
        lblTotalLoss.setText(String.format("%.2f", profitsLosses.get("TotalLoss")));
        lblFinalProfit.setText(String.format("%.2f", profitsLosses.get("TotalProfit") - profitsLosses.get("TotalLoss")));
    }


    @FXML
    void btnCalculateOnAction(ActionEvent event) {

        Double taxRate = service.calculateTaxRate(Double.parseDouble(lblFinalProfit.getText()), Double.parseDouble(txtTaxRate.getText()));

        if (taxRate == null){
            new Alert(Alert.AlertType.ERROR, "Tax rate can not be negative").show();
            return;
        }

        lblFinalTax.setText(String.format("%.2f", taxRate));
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        // Ensure a valid transaction (with profit 0) is selected for deletion
        if (selectedTransaction == null || (selectedTransaction.getProfit() != 0.0)) {
            // Show an error message if the selected row is invalid
            new Alert(Alert.AlertType.ERROR, "Please select a profit 0 row to delete.").show();
            return;
        }

        // Confirmation prompt before deleting the transaction
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this transaction?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);

        // If the user confirms, remove the transaction from the list
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                transactions.remove(selectedTransaction);
                tblProfit.getSelectionModel().clearSelection();  // Clear the selection
                selectedTransaction = null;  // Reset the selected transaction
                lblLineSelected.setText("Null");  // Clear the label showing the line number
                tblProfit.refresh();  // Refresh the table view
                setValuesToLabels();  // Update the labels
            }
        });
    }

    @FXML
    void btnUpdateFileOnAction(ActionEvent event) {
        // Call the service to update the file
        FileHandleUtils.updateFile();

        // Show an information message after updating
        new Alert(Alert.AlertType.INFORMATION, "File updated successfully.").show();
    }
}
