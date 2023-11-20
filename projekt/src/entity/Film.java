package entity;
import app.DBContext;
import com.sun.tools.javac.Main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Film {

    private Integer id;
    private String title;
    private Integer minutes;
    private Integer rating;
    private String description;
    public List<String> categories;
    private String year;

    public Film(){
        this.categories = new ArrayList<>();
    }

    @Override
    public String toString() {
        return  "id: " + getId() +
                "\nTitle: " + getTitle() +
                "\nGenres: " + getCategories().toString() +
                "\nDuration in minutes: " + getMinutes() +
                "\nrating: " + getRating() +
                "\ndescription:\n" + getDescription() +
                "\nyear: " + getYear();
    }

    public List<String> getCategories() { return this.categories; }

    public void addCategories(String cat) { this.categories.add(cat); }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void insert() throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO movies (movie_name, movie_minutes, movie_avg_rating, movie_description, movie_year) VALUES (?,?,?,?,cast(? as date))", Statement.RETURN_GENERATED_KEYS) ){
            s.setString(1, title);
            s.setInt(2, minutes);
            s.setInt(3, rating);
            s.setString(4, description);
            s.setString(5, year);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()){
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException{
        if(id == null) {
            throw new IllegalStateException("id not set");
        }
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE movies SET movie_name = ? WHERE movie_id = ?")) {
            s.setString(1, title);
            s.setInt(2, id);
            s.executeUpdate();
        }

    }

    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM movies WHERE movie_id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

}
