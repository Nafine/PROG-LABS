package se.ifmo.client;

import javafx.application.Application;
import javafx.stage.Stage;
import se.ifmo.client.ui.scene.SceneManager;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Client.getInstance();

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setStage(primaryStage);

        sceneManager.loadScene("auth", "/ui/auth/auth.fxml");
        sceneManager.loadScene("main", "/ui/main/main.fxml");
        sceneManager.loadScene("filter", "/ui/main/buttonBar/filter.fxml");

        sceneManager.setScene("auth");
    }
}
