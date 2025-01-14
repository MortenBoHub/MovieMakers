package dk.easv.moviemakers.BLL.Util;

import dk.easv.moviemakers.BE.Movies;

import java.util.List;
import java.util.ArrayList;

public class MovieSearcher {

    //Method to search for movies
    public List<Movies> search(List<Movies> searchBase, String query) {
        List<Movies> searchResult = new ArrayList<>();

        for (Movies movie : searchBase) {
            if (compareToMovieTitle(query, movie) || compareToMovieCategory(query, movie)) {
                searchResult.add(movie);
            }
        }
        return searchResult;
    }

    private boolean compareToMovieTitle(String query, Movies movie) {
        //Method to compare the searchbar with title of the movie
        return movie.getTitle().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToMovieCategory(String query, Movies movie) {
        //Method to compare the searchbar with category of the movie
        return movie.getCategory().toLowerCase().contains(query.toLowerCase());
    }

    // Method to filter movies by category
    public List<Movies> filterByCategory(List<Movies> searchBase, String category) {
        List<Movies> filteredMovies = new ArrayList<>();

        for (Movies movie : searchBase) {
            if (movie.getCategory().equalsIgnoreCase(category)) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }

}
