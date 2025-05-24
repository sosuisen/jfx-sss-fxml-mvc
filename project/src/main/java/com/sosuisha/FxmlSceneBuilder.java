package com.sosuisha;

import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;

public class FxmlSceneBuilder {
    private URL fxmlURL;
    private URL cssURL;
    private int width;
    private int height;
    private Object[] ctrlConstructorParams;

    public static FxmlSceneBuilder create() {
        return new FxmlSceneBuilder();
    }

    public static FxmlSceneBuilder create(String fxmlResourceName) {
        return new FxmlSceneBuilder().fxml(fxmlResourceName);
    }

    public static FxmlSceneBuilder create(URL fxmlURL) {
        return new FxmlSceneBuilder().fxml(fxmlURL);
    }

    private FxmlSceneBuilder() {
    }

    public FxmlSceneBuilder fxml(String resourceName) {
        this.fxmlURL = getClass().getResource(resourceName);
        return this;
    }

    public FxmlSceneBuilder fxml(URL url) {
        this.fxmlURL = url;
        return this;
    }

    public FxmlSceneBuilder css(String resourceName) {
        this.cssURL = getClass().getResource(resourceName);
        return this;
    }

    public FxmlSceneBuilder css(URL url) {
        this.cssURL = url;
        return this;
    }

    public FxmlSceneBuilder newController(Object... constructorArgs) {
        this.ctrlConstructorParams = constructorArgs;
        return this;
    }

    public FxmlSceneBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public Scene build() throws IOException {
        if (fxmlURL == null) {
            throw new IllegalStateException("FXML must be specified.");
        }
        var loader = new FXMLLoader(fxmlURL);

        if (ctrlConstructorParams != null && ctrlConstructorParams.length > 0) {
            loader.setControllerFactory(controllerClass -> {
                // This lambda is a factory that instantiates the controller class when the root
                // container node in main.fxml includes an fx:controller attribute.
                // controllerClass refers to the class specified by the fx:controller attribute.
                var paramTypes = new Class<?>[ctrlConstructorParams.length];
                for (var i = 0; i < ctrlConstructorParams.length; i++) {
                    paramTypes[i] = ctrlConstructorParams[i].getClass();
                }
                // Retrieve a constructor that accepts paramTypes as a parameter,
                // and then use it to create an instance.
                try {
                    return controllerClass.getDeclaredConstructor(paramTypes).newInstance(ctrlConstructorParams);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create controller instance", e);
                }
            });
        }

        var scene = new Scene(loader.load(), width, height);
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        }
        return scene;
    }
}
