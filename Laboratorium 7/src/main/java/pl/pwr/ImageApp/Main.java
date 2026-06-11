package pl.pwr.ImageApp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainViewController controller = new MainViewController(primaryStage);
        Scene scene = new Scene(controller.getMainPane(), 1050, 650);

        primaryStage.setTitle("Platformy Programistyczne .NET i Java - lab6");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(550);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}