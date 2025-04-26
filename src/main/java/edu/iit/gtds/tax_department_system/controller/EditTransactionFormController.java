package edu.iit.gtds.tax_department_system.controller;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.service.EditTransactionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class EditTransactionFormController {

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
    private TextField txtLineTotal;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtSalesPrice;

    private Transaction transaction;

    private final EditTransactionService service = new EditTransactionService();

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        // Close the edit window
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        List<String> errors = service.validateAndUpdate(
                transaction,
                txtItemCode.getText(),
                txtInternalPrice.getText(),
                txtDiscount.getText(),
                txtSalesPrice.getText(),
                txtQuantity.getText(),
                txtLineTotal.getText(),
                txtChecksum.getText()
        );

        if (!errors.isEmpty()) {
            showAlert(String.join("\n", errors));
            return;
        }

        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    private void showAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Validation Errors");
        alert.setHeaderText("Please correct the following:");
        alert.setContentText(message);
        alert.showAndWait();
    }



    public void setTransactionData(Transaction transaction) {
        this.transaction = transaction;
        lblBillNo.setText(transaction.getBillId());
        txtItemCode.setText(transaction.getItemCode());
        txtInternalPrice.setText(transaction.getInternalPrice().toString());
        txtDiscount.setText(transaction.getDiscount().toString());
        txtSalesPrice.setText(transaction.getSalePrice().toString());
        txtQuantity.setText(transaction.getQuantity().toString());
        txtLineTotal.setText(transaction.getLineTotal().toString());
        txtChecksum.setText(transaction.getChecksum().toString());
    }

}
