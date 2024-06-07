package at.htlleonding.hexcalc;

import at.htlleonding.hexcalc.controller.HexCalcController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hexcalc.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 300, 400);
        scene.getStylesheets().add(getClass().getResource("hexcalc.css").toExternalForm());

        HexCalcController controller = loader.getController();

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                controller.onEnterKeyPressed();
            }
        });

        primaryStage.setTitle("HexCalc");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}