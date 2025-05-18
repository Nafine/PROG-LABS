package se.ifmo.client.ui.auth;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import lombok.Setter;
import se.ifmo.client.Client;
import se.ifmo.client.ui.locale.LocaleManager;
import se.ifmo.client.ui.scene.SceneManager;
import se.ifmo.shared.communication.Callback;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
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
                messageText.setText(LocaleManager.getString("field.empty"));
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

        initLocale();
    }

    private void initLocale(){
        updateUI();
        LocaleManager.addLocaleChangeListener(this::updateUI);
    }

    private void updateUI(){
        usernameLabel.setText(LocaleManager.getString("username"));
        passwordLabel.setText(LocaleManager.getString("password"));

        registerButton.setText(LocaleManager.getString("register"));
        loginLink.setText(LocaleManager.getString("login"));
    }
}
