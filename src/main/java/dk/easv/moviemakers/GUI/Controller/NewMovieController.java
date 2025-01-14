package dk.easv.moviemakers.GUI.Controller;

import dk.easv.moviemakers.BE.Movies;
import dk.easv.moviemakers.GUI.Model.MovieMakerModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewMovieController {


    @FXML private TextField titBarN;
    @FXML private TextField yeaBarN;
    @FXML private TextField perBarN;
    @FXML private TextField imrBarN;
    @FXML private TextField imlBarN;
    @FXML private TextField filBarN;
    @FXML private TextField timBarN;
    @FXML private Button filPicN;
    @FXML private Button savBtnN;
    @FXML private Button canBtnN;

    //Category Checkboxes
    @FXML
    private CheckBox comBoxN, draBoxN, horBoxN, actBoxN, fanBoxN, romBoxN, sciBoxN, thrBoxN, wesBoxN, aniBoxN, mysBoxN, criBoxN, musBoxN, advBoxN, docBoxN, hisBoxN, othBoxN, spoBoxN, famBoxN, melBoxN, warBoxN;

    private MovieMakerModel movieMakerModel;

    private MovieController movieController;

    public NewMovieController() {
        try {
            movieMakerModel = new MovieMakerModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMovieController(MovieController movieController) {
        this.movieController = movieController;
    }

    @FXML
    private void initialize() {
        titBarN.setText("");
        yeaBarN.setText("");
        perBarN.setText("");
        imrBarN.setText("");
        imlBarN.setText("");
        filBarN.setText("");
        timBarN.setText("");

        // Initialize category checkboxes if needed
        comBoxN.setSelected(false);
        draBoxN.setSelected(false);
        horBoxN.setSelected(false);
        actBoxN.setSelected(false);
        fanBoxN.setSelected(false);
        romBoxN.setSelected(false);
        sciBoxN.setSelected(false);
        thrBoxN.setSelected(false);
        wesBoxN.setSelected(false);
        aniBoxN.setSelected(false);
        mysBoxN.setSelected(false);
        criBoxN.setSelected(false);
        musBoxN.setSelected(false);
        advBoxN.setSelected(false);
        docBoxN.setSelected(false);
        hisBoxN.setSelected(false);
        othBoxN.setSelected(false);
        spoBoxN.setSelected(false);
        famBoxN.setSelected(false);
        melBoxN.setSelected(false);
        warBoxN.setSelected(false);

        // Set the current timestamp on initialize
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentTimestamp = LocalDateTime.now().format(formatter);
        timBarN.setText(currentTimestamp);
    }

    private void displayError(Throwable t) {
        // Display error message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    @FXML
    public void onCreate() throws Exception {
        //Get data from the textfields
        String title = titBarN.getText();
        int year = Integer.parseInt(yeaBarN.getText());
        float personalrating = Float.parseFloat(perBarN.getText());
        float rating = Float.parseFloat(imrBarN.getText());
        String filelink = imlBarN.getText();
        String lastviewText = timBarN.getText();
        String address = filBarN.getText();

        if (title.isEmpty() || /*personalrating.isEmpty() || rating.isEmpty() ||*/ filelink.isEmpty() || address.isEmpty()) {
            System.out.println("Please fill out all fields");
            return;
        }

        Path selectedFilePath = Path.of(address);
        System.out.println("Selected file path: " + selectedFilePath.toAbsolutePath());

        if (!Files.exists(selectedFilePath)) {
            throw new java.nio.file.NoSuchFileException(selectedFilePath.toString());
        }

        //Check if file is in the resource folder
        Path movieFolderPath = Path.of("src/main/resources/Movies");
        Path destinationPath = movieFolderPath.resolve(selectedFilePath.getFileName());
        System.out.println("Destination path: " + destinationPath.toAbsolutePath());

        if (!selectedFilePath.startsWith(movieFolderPath)) {
            Files.copy(selectedFilePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            address = "Movies/" + selectedFilePath.getFileName().toString();
        }

        StringBuilder category = new StringBuilder();
        if (comBoxN.isSelected()) category.append("Comedy,");
        if (draBoxN.isSelected()) category.append("Drama,");
        if (horBoxN.isSelected()) category.append("Horror,");
        if (actBoxN.isSelected()) category.append("Action,");
        if (fanBoxN.isSelected()) category.append("Fantasy,");
        if (romBoxN.isSelected()) category.append("Romance,");
        if (sciBoxN.isSelected()) category.append("Sci-Fi,");
        if (thrBoxN.isSelected()) category.append("Thriller,");
        if (wesBoxN.isSelected()) category.append("Western,");
        if (aniBoxN.isSelected()) category.append("Animation,");
        if (mysBoxN.isSelected()) category.append("Mystery,");
        if (criBoxN.isSelected()) category.append("Crime,");
        if (musBoxN.isSelected()) category.append("Musical,");
        if (advBoxN.isSelected()) category.append("Adventure,");
        if (docBoxN.isSelected()) category.append("Documentary,");
        if (hisBoxN.isSelected()) category.append("History,");
        if (othBoxN.isSelected()) category.append("Other,");
        if (spoBoxN.isSelected()) category.append("Sport,");
        if (famBoxN.isSelected()) category.append("Family,");
        if (melBoxN.isSelected()) category.append("Melodrama,");
        if (warBoxN.isSelected()) category.append("War,");

        // Remove the trailing comma
        if (category.length() > 0) {
            category.setLength(category.length() - 1);
        }

        //Parse lastview text to a Timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(lastviewText, formatter);
        Timestamp lastview = Timestamp.valueOf(lastviewText);

        //Create a new movie object
        Movies newMovie = new Movies(-1, title, year, category.toString(), rating, personalrating, filelink, lastview, address);

        //Call model to create movie in the DAL
        movieMakerModel.createMovie(newMovie);
        System.out.println("New Movie created: " + newMovie);

        //Refresh
        if (movieController != null) {
            movieController.tableRefresh();
        }

        //Close the window
        Stage stage = (Stage) savBtnN.getScene().getWindow();
        stage.close();

    }


    @FXML
    public void onCancelButtonPressed() {
        //Closes the window
        Stage stage = (Stage) canBtnN.getScene().getWindow();
        stage.close();
    }

    public void onMP4Pressed() {
        //This is to add .mp4 to the address so that the user doesn't have to write it
        String currentText = filBarN.getText();
        if (!currentText.toLowerCase().endsWith(".mp4")) {
            currentText += ".mp4";
        } filBarN.setText(currentText);
    }


    @FXML
    public void onFilePickerPressed() {
        FileChooser filechooser = new FileChooser();
        filechooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4 files", "*.mp4"));

        File selectedFile = filechooser.showOpenDialog(null);

        if (selectedFile != null) {
            filBarN.setText(selectedFile.getAbsolutePath()); //Store the absolute path

            String fileName = selectedFile.getName().toLowerCase();
            if (fileName.endsWith(".mp4")) {
                onMP4Pressed();
            }

        }
    }



}
