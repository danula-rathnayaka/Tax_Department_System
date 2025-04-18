package edu.iit.gtds.tax_department_system.controller;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.service.FileService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
        // Show file chooser dialog
        File file = fileChooser.showOpenDialog(new Stage());

        // Set the file path when the file is picked
        txtPathForFile.setText(file.getPath());

        // Read and extract data using the service method
        ObservableList<Transaction> data = service.readFile(file);

        if (data == null) {
            // If no data show invalid valid alert
            new Alert(Alert.AlertType.ERROR, "Invalid File").show();
        } else {
            loadTranslationTable(data);
        }
    }

    @FXML
    void goBtnOnAction(ActionEvent event) {

        // Read the file in the provided path and extract data using the service method
        ObservableList<Transaction> data = service.readFile(new File(txtPathForFile.getText()));


        if (data == null) {
            // If no data show invalid valid alert
            new Alert(Alert.AlertType.ERROR, "Invalid File").show();
        } else {
            loadTranslationTable(data);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initialize the default path for file picker
        fileChooser.setInitialDirectory(new File("../data"));
    }

    private void loadTranslationTable(ObservableList<Transaction> transactions) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/iit/gtds/tax_department_system/view/transaction_table.fxml"));
            Parent root = null;
            root = loader.load();

            TransactionTableController controller = loader.getController();

            controller.setTransactions(transactions);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
