package edu.iit.gtds.tax_department_system.controller;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.model.TransactionList;
import edu.iit.gtds.tax_department_system.service.FileService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FilePathFormController implements Initializable {

    // Instance of file chooser
    private final FileChooser fileChooser = new FileChooser();

    // Instance of service layer methods
    private final FileService service = new FileService();

    @FXML
    public TextField txtPathForFile;

    @FXML
    void chooseBtnOnAction(ActionEvent event) {
        // Allow only csv files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));

        // Show file chooser dialog
        File file = fileChooser.showOpenDialog(new Stage());

        // Set the file path when the file is picked
        txtPathForFile.setText(file.getPath());
    }

    @FXML
    void goBtnOnAction(ActionEvent event) {

        // Read the file in the provided path and extract data using the service method
        ObservableList<Transaction> data = service.readFile(new File(txtPathForFile.getText()));


        if (data.isEmpty()) {
            // If no data show invalid valid alert
            new Alert(Alert.AlertType.ERROR, "Invalid file. Please select a valid csv file.").show();
        } else {
            loadTranslationTable(data, txtPathForFile.getText(), event);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initialize the default path for file picker
        fileChooser.setInitialDirectory(new File("../"));
    }

    private void loadTranslationTable(ObservableList<Transaction> transactions, String path, ActionEvent event) {
        TransactionList.getInstance().setTransactions(transactions);
        TransactionList.getInstance().setPath(path);

        try {

            // Open the transaction table validator form
            Stage stage = new Stage();
            stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/edu/iit/gtds/tax_department_system/view/transaction_table.fxml")).load()));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid File.").show();
        }
    }
}
