package se.ifmo.client.ui.main.button.bar;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import lombok.Setter;
import se.ifmo.client.Client;
import se.ifmo.client.ui.main.MainController;
import se.ifmo.shared.command.Add;
import se.ifmo.shared.command.AddRandom;
import se.ifmo.shared.enums.FuelType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static se.ifmo.client.ui.util.TextFieldManager.*;

public class AddController {
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
    private Text textMessage;
    @Setter
    private MainController mainController;

    @FXML
    public void initialize() {
        restrictToInteger(enginePowerField);
        restrictToInteger(coordinateXField);

        restrictToFloat(capacityField);
        restrictToFloat(distanceTravelledField);
        restrictToFloat(coordinateYField);

        fuelTypeBox.setItems(FXCollections.observableArrayList(FuelType.values()));
        fuelTypeBox.setEditable(false);

        addButton.setOnAction(e -> {
                    addItem();
                    mainController.refreshTable();
                }
        );

        addRandomButton.setOnAction(e -> {
                    addRandomItem();
                    mainController.refreshTable();
                }
        );
    }

    private void addItem() {
        try {
            String name = validateNonNull(nameField.getText(), "Name");
            Long coordX = parseLongOrNull(coordinateXField.getText());
            Double coordY = validateNonNull(parseDoubleOrNull(coordinateYField.getText()), "Coord Y");
            Integer enginePower = validateNonNull(parseIntOrNull(enginePowerField.getText()), "Engine Power");
            Double capacity = validateNonNull(parseDoubleOrNull(capacityField.getText()), "Capacity");
            Float distance = parseFloatOrNull(distanceTravelledField.getText());
            FuelType fuelType = validateNonNull(fuelTypeBox.getValue(), "Fuel type");

            textMessage.setText(Client.getInstance().forwardCommand(new Add(), Stream.of(name, coordX, coordY, enginePower, capacity, distance, fuelType)
                            .map(t -> t == null ? "null" : t.toString())
                            .collect(Collectors.toList()))
                    .message());
        } catch (IllegalArgumentException e) {
            showAlert("Ошибка: " + e.getMessage());
        }
    }

    private void addRandomItem() {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.initStyle(StageStyle.UTILITY);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/ui/main/styles.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("styles");
        dialog.setTitle("Добавить случайные элементы");
        dialog.setHeaderText("Введите количество элементов");
        dialog.setContentText("Количество:");
        dialog.setGraphic(null);

        dialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                dialog.getEditor().setText(newValue.replaceAll("\\D", ""));
            }
        });

        dialog.showAndWait().ifPresent(count -> {
            if (count.isEmpty()) {
                showAlert("Количество не может быть пустым");
            } else {
                try {
                    int num = Integer.parseInt(count);
                    if (num <= 0) {
                        showAlert("Количество должно быть положительным числом");
                    } else if (num > 10000) {
                        showAlert("Максимальное количество - 10000");
                    } else {
                        textMessage.setText(Client.getInstance().forwardCommand(
                                new AddRandom(), List.of(count)).message());
                    }
                } catch (NumberFormatException e) {
                    showAlert("Некорректное число");
                }
            }
        });
    }

    private <T> T validateNonNull(T value, String fieldName) {
        if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым.");
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
