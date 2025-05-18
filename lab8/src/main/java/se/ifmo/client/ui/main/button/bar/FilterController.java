package se.ifmo.client.ui.main.button.bar;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Setter;
import se.ifmo.client.ui.locale.LocaleManager;
import se.ifmo.client.ui.main.MainController;
import se.ifmo.shared.enums.FuelType;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static se.ifmo.client.ui.util.TextFieldManager.*;

public class FilterController {
    @FXML
    private Label idLabel;
    @FXML
    private Label ownerIdLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label coordinateXLabel;
    @FXML
    private Label coordinateYLabel;
    @FXML
    private Label creationDateLabel;
    @FXML
    private Label enginePowerLabel;
    @FXML
    private Label capacityLabel;
    @FXML
    private Label distanceTravelledLabel;
    @FXML
    private Label fuelTypeLabel;

    @FXML
    private TextField idField;
    @FXML
    private TextField ownerIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinateXField;
    @FXML
    private TextField coordinateYField;
    @FXML
    private DatePicker creationDatePicker;
    @FXML
    private TextField enginePowerField;
    @FXML
    private TextField capacityField;
    @FXML
    private TextField distanceTravelledField;
    @FXML
    private ComboBox<FuelType> fuelTypeBox;

    @FXML
    private Button applyButton;
    @FXML
    private Button resetButton;

    @Setter
    private MainController mainController;

    @FXML
    public void initialize() {
        restrictToInteger(idField);
        restrictToInteger(ownerIdField);
        restrictToInteger(enginePowerField);
        restrictToInteger(coordinateXField);


        restrictToFloat(capacityField);
        restrictToFloat(distanceTravelledField);
        restrictToFloat(coordinateYField);

        fuelTypeBox.setItems(FXCollections.observableArrayList(FuelType.values()));
        fuelTypeBox.setEditable(false);

        applyButton.setOnAction(e -> applyFilters());
        resetButton.setOnAction(e -> resetFields());

        initLocale();
    }

    private void initLocale(){
        updateUI();
        LocaleManager.addLocaleChangeListener(this::updateUI);
    }

    private void updateUI(){
        idLabel.setText(LocaleManager.getString("id"));
        ownerIdLabel.setText(LocaleManager.getString("ownerId"));
        nameLabel.setText(LocaleManager.getString("name"));
        coordinateXLabel.setText(LocaleManager.getString("coordinateX"));
        coordinateYLabel.setText(LocaleManager.getString("coordinateY"));
        creationDateLabel.setText(LocaleManager.getString("creationDate"));
        enginePowerLabel.setText(LocaleManager.getString("enginePower"));
        capacityLabel.setText(LocaleManager.getString("capacity"));
        distanceTravelledLabel.setText(LocaleManager.getString("distanceTravelled"));
        fuelTypeLabel.setText(LocaleManager.getString("fuelType"));

        applyButton.setText(LocaleManager.getString("filter.apply"));
        resetButton.setText(LocaleManager.getString("filter.reset"));
    }

    private void applyFilters() {
        try {
            Long id = parseLongOrNull(idField.getText());
            Long ownerId = parseLongOrNull(ownerIdField.getText());
            String name = nameField.getText();
            Long coordX = parseLongOrNull(coordinateXField.getText());
            Double coordY = parseDoubleOrNull(coordinateYField.getText());
            LocalDate localDate = creationDatePicker.getValue();
            Date creationDate = localDate == null ? null : Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Integer enginePower = parseIntOrNull(enginePowerField.getText());
            Double capacity = parseDoubleOrNull(capacityField.getText());
            Float distance = parseFloatOrNull(distanceTravelledField.getText());
            FuelType fuel = fuelTypeBox.getValue();

            mainController.filterTable(vehicle ->
                    (id == null || vehicle.getId() == id) && (ownerId == null || vehicle.getOwnerId() == ownerId)
                            && (name == null || name.isEmpty() || vehicle.getName().equals(name)) && (coordX == null || vehicle.getCoordinates().getX() == coordX)
                            && (coordY == null || vehicle.getCoordinates().getY().equals(coordY)) && (creationDate == null || vehicle.getCreationDate().equals(creationDate))
                            && (enginePower == null || vehicle.getEnginePower() == enginePower) && (capacity == null || vehicle.getCapacity() == capacity)
                            && (distance == null || vehicle.getDistanceTravelled().equals(distance)) && (fuel == null || vehicle.getFuelType().equals(fuel))
            );
        } catch (Exception ignored) {
        }
    }

    private void resetFields() {
        idField.clear();
        ownerIdField.clear();
        nameField.clear();
        coordinateXField.clear();
        coordinateYField.clear();
        creationDatePicker.setValue(null);
        enginePowerField.clear();
        capacityField.clear();
        distanceTravelledField.clear();
        fuelTypeBox.setValue(null);
    }
}
