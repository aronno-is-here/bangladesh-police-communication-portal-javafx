module com.bangladeshpolice {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires commons.io;

    opens com.bangladeshpolice to javafx.fxml;
    opens com.bangladeshpolice.controller to javafx.fxml;
    opens com.bangladeshpolice.model to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.bangladeshpolice.controller to javafx.fxml;

    exports com.bangladeshpolice;
    exports com.bangladeshpolice.controller;
    exports com.bangladeshpolice.model;
}