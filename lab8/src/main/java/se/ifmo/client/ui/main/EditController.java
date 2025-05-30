package se.ifmo.client.ui.main;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import se.ifmo.client.ui.locale.LocaleManager;
import se.ifmo.shared.enums.FuelType;
import se.ifmo.shared.model.Coordinates;
import se.ifmo.shared.model.Vehicle;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static se.ifmo.client.ui.util.Notify.showAlert;
import static se.ifmo.client.ui.util.TextFieldManager.*;

public class EditController implements Initializable {
    private final ObjectProperty<Vehicle> item = new SimpleObjectProperty<>();
    private final BooleanProperty readOnly = new SimpleBooleanProperty(false);

    @FXML
    private Label nameLabel;
    @FXML
    private Label coordinateXLabel;
    @FXML
    private Label coordinateYLabel;
    @FXML
    private Label enginePowerLabel;
    @FXML
    private Label capacityLabel;
    @FXML
    private Label distanceTravelledLabel;
    @FXML
    private Label fuelTypeLabel;

    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinateXField;
    @FXML
    private TextField coordinateYField;
    @FXML
    private TextField enginePowerField;
    @FXML
    private TextField capacityField;
    @FXML
    private TextField distanceTravelledField;
    @FXML
    private ChoiceBox<FuelType> fuelTypeBox;
    @FXML
    private Button editButton;
    @Setter
    private Consumer<Vehicle> onComplete;

    @FXML
    private Label header;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fuelTypeBox.getItems().addAll(FuelType.values());

        item.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;

            nameField.setText(newValue.getName());
            coordinateXField.setText(Long.toString(newValue.getCoordinates().getX()));
            coordinateYField.setText(newValue.getCoordinates().getY().toString());
            enginePowerField.setText(Integer.toString(newValue.getEnginePower()));
            capacityField.setText(Double.toString(newValue.getCapacity()));
            distanceTravelledField.setText(Double.toString(newValue.getDistanceTravelled()));
            fuelTypeBox.getSelectionModel().select(newValue.getFuelType());
        });

        readOnly.addListener((observable, oldValue, newValue) -> {
            nameField.setDisable(newValue);
            coordinateXField.setDisable(newValue);
            coordinateYField.setDisable(newValue);
            enginePowerField.setDisable(newValue);
            capacityField.setDisable(newValue);
            distanceTravelledField.setDisable(newValue);
            fuelTypeBox.setDisable(newValue);

            editButton.setVisible(!newValue);
            editButton.setManaged(!newValue);
        });

        editButton.setOnAction(event -> applyChanges());

        initLocale();
    }

    public void setItem(Vehicle item) {
        this.item.set(item);
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly.set(readOnly);
    }

    public void applyChanges() {
        Vehicle item = this.item.get() != null
                ? this.item.get()
                : new Vehicle();

        try {
            item.setName(validateNonNull(nameField.getText(), LocaleManager.getString("name")));
            item.setCoordinates(new Coordinates(Long.parseLong(validateNonNull(coordinateXField.getText(), LocaleManager.getString("coordinateX"))),
                    validateNonNull(parseDoubleOrNull(coordinateYField.getText()), LocaleManager.getString("coordinateY"))));
            item.setEnginePower(validateNonNull(parseIntOrNull(enginePowerField.getText()), LocaleManager.getString("enginePower")));
            item.setCapacity(validateNonNull(parseDoubleOrNull(capacityField.getText()), LocaleManager.getString("capacity")));
            item.setDistanceTravelled(parseFloatOrNull(distanceTravelledField.getText()));
            item.setFuelType(validateNonNull(fuelTypeBox.getValue(), LocaleManager.getString("fuelType")));

            if (onComplete != null) onComplete.accept(item);

            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.close();
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        }
    }

    private void initLocale() {
        updateUI();
        LocaleManager.addLocaleChangeListener(this::updateUI);
    }

    private void updateUI() {
        nameLabel.setText(LocaleManager.getString("name"));
        coordinateXLabel.setText(LocaleManager.getString("coordinateX"));
        coordinateYLabel.setText(LocaleManager.getString("coordinateY"));
        enginePowerLabel.setText(LocaleManager.getString("enginePower"));
        capacityLabel.setText(LocaleManager.getString("capacity"));
        distanceTravelledLabel.setText(LocaleManager.getString("distanceTravelled"));
        fuelTypeLabel.setText(LocaleManager.getString("fuelType"));

        editButton.setText(LocaleManager.getString("edit"));
        header.setText(LocaleManager.getString("edit.header"));
    }
}
