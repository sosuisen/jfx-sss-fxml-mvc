package com.sosuisha;

import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Builder for constructing a JavaFX Scene with specified FXML and CSS files.
 * <p>
 * Example:
 * 
 * <pre>
 * var scene = SceneBuilder.withFxml("/com/example/main.fxml")
 *         .css("/com/example/style.css")
 *         .build();
 * </pre>
 */
public class SceneBuilder {
    private final URL fxmlURL;
    private URL cssURL;
    private int width = -1;
    private int height = -1;
    private Object[] ctrlConstructorParams;

    /**
     * Creates a new SceneBuilder instance with the specified FXML resource name.
     * 
     * @param resourceName the path to the FXML resource
     * @return a new SceneBuilder instance
     * @throws IllegalArgumentException if the resource is not found
     */
    public static SceneBuilder withFxml(String resourceName) {
        var url = SceneBuilder.class.getResource(resourceName);
        if (url == null) {
            throw new IllegalArgumentException("FXML resource not found: " + resourceName);
        }
        return new SceneBuilder(url);
    }

    /**
     * Creates a new SceneBuilder instance with the specified FXML URL.
     * 
     * @param fxmlURL the URL of the FXML file
     * @return a new SceneBuilder instance
     */
    public static SceneBuilder withFxml(URL fxmlURL) {
        return new SceneBuilder(Objects.requireNonNull(fxmlURL, "Url must not be null."));
    }

    private SceneBuilder(URL fxmlURL) {
        this.fxmlURL = Objects.requireNonNull(fxmlURL, "fxmlURL must not be null.");
    }

    /**
     * Specifies the CSS resource name.
     * 
     * @param resourceName the path to the CSS resource
     * @return this builder
     * @throws IllegalArgumentException if the resource is not found
     */
    public SceneBuilder css(String resourceName) {
        cssURL = SceneBuilder.class.getResource(resourceName);
        if (cssURL == null) {
            throw new IllegalArgumentException("CSS resource not found: " + resourceName);
        }
        return this;
    }

    /**
     * Specifies the CSS URL.
     * 
     * @param url the URL of the CSS file
     * @return this builder
     */
    public SceneBuilder css(URL url) {
        cssURL = Objects.requireNonNull(url, "Url must not be null.");
        return this;
    }

    /**
     * Specifies the constructor arguments for the controller.
     * 
     * @param constructorArgs the constructor arguments for the controller
     * @return this builder
     */
    public SceneBuilder newController(Object... constructorArgs) {
        ctrlConstructorParams = constructorArgs;
        return this;
    }

    /**
     * Specifies the size of the Scene.
     * If not set, the Scene will use the size of the root container.
     * 
     * @param width  the width
     * @param height the height
     * @return this builder
     */
    public SceneBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Builds the Scene.
     * 
     * @return the constructed Scene
     * @throws IOException if loading the FXML fails
     */
    public Scene build() throws IOException {
        var loader = new FXMLLoader(fxmlURL);

        if (ctrlConstructorParams != null && ctrlConstructorParams.length > 0) {
            loader.setControllerFactory(controllerClass -> {
                // This lambda is a factory that instantiates the controller class when the root
                // container node in main.fxml includes an fx:controller attribute.
                // controllerClass refers to the class specified by the fx:controller attribute.
                var paramTypes = new Class<?>[ctrlConstructorParams.length];
                for (var i = 0; i < ctrlConstructorParams.length; i++) {
                    paramTypes[i] = ctrlConstructorParams[i] != null
                            ? ctrlConstructorParams[i].getClass()
                            : Object.class; // If null, use Object.class to avoid NullPointerException.
                }
                // Retrieve a constructor that accepts paramTypes as a parameter,
                // and then use it to create an instance.
                try {
                    return controllerClass.getDeclaredConstructor(paramTypes)
                            .newInstance(ctrlConstructorParams);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create controller instance for "
                            + controllerClass.getName(), e);
                }
            });
        }

        Scene scene;
        try {
            scene = (width < 0 || height < 0)
                ? new Scene(loader.load())
                : new Scene(loader.load(), width, height);
        } catch (javafx.fxml.LoadException e) {
            // Set more informative message
            throw new javafx.fxml.LoadException("Failed to load FXML from " + fxmlURL, e);
        }

        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        }
        return scene;
    }
}
