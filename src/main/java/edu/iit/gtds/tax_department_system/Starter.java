package edu.iit.gtds.tax_department_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Starter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Read the fxml file and set the scene
        stage.setScene(new Scene(new FXMLLoader(Starter.class.getResource("view/file_path_form.fxml")).load()));
        stage.setTitle("Transaction file path picker");

        // Show the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}