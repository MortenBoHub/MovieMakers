package dk.easv.moviemakers.GUI.Model;

import dk.easv.moviemakers.BE.Movies;
import dk.easv.moviemakers.BLL.MovieManager;
import dk.easv.moviemakers.DAL.Dao.MovieDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class MovieMakerModel {

    private final ObservableList<Movies> moviesToBeViewed;

    private final MovieManager movieManager;

    private final MovieDAO movieDAO = new MovieDAO();


    public MovieMakerModel() throws Exception {
        movieManager = new MovieManager();
        moviesToBeViewed = FXCollections.observableArrayList();
        moviesToBeViewed.addAll(movieManager.getAllMovies());
    }

    public ObservableList<Movies> getObservableList() {
        return moviesToBeViewed;
    }

    public void searchMovies(String query) throws Exception {
        //Calls the manager to search for the movies by the searchbar input
        List<Movies> searchResult = movieManager.searchMovies(query);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(searchResult);
    }

    public void filterMoviesByCategory(ArrayList<String> filters) throws Exception {
        //Calls the manager to filter the movies by their category checkboxes
        List<Movies> filteredMovies = movieManager.filterMoviesByCategory(filters);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(filteredMovies);
    }

    public void createMovie(Movies newMovie) throws Exception {
        //Calls the manager to create the movie
        Movies movieCreated = movieManager.createMovie(newMovie);
        moviesToBeViewed.add(movieCreated);
        System.out.println("Movie added to ObservableList" + movieCreated);

    }

    public void updateMovie(Movies updatedMovie) throws Exception {
        try {
            //Calls the manager to update the movie
            movieManager.updateMovie(updatedMovie);
            moviesToBeViewed.add(updatedMovie);

            int index = moviesToBeViewed.indexOf(updatedMovie);

            if (index != -1) {
                //Setters for the updated movie
                Movies m = moviesToBeViewed.get(index);
                m.setTitle(updatedMovie.getTitle());
                m.setYear(updatedMovie.getYear());
                m.setCategory(updatedMovie.getCategory());
                m.setRating(updatedMovie.getRating());
                m.setPersonalrating(updatedMovie.getPersonalrating());
                m.setFilelink(updatedMovie.getFilelink());
                m.setAddress(updatedMovie.getAddress());
                m.setLastview(updatedMovie.getLastview());

            } else {
                throw new Exception("Movie not found in the ObservableList");
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw new Exception("Failed to update the movie: " + e.getMessage(), e);
        }
    }

    public void deleteMovies(Movies selectedMovie) throws Exception {
        //Delete from the database through the layers
        movieManager.deleteMovie(selectedMovie);

        //Remove from the ObservableList
        moviesToBeViewed.remove(selectedMovie);
    }

    public void refreshMovies() throws Exception {
        //Method to refresh the tableview by calling it from the Manager and going through the layers
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(movieManager.getAllMovies());
    }


}
