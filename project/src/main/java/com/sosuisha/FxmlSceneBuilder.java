package com.sosuisha;

import javafx.scene.Scene;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class FxmlSceneBuilder {
    private FXMLLoader loader;
    private String cssPath;
    private int width;
    private int height;

    public FxmlSceneBuilder(String fxmlPath) {
        loader = new FXMLLoader(getClass().getResource(fxmlPath));
    }

    @SuppressWarnings("unused")
    private FxmlSceneBuilder() {
    }

    public FxmlSceneBuilder css(String cssPath) {
        this.cssPath = cssPath;
        return this;
    }

    public FxmlSceneBuilder controller(Callback<Class<?>, Object> controllerFactory) {
        loader.setControllerFactory(controllerFactory);
        return this;
    }

    public FxmlSceneBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public Scene build() throws IOException {
        var scene = new Scene(loader.load(), width, height);
        if (cssPath != null) {
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
        return scene;
    }
}
