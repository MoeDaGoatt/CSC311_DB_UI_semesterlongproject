package viewmodel;

import dao.DbConnectivityClass;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Node;

import java.io.IOException;

public class MainApplication extends Application {

    private static DbConnectivityClass cnUtil;
    private Stage primaryStage;

    public static void main(String[] args) {
        cnUtil = new DbConnectivityClass();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Image icon = new Image(getClass().getResourceAsStream("/images/DollarClouddatabase.png"));
        this.primaryStage = primaryStage;
        this.primaryStage.setResizable(false);
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("FSC CSC311 _ Database Project");
        showScene1();
    }

    private void showScene1() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/splashscreen.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource( "/css/default.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
            changeScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeScene() {
        try {
            Parent newRoot = FXMLLoader.load(getClass().getResource("/view/login.fxml").toURI().toURL());
            Scene currentScene = primaryStage.getScene();
//            Parent currentRoot = currentScene.getRoot();
            currentScene.getStylesheets().clear();
            currentScene.getStylesheets().add(getClass().getResource( "/css/default.css").toExternalForm());
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), currentScene.getRoot());
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                Scene newScene = new Scene(newRoot, 900, 600);
                newScene.getStylesheets().add(getClass().getResource( "/css/default.css").toExternalForm());
                primaryStage.setScene(newScene);
                primaryStage.show();
            });
            fadeOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void signup(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/signup.fxml"));
            Scene sc = new Scene(root, 900, 600);
            sc.getStylesheets().add(getClass().getResource("/css/signup.css").toExternalForm());
            Stage window = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(sc);
            window.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
