package se.ifmo.client.ui.auth;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lombok.Setter;
import se.ifmo.client.Client;
import se.ifmo.client.ui.locale.LocaleManager;
import se.ifmo.client.ui.scene.SceneManager;
import se.ifmo.shared.communication.Callback;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
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
                messageText.setText(LocaleManager.getString("field.empty"));
            } else {
                Callback callback = Client.getInstance().login(username, password);
                messageText.setText(callback.message());
                if (callback.equals(Callback.successfulLogin()))
                    SceneManager.getInstance().setScene("main");
            }
        });

        signupLink.setOnAction(event -> {
            if (authController != null) {
                authController.loadRegister();
            }
        });

        initLocale();
    }

    private void initLocale() {
        updateUI();
        LocaleManager.addLocaleChangeListener(this::updateUI);
    }

    private void updateUI() {
        usernameLabel.setText(LocaleManager.getString("username"));
        passwordLabel.setText(LocaleManager.getString("password"));

        loginButton.setText(LocaleManager.getString("login"));
        signupLink.setText(LocaleManager.getString("signup"));
    }
}
