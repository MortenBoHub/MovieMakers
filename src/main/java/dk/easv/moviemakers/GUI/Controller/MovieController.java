package dk.easv.moviemakers.GUI.Controller;

import dk.easv.moviemakers.BE.Movies;
import dk.easv.moviemakers.GUI.Model.MovieMakerModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MovieController implements Initializable {

    //TODO: Check todos at the bottom

    @FXML
    private Button plyBtn;
    private Button newBtn;
    private Button delBtn;
    private Button editBtn;
    private Button cloBtn;
    private Button linkBtn;
    private TableView maiTbl;
    private TextField seaBar;
    private Label currentLab;
    private AnchorPane scrMen;

    //Tableview Columns
    @FXML
    private TableColumn<Movies, String> movCol;
    private TableColumn<Movies, Integer> relCol;
    private TableColumn<Movies, Float> ratCol;
    private TableColumn<Movies, Float> priCol;
    private TableColumn<Movies, OffsetDateTime> lasCol;

    //Category Checkboxes
    @FXML
    private CheckBox comBox;
    private CheckBox draBox;
    private CheckBox horBox;
    private CheckBox actBox;
    private CheckBox fanBox;
    private CheckBox romBox;
    private CheckBox sciBox;
    private CheckBox thrBox;
    private CheckBox wesBox;
    private CheckBox aniBox;
    private CheckBox mysBox;
    private CheckBox criBox;
    private CheckBox musBox;
    private CheckBox advBox;
    private CheckBox docBox;
    private CheckBox hisBox;
    private CheckBox othBox;
    private CheckBox spoBox;
    private CheckBox famBox;
    private CheckBox melBox;
    private CheckBox warBox;

    private MovieMakerModel movieMakerModel;

    private Movies selectedMovie;

    public MovieController() {
        try {
            movieMakerModel = new MovieMakerModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

    private void displayError(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Setup the columns in the tableview
        movCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        relCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        ratCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        priCol.setCellValueFactory(new PropertyValueFactory<>("personalrating"));
        lasCol.setCellValueFactory(new PropertyValueFactory<>("lastview"));
        //IMPLEMENT FILELINK

        //Set the date format for the Lastview column
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        lasCol.setCellFactory(new Callback<TableColumn<Movies, OffsetDateTime>, TableCell<Movies, OffsetDateTime>>() {
            @Override
            public TableCell<Movies, OffsetDateTime> call(TableColumn<Movies, OffsetDateTime> column) {
                return new TableCell<Movies, OffsetDateTime>() {
                    @Override
                    protected void updateItem(OffsetDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.format(formatter));
                        }
                    }
                };
            }
        });

        //Connect tableview and ObservableList
        maiTbl.setItems(movieMakerModel.getObservableList());

        //Table view listener setup to show selected movie
        maiTbl.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                selectedMovie = (Movies) newValue;
                System.out.println("Selected Movie: " + selectedMovie.gettitle());
                currentLab.setText("Selected: " + selectedMovie.gettitle());
            }
        });

        //Searchbar listener setup
        seaBar.textProperty().addListener((_, _, newValue) -> {
            try {
                movieMakerModel.searchMovies(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });
    }

    public Movies getSelectedMovie() {return selectedMovie;}

    @FXML
    private void onNewMovieButtonPressed() throws IOException {
        //Method to open the new movie dialogue window
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/NewMovieView.fxml"));
        Parent root = fxmlLoader.load();

        //Get the controller and set the controller
        NewMovieController newMovieController = fxmlLoader.getController();
        newMovieController.setMovieController(this);

        Stage stage = new Stage();
        stage.setTitle("New Movie");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void onEditMovieButtonPressed() throws IOException {
        //Method to open the edit movie dialogue window
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/EditMovieView.fxml"));
        Parent root = fxmlLoader.load();

        //Get the controller and set the controller
        EditMovieController editMovieController = fxmlLoader.getController();
        editMovieController.setMovieController(this);

        Stage stage = new Stage();
        stage.setTitle("Edit Movie");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void onDeleteMovieButtonPressed() throws Exception {
        //Show confirmation dialogue
        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the movie?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            Movies selectedMovie = (Movies) maiTbl.getSelectionModel().getSelectedItem();

            if (selectedMovie != null) {
                movieMakerModel.deleteMovies(selectedMovie);
                System.out.println("Movie deleted succesfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No movie selected. Please select a movie to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public void tableRefresh() {
        System.out.println("Table Refreshed");
        try {
            movieMakerModel.refreshMovies();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
        ObservableList<Movies> currentMovies = movieMakerModel.getObservableList();
        System.out.println("Number of movies in ObservableList: " + currentMovies.size());
    }

    //TODO: Implement the play button pressed method, idk what it should do exactly yet
    //TODO: Also find a way to implement the ability to open a link to the movie in the browser
    //TODO: Also also find a way to implement the ability to filter the movies by Categories


}