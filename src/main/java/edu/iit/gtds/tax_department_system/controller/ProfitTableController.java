package edu.iit.gtds.tax_department_system.controller;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.model.TransactionList;
import edu.iit.gtds.tax_department_system.service.ProfitTableService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfitTableController implements Initializable {

    private final ObservableList<Transaction> transactions = TransactionList.getInstance().getTransactions();
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
    private TableColumn<Transaction, Double> colLineTotal;
    @FXML
    private TableColumn<Transaction, Integer> colQuantity;
    @FXML
    private TableColumn<Transaction, Double> colSalesPrice;
    @FXML
    private TableColumn<Transaction, Void> colAction;
    @FXML
    private TableView<Transaction> tblProfit;
    @FXML
    private Label lblFinalProfit;
    @FXML
    private Label lblFinalTax;
    @FXML
    private Label lblTotalLoss;
    @FXML
    private Label lblTotalProfit;
    @FXML
    private TextField txtTaxRate;

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
        colProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));

        tblProfit.setItems(transactions);

        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    int index = getIndex();
                    Transaction transaction = getTableView().getItems().get(index);
                    // Confirm before delete (optional)
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this transaction?", ButtonType.YES, ButtonType.NO);
                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            getTableView().getItems().remove(index);
                            // Delete Logic
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                    return;
                }

                Transaction row = getTableView().getItems().get(getIndex());

                deleteButton.setVisible(row.getProfit().equals(0.0));
                setGraphic(deleteButton);
            }
        });

        setValuesToLabels();
    }

    private void setValuesToLabels() {
        double totalProfit = 0;
        double totalLoss = 0;

        for (Transaction t : transactions) {
            double profit = (t.getInternalPrice() * t.getQuantity()) -
                    (t.getSalePrice() * t.getQuantity() - t.getDiscount());
            t.setProfit(profit);

            if (profit >= 0) {
                totalProfit += profit;
            } else {
                totalLoss += Math.abs(profit);
            }
        }


        lblTotalProfit.setText(String.format("%.2f", totalProfit));
        lblTotalLoss.setText(String.format("%.2f", totalLoss));
        lblFinalProfit.setText(String.format("%.2f", totalProfit - totalLoss));
    }


    @FXML
    void btnCalculateOnAction(ActionEvent event) {
        lblFinalTax.setText(String.format("%.2f", service.calculateTaxRate(Double.parseDouble(lblFinalProfit.getText()), Double.parseDouble(txtTaxRate.getText()))));
    }

    private static class InvalidTaxRateException extends Exception {
        public InvalidTaxRateException(String message) {
            super(message);
        }
    }

}
