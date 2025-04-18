package edu.iit.gtds.tax_department_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Starter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(new Scene(new FXMLLoader(Starter.class.getResource("")).load()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}