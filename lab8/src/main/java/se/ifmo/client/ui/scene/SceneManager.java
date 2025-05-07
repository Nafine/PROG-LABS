package se.ifmo.client.ui.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class SceneManager {
    private static SceneManager instance;

    private final HashMap<String, Scene> scenes = new HashMap<>();

    private Stage primaryStage;
    private String currentScene;

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        return instance == null ? instance = new SceneManager() : instance;
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    public void reloadCurrentScene() {
        if (currentScene != null) {
            setScene(currentScene);
        }
    }

    public <T> T setPopup(String name, String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        Stage newStage = new Stage();
        newStage.setTitle(name);
        newStage.setScene(scene);

        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.show();
        newStage.setResizable(false);

        return loader.getController();
    }

    public void setScene(String name) {
        if (primaryStage == null) throw new IllegalStateException("Primary stage is not set");

        Scene scene = scenes.get(name);
        if (scene == null) throw new IllegalArgumentException("No scene found with name: " + name);

        currentScene = name;
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void loadScene(String name, String path) throws IOException {
        scenes.put(name, loadFXML(path));
    }

    private Scene loadFXML(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        return new Scene(loader.load());
    }
}
