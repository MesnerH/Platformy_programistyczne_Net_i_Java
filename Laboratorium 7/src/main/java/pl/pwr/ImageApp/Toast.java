package pl.pwr.ImageApp;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Toast {
    public enum ToastType { SUCCESS, ERROR, WARNING }

    public static void show(Stage ownerStage, String message, ToastType type) {
        Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Text text = new Text(message);
        text.setFont(Font.font("Arial", 14));
        text.setFill(Color.WHITE);

        StackPane root = new StackPane(text);
        root.setPadding(new Insets(10, 20, 10, 20));
        root.setStyle(getStyleForType(type));

        Scene scene = new Scene(root);
        scene.fillProperty().set(Color.TRANSPARENT);
        toastStage.setScene(scene);

        toastStage.setOnShown(e -> {
            toastStage.setX(ownerStage.getX() + ownerStage.getWidth() / 2 - toastStage.getWidth() / 2);
            toastStage.setY(ownerStage.getY() + ownerStage.getHeight() - 100);
        });

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(3));
        fadeOut.setOnFinished(e -> toastStage.close());

        fadeIn.setOnFinished(e -> fadeOut.play());
        toastStage.show();
        fadeIn.play();
    }

    private static String getStyleForType(ToastType type) {
        return switch (type) {
            case SUCCESS -> "-fx-background-color: #2e7d32; -fx-background-radius: 20;";
            case ERROR -> "-fx-background-color: #d32f2f; -fx-background-radius: 20;";
            case WARNING -> "-fx-background-color: #ed6c02; -fx-background-radius: 20;";
        };
    }
}