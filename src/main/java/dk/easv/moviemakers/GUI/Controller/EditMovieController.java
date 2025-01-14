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
import java.sql.Timestamp;

public class EditMovieController {

    @FXML
    private TextField titBarE;
    @FXML
    private TextField yeaBarE;
    @FXML
    private TextField perBarE;
    @FXML
    private TextField imrBarE;
    @FXML
    private TextField imlBarE;
    @FXML
    private TextField filBarE;
    @FXML
    private TextField timBarE;

    @FXML
    private Button savBtnE;
    @FXML
    private Button canBtnE;
    @FXML
    private Button filPicE;

    //Category Checkboxes
    @FXML
    private CheckBox comBoxE, draBoxE, horBoxE, actBoxE, fanBoxE, romBoxE, sciBoxE, thrBoxE, wesBoxE, aniBoxE, mysBoxE, criBoxE, musBoxE, advBoxE, docBoxE, hisBoxE, othBoxE, spoBoxE, famBoxE, melBoxE, warBoxE;


    @FXML
    private MovieMakerModel movieMakerModel;

    private MovieController movieController;

    public EditMovieController() {
        try {
            movieMakerModel = new MovieMakerModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    public void setMovieController(MovieController movieController) {
        this.movieController = movieController;

        //Initialize fields with the selected movie's details
        if (movieController.getSelectedMovie() != null) {
            Movies selectedMovie = movieController.getSelectedMovie();
            titBarE.setText(selectedMovie.getTitle());
            yeaBarE.setText(String.valueOf(selectedMovie.getYear()));
            perBarE.setText(String.valueOf(selectedMovie.getPersonalrating()));
            imrBarE.setText(String.valueOf(selectedMovie.getRating()));
            filBarE.setText(selectedMovie.getAddress());
            imlBarE.setText(selectedMovie.getFilelink());
            timBarE.setText(String.valueOf(selectedMovie.getLastview()));
            String category = selectedMovie.getCategory();
            comBoxE.setSelected(category.contains("Comedy"));
            draBoxE.setSelected(category.contains("Drama"));
            horBoxE.setSelected(category.contains("Horror"));
            actBoxE.setSelected(category.contains("Action"));
            fanBoxE.setSelected(category.contains("Fantasy"));
            romBoxE.setSelected(category.contains("Romance"));
            sciBoxE.setSelected(category.contains("Sci-Fi"));
            thrBoxE.setSelected(category.contains("Thriller"));
            wesBoxE.setSelected(category.contains("Western"));
            aniBoxE.setSelected(category.contains("Animation"));
            mysBoxE.setSelected(category.contains("Mystery"));
            criBoxE.setSelected(category.contains("Crime"));
            musBoxE.setSelected(category.contains("Music"));
            advBoxE.setSelected(category.contains("Adventure"));
            docBoxE.setSelected(category.contains("Documentary"));
            hisBoxE.setSelected(category.contains("History"));
            othBoxE.setSelected(category.contains("Other"));
            spoBoxE.setSelected(category.contains("Sport"));
            famBoxE.setSelected(category.contains("Family"));
            melBoxE.setSelected(category.contains("Melodrama"));
            warBoxE.setSelected(category.contains("War"));
        }
    }

    @FXML
    private void initialize() {

    }

    private void displayError(Throwable t) {
        //Display error message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    @FXML
    public void onEdit() throws Exception {
        Movies selectedMovie = movieController.getSelectedMovie();

        //Gets all info into the edit dialogue, updates info to list and database when saving
        if (selectedMovie != null) {
            StringBuilder category = new StringBuilder();
            if (comBoxE.isSelected()) category.append("Comedy,");
            if (draBoxE.isSelected()) category.append("Drama,");
            if (horBoxE.isSelected()) category.append("Horror,");
            if (actBoxE.isSelected()) category.append("Action,");
            if (fanBoxE.isSelected()) category.append("Fantasy,");
            if (romBoxE.isSelected()) category.append("Romance,");
            if (sciBoxE.isSelected()) category.append("Sci-Fi,");
            if (thrBoxE.isSelected()) category.append("Thriller,");
            if (wesBoxE.isSelected()) category.append("Western,");
            if (aniBoxE.isSelected()) category.append("Animation,");
            if (mysBoxE.isSelected()) category.append("Mystery,");
            if (criBoxE.isSelected()) category.append("Crime,");
            if (musBoxE.isSelected()) category.append("Musical,");
            if (advBoxE.isSelected()) category.append("Adventure,");
            if (docBoxE.isSelected()) category.append("Documentary,");
            if (hisBoxE.isSelected()) category.append("History,");
            if (othBoxE.isSelected()) category.append("Other,");
            if (spoBoxE.isSelected()) category.append("Sport,");
            if (famBoxE.isSelected()) category.append("Family,");
            if (melBoxE.isSelected()) category.append("Melodrama,");
            if (warBoxE.isSelected()) category.append("War,");

            // Remove the trailing comma
            if (!category.isEmpty()) {
                category.setLength(category.length() - 1);
            }

            selectedMovie.setCategory(category.toString());
            selectedMovie.setTitle(titBarE.getText());
            selectedMovie.setYear(Integer.parseInt(yeaBarE.getText()));
            selectedMovie.setPersonalrating(Float.parseFloat(perBarE.getText()));
            selectedMovie.setRating(Float.parseFloat(imrBarE.getText()));
            selectedMovie.setFilelink(imlBarE.getText());
            selectedMovie.setAddress(filBarE.getText());
            selectedMovie.setLastview(Timestamp.valueOf(timBarE.getText()));

            movieMakerModel.updateMovie(selectedMovie);

            if (movieController != null) {
                movieController.tableRefresh();
            }
        }

        Stage stage = (Stage) savBtnE.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onCancelButtonPressed() {
        //Closes the window
        Stage stage = (Stage) canBtnE.getScene().getWindow();
        stage.close();
    }


    public void onMP4Pressed() {
        //Adds mp4 to the address path so the user doesn't have to write it
        String currentText = filBarE.getText();
        if (!currentText.toLowerCase().endsWith(".mp4")) {
            currentText += ".mp4";
        }
        filBarE.setText(currentText);
    }

    @FXML
    public void onFilePickerPressed() {
        FileChooser filechooser = new FileChooser();
        filechooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4 files", "*.mp4"));

        File selectedFile = filechooser.showOpenDialog(null);

        if (selectedFile != null) {
            filBarE.setText(selectedFile.getAbsolutePath()); //Store the absolute path

            String fileName = selectedFile.getName().toLowerCase();
            if (fileName.endsWith(".mp4")) {
                onMP4Pressed();
            }

        }
    }

}
