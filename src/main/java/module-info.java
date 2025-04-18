module edu.iit.gtds.tax_department_system {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.iit.gtds.tax_department_system to javafx.fxml;
    exports edu.iit.gtds.tax_department_system;
    exports edu.iit.gtds.tax_department_system.controller;
    opens edu.iit.gtds.tax_department_system.controller to javafx.fxml;
}