package edu.iit.gtds.tax_department_system.controller;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.service.EditTransactionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class EditTransactionFormController {

    // Service instance for the edit transaction
    private final EditTransactionService service = new EditTransactionService();
    @FXML
    private Label lblBillNo;
    @FXML
    private TextField txtChecksum;
    @FXML
    private TextField txtDiscount;
    @FXML
    private TextField txtInternalPrice;
    @FXML
    private TextField txtItemCode;
    @FXML
    private Label lblLineTotal;
    @FXML
    private TextField txtQuantity;
    @FXML
    private TextField txtSalesPrice;
    // Transaction to be edited
    private Transaction transaction;

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        // Close the edit window
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        // Validate and update the transaction with the user provided data
        List<String> errors = service.validateAndUpdate(
                transaction,
                txtItemCode.getText(),
                txtInternalPrice.getText(),
                txtDiscount.getText(),
                txtSalesPrice.getText(),
                txtQuantity.getText(),
                txtChecksum.getText()
        );

        // If there are validation errors, display them in an alert
        if (!errors.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, String.join("\n", errors));
            alert.setTitle("Validation Errors");
            alert.setHeaderText("Please correct the following:");
            alert.showAndWait();
            return;
        }

        // Close the edit window after successful update if no errors
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    public void setTransactionData(Transaction transaction) {
        this.transaction = transaction;

        // Set the form fields with the transaction's existing data
        lblBillNo.setText(transaction.getBillId());
        txtItemCode.setText(transaction.getItemCode());
        txtInternalPrice.setText(transaction.getInternalPrice().toString());
        txtDiscount.setText(transaction.getDiscount().toString());
        txtSalesPrice.setText(transaction.getSalePrice().toString());
        txtQuantity.setText(transaction.getQuantity().toString());
        lblLineTotal.setText(transaction.getLineTotal().toString());
        txtChecksum.setText(transaction.getChecksum().toString());
    }

}
