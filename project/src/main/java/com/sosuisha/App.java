package com.sosuisha;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Model
        var model = new Model();

        // View
        var scene = FxmlSceneBuilder.create()
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
    }
}
