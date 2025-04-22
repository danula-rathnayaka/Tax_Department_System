package edu.iit.gtds.tax_department_system.controller;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.model.TransactionList;
import edu.iit.gtds.tax_department_system.service.TransactionTableService;
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

    private final TransactionTableService service = new TransactionTableService();
    private final ObservableList<Transaction> transactions = TransactionList.getInstance().getTransactions();
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
    @FXML
    private TextArea txtErrors;

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

        tblTransactions.setItems(transactions);

        List<String[]> errorList = service.validateTransactions(transactions);

        lblValidRecords.setText((transactions.size() - errorList.size()) + "");
        lblInvalidRecords.setText(errorList.size() + "");
        lblTotalRecords.setText(transactions.size() + "");

        if (errorList.isEmpty()) {
            txtErrors.setText("No errors were found");
            return;
        }

        StringBuilder errorText = new StringBuilder();
        for (String[] error : errorList) {
            String billId = error[0];
            String itemCode = error[1];
            String field = error[2];

            String message = switch (field) {
                case "itemCode" -> "Invalid Item Code";
                case "internalPrice" -> "Negative Internal Price";
                case "discount" -> "Negative Discount";
                case "salePrice" -> "Negative Sale Price";
                case "quantity" -> "Negative Quantity";
                case "lineTotal" -> "Negative Line Total";
                case "checksum" -> "Invalid Checksum";
                default -> "Unknown Error";
            };

            errorText.append("• Bill ID: ").append(billId)
                    .append(", Item Code: ").append(itemCode)
                    .append(" → ").append(message).append("\n");
        }
        txtErrors.setText(errorText.toString());

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

        // Apply cell-level highlighting using a reusable method
        applyCellStyle(colItemCode, "itemCode", errorList);
        applyCellStyle(colInternalPrice, "internalPrice", errorList);
        applyCellStyle(colDiscount, "discount", errorList);
        applyCellStyle(colSalesPrice, "salePrice", errorList);
        applyCellStyle(colQuantity, "quantity", errorList);
        applyCellStyle(colLineTotal, "lineTotal", errorList);
        applyCellStyle(colChecksum, "checksum", errorList);

        colEdit.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/iit/gtds/tax_department_system/view/edit_transaction_form.fxml"));
                        Parent root = null;
                        root = loader.load();

                        EditTransactionFormController controller = loader.getController();

                        controller.setTransactionData(transaction);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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

                // Check if the current row has any error
                boolean hasError = errorList.stream().anyMatch(e ->
                        e[0].equals(row.getBillId()) && e[1].equals(row.getItemCode())
                );

                setGraphic(hasError ? editButton : null);
            }
        });
    }


    private <T> void applyCellStyle(TableColumn<Transaction, T> column, String fieldName, List<String[]> errors) {
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

    @FXML
    void btnCalculateProfitOnAction(ActionEvent event) {
        try {
            // Open the profit table stage
            Stage stage = new Stage();
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/edu/iit/gtds/tax_department_system/view/profit_table.fxml")).load()));
            stage.show();

            // Close current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
