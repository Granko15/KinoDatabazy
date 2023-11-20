package entity;

import app.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilmGetter {

    private static final FilmGetter INSTANCE = new FilmGetter();

    public static FilmGetter getInstance() {
        return INSTANCE;
    }

    private FilmGetter() {}

    public List<Film> findAll() throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM movies")) {
            try (ResultSet r = s.executeQuery()) {
                List<Film> elements = new ArrayList<>();
                while (r.next()) {
                    Film f = new Film();

                    f.setId(r.getInt("movie_id"));
                    f.setTitle(r.getString("movie_name"));
                    f.setMinutes(r.getInt("movie_minutes"));
                    f.setRating(r.getInt("movie_avg_rating"));
                    f.setDescription(r.getString("movie_description"));
                    f.setYear(r.getString("movie_year"));

                    elements.add(f);
                }
                return elements;
            }
        }
    }

    public Film getById(int id) throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM movies JOIN movie_categories mc on movies.movie_id = mc.movie_id JOIN categories c ON c.category_id = mc.category_id WHERE movies.movie_id = ?")){
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()){
                Film f = new Film();
                while (r.next()) {
                    f.setId(r.getInt("movie_id"));
                    f.setTitle(r.getString("movie_name"));
                    f.setMinutes(r.getInt("movie_minutes"));
                    f.setRating(r.getInt("movie_avg_rating"));
                    f.setDescription(r.getString("movie_description"));
                    f.setYear(r.getString("movie_year"));


                    f.addCategories(r.getString("category_name"));
                }
                return f;
            }
        }

    }

}
