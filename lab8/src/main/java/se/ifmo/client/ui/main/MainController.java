package se.ifmo.client.ui.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import se.ifmo.client.Client;
import se.ifmo.client.ui.main.button.bar.AddController;
import se.ifmo.client.ui.main.button.bar.FilterController;
import se.ifmo.client.ui.main.button.bar.ProfileController;
import se.ifmo.client.ui.scene.SceneManager;
import se.ifmo.shared.annotations.Nested;
import se.ifmo.shared.command.Clear;
import se.ifmo.shared.command.Show;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.model.Vehicle;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainController {
    private final ObservableList<Vehicle> originalList = FXCollections.observableArrayList();
    @FXML
    private TableView<Vehicle> tableView;
    @FXML
    private Button profileButton;
    @FXML
    private Button filterButton;
    @FXML
    private Button addButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button visualizeButton;

    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem editItem;
    @FXML
    private MenuItem deleteItem;

    @FXML
    private Label conErr;

    private Timeline timeline;

    @FXML
    private void initialize() {
        conErr.setVisible(false);
        tableView.sceneProperty().addListener((obs, oldScene, newScene) -> initTable(newScene));
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

        tableView.setRowFactory(tv -> {
            TableRow<Vehicle> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !row.isEmpty()) {
                    Vehicle rowData = row.getItem();
                    openEditor(rowData, item -> {
                        Client.getInstance().updateByID(item.getId(), item);
                        refreshTable();
                    });
                }

                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    if (row.getItem().getOwnerId() != Client.getInstance().getUID())
                        return;
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    refreshTable();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        editItem.setOnAction(event -> {
            Vehicle selectedItem = tableView.getSelectionModel().getSelectedItem();
            openEditor(selectedItem, item -> {
                Client.getInstance().updateByID(item.getId(), item);
            });
        });

        deleteItem.setOnAction(event -> {
            Vehicle selectedRow = tableView.getSelectionModel().getSelectedItem();
            Client.getInstance().removeByID(selectedRow.getId());
        });

        profileButton.setOnAction(event -> showProfile());
        filterButton.setOnAction(event -> showFilter());
        addButton.setOnAction(event -> handleAdd());
        clearButton.setOnAction(event -> {
            Client.getInstance().forwardCommand(new Clear());
        });
        visualizeButton.setOnAction(event -> {
            timeline.pause();
            long uid = Client.getInstance().getUID();
            CollectionVisualizer.visualize(originalList.filtered(v -> v.getOwnerId() == uid), this);
            //timeline.play();
        });
    }

    private void initTable(Scene newScene) {
        if (newScene != null) {
            newScene.windowProperty().addListener((wObs, oldWindow, newWindow) -> {
                if (newWindow instanceof Stage stage) {
                    getFields(Vehicle.class).forEach(field -> {
                        TableColumn<Vehicle, String> column = new TableColumn<>(field);
                        column.setReorderable(false);
                        column.setCellValueFactory(data ->
                                new ReadOnlyStringWrapper(getFieldValue(data.getValue(), field)));
                        tableView.getColumns().add(column);
                    });

                    setMinWidthByText(stage);
                    tableView.setFixedCellSize(25);
                    tableView.setItems(FXCollections.observableArrayList(Client.getInstance().forwardCommand(new Show()).vehicles()));
                    originalList.addAll(tableView.getItems());
                }
            });
        }
    }

    public void pauseRefresh() {
        timeline.pause();
    }

    public void resumeRefresh() {
        timeline.play();
    }

    public void refreshTable() {
        Client.getInstance().forwardCommandAsync(new Show())
                .thenAccept(callback -> {
                    Platform.runLater(() -> handleUpdate(callback));
                });
    }

    private void handleUpdate(Callback callback) {
        if (callback.equals(Callback.serverDidNotRespond()))
            conErr.setVisible(true);
        else if (!callback.equals(Callback.serverSideError())) {
            tableView.setItems(FXCollections.observableArrayList(callback.vehicles()));
            originalList.setAll(tableView.getItems());
            if (conErr.isVisible()) conErr.setVisible(false);
        }
    }

    private void showProfile() {
        try {
            ((ProfileController) SceneManager.getInstance().setPopup("profile", "/ui/main/buttonBar/profile.fxml")).init();
        } catch (IOException ignored) {
        }
    }

    private void showFilter() {
        try {
            ((FilterController) SceneManager.getInstance().setPopup("filter", "/ui/main/buttonBar/filter.fxml")).setMainController(this);
        } catch (IOException ignored) {
        }
    }

    private void handleAdd() {
        try {
            ((AddController) SceneManager.getInstance().setPopup("add", "/ui/main/buttonBar/add.fxml")).setMainController(this);
        } catch (IOException ignored) {
        }
    }

    public void filterTable(Predicate<Vehicle> predicate) {
        tableView.setItems(FXCollections.observableArrayList(originalList.stream().filter(predicate).collect(Collectors.toList())));
    }

    private void openEditor(Vehicle vehicle, Consumer<Vehicle> onComplete) {
        try {
            EditController controller = SceneManager.getInstance().setPopup(
                    "edit",
                    "/ui/main/editor.fxml"
            );
            controller.setItem(vehicle);
            controller.setReadOnly(vehicle != null && Client.getInstance().getUID() != vehicle.getOwnerId());
            controller.setOnComplete(onComplete);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getFieldValue(Object object, String fieldPath) {
        Objects.requireNonNull(object);
        try {
            for (String fieldName : fieldPath.split("\\.")) {
                Field field = object.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                object = field.get(object);
            }
            return object.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> getFields(Class<?> aClass) {
        List<String> fieldNames = new ArrayList<>();
        if (aClass == null) return fieldNames;

        Arrays.stream(aClass.getDeclaredFields()).forEach(field -> {
            if (field.isAnnotationPresent(Nested.class)) {
                List<String> nestedFields = getFields(field.getType());
                nestedFields.forEach(f -> fieldNames.add(field.getName() + "." + f));
            } else if (!Modifier.isStatic(field.getModifiers())) {
                fieldNames.add(field.getName());
            }
        });

        return fieldNames;
    }

    private void setMinWidthByText(Stage stage) {
        for (var column : tableView.getColumns()) {
            String headerText = column.getText();
            if (headerText != null && !headerText.isEmpty()) {
                double textWidth = computeTextWidth(Font.font(12), headerText);
                column.setMinWidth(textWidth + 16);
                column.setPrefWidth(column.getMinWidth());
            }
        }

        double minWidth = calculateMinWidth();
        stage.setMinWidth(minWidth);
        stage.setWidth(minWidth * 1.5);
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
