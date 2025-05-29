package com.sosuisha;

import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;

/**
 * Builder for constructing a JavaFX Scene with specified FXML and CSS files.
 * <p>
 * Example:
 * <pre>
 * var scene = FxmlSceneBuilder.create("/com/example/main.fxml")
 *     .css("/com/example/style.css")
 *     .size(800, 600)
 *     .build();
 * </pre>
 */
public class FxmlSceneBuilder {
    private URL fxmlURL;
    private URL cssURL;
    private int width = 0; // can be 0 for default width
    private int height = 0; // can be 0 for default height
    private Object[] ctrlConstructorParams;

    /**
     * Creates a default FxmlSceneBuilder.
     * @return a new FxmlSceneBuilder instance
     */
    public static FxmlSceneBuilder create() {
        return new FxmlSceneBuilder();
    }

    /**
     * Creates a FxmlSceneBuilder with the specified FXML resource name.
     * @param fxmlResourceName the path to the FXML resource
     * @return a new FxmlSceneBuilder instance
     */
    public static FxmlSceneBuilder create(String fxmlResourceName) {
        return new FxmlSceneBuilder().fxml(fxmlResourceName);
    }

    /**
     * Creates a FxmlSceneBuilder with the specified FXML URL.
     * @param fxmlURL the URL of the FXML file
     * @return a new FxmlSceneBuilder instance
     */
    public static FxmlSceneBuilder create(URL fxmlURL) {
        return new FxmlSceneBuilder().fxml(fxmlURL);
    }

    /**
     * Constructor (not accessible from outside)
     */
    private FxmlSceneBuilder() {
    }

    /**
     * Specifies the FXML resource name.
     * @param resourceName the path to the FXML resource
     * @return this builder
     * @throws IllegalStateException if the resource is not found
     */
    public FxmlSceneBuilder fxml(String resourceName) {
        fxmlURL = getClass().getResource(resourceName);
        if (fxmlURL == null) {
            throw new IllegalStateException("FXML resource not found: " + resourceName);
        }
        return this;
    }

    /**
     * Specifies the FXML URL.
     * @param url the URL of the FXML file
     * @return this builder
     */
    public FxmlSceneBuilder fxml(URL url) {
        fxmlURL = url;
        return this;
    }

    /**
     * Specifies the CSS resource name.
     * @param resourceName the path to the CSS resource
     * @return this builder
     * @throws IllegalStateException if the resource is not found
     */
    public FxmlSceneBuilder css(String resourceName) {
        cssURL = getClass().getResource(resourceName);
        if (cssURL == null) {
            throw new IllegalStateException("CSS resource not found: " + resourceName);
        }        
        return this;
    }

    /**
     * Specifies the CSS URL.
     * @param url the URL of the CSS file
     * @return this builder
     */
    public FxmlSceneBuilder css(URL url) {
        cssURL = url;
        return this;
    }

    /**
     * Specifies the constructor arguments for the controller.
     * @param constructorArgs the constructor arguments for the controller
     * @return this builder
     */
    public FxmlSceneBuilder newController(Object... constructorArgs) {
        ctrlConstructorParams = constructorArgs;
        return this;
    }

    /**
     * Specifies the size of the Scene.
     * @param width the width
     * @param height the height
     * @return this builder
     */
    public FxmlSceneBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Builds the Scene.
     * @return the constructed Scene
     * @throws IOException if loading the FXML fails
     * @throws IllegalStateException if fxml() has not been called
     */
    public Scene build() throws IOException {
        if (fxmlURL == null) {
            throw new IllegalStateException("fxml() must be called before build()");
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
