package se.ifmo.client.ui.auth;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.Setter;
import se.ifmo.client.Client;
import se.ifmo.client.ui.scene.SceneManager;
import se.ifmo.shared.communication.Callback;

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
                Callback callback = Client.getInstance().register(username, password);
                messageText.setText(callback.message());
                if (callback.equals(Callback.successfulLogin())) SceneManager.getInstance().setScene("main");
            }
        });

        loginLink.setOnAction(event -> {
            if (authController != null) {
                authController.loadLogin();
            }
        });
    }
}
