package viewmodel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelpController {

    @FXML
    private Button closeHelpButton;

    @FXML
    private VBox helpOptionsContainer;

    @FXML
    public void initialize() {
        String[] topics = {
                "How to add a person?",
                "How to make a PDF?",
                "How to edit a record?",
                "How to change the theme?",
                "Something else?"
        };

        for (String topic : topics) {
            Text option = new Text("• " + topic);
            option.setStyle("-fx-font-size: 18px; -fx-underline: true; -fx-fill: blue;");
            option.setOnMouseClicked(event -> handleHelpOption(topic));
            helpOptionsContainer.getChildren().add(option);
        }
    }

    private void handleHelpOption(String topic) {
        switch (topic) {
            case "How to add a person?":
                showHelpDialog(topic, "To add a person, navigate to the 'Add' button, fill in the details, and press save.");
                break;
            case "How to make a PDF?":
                showHelpDialog(topic, "To create a PDF, go to 'File' > 'Export as PDF' and choose a save location.");
                break;
            case "How to edit a record?":
                showHelpDialog(topic, "To edit a record, select it from the table and press the 'Edit' button.");
                break;
            case "How to change the theme?":
                showHelpDialog(topic, "You can change the theme from the 'Theme' menu. Choose between light and dark modes.");
                break;
            case "Something else?":
                showHelpDialog(topic, "For other queries, please contact support.");
                break;
        }
    }

    private void showHelpDialog(String title, String message) {
        Stage stage = new Stage();
        AnchorPane pane = new AnchorPane();
        javafx.scene.control.Label label = new javafx.scene.control.Label(message);
        Button closeButton = new Button("Close");

        label.setStyle("-fx-font-size: 14px;");
        label.setWrapText(true);
        label.setPrefWidth(300);

        closeButton.setOnAction(e -> stage.close());
        label.setLayoutX(20);
        label.setLayoutY(20);
        closeButton.setLayoutX(130);
        closeButton.setLayoutY(80);

        pane.getChildren().addAll(label, closeButton);
        pane.setPrefSize(400, 200);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(true);
        stage.showAndWait();
    }
    @FXML
    protected void closeHelp(ActionEvent event) {
        Stage stage = (Stage) closeHelpButton.getScene().getWindow();
        stage.close();
    }
}

