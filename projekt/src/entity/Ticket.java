package entity;
import app.DBContext;
import com.sun.tools.javac.Main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ticket {

    private Integer id;
    private Integer bookingId;
    private Integer hallSeatId;
    private Integer movieId;
    private String movieName;
    private Integer movieShowing;
    private Integer ticketPrice;
    private String ticketType;

    @Override
    public String toString() {
        return  "id: " + getId() +
                "\nbookingId: " + getBookingId() +
                "\nseat: " + getHallSeatId() +
                "\nFilm: " + getMovieName() +
                "\nPrice: " + getTicketPrice() +
                "\nType: " + getTicketType();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getHallSeatId() {
        return hallSeatId;
    }

    public void setHallSeatId(Integer hallSeatId) {
        this.hallSeatId = hallSeatId;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Integer getMovieShowing() {
        return movieShowing;
    }

    public void setMovieShowing(Integer movieShowing) {
        this.movieShowing = movieShowing;
    }

    public Integer getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Integer ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public void insert() throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO tickets (booking_id, hall_seat_id, movie_id, movie_showing_id, ticket_price, ticket_type) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS) ){
            s.setInt(1, bookingId);
            s.setInt(2, hallSeatId);
            s.setInt(3, movieId);
            s.setInt(4, movieShowing);
            s.setInt(5, ticketPrice);
            s.setString( 6, ticketType);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()){
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void freeTheSeat() throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE hall_seats h SET hall_seat_sold = false FROM tickets t WHERE t.hall_seat_id = h.hall_seat_id", Statement.RETURN_GENERATED_KEYS)) {
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
    public void delete() throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM tickets WHERE ticket_id=?", Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, id);
            s.executeUpdate();

        }
    }

}
