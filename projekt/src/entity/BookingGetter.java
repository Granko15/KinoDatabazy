package entity;

import app.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingGetter {

    private static final BookingGetter INSTANCE = new BookingGetter();

    public static BookingGetter getInstance() {
        return INSTANCE;
    }

    private BookingGetter() {
    }

    public Booking getById(int id) throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM bookings JOIN tickets t on bookings.booking_id = t.booking_id JOIN movies m on t.movie_id = m.movie_id JOIN movie_showings ms on m.movie_id = ms.movie_id WHERE bookings.booking_id = ?")){
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()){
                if (r.next()) {
                    Booking b = new Booking();
                    b.setId(r.getInt("booking_id"));
                    b.setCustomerId(r.getInt("customer_id"));
                    b.setCouponId(r.getInt("coupon_id"));
                    b.setBookingPrice(r.getInt("booking_price"));
                    b.setMovieTitle(r.getString("movie_name"));
                    b.setReserved(r.getBoolean("booking_reserved"));
                    b.setSold(r.getBoolean("booking_sold"));
                    b.setBookingTime(r.getString("movie_showing_time_from"));
                    b.setBookingDate(r.getString("movie_showing_date"));
                    b.setCreatedDate(r.getString("booking_created_date"));
                    b.setCreatedTime(r.getString("booking_created_time"));
                    return b;
                }
                return null;
            }
        }

    }

    public List<Booking> getByCustomerId(int id) throws SQLException{
        List<Booking> bbookings = new ArrayList<>();
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM bookings WHERE customer_id = ?")){
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()){
                while (r.next()) {
                    Booking b = BookingGetter.getInstance().getById(r.getInt("booking_id"));
                    bbookings.add(b);
                }
                return bbookings;
            }
        }
    }

    public List<Booking> getUnconfirmedBookings() throws SQLException{
        List<Booking> bookings = new ArrayList<>();
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM bookings WHERE booking_created_time < cast((now() - '10 minutes'::interval) as time) AND booking_sold != true")){
            try (ResultSet r = s. executeQuery()){
                while (r.next()){
                    Booking b = new Booking();
                    b.setId(r.getInt("booking_id"));
                    b.setCustomerId(r.getInt("customer_id"));
                    b.setCouponId(r.getInt("coupon_id"));
                    b.setBookingPrice(r.getInt("booking_price"));
                    b.setReserved(r.getBoolean("booking_reserved"));
                    b.setSold(r.getBoolean("booking_sold"));
                    b.setCreatedDate(r.getString("booking_created_date"));
                    b.setCreatedTime(r.getString("booking_created_time"));
                    bookings.add(b);
                }
                return bookings;
            }
        }
    }

}
