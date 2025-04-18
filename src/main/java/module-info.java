module edu.iit.gtds.tax_department_system {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.iit.gtds.tax_department_system to javafx.fxml;
    opens edu.iit.gtds.tax_department_system.controller to javafx.fxml;
    opens edu.iit.gtds.tax_department_system.model to javafx.base;

    exports edu.iit.gtds.tax_department_system;
    exports edu.iit.gtds.tax_department_system.controller;
    exports edu.iit.gtds.tax_department_system.model;
}
