package se.ifmo.client.ui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import se.ifmo.shared.builders.VehicleDirector;
import se.ifmo.shared.model.Vehicle;

public class MainController {
    @FXML
    private HBox buttons;
    @FXML
    private TableView<Vehicle> tableView;

    @FXML
    private void initialize() {
        tableView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((wObs, oldWindow, newWindow) -> {
                    if (newWindow instanceof Stage stage) {
                        for (TableColumn<?, ?> column : tableView.getColumns()) {
                            String headerText = column.getText();
                            if (headerText != null && !headerText.isEmpty()) {
                                double textWidth = computeTextWidth(Font.font(12), headerText);
                                column.setMinWidth(textWidth + 5);
                            }
                        }

                        tableView.setFixedCellSize(100);

                        fillTable();

                        double minWidth = calculateMinWidth();
                        stage.setMinWidth(minWidth);
                        stage.setWidth(minWidth);
                    }
                });
            }
        });

        tableView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnMouseClicked(event -> {
                    if (!tableView.getBoundsInParent().contains(event.getX(), event.getY())) {
                        tableView.getSelectionModel().clearSelection();
                        tableView.getFocusModel().focus(-1);
                    }
                });
            }
        });
    }

    private <T> void setValueFactory(TableColumn<Vehicle, T> column) {
        switch (column.getText()) {
            case "ID":
                column.setCellValueFactory(new PropertyValueFactory<>("id"));
                break;
            case "Owner ID":
                column.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
                break;
            case "Name":
                column.setCellValueFactory(new PropertyValueFactory<>("name"));
                break;
            case "Coord X":
                column.setCellValueFactory(cellData ->
                        (ObservableValue<T>) new SimpleLongProperty(cellData.getValue().getCoordinates().getX()).asObject());
                break;
            case "Coord Y":
                column.setCellValueFactory(cellData ->
                        (ObservableValue<T>) new SimpleDoubleProperty(cellData.getValue().getCoordinates().getY()).asObject());
                break;
            case "Creation date":
                column.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
                break;
            case "Engine power":
                column.setCellValueFactory(new PropertyValueFactory<>("enginePower"));
                break;
            case "Capacity":
                column.setCellValueFactory(new PropertyValueFactory<>("capacity"));
                break;
            case "Distance travelled":
                column.setCellValueFactory(new PropertyValueFactory<>("distanceTravelled"));
                break;
            case "Fuel type":
                column.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + column.getText());
        }
    }

    private void fillTable() {
        for (TableColumn<Vehicle, ?> column : tableView.getColumns())
            setValueFactory(column);

        ObservableList<Vehicle> data = FXCollections.observableArrayList();

        data.addAll(VehicleDirector.constructAndGetRandomVehicles(1000, 0));

        tableView.setItems(data);
    }

    private double computeTextWidth(Font font, String text) {
        Text textNode = new Text(text);
        textNode.setFont(font);
        return textNode.getLayoutBounds().getWidth();
    }

    private double calculateMinWidth() {
        double totalColumnWidth = 0;
        for (var column : tableView.getColumns()) {
            totalColumnWidth += column.getWidth();
        }
        return totalColumnWidth + 50;
    }
}
