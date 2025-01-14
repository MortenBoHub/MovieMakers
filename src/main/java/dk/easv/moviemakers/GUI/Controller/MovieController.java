package dk.easv.moviemakers.GUI.Controller;

import dk.easv.moviemakers.BE.Movies;
import dk.easv.moviemakers.GUI.Model.MovieMakerModel;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class MovieController implements Initializable {

    @FXML
    public Button plyBtn, newBtn, delBtn, editBtn, cloBtn, linkBtn;
    @FXML
    public TableView maiTbl;
    @FXML
    public TextField seaBar;
    @FXML
    private Label currentLab;
    @FXML
    private AnchorPane scrMen;

    //Tableview Columns
    @FXML
    private TableColumn<Movies, String> movCol;
    @FXML
    private TableColumn<Movies, Integer> relCol;
    @FXML
    private TableColumn<Movies, Float> ratCol;
    @FXML
    private TableColumn<Movies, Float> priCol;
    @FXML
    private TableColumn<Movies, Timestamp> lasCol;

    //Category CheckBoxes
    @FXML
    private CheckBox comBox, draBox, horBox, actBox, fanBox, romBox, sciBox, thrBox, wesBox, aniBox, mysBox, criBox, musBox, advBox, docBox, hisBox, othBox, spoBox, famBox, melBox, warBox;


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
        //Set up the columns in the tableview
        movCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        relCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        ratCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        priCol.setCellValueFactory(new PropertyValueFactory<>("personalrating"));
        lasCol.setCellValueFactory(new PropertyValueFactory<>("lastview"));

        comBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        draBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        horBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        actBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        fanBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        romBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        sciBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        thrBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        wesBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        aniBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        mysBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        criBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        musBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        advBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        docBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        hisBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        othBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        spoBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        famBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        melBox.setOnAction(_ -> filterMoviesBySelectedCategories());
        warBox.setOnAction(_ -> filterMoviesBySelectedCategories());

        //Set the date format for the Lastview column
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        lasCol.setCellFactory(new Callback<TableColumn<Movies, Timestamp>, TableCell<Movies, Timestamp>>() {
            @Override
            public TableCell<Movies, Timestamp> call(TableColumn<Movies, Timestamp> column) {
                return new TableCell<Movies, Timestamp>() {
                    @Override
                    protected void updateItem(Timestamp item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            LocalDateTime today = LocalDateTime.now();
                            LocalDateTime lastView = item.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            long daysBetween = ChronoUnit.DAYS.between(lastView, today);

                            if (daysBetween > 1) {
                                if (daysBetween > 365) {
                                    long years = daysBetween / 365;
                                    long days = daysBetween % 365;
                                    setText(years + "Y " + days + "D Ago");
                                } else {
                                    long hours = ChronoUnit.HOURS.between(lastView, today) % 24;
                                    setText(daysBetween + "D " + hours + "H Ago");
                                }
                            } else {
                                long hours = ChronoUnit.HOURS.between(lastView, today);
                                long minutes = ChronoUnit.MINUTES.between(lastView, today) % 60;
                                setText(hours + "H " + minutes + "M Ago");
                            }
                        }
                    }
                };
            }
        });

        //Setup columns in the tableview


        //Connect tableview and ObservableList
        ObservableList<Movies> moviesList = movieMakerModel.getObservableList();
        if (moviesList != null && !moviesList.isEmpty()) {
            maiTbl.setItems(moviesList);
        } else {
            System.out.println("ObservableList is empty or null.");
        }

        //Table view listener setup to show selected movie
        maiTbl.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                selectedMovie = (Movies) newValue;
                System.out.println("Selected Movie: " + selectedMovie.getTitle());
                currentLab.setText("Selected: " + selectedMovie.getTitle());
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

        Platform.runLater(() -> badMovieReminder());

    }

    //Movie Delete Reminder for "Bad Movies"
    public void badMovieReminder() {
        ObservableList<Movies> moviesList = movieMakerModel.getObservableList();
        if (moviesList != null && !moviesList.isEmpty()) {

            for (Movies movie : moviesList) {
                LocalDateTime today = LocalDateTime.now();
                LocalDateTime lastView = movie.getLastview().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                long yearsBetween = ChronoUnit.YEARS.between(lastView, today);
                if (yearsBetween >= 2 && movie.getPersonalrating() < 6) {
                    JOptionPane.showMessageDialog(null, "You haven't watched " + movie.getTitle() + " in over two years and your personal rating of it is below a 6. Consider deleting it.", "Bad Movie Reminder", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }


    public Movies getSelectedMovie() {
        return selectedMovie;
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

    //Method to filter the tableview by categories that have been checkmarked
    private void filterMoviesBySelectedCategories() {
        try {
            ArrayList<String> filters = new ArrayList<>();
            if (comBox.isSelected()) filters.add("Comedy");
            if (draBox.isSelected()) filters.add("Drama");
            if (horBox.isSelected()) filters.add("Horror");
            if (actBox.isSelected()) filters.add("Action");
            if (fanBox.isSelected()) filters.add("Fantasy");
            if (romBox.isSelected()) filters.add("Romance");
            if (sciBox.isSelected()) filters.add("Sci-Fi");
            if (thrBox.isSelected()) filters.add("Thriller");
            if (wesBox.isSelected()) filters.add("Western");
            if (aniBox.isSelected()) filters.add("Animation");
            if (mysBox.isSelected()) filters.add("Mystery");
            if (criBox.isSelected()) filters.add("Crime");
            if (musBox.isSelected()) filters.add("Musical");
            if (advBox.isSelected()) filters.add("Adventure");
            if (docBox.isSelected()) filters.add("Documentary");
            if (hisBox.isSelected()) filters.add("History");
            if (othBox.isSelected()) filters.add("Other");
            if (spoBox.isSelected()) filters.add("Sport");
            if (famBox.isSelected()) filters.add("Family");
            if (melBox.isSelected()) filters.add("Melodrama");
            if (warBox.isSelected()) filters.add("War");


            if (!filters.isEmpty()) {
                movieMakerModel.filterMoviesByCategory(filters);
            } else {
                movieMakerModel.refreshMovies(); // Show all movies if no category is selected
            }
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }

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

    @FXML
    private void onEditMovieButtonPressed() throws IOException {
        //Method to open the edit movie dialogue window
        if (maiTbl.getSelectionModel().getSelectedItem() != null) {
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
        } else {
            JOptionPane.showMessageDialog(null, "No movie selected. Please select a movie to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }


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


    @FXML
    private void onPlayButtonPressed() {
        //Method to play the selected movie
        Movies selectedMovie = (Movies) maiTbl.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            File file = new File("src/main/resources/" + selectedMovie.getAddress());
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(file);
                    selectedMovie.setLastview(Timestamp.valueOf(LocalDateTime.now()));
                    movieMakerModel.updateMovie(selectedMovie);
                    tableRefresh();
                } catch (IOException e) {
                    displayError(e);
                    e.printStackTrace();
                } catch (Exception e) {
                    displayError(e);
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No movie selected. Please select a movie to play.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    public void onCloseButtonPressed() {
        //Closes the window
        Stage stage = (Stage) cloBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onLinkButtonPressed() {
        Movies selectedMovie = (Movies) maiTbl.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(selectedMovie.getFilelink()));
                } catch (Exception e) {
                    displayError(e);
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No movie selected. Please select a movie to open the link.", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }
}