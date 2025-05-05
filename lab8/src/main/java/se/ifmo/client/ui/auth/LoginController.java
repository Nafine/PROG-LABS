package se.ifmo.client.ui.auth;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;

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
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main/main.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Dashboard");
                    stage.show();
                } catch (IOException e) {
                }
            }
        });

        signupLink.setOnAction(event -> {
            if (authController != null) {
                authController.loadRegister();
            }
        });
    }
}
