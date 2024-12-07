package viewmodel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;

public class AboutController {

    @FXML
    private Hyperlink cricketLink;

    @FXML
    private Button closeButton;

    @FXML
    public void initialize() {
        cricketLink.setText("Visit ICC Cricket Official Website");
        cricketLink.setOnAction(event -> openCricketWebsite());
    }

    @FXML
    protected void openCricketWebsite() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.icc-cricket.com/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void closeAbout(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

