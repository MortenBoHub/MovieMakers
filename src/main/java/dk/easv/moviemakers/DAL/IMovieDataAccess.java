package dk.easv.moviemakers.DAL;

import dk.easv.moviemakers.BE.Movies;

import java.util.List;

public interface IMovieDataAccess {

    List<Movies> getAllMovies() throws Exception;

    Movies createMovie(Movies newMovie) throws Exception;

    void deleteMovie(Movies movie) throws Exception;

    void updateMovie(Movies movie) throws Exception;


}
