package se.ifmo.client.ui.auth;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lombok.Setter;
import se.ifmo.client.Client;
import se.ifmo.client.ui.scene.SceneManager;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox rememberCheckbox;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink signupLink;
    @FXML
    private Text messageText;
    @Setter
    private AuthController authController;

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                messageText.setText("Please fill up all the fields.");
            } else if (!Client.getInstance().login(username, password))
                messageText.setText("Invalid username or password.");
            else
                SceneManager.getInstance().setScene("main");
        });

        signupLink.setOnAction(event -> {
            if (authController != null) {
                authController.loadRegister();
            }
        });
    }
}
