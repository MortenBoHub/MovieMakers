package dk.easv.moviemakers.BLL.Util;

import dk.easv.moviemakers.BE.Movies;

import java.util.List;
import java.util.ArrayList;

public class MovieSearcher {

    //Method to search for movies
    public List<Movies> search(List<Movies> searchBase, String query) {
        List<Movies> searchResult = new ArrayList<>();

        for (Movies movie : searchBase) {
            if (compareToMovieTitle(query, movie) || compareToMovieCategory(query, movie) || compareToMovieRating(query, movie)) {
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

    private boolean compareToMovieRating(String query, Movies movie) {
        //Method to compare the searchbar with rating of the movie
        return String.valueOf(movie.getRating()).contains(query);
    }

    // Method to check if a movie contains all the filters
    public boolean checkFilterCategories(ArrayList<String> filters, String filterString) {
        for (String filter : filters) {
            if (!filterString.contains(filter)) return false;  // Return false if any element isn't true.
        }
        return true;
    }

    // Method to filter movies by category
    public List<Movies> filterByCategory(List<Movies> searchBase, ArrayList<String> filters) {
        List<Movies> filteredMovies = new ArrayList<>();

        for (Movies movie : searchBase) {
            if (checkFilterCategories(filters, movie.getCategory())) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }


}
