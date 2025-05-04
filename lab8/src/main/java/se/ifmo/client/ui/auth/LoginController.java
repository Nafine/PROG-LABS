package se.ifmo.client.ui.auth;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lombok.Setter;

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
            boolean remember = rememberCheckbox.isSelected();

            if (username.isEmpty() || password.isEmpty()) {
                messageText.setText("Please fill up all the fields.");
            } else {
                // логика входа...
                messageText.setText("Вход успешен (или нет).");
            }
        });

        signupLink.setOnAction(event -> {
            if (authController != null) {
                authController.loadRegister();
            }
        });
    }
}
