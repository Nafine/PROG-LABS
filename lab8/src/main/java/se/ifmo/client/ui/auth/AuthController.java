package se.ifmo.client.ui.auth;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class AuthController {

    @FXML private StackPane authPane;

    @FXML
    public void initialize() {
        loadLogin();
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