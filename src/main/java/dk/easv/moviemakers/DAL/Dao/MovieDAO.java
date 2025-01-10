package dk.easv.moviemakers.DAL.Dao;

import dk.easv.moviemakers.BE.Movies;
import dk.easv.moviemakers.DAL.DBConnecter;
import dk.easv.moviemakers.DAL.IMovieDataAccess;

import java.sql.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements IMovieDataAccess {

    private final DBConnecter dbConnecter = new DBConnecter();

    public MovieDAO() throws IOException {
    }

    @Override
    public List<Movies> getAllMovies() throws Exception {
        //this method gets all the movies from the database
        ArrayList<Movies> allMovies = new ArrayList<>();

        try (Connection connection = dbConnecter.getConnection()) {
            String sql = "SELECT * FROM Movies";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                //Get the data from the database
                int id = rs.getInt("id");
                String title = rs.getString("title");
                int year = rs.getInt("year");
                String category = rs.getString("category");
                float rating = rs.getFloat("rating");
                float personalrating = rs.getFloat("personalrating");
                //String filelink = rs.getString("filelink");
                //int time = rs.getInt("time");
                Movies movie = new Movies(id, title, year, category, rating, personalrating); //remember to add filelink?
                allMovies.add(movie);
            }
            //Return the list of movies
            return allMovies;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get movies from database", ex);
        }

    }

    @Override
    public Movies createMovie(Movies movie) throws Exception {
        // this method helps import the data from Movies to add to the song table in the sql server
        //REMEMBER TO ADD FILELINK
        String sql = "INSERT INTO dbo.Movies (title, year, category, rating, personalrating) VALUES ( ?, ?, ?, ?, ?)";
        DBConnecter dbConnecter = new DBConnecter();

        try (Connection connection = dbConnecter.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //REMEMBER TO ADD FILELINK
            stmt.setString(1, movie.gettitle());
            stmt.setInt(2, movie.getyear());
            stmt.setString(3, movie.getcategory());
            stmt.setFloat(4, movie.getrating());
            stmt.setFloat(5, movie.getpersonalrating());

            //Run the SQL statement
            stmt.executeUpdate();
            //Get the generated keys
            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;

            //If there is a key, set the id to the key
            if(rs.next()) {
                id = rs.getInt(1);
            }
            //Create movie and send up the layers
            return new Movies(id, movie.gettitle(), movie.getyear(), movie.getcategory(), movie.getrating(), movie.getpersonalrating());

        } catch (SQLException ex) {
            throw new Exception("Could not get movies from database.", ex);
        }
    }

    @Override
    public void updateMovie(Movies movie) throws Exception {
        // this method helps update the data from the movie's tables in the sql server
        //REMEMBER TO ADD FILELINK
        String sql = "UPDATE dbo.Movies SET title = ?, year = ?, category = ?, rating = ?, personalrating = ? WHERE id = ?";
        DBConnecter dbConnecter = new DBConnecter();

        try (Connection connection = dbConnecter.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);

            //REMEMBER TO ADD FILELINK
            stmt.setString(1, movie.gettitle());
            stmt.setInt(2, movie.getyear());
            stmt.setString(3, movie.getcategory());
            stmt.setFloat(4, movie.getrating());
            stmt.setFloat(5, movie.getpersonalrating());
            stmt.setInt(6, movie.getid());

            //Run the SQL statement
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new Exception("Could not get movies from database.", ex);
        }
    }

    @Override
    public void deleteMovie(Movies movie) throws Exception {
        // this method helps delete the data from the movie's tables in the sql server
        String sql = "DELETE FROM dbo.Movies WHERE id = ?";

        try (Connection connection = dbConnecter.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, movie.getid());

            //Run the SQL statement
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new Exception("Could not get movies from database.", ex);
        }
    }



}
