package edu.iit.gtds.tax_department_system.controller;

import edu.iit.gtds.tax_department_system.model.Transaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    @FXML
    void btnCancelOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    public void setTransactionData(Transaction transaction) {
        this.transaction = transaction;
        lblBillNo.setText(transaction.getBillId());
        txtItemCode.setText(transaction.getItemCode());
        txtInternalPrice.setText(transaction.getItemCode());
        txtDiscount.setText(transaction.getDiscount().toString());
        txtSalesPrice.setText(transaction.getSalePrice().toString());
        txtQuantity.setText(transaction.getQuantity().toString());
        txtLineTotal.setText(transaction.getLineTotal().toString());
        txtChecksum.setText(transaction.getChecksum().toString());
    }

}
