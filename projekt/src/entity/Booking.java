package entity;
import app.DBContext;
import com.sun.tools.javac.Main;

import java.sql.*;

public class Booking {

    private Integer id;
    private Integer customerId;
    private Integer couponId;
    private String bookingTime;
    private String bookingDate;
    private String movieTitle;
    private Integer bookingPrice;
    private String createdDate;
    private String createdTime;
    private Boolean reserved;
    private Boolean sold;

    @Override
    public String toString() {
        return  "id: " + getId() +
                "\ncustomerId: " + getCustomerId() +
                "\ncouponId: " + getCouponId() +
                "\nFilm: " + getMovieTitle() +
                "\nPrice: " + getBookingPrice() +
                "\nDate: " + getCreatedDate()+
                "\nTime: " + getCreatedTime();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getBookingDate() {return bookingDate;}

    public void setBookingDate(String bookingDate) {this.bookingDate = bookingDate; }

    public String getBookingTime() { return bookingTime;}

    public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }

    public String getMovieTitle() { return movieTitle;    }

    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public Integer getBookingPrice() { return bookingPrice; }

    public void setBookingPrice(Integer bookingPrice) {this.bookingPrice = bookingPrice; }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public void apllyDiscount(Coupon coupon, Customer customer) throws SQLException{
        if(coupon == null || customer == null) throw new IllegalStateException("customer or coupon is null");

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE bookings SET booking_price = ? WHERE customer_id=?")){
            s.setInt(1,this.getBookingPrice() - coupon.getCouponValue());
            s.setInt(2,customer.getId());
            s.executeUpdate();
        }
    }

    public void createReservation() throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO bookings (customer_id, booking_price, booking_created_date, booking_created_time, booking_reserved) VALUES (?,?,cast(? as date),cast(? as time), true)", Statement.RETURN_GENERATED_KEYS) ){
            s.setInt(1, customerId);
            s.setInt(2, bookingPrice);
            long millis=System.currentTimeMillis();
            Date date = new java.sql.Date(millis);
            s.setDate(3, date);
            Time time = new java.sql.Time(millis);
            s.setTime(4, time);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()){
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void delete() throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM bookings WHERE booking_id = ? ", Statement.RETURN_GENERATED_KEYS) ){
            s.setInt(1, id);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()){
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void confirmBooking() throws SQLException{

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE bookings SET booking_sold = true WHERE booking_id=?")){
            s.setInt(1,id);
            s.executeUpdate();
        }
    }
}
