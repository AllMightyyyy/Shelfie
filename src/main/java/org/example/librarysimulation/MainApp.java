package org.example.librarysimulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.librarysimulation.util.DatabaseInitializer;
import org.example.librarysimulation.util.DatabaseUtility;
import org.burningwave.core.assembler.StaticComponentContainer;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Export all modules to all modules
            StaticComponentContainer.Modules.exportAllToAll();

            DatabaseInitializer.initializeDatabase();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/MainLayout.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Book Library Management");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        DatabaseUtility.getInstance().closeDataSource();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
