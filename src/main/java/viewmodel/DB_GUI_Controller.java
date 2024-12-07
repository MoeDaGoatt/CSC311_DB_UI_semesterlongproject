package viewmodel;

import com.azure.storage.blob.BlobClient;
import dao.DbConnectivityClass;
import dao.StorageUploader;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Person;
import service.MyLogger;

import java.awt.TextArea;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class DB_GUI_Controller implements Initializable {
    private static final String Rname = "^[A-Za-z]{1,50}$";
    private static final String Rdep = "^^[A-Za-z\\\\s]{1,50}$";
    private static final String Rmajors = "^[A-Za-z\\\\s]{1,50}$";
    private static final String Remail = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    @FXML
    private MenuItem printMenuItem;
    @FXML
    private Label statusMessageLabel;
    @FXML
    private Button editBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private MenuItem editItem;
    @FXML
    private MenuItem deleteItem;
    @FXML
    private MenuItem ClearItem;
    @FXML
    private MenuItem CopyItem;
    @FXML
    private Button addBtn;

    @FXML
    private MenuItem exportCSV;
    @FXML
    private MenuItem importCSV;
    @FXML
    private ComboBox<Major> majorComboBox;

    StorageUploader store = new StorageUploader();
    @FXML
    ProgressBar progressBar;
    @FXML
    TextField first_name, last_name, department, major, email, imageURL;
    @FXML
    ImageView img_view;
    @FXML
    MenuBar menuBar;
    @FXML
    MenuItem ChangePic;
    @FXML
    private TableView<Person> tv;
    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_department, tv_major, tv_email;
    private final DbConnectivityClass cnUtil = new DbConnectivityClass();
    private final ObservableList<Person> data = cnUtil.getData();

    @FXML
    private TextArea aboutText;
    @FXML
    private Hyperlink cricketLink;
    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            ChangePic.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
            ChangePic.setOnAction(event -> showImage());

            importCSV.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));
            importCSV.setOnAction(event -> importCSVFile(null));

            exportCSV.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
            exportCSV.setOnAction(event -> exportCSVFile(null));

            tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            tv_department.setCellValueFactory(new PropertyValueFactory<>("department"));
            tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));
            tv_email.setCellValueFactory(new PropertyValueFactory<>("email"));
            tv.setItems(data);

            editBtn.setDisable(true);
            deleteBtn.setDisable(true);
            editItem.setDisable(true);
            deleteItem.setDisable(true);
            ClearItem.setDisable(true);
            CopyItem.setDisable(true);

            tv.setEditable(true);

            tv_fn.setCellFactory(TextFieldTableCell.forTableColumn());
            tv_fn.setOnEditCommit(event -> {
                Person p = event.getRowValue();
                p.setFirstName(event.getNewValue());
                cnUtil.editUser(p.getId(), p);
                displayMessage("Added");
            });

            tv_ln.setCellFactory(TextFieldTableCell.forTableColumn());
            tv_ln.setOnEditCommit(event -> {
                Person p = event.getRowValue();
                p.setFirstName(event.getNewValue());
                cnUtil.editUser(p.getId(), p);
                displayMessage("Added");

            });

            tv_department.setCellFactory(TextFieldTableCell.forTableColumn());
            tv_department.setOnEditCommit(event -> {
                Person p = event.getRowValue();
                p.setFirstName(event.getNewValue());
                cnUtil.editUser(p.getId(),p);
                displayMessage("Added");
            });

            tv_email.setCellFactory(TextFieldTableCell.forTableColumn());
            tv_email.setOnEditCommit(event -> {
                Person p = event.getRowValue();
                p.setFirstName(event.getNewValue());

                    cnUtil.editUser(p.getId(),p);
                    displayMessage("Added");
            });

            tv_major.setCellFactory(column -> new TableCell<>() {
                private final ComboBox<String> comboBox = new ComboBox<>();

                {
                    comboBox.setItems(FXCollections.observableArrayList(
                            Major.Bowler.name(),
                            Major.Batter.name(),
                            Major.Fielder.name()
                    ));
                    comboBox.setOnAction(event -> {
                        Person p = getTableView().getItems().get(getIndex());
                        p.setMajor(comboBox.getValue());
                        cnUtil.editUser(p.getId(), p); // Update database
                        displayMessage("Major updated.");
                    });
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(comboBox);
                        comboBox.setValue(item);
                    }
                }
            });


            tv.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && tv.getSelectionModel().getSelectedItem() == null) {
                    Person newPerson = new Person("", "", "", "", "", "");
                    data.add(newPerson);
                    tv.getSelectionModel().select(newPerson);
                    displayMessage("New row added. Fill in the details.");
                }
            });

            tv.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                editBtn.setDisable(newValue == null);
                deleteBtn.setDisable(newValue == null);
            });
            tv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() {
                @Override
                public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue) {
                    editBtn.setDisable(newValue == null);
                    deleteBtn.setDisable(newValue == null);
                    ClearItem.setDisable(newValue == null);
                    boolean isSelected = newValue != null;
                    editItem.setDisable(!isSelected);
                    deleteItem.setDisable(!isSelected);
                }
            });
            first_name.textProperty().addListener((observable, oldValue, newValue) -> validateClear());
            last_name.textProperty().addListener((observable, oldValue, newValue) -> validateClear());
            department.textProperty().addListener((observable, oldValue, newValue) -> validateClear());
            majorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateClear());
            email.textProperty().addListener((observable, oldValue, newValue) -> validateClear());
            imageURL.textProperty().addListener((observable, oldValue, newValue) -> validateClear());


            addBtn.setDisable(true);

            //comboBox
            majorComboBox.setItems(FXCollections.observableArrayList(Major.values()));
            majorComboBox.getSelectionModel().selectFirst();

            first_name.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
            last_name.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
            department.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
            majorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateForm());
            email.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
            imageURL.textProperty().addListener((observable, oldValue, newValue) -> validateForm());


            addBtn.setOnAction(event -> {
                if (checkAllFields()) {
                    Person p = new Person(
                            first_name.getText(),
                            last_name.getText(),
                            department.getText(),
                            majorComboBox.getValue().toString(),
                            email.getText(),
                            imageURL.getText()
                    );

                    cnUtil.insertUser(p);
                    cnUtil.retrieveId(p);
                    p.setId(cnUtil.retrieveId(p));
                    data.add(p);
                    clearForm();
                    displayMessage("Person added successfully");
                } else {
                    showError();
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private void validateForm() {
        boolean isFormFilled = !first_name.getText().isEmpty() ||
                !last_name.getText().isEmpty() ||
                !department.getText().isEmpty() ||
                majorComboBox.getValue() != null ||
                !email.getText().isEmpty();

        ClearItem.setDisable(!isFormFilled && tv.getSelectionModel().getSelectedItem() == null);
    }

    private void validateClear() {
        boolean isFormFilled = !first_name.getText().isEmpty() ||
                !last_name.getText().isEmpty() ||
                !department.getText().isEmpty() ||
                majorComboBox.getValue() != null ||
                !email.getText().isEmpty();
        addBtn.setDisable(!isFormFilled);
        ClearItem.setDisable(!isFormFilled && tv.getSelectionModel().getSelectedItem() == null);
    }


    private boolean checkAllFields() {
        boolean isFormValid = first_name.getText().matches(Rname) &&
                last_name.getText().matches(Rname) &&
                department.getText().matches(Rdep) &&
                majorComboBox.getValue() != null &&
                email.getText().matches(Remail);

        if (!isFormValid) {
            showError();
        }
        return isFormValid;

    }

    private void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Please try again");
        alert.setContentText("One or more fields have invalid input.Make sure you are using the farmingdale email");

        alert.showAndWait();
    }

    @FXML
    protected void addNewRecord() {
        if (checkAllFields()) {
            Person p = new Person(first_name.getText(), last_name.getText(), department.getText(),
                    majorComboBox.getValue().toString(), email.getText(), imageURL.getText());
            cnUtil.insertUser(p);
            cnUtil.retrieveId(p);
            p.setId(cnUtil.retrieveId(p));
            data.add(p);
            clearForm();
            displayMessage("Person added successfully");
        }
        else {
            displayMessage("PLease check if the fields match the requirements");
        }
    }

    @FXML
    protected void clearForm() {
        first_name.setText("");
        last_name.setText("");
        department.setText("");
        majorComboBox.getSelectionModel().clearSelection();
        email.setText("");
        imageURL.setText("");
    }


    @FXML
    protected void logOut(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").getFile());
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void closeApplication() {
        System.exit(0);
    }


    @FXML
    protected void displayAbout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/about.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 500);
            stage.setTitle("About Cricket");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.alwaysOnTopProperty();
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void displayHelp() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/help.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Help");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root, 700, 700));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void editRecord() {
        Person p = tv.getSelectionModel().getSelectedItem();
        int index = data.indexOf(p);
        Person p2 = new Person(index + 1, first_name.getText(), last_name.getText(), department.getText(),
                majorComboBox.getValue().toString(), email.getText(), imageURL.getText());
        cnUtil.editUser(p.getId(), p2);
        data.remove(p);
        data.add(index, p2);
        tv.getSelectionModel().select(index);
        displayMessage("Updated Successfully!");
    }

    @FXML
    protected void deleteRecord() {
        Person p = tv.getSelectionModel().getSelectedItem();
        int index = data.indexOf(p);
        cnUtil.deleteRecord(p);
        data.remove(index);
        tv.getSelectionModel().select(index);
    }

    @FXML
    protected void showImage() {
        File file = (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if (file != null) {
            img_view.setImage(new Image(file.toURI().toString()));
            Task<Void> uploadTask = createUploadTask(file, progressBar);
            progressBar.progressProperty().bind(uploadTask.progressProperty());
            new Thread(uploadTask).start();
        }
    }

    private Task<Void> createUploadTask(File file, ProgressBar progressBar) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                BlobClient blobClient = store.getContainerClient().getBlobClient(file.getName());
                long fileSize = Files.size(file.toPath());
                long uploadedBytes = 0;

                try (FileInputStream fileInputStream = new FileInputStream(file);
                     OutputStream blobOutputStream = blobClient.getBlockBlobClient().getBlobOutputStream()) {

                    byte[] buffer = new byte[1024 * 1024]; // 1 MB buffer size
                    int bytesRead;

                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        blobOutputStream.write(buffer, 0, bytesRead);
                        uploadedBytes += bytesRead;

                        // Calculate and update progress as a percentage
                        int progress = (int) ((double) uploadedBytes / fileSize * 100);
                        updateProgress(progress, 100);
                    }
                }

                return null;
            }
        };
    }

    @FXML
    protected void addRecord() {
        showSomeone();
    }

    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person p = tv.getSelectionModel().getSelectedItem();
        first_name.setText(p.getFirstName());
        last_name.setText(p.getLastName());
        department.setText(p.getDepartment());
        majorComboBox.setValue(Major.valueOf(p.getMajor()));
        email.setText(p.getEmail());
        imageURL.setText(p.getImageURL());
    }




    public void lightTheme(ActionEvent actionEvent) {
        try {
            Scene scene = menuBar.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/default.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void darkTheme(ActionEvent actionEvent) {
        try {
            Scene scene = menuBar.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/darkTheme.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showSomeone() {
        Dialog<Results> dialog = new Dialog<>();
        dialog.setTitle("New User");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField1 = new TextField("Name");
        TextField textField2 = new TextField("Last Name");
        TextField textField3 = new TextField("Email ");
        ObservableList<Major> options =
                FXCollections.observableArrayList(Major.values());
        ComboBox<Major> comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().selectFirst();
        dialogPane.setContent(new VBox(8, textField1, textField2,textField3, comboBox));
        Platform.runLater(textField1::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Results(textField1.getText(),
                        textField2.getText(), comboBox.getValue());
            }
            return null;
        });
        Optional<Results> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Results results) -> {
            MyLogger.makeLog(
                    results.fname + " " + results.lname + " " + results.major);
        });
    }

    public void importCSVFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fc.showOpenDialog(menuBar.getScene().getWindow());

        if (file != null) {
            try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
                String line;
                int lineNumber = 0;
                boolean hasErrors = false;

                reader.readLine(); // Skip header row
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    String[] data = line.split(",");
                    if (data.length == 7) {
                        try {
                            Person p = new Person(
                                    Integer.parseInt(data[0]),
                                    data[1],
                                    data[2],
                                    data[3],
                                    data[4],
                                    data[5],
                                    data[6]
                            );
                            cnUtil.insertUser(p);
                            this.data.add(p);
                        } catch (NumberFormatException e) {
                            hasErrors = true;
                            System.err.println("Invalid number format on line " + lineNumber + ": " + line);
                        }
                    } else {
                        hasErrors = true;
                        System.err.println("Invalid row format on line " + lineNumber + ": " + line);
                    }
                }

                if (hasErrors) {
                    statusMessageLabel.setText("Import completed with some errors. Check logs for details.");
                } else {
                    statusMessageLabel.setText("Data imported successfully.");
                }
            } catch (IOException e) {
                statusMessageLabel.setText("Error reading the file.");
                e.printStackTrace();
            }
        } else {
            statusMessageLabel.setText("No file selected.");
        }
    }


    public void exportCSVFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fc.showSaveDialog(menuBar.getScene().getWindow());

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Write header row
                writer.append("ID,First Name,Last Name,Department,Major,Email,Image URL\n");

                if (data != null && !data.isEmpty()) {
                    for (Person person : data) {
                        writer.append(String.format("%d,%s,%s,%s,%s,%s,%s\n",
                                person.getId(),
                                person.getFirstName(),
                                person.getLastName(),
                                person.getDepartment(),
                                person.getMajor(),
                                person.getEmail(),
                                person.getImageURL()
                        ));
                    }
                    statusMessageLabel.setText("Data exported successfully.");
                } else {
                    statusMessageLabel.setText("No data available to export.");
                }
            } catch (IOException e) {
                statusMessageLabel.setText("Error writing to the file.");
                e.printStackTrace();
            }
        } else {
            statusMessageLabel.setText("Export cancelled.");
        }
    }




    private static enum Major {Bowler, Batter, Fielder}

    private static class Results {

        String fname;
        String lname;
        Major major;

        public Results(String name, String date, Major venue) {
            this.fname = name;
            this.lname = date;
            this.major = venue;
        }
    }
    private void displayMessage(String message) {
        statusMessageLabel.setText(message);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> statusMessageLabel.setText(""));
        pause.play();
    }


    public void handlePrintAction(ActionEvent actionEvent) {
        PDFGenerator generator = new PDFGenerator();
        String filePath = "StudentReport.pdf";
        StringBuilder content = new StringBuilder();
        content.append("Student Report \n\n");

        Map<String, Long> studentsByMajor = data.stream()
                .collect(Collectors.groupingBy(Person::getMajor, Collectors.counting()));

        studentsByMajor.forEach((major, count) -> {
            content.append(String.format("Major: %s, Number of Students: %d\n", major, count));

        });
        if (studentsByMajor.isEmpty()) {
            displayMessage("No data is available to generate a pdf with");
            return;
        }

        try {
            generator.generatePdf(filePath, content.toString());
            displayMessage("PDF report generated successfully: " + filePath);

            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    displayMessage("PDF generated, but your system does not support automatic opening.");
                }
            } else {
                displayMessage("PDF file not found after generation.");
            }
        } catch (IOException e) {
            displayMessage("Error generating PDF report.");
            e.printStackTrace();
        }
    }

}