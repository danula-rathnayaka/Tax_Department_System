package edu.iit.gtds.tax_department_system.controller;

import edu.iit.gtds.tax_department_system.model.Transaction;
import edu.iit.gtds.tax_department_system.service.FileService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
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
        List<Transaction> data = service.readFile(file);

        if (data == null) {
            // If no data show invalid valid alert
            new Alert(Alert.AlertType.ERROR, "Invalid File").show();
        } else {
            data.forEach(System.out::println);
        }
    }

    @FXML
    void goBtnOnAction(ActionEvent event) {

        // Read the file in the provided path and extract data using the service method
        List<Transaction> data = service.readFile(new File(txtPathForFile.getText()));

        if (data == null) {
            // If no data show invalid valid alert
            new Alert(Alert.AlertType.ERROR, "Invalid File").show();
        } else {
            data.forEach(System.out::println);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initialize the default path for file picker
        fileChooser.setInitialDirectory(new File("../"));
    }
}
