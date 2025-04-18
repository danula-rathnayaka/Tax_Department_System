package edu.iit.gtds.tax_department_system.controller;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.service.TransactionTableService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TransactionTableController implements Initializable {

    private final TransactionTableService service = new TransactionTableService();
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
    private TableColumn<Transaction, Void> colEdit;
    @FXML
    private Label lblInvalidRecords;
    @FXML
    private Label lblTotalRecords;
    @FXML
    private Label lblValidRecords;
    @FXML
    private TableView<Transaction> tblTransactions;
    private ObservableList<Transaction> transactions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colInternalPrice.setCellValueFactory(new PropertyValueFactory<>("internalPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colSalesPrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colLineTotal.setCellValueFactory(new PropertyValueFactory<>("lineTotal"));
        colChecksum.setCellValueFactory(new PropertyValueFactory<>("checksum"));
    }

    public void setTransactions(ObservableList<Transaction> transactions) {
        this.transactions = transactions;
        tblTransactions.setItems(transactions);

        var errorList = service.validateTransactions(transactions);

        tblTransactions.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Transaction item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                    return;
                }

                boolean hasError = errorList.stream().anyMatch(e ->
                        e[0].equals(item.getBillId()) && e[1].equals(item.getItemCode())
                );
                setStyle(hasError ? "-fx-background-color: #ffcccc;" : "");
            }
        });

        applyCellStyle(colItemCode, "itemCode", errorList);
        applyCellStyle(colInternalPrice, "internalPrice", errorList);
        applyCellStyle(colDiscount, "discount", errorList);
        applyCellStyle(colSalesPrice, "salePrice", errorList);
        applyCellStyle(colQuantity, "quantity", errorList);
        applyCellStyle(colLineTotal, "lineTotal", errorList);
        applyCellStyle(colChecksum, "checksum", errorList);

        lblValidRecords.setText((transactions.size() - errorList.size()) + "");
        lblInvalidRecords.setText(errorList.size() + "");
        lblTotalRecords.setText(transactions.size() + "");
    }


    private <T> void applyCellStyle(TableColumn<Transaction, T> column, String fieldName, ArrayList<String[]> errors) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(T value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(value.toString());
                Transaction row = getTableView().getItems().get(getIndex());

                boolean match = errors.stream().anyMatch(e ->
                        e[0].equals(row.getBillId()) &&
                                e[1].equals(row.getItemCode()) &&
                                e[2].equals(fieldName)
                );

                setStyle(match ? "-fx-background-color: #8B0000; -fx-text-fill: white;" : "");
            }
        });
    }

}
