package entity;

import app.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieShowingGetter {

    private static final MovieShowingGetter INSTANCE = new MovieShowingGetter();

    public static MovieShowingGetter getInstance() {return INSTANCE;}

    private MovieShowingGetter(){}

    public List<MovieShowing> getShowingsOfFilm(int filmId) throws SQLException{
        List<MovieShowing> showings = new ArrayList<>();
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM movie_showings JOIN movies m on m.movie_id = movie_showings.movie_id WHERE m.movie_id=? AND movie_showings_active = true")){
            s.setInt(1, filmId);
            try (ResultSet r = s.executeQuery()){
                while (r.next()) {
                    MovieShowing m = new MovieShowing();
                    m.setId(r.getInt("movie_showing_id"));
                    m.setMovieId(filmId);
                    m.setMovieName(r.getString("movie_name"));
                    m.setCinemaHallId(r.getInt("cinema_hall_id"));
                    m.setShowingDate(r.getString("movie_showing_date"));
                    m.setShowingTime(r.getString("movie_showing_time_from"));
                    m.setShowingActive(r.getBoolean("movie_showings_active"));
                    showings.add(m);
                }
                return showings;
            }

        }
    }

    public MovieShowing getById(int id) throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM movie_showings JOIN movies m on m.movie_id = movie_showings.movie_id WHERE movie_showing_id = ?")) {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()){
                if(r.next()){
                    MovieShowing m = new MovieShowing();
                    m.setId(id);
                    m.setMovieId(r.getInt("movie_id"));
                    m.setMovieName(r.getString("movie_name"));
                    m.setCinemaHallId(r.getInt("cinema_hall_id"));
                    m.setShowingDate(r.getString("movie_showing_date"));
                    m.setShowingTime(r.getString("movie_showing_time_from"));
                    m.setShowingActive(r.getBoolean("movie_showings_active"));

                    if(r.next()) {
                        throw new RuntimeException("More than one row was returned");
                    }

                    return m;
                }
            }
            return null;
        }
    }

    public Boolean checkFreeSeats(int numberOfTickets, int showingId) throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT count(hall_seat_id) as free FROM hall_seats JOIN cinema_halls ch on hall_seats.cinema_hall_id = ch.cinema_hall_id JOIN movie_showings ms on ch.cinema_hall_id = ms.cinema_hall_id WHERE hall_seat_sold = false AND movie_showing_id = ?")) {
           s.setInt(1, showingId);
            try (ResultSet r = s.executeQuery()){
                if(r.next()){
                    int freeSeats = r.getInt("free");
                    System.out.println(freeSeats);
                    System.out.println(numberOfTickets);
                    return freeSeats >= numberOfTickets;
                }
            }
            return null;
        }
    }


}
