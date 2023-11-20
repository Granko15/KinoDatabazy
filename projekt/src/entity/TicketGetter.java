package entity;

import app.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketGetter {

    private static final TicketGetter INSTANCE = new TicketGetter();

    public static TicketGetter getInstance() {return INSTANCE;}

    private TicketGetter(){}

    public List<Ticket> getTicketsByBooking(int bookingId) throws SQLException{
        List<Ticket> t = new ArrayList<>();
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM tickets JOIN bookings b on tickets.booking_id = b.booking_id WHERE b.booking_id = ?")){
            s.setInt(1, bookingId);
            try (ResultSet r = s.executeQuery()){
                while (r.next()) {
                    Ticket tic = new Ticket();
                    tic.setId(r.getInt("ticket_id"));
                    tic.setBookingId(bookingId);
                    tic.setHallSeatId(r.getInt("hall_seat_id"));
                    tic.setMovieId(r.getInt("movie_id"));
                    tic.setMovieShowing(r.getInt("movie_showing_id"));
                    tic.setTicketPrice(r.getInt("ticket_price"));
                    tic.setTicketType(r.getString("ticket_type"));
                    t.add(tic);
                }
                return t;
            }

        }
    }
}
