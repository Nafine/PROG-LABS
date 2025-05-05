package se.ifmo.client.ui;

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
import lombok.Setter;
import se.ifmo.client.ui.auth.AuthController;
import se.ifmo.shared.builders.VehicleDirector;
import se.ifmo.shared.model.Vehicle;

public class MainController {
    @FXML
    private HBox buttons;
    @FXML
    private TableView<Vehicle> tableView;

    @Setter
    private AuthController authController;

    @FXML
    private void initialize() {
        tableView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();

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

    private void setValueFactory(TableColumn<Vehicle, ?> column) {
        switch (column.getText()) {
            case "ID":
                column.setCellValueFactory(new PropertyValueFactory<>("id"));
                break;
            case "Owner ID":
                column.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
                break;
            case "Coord X":
                column.setCellValueFactory(new PropertyValueFactory<>("coordinates.x"));
                break;
            case "Coord Y":
                column.setCellValueFactory(new PropertyValueFactory<>("coordinates.y"));
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
        }
    }

    private void fillTable() {
        for (TableColumn<Vehicle, ?> column : tableView.getColumns())
            setValueFactory(column);

        ObservableList<Vehicle> data = FXCollections.observableArrayList();

        for (int i = 0; i < 10_000; i++)
            data.add(VehicleDirector.constructAndGetRandomVehicles(1, 0).getFirst());

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
