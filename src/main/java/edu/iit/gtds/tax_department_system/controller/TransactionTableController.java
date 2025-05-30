package edu.iit.gtds.tax_department_system.controller;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.model.TransactionList;
import edu.iit.gtds.tax_department_system.service.TransactionTableService;
import edu.iit.gtds.tax_department_system.util.FileHandleUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionTableController implements Initializable {

    // Transaction service instance
    private final TransactionTableService service = new TransactionTableService();
    // List of transactions
    private final ObservableList<Transaction> transactions = TransactionList.getInstance().getTransactions();
    @FXML
    private TableColumn<Transaction, Integer> colLineNo;
    @FXML
    private TableColumn<Transaction, String> colBillId;
    @FXML
    private TableColumn<Transaction, Integer> colChecksum;
    @FXML
    private TableColumn<Transaction, Double> colDiscount;
    @FXML
    private TableColumn<Transaction, Double> colInternalPrice;
    @FXML
    private TableColumn<Transaction, String> colItemCode;
    @FXML
    private TableColumn<Transaction, Double> colLineTotal;
    @FXML
    private TableColumn<Transaction, Integer> colQuantity;
    @FXML
    private TableColumn<Transaction, Double> colSalesPrice;
    @FXML
    private TableColumn<Transaction, String> colValidity;
    @FXML
    private Label lblLineSelected;
    @FXML
    private Label lblInvalidRecords;
    @FXML
    private Label lblTotalRecords;
    @FXML
    private Label lblValidRecords;
    @FXML
    private TableView<Transaction> tblTransactions;
    @FXML
    private TextArea txtErrors;
    // Selected transaction
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
        colValidity.setCellValueFactory(new PropertyValueFactory<>("isValid"));

        // Set the data to the table view
        tblTransactions.setItems(transactions);

        // Listener for row selection in the table
        tblTransactions.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
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

        // Show the validation summary on UI
        updateValidationStatus();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        // Allow deletion only if a transaction is selected and is marked "Invalid"
        if (selectedTransaction == null || selectedTransaction.getIsValid().equals("Valid")) {
            new Alert(Alert.AlertType.ERROR, "Please select an invalid row to delete.").show();
            return;
        }

        // Confirmation alert
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this transaction?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);

        // If confirmed, remove transaction and refresh UI
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                transactions.remove(selectedTransaction);
                selectedTransaction = null;
                tblTransactions.getSelectionModel().clearSelection();
                lblLineSelected.setText("Null");
                tblTransactions.refresh();
                updateValidationStatus();
            }
        });
    }


    @FXML
    void btnEditOnAction(ActionEvent event) {
        // Only allow editing if selected transaction is invalid
        if (selectedTransaction == null || selectedTransaction.getIsValid().equals("Valid")) {
            new Alert(Alert.AlertType.ERROR, "Please select a invalid row to edit.").show();
            return;
        }
        try {
            // Load the edit form FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/iit/gtds/tax_department_system/view/edit_transaction_form.fxml"));
            Parent root = loader.load();

            // Pass selected transaction to the controller
            EditTransactionFormController controller = loader.getController();
            controller.setTransactionData(selectedTransaction);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Refresh table when the edit window is closed
            stage.setOnHidden(e -> {
                selectedTransaction = null;
                tblTransactions.getSelectionModel().clearSelection();
                lblLineSelected.setText("Null");
                tblTransactions.refresh();
                updateValidationStatus();
            });
            stage.show();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid File").show();
        }
    }

    @FXML
    void btnCalculateProfitOnAction(ActionEvent event) {
        // Profit should only be calculated if there are no invalid transactions
        if (Integer.parseInt(lblInvalidRecords.getText()) != 0) {
            new Alert(Alert.AlertType.ERROR, "Please fix all errors before calculating the profit.").show();
            return;
        }
        try {
            // Load and open the profit table window
            Stage stage = new Stage();
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/edu/iit/gtds/tax_department_system/view/profit_table.fxml")).load()));
            stage.show();

            // Close current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid File").show();
        }
    }

    // Recalculate and update validation status and error messages
    private void updateValidationStatus() {
        List<String[]> errorList = service.validateTransactions(transactions);

        // Update record summary counts
        lblValidRecords.setText((transactions.size() - errorList.size()) + "");
        lblInvalidRecords.setText(errorList.size() + "");
        lblTotalRecords.setText(transactions.size() + "");

        // Show formatted error messages in the text area
        if (errorList.isEmpty()) {
            txtErrors.setText("No errors were found");
            return;
        }

        StringBuilder errorText = new StringBuilder();
        for (String[] error : errorList) {
            errorText.append("• Line No. ").append(error[0])
                    .append(" → ").append(error[1]).append("\n");
        }
        txtErrors.setText(errorText.toString());
    }

    @FXML
    void btnUpdateFileOnAction(ActionEvent event) {
        // Persist the current list of transactions to file
        FileHandleUtils.updateFile();
        new Alert(Alert.AlertType.INFORMATION, "File updated successfully.").show();
    }
}
