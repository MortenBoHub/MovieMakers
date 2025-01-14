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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class MovieController implements Initializable {

    //TODO: Check todos at the bottom


    @FXML public Button plyBtn, newBtn, delBtn, editBtn, cloBtn, linkBtn;
    @FXML public TableView maiTbl;
    @FXML public TextField seaBar;
    @FXML private Label currentLab;
    @FXML private AnchorPane scrMen;

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

    //Category Checkboxes
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
        //Setup the columns in the tableview
        movCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        relCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        ratCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        priCol.setCellValueFactory(new PropertyValueFactory<>("personalrating"));
        lasCol.setCellValueFactory(new PropertyValueFactory<>("lastview"));


        //Set the date format for the Lastview column
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        lasCol.setCellFactory(new Callback<TableColumn<Movies, Timestamp>, TableCell<Movies, Timestamp>>() {
            @Override
            public TableCell<Movies, Timestamp> call(TableColumn<Movies, Timestamp> column) {
                return new TableCell<Movies, Timestamp>(){
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
        //maiTbl.setItems(movieMakerModel.getObservableList());

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


    }

    //Add listeners to the checkboxes
    private void addCategoryCheckBoxListeners() {
        comBox.setOnAction(event -> filterMoviesBySelectedCategories());
        draBox.setOnAction(event -> filterMoviesBySelectedCategories());
        horBox.setOnAction(event -> filterMoviesBySelectedCategories());
        actBox.setOnAction(event -> filterMoviesBySelectedCategories());
        fanBox.setOnAction(event -> filterMoviesBySelectedCategories());
        romBox.setOnAction(event -> filterMoviesBySelectedCategories());
        sciBox.setOnAction(event -> filterMoviesBySelectedCategories());
        thrBox.setOnAction(event -> filterMoviesBySelectedCategories());
        wesBox.setOnAction(event -> filterMoviesBySelectedCategories());
        aniBox.setOnAction(event -> filterMoviesBySelectedCategories());
        mysBox.setOnAction(event -> filterMoviesBySelectedCategories());
        criBox.setOnAction(event -> filterMoviesBySelectedCategories());
        musBox.setOnAction(event -> filterMoviesBySelectedCategories());
        advBox.setOnAction(event -> filterMoviesBySelectedCategories());
        docBox.setOnAction(event -> filterMoviesBySelectedCategories());
        hisBox.setOnAction(event -> filterMoviesBySelectedCategories());
        othBox.setOnAction(event -> filterMoviesBySelectedCategories());
        spoBox.setOnAction(event -> filterMoviesBySelectedCategories());
        famBox.setOnAction(event -> filterMoviesBySelectedCategories());
        melBox.setOnAction(event -> filterMoviesBySelectedCategories());
        warBox.setOnAction(event -> filterMoviesBySelectedCategories());
    }

    public Movies getSelectedMovie() {return selectedMovie;}

    //Method to filter the tableview by categories that have been checkmarked
    private void filterMoviesBySelectedCategories() {
        try {
            StringBuilder selectedCategories = new StringBuilder();
            if (comBox.isSelected()) selectedCategories.append("Comedy,");
            if (draBox.isSelected()) selectedCategories.append("Drama,");
            if (horBox.isSelected()) selectedCategories.append("Horror,");
            if (actBox.isSelected()) selectedCategories.append("Action,");
            if (fanBox.isSelected()) selectedCategories.append("Fantasy,");
            if (romBox.isSelected()) selectedCategories.append("Romance,");
            if (sciBox.isSelected()) selectedCategories.append("Sci-Fi,");
            if (thrBox.isSelected()) selectedCategories.append("Thriller,");
            if (wesBox.isSelected()) selectedCategories.append("Western,");
            if (aniBox.isSelected()) selectedCategories.append("Animation,");
            if (mysBox.isSelected()) selectedCategories.append("Mystery,");
            if (criBox.isSelected()) selectedCategories.append("Crime,");
            if (musBox.isSelected()) selectedCategories.append("Musical,");
            if (advBox.isSelected()) selectedCategories.append("Adventure,");
            if (docBox.isSelected()) selectedCategories.append("Documentary,");
            if (hisBox.isSelected()) selectedCategories.append("History,");
            if (othBox.isSelected()) selectedCategories.append("Other,");
            if (spoBox.isSelected()) selectedCategories.append("Sport,");
            if (famBox.isSelected()) selectedCategories.append("Family,");
            if (melBox.isSelected()) selectedCategories.append("Melodrama,");
            if (warBox.isSelected()) selectedCategories.append("War,");

            if (selectedCategories.length() > 0) {
                selectedCategories.setLength(selectedCategories.length() - 1);
                movieMakerModel.filterMoviesByCategory(selectedCategories.toString());
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

    private void onEditMovieButtonPressed() throws IOException {
        //Method to open the edit movie dialogue window
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/EditMovieView.fxml"));
        Parent root = fxmlLoader.load();

        //Get the controller and set the controller
        //EditMovieController editMovieController = fxmlLoader.getController();
        //editMovieController½.setMovieController(this);

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