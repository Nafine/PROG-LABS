package se.ifmo.client.ui.main.button.bar;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import se.ifmo.client.Client;
import se.ifmo.client.ui.locale.LocaleManager;
import se.ifmo.shared.command.GetUID;
import se.ifmo.shared.command.Info;

public class ProfileController {
    @FXML
    private ImageView avatarImage;
    @FXML
    private Label titleLabel;
    @FXML
    private Label userIdLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label userId;
    @FXML
    private Label username;
    @FXML
    private Label collectionInfoLabel;

    @FXML
    public void initialize() {
        userId.setText(Client.getInstance().forwardCommand(new GetUID()).message());
        username.setText("placeholder");
        collectionInfoLabel.setText(Client.getInstance().forwardCommand(new Info()).message());
        avatarImage.setImage(new Image("https://media.istockphoto.com/id/640200792/ru/%D1%84%D0%BE%D1%82%D0%BE/%D0%BF%D0%BE%D1%80%D1%82%D1%80%D0%B5%D1%82-%D0%BF%D0%BE%D0%B6%D0%B8%D0%BB%D0%BE%D0%B3%D0%BE-%D0%B4%D0%B6%D0%B5%D0%BD%D1%82%D0%BB%D1%8C%D0%BC%D0%B5%D0%BD%D0%B0-%D1%81-%D1%81%D0%B8%D0%B3%D0%B0%D1%80%D0%BE%D0%B9-%D0%B3%D0%B0%D0%B2%D0%B0%D0%BD%D0%B0-%D0%BA%D1%83%D0%B1%D0%B0.jpg?s=2048x2048&w=is&k=20&c=ZMClk6eTF7dIHEC_GXsIQVwQw0vZL7mdhQFdeXEhEOg="));

        initLocale();
    }

    public void update(){
        username.setText(Client.getInstance().getUsername());
    }

    private void initLocale(){
        updateUI();
        LocaleManager.addLocaleChangeListener(this::updateUI);
    }

    private void updateUI(){
        titleLabel.setText(LocaleManager.getString("profile.title"));
        userIdLabel.setText(LocaleManager.getString("id"));
        usernameLabel.setText(LocaleManager.getString("username"));
    }
}