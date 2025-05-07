package se.ifmo.client.ui.auth;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import se.ifmo.client.Client;
import se.ifmo.shared.communication.Credentials;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private Hyperlink loginLink;
    @FXML
    private Text messageText;
    @Setter
    private AuthController authController;

    @FXML
    private void initialize() {
        registerButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageText.setText("Please fill up all the fields.");
            } else {
                if (!Client.getInstance().register(username, password)) {
                    messageText.setText("Failed to register, try again.");
                }
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main/main.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.setTitle("Collection manager");
                    stage.show();
                } catch (IOException e) {
                }
            }
        });

        loginLink.setOnAction(event -> {
            if (authController != null) {
                authController.loadLogin();
            }
        });
    }
}
