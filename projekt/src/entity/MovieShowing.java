package entity;
import app.DBContext;
import com.sun.tools.javac.Main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MovieShowing {

    private Integer id;
    private Integer movieId;
    private String MovieName;
    private Integer cinemaHallId;
    private String showingDate;
    private String showingTime;
    private boolean showingActive;

    @Override
    public String toString() {
        return  "id: " + getId() +
                "\nMovie ID: " + getMovieId() +
                "\nMovie name: " + getMovieName() +
                "\nCinema Hall: " + getCinemaHallId() +
                "\nDate: " + getShowingDate() +
                "\nTime: " + getShowingTime() +
                "\nActive: " + getShowingActive();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return MovieName;
    }

    public void setMovieName(String movieName) {
        MovieName = movieName;
    }

    public Integer getCinemaHallId() {
        return cinemaHallId;
    }

    public void setCinemaHallId(Integer cinemaHall) {
        this.cinemaHallId = cinemaHall;
    }

    public String getShowingDate() {
        return showingDate;
    }

    public void setShowingDate(String showingDate) {
        this.showingDate = showingDate;
    }

    public String getShowingTime() {
        return showingTime;
    }

    public void setShowingTime(String showingTime) {
        this.showingTime = showingTime;
    }

    public boolean isShowingActive() {
        return showingActive;
    }

    public void setShowingActive(boolean showingActive) {
        this.showingActive = showingActive;
    }

    public boolean getShowingActive() {
        return this.showingActive;
    }

    public void insert() throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO movie_showings (cinema_hall_id, movie_id, movie_showing_time_from, movie_showing_date) VALUES (?,?,cast(? as time),cast(? as date))", Statement.RETURN_GENERATED_KEYS) ){
            s.setInt(1, cinemaHallId);
            s.setInt(2, movieId);
            s.setString(3, showingTime);
            s.setString( 4, showingDate);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()){
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void delete() throws SQLException{
        if (id == null) {
            throw new IllegalStateException("id not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE movie_showings SET movie_showings_active = false WHERE movie_showing_id = ?")) {

            s.setInt(1, id);

            s.executeUpdate();
        }

    }

}
