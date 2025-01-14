package dk.easv.moviemakers.BLL;

import dk.easv.moviemakers.BE.Movies;
import dk.easv.moviemakers.BLL.Util.MovieSearcher;
import dk.easv.moviemakers.DAL.Dao.MovieDAO;
import dk.easv.moviemakers.DAL.IMovieDataAccess;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class MovieManager {
    private final IMovieDataAccess MovieDAO;
    private final MovieSearcher movieSearcher = new MovieSearcher();

    public MovieManager() throws IOException {
        this.MovieDAO = new MovieDAO();

    }

    public List<Movies> getAllMovies() throws Exception {
        return MovieDAO.getAllMovies();
    }

    public List<Movies> searchMovies(String query) throws Exception {
        List<Movies> allMovies = getAllMovies();
        return movieSearcher.search(allMovies, query);
    }

    public List<Movies> filterMoviesByCategory(ArrayList<String> filters) throws Exception {
        List<Movies> allMovies = getAllMovies();
        return movieSearcher.filterByCategory(allMovies, filters);
    }

    public Movies createMovie(Movies newMovie) throws Exception {
        return MovieDAO.createMovie(newMovie);
    }

    public void deleteMovie(Movies selectedMovie) throws Exception {
        MovieDAO.deleteMovie(selectedMovie);
    }

    public void updateMovie(Movies updatedMovie) throws Exception {
        MovieDAO.updateMovie(updatedMovie);
    }


}
