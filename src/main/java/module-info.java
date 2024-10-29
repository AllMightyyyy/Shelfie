module org.example.librarysimulation {
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.google.gson;
    requires com.jfoenix;
    requires org.burningwave.core;
    requires javafx.swing;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires java.desktop;
    requires jdk.compiler;

    opens org.example.librarysimulation to javafx.fxml;
    exports org.example.librarysimulation;
    exports org.example.librarysimulation.controller;
    opens org.example.librarysimulation.controller to javafx.fxml;
}