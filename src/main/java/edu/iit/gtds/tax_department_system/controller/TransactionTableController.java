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
        colValidity.setCellValueFactory(new PropertyValueFactory<>("isValid"));

        tblTransactions.setItems(transactions);

        tblTransactions.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lblLineSelected.setText(newSelection.getLineNo().toString());
                selectedTransaction = newSelection;
            } else {
                lblLineSelected.setText("Null");
                selectedTransaction = null;
            }
        });

        updateValidationStatus();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
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
        if (selectedTransaction == null || selectedTransaction.getIsValid().equals("Valid")) {
            new Alert(Alert.AlertType.ERROR, "Please select a invalid row to edit.").show();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/iit/gtds/tax_department_system/view/edit_transaction_form.fxml"));
            Parent root = loader.load();

            EditTransactionFormController controller = loader.getController();
            controller.setTransactionData(selectedTransaction);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> {
                tblTransactions.refresh();
                updateValidationStatus();
            });
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnCalculateProfitOnAction(ActionEvent event) {
        if (Integer.parseInt(lblInvalidRecords.getText()) != 0) {
            new Alert(Alert.AlertType.ERROR, "Please fix all errors before calculating the profit..").show();
            return;
        }
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

    private void updateValidationStatus() {
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
            errorText.append("• Line No. ").append(error[0])
                    .append(" → ").append(error[1]).append("\n");
        }
        txtErrors.setText(errorText.toString());

    }
}
