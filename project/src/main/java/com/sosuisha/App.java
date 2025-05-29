package com.sosuisha;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * JavaFX MVC(Model-View-Controller) application
 */
public class App extends Application {

    /**
     * Called when the application is started.
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        // Model
        var model = new Model();

        // View
        showMainWindow(stage, model);
    }

    /**
     * Shows the main window of the application.
     * @param stage the primary stage
     * @param model the application model
     */
    private void showMainWindow(Stage stage, Model model) {
        try {
            var scene = FxmlSceneBuilder.create()
                    // The controller class must be specified in the FXML file.
                    .fxml("main.fxml")
                    .css("style.css")
                    .size(640, 480)
                    // The parameters of newController must match
                    // the constructor parameters of the controller class.
                    .newController(model)
                    .build();
            stage.setScene(scene);
            stage.setTitle("MyApp");
            stage.show();
        } catch (Exception e) {
            showStartupErrorAndExit(e);
        }
    }

    /**
     * Displays an error dialog and exits the application if startup fails.
     * @param e the exception that occurred during startup
     */
    private void showStartupErrorAndExit(Exception e) {
        e.printStackTrace();

        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Startup Error");
        alert.setHeaderText("An error occurred during startup");
        alert.getDialogPane().setExpandableContent(new Label(e.getMessage()));
        alert.getDialogPane().setExpanded(true);
        alert.setOnHidden(event -> Platform.exit());
        alert.show();
    }
}
