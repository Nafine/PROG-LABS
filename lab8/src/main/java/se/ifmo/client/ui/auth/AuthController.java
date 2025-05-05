package se.ifmo.client.ui.auth;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import se.ifmo.client.ui.MainController;

import java.io.IOException;

public class AuthController {
    @FXML
    private StackPane authPane;

    @FXML
    public void initialize() {
        loadLogin();
    }

    public void loadMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main/main.fxml"));
            Node node = loader.load();

            MainController controller = loader.getController();
            controller.setAuthController(this);
        } catch (IOException e) {
        }
    }

    public void loadLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/auth/login.fxml"));
            Node node = loader.load();

            LoginController controller = loader.getController();
            controller.setAuthController(this);

            authPane.getChildren().setAll(node);
        } catch (IOException e) {
        }
    }

    public void loadRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/auth/register.fxml"));
            Node node = loader.load();

            RegisterController controller = loader.getController();
            controller.setAuthController(this);

            authPane.getChildren().setAll(node);
        } catch (IOException e) {
        }
    }
}