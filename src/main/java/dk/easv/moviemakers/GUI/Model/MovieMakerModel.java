package dk.easv.moviemakers.GUI.Model;

import dk.easv.moviemakers.BE.Movies;
import dk.easv.moviemakers.BLL.MovieManager;
import dk.easv.moviemakers.DAL.Dao.MovieDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
        List<Movies> searchResult = movieManager.searchMovies(query);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(searchResult);
    }

    public void createMovie(Movies newMovie) throws Exception {
        Movies movieCreated = movieManager.createMovie(newMovie);
        moviesToBeViewed.add(movieCreated);
        System.out.println("Movie added to ObservableList" + movieCreated);

    }

    public void updateMovie(Movies updatedMovie) throws Exception {
        try {
            movieManager.updateMovie(updatedMovie);
            moviesToBeViewed.add(updatedMovie);

            int index = moviesToBeViewed.indexOf(updatedMovie);

            if (index != -1) {

                Movies m = moviesToBeViewed.get(index);
                m.settitle(updatedMovie.gettitle());
                m.setyear(updatedMovie.getyear());
                m.setcategory(updatedMovie.getcategory());
                m.setrating(updatedMovie.getrating());
                m.setpersonalrating(updatedMovie.getpersonalrating());
                //REMEMBER FILELINK(?) FOR THE DATABASE
                //TIME TOO
            } else {
                throw new Exception("Movie not found in the ObservableList");
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw new Exception("Failed to update the movie: " + e.getMessage(), e);
        }
    }
}
