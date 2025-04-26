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
    private TableColumn<?, ?> colLineNo;
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
    private Transaction selectedTransaction;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        tblProfit.setItems(transactions);

        setValuesToLabels();

        tblProfit.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lblLineSelected.setText(newSelection.getLineNo().toString());
                selectedTransaction = newSelection;
            } else {
                lblLineSelected.setText("Null");
                selectedTransaction = null;
            }
        });

    }

    private void setValuesToLabels() {
        double totalProfit = 0;
        double totalLoss = 0;

        for (Transaction transaction : transactions) {
            double profit = (transaction.getSalePrice() * transaction.getQuantity() - transaction.getDiscount()) - (transaction.getInternalPrice() * transaction.getQuantity());
            transaction.setProfit(profit);

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

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (selectedTransaction == null || (selectedTransaction.getProfit() != 0.0)) {
            new Alert(Alert.AlertType.ERROR, "Please select a profit 0 row to delete.").show();
            return;
        }

        // Confirmation before deleting
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this transaction?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                transactions.remove(selectedTransaction);
                tblProfit.getSelectionModel().clearSelection();
                selectedTransaction = null;
                lblLineSelected.setText("Null");
                tblProfit.refresh();
                setValuesToLabels();
            }
        });
    }

    @FXML
    void btnUpdateFileOnAction(ActionEvent event) {
        service.updateFile();
        new Alert(Alert.AlertType.INFORMATION, "File updated successfully.").show();
    }
}
