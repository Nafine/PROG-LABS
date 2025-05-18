package se.ifmo.client.ui.main;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import se.ifmo.client.Client;
import se.ifmo.client.ui.locale.LocaleManager;
import se.ifmo.client.ui.scene.SceneManager;
import se.ifmo.shared.model.Vehicle;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CollectionVisualizer {

    private static final int MAX_ITEMS = 20;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private final Random random = new Random();
    private Stage stage;

    public static void visualize(Collection<Vehicle> items, MainController mainController) {
        Platform.runLater(() -> {
            (new CollectionVisualizer()).show(items, mainController);
        });
    }

    public void show(Collection<Vehicle> items, MainController mainController) {
        mainController.pauseRefresh();
        List<Vehicle> itemsToShow = items.stream()
                .limit(MAX_ITEMS)
                .collect(Collectors.toList());

        stage = new Stage();
        Pane root = createVisualizationPane(itemsToShow);

        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.setTitle(LocaleManager.getString("visualize.title"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.setOnCloseRequest(event -> {mainController.resumeRefresh();});
    }

    private Pane createVisualizationPane(List<Vehicle> items) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #222;");

        items.forEach(item -> {
            Node node = createItemNode(item);
            positionRandomly(node);
            animateAppearance(node);
            makeInteractive(node, item);
            pane.getChildren().add(node);
        });

        return pane;
    }

    private Node createItemNode(Vehicle item) {
        // Создаем круг с подписью (можно заменить на любую другую фигуру)
        Circle circle = new Circle(30);

        Color fillColor = Color.rgb(
                random.nextInt(200) + 55,
                random.nextInt(200) + 55,
                random.nextInt(200) + 55
        );

        circle.setFill(fillColor);
        circle.setStroke(Color.WHITE);

        Label label = new Label(String.valueOf(item.getId()));
        label.setTextFill(new Color(
                1.0 - fillColor.getRed(),
                1.0 - fillColor.getGreen(),
                1.0 - fillColor.getBlue(),
                1.0
        ));
        label.setLayoutX(-20);
        label.setLayoutY(-10);

        return new Group(circle, label);
    }

    private void positionRandomly(Node node) {
        node.setLayoutX(random.nextInt(WIDTH - 100) + 50);
        node.setLayoutY(random.nextInt(HEIGHT - 100) + 50);
        node.setOpacity(0);
        node.setScaleX(0.5);
        node.setScaleY(0.5);
    }

    private void animateAppearance(Node node) {
        ParallelTransition animation = new ParallelTransition(
                new FadeTransition(Duration.millis(800), node),
                new ScaleTransition(Duration.millis(1000), node)
        );

        animation.getChildren().forEach(anim -> {
            if (anim instanceof FadeTransition) {
                ((FadeTransition) anim).setToValue(1);
            } else if (anim instanceof ScaleTransition) {
                ((ScaleTransition) anim).setToX(1);
                ((ScaleTransition) anim).setToY(1);
            }
        });

        animation.setDelay(Duration.millis(random.nextInt(500)));
        animation.play();
    }

    private void makeInteractive(Node node, Vehicle item) {
        node.setOnMouseEntered(e -> {
            node.setEffect(new javafx.scene.effect.Glow(0.5));
            ((Circle) ((Group) node).getChildren().getFirst()).setStroke(Color.GOLD);
        });

        node.setOnMouseExited(e -> {
            node.setEffect(null);
            ((Circle) ((Group) node).getChildren().getFirst()).setStroke(Color.WHITE);
        });

        node.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) { // Двойной клик
                showDetailWindow(item);
                animateClick(node);// Эффект при клике
            }
        });
    }

    private void animateClick(Node node) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), node);
        scale.setToX(0.8);
        scale.setToY(0.8);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    private void showDetailWindow(Vehicle selectedItem) {
        openEditor(selectedItem, item -> {
            Client.getInstance().updateByID(item.getId(), item);
        });
    }

    private void openEditor(Vehicle vehicle, Consumer<Vehicle> onComplete) {
        try {
            EditController controller = SceneManager.getInstance().setPopup(
                    "edit",
                    "/ui/main/editor.fxml", stage
            );
            controller.setItem(vehicle);
            controller.setReadOnly(vehicle != null && Client.getInstance().getUID() != vehicle.getOwnerId());
            controller.setOnComplete(onComplete);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}