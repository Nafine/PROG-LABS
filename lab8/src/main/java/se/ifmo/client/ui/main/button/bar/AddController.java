package se.ifmo.client.ui.main.button.bar;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import lombok.Setter;
import se.ifmo.client.Client;
import se.ifmo.client.ui.locale.LocaleManager;
import se.ifmo.client.ui.main.MainController;
import se.ifmo.shared.command.Add;
import se.ifmo.shared.command.AddIfMin;
import se.ifmo.shared.command.AddRandom;
import se.ifmo.shared.command.Command;
import se.ifmo.shared.enums.FuelType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static se.ifmo.client.ui.util.TextFieldManager.*;

public class AddController {
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
    private ComboBox<FuelType> fuelTypeBox;
    @FXML
    private Button addButton;
    @FXML
    private Button addRandomButton;
    @FXML
    private Button addIfMinButton;
    @FXML
    private Text textMessage;
    @Setter
    private MainController mainController;

    @FXML
    private Label header;

    @FXML
    public void initialize() {
        fuelTypeBox.setItems(FXCollections.observableArrayList(FuelType.values()));
        fuelTypeBox.setEditable(false);

        restrictFields();
        initButtons();

        initLocale();
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

        addButton.setText(LocaleManager.getString("add"));
        addRandomButton.setText(LocaleManager.getString("add.random"));
        addIfMinButton.setText(LocaleManager.getString("add.min"));

        header.setText(LocaleManager.getString("add.header"));
    }

    private void restrictFields() {
        restrictToInteger(enginePowerField);
        restrictToInteger(coordinateXField);

        restrictToFloat(capacityField);
        restrictToFloat(distanceTravelledField);
        restrictToFloat(coordinateYField);
    }

    private void initButtons() {
        addButton.setOnAction(e ->
                forwardWithVehicleFields(new Add()));

        addRandomButton.setOnAction(e ->
                addRandomItem()
        );

        addIfMinButton.setOnAction(e ->
                forwardWithVehicleFields(new AddIfMin())
        );
    }

    private void addRandomItem() {
        TextInputDialog dialog = initDialog();

        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                dialog.getEditor().setText(newValue.replaceAll("\\D", ""));
            }
        });

        dialog.showAndWait().ifPresent(this::handleDialogInput);
    }

    private void forwardWithVehicleFields(Command command) {
        try {
            String name = validateNonNull(nameField.getText(), "Name");
            Long coordX = parseLongOrNull(coordinateXField.getText());
            Double coordY = validateNonNull(parseDoubleOrNull(coordinateYField.getText()), "Coord Y");
            Integer enginePower = validateNonNull(parseIntOrNull(enginePowerField.getText()), "Engine Power");
            Double capacity = validateNonNull(parseDoubleOrNull(capacityField.getText()), "Capacity");
            Float distance = parseFloatOrNull(distanceTravelledField.getText());
            FuelType fuelType = validateNonNull(fuelTypeBox.getValue(), "Fuel type");


            textMessage.setText(Client.getInstance().forwardCommand(command, Stream.of(name, coordX, coordY, enginePower, capacity, distance, fuelType)
                            .map(t -> t == null ? "null" : t.toString())
                            .collect(Collectors.toList()))
                    .message());
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        }
    }

    private void handleDialogInput(String count) {
        if (count.isEmpty()) {
            showAlert(LocaleManager.getString("add.dialog.empty"));
        } else {
            try {
                int num = Integer.parseInt(count);
                if (num <= 0) {
                    showAlert(LocaleManager.getString("add.dialog.amount.positive"));
                } else if (num > 10000) {
                    showAlert(LocaleManager.getString("add.dialog.amount.max"));
                } else {
                    textMessage.setText(Client.getInstance().forwardCommand(
                            new AddRandom(), List.of(count)).message());
                }
            } catch (NumberFormatException e) {
                showAlert("add.dialog.amount.invalid");
            }
        }
    }

    private TextInputDialog initDialog() {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.initStyle(StageStyle.UTILITY);
        dialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ui/main/styles.css")).toExternalForm());
        dialog.getDialogPane().getStyleClass().add("styles");
        dialog.setTitle(LocaleManager.getString("add.dialog.title"));
        dialog.setHeaderText(LocaleManager.getString("add.dialog.header"));
        dialog.setContentText(LocaleManager.getString("add.dialog.content"));
        dialog.setGraphic(null);
        return dialog;
    }

    private <T> T validateNonNull(T value, String fieldName) {
        if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
        return value;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
