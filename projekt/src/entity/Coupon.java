package entity;
import app.DBContext;
import com.sun.tools.javac.Main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Coupon {

    private Integer id;
    private Integer customerId;
    private String validUntil;
    private Integer couponValue;
    private Boolean couponUsed;
    private String couponText;

    public Coupon(){}

    @Override
    public String toString() {
        return  "id: " + getId() +
                "\nCustomer id: " + getCustomerId() +
                "\nCODE: " + getCouponText() +
                "\nValid until: " + getValidUntil() +
                "\nValue: " + getCouponValue() +
                "\nUsed: " + getCouponUsed();
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

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public Integer getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(Integer couponValue) {
        this.couponValue = couponValue;
    }

    public Boolean getCouponUsed() {
        return couponUsed;
    }

    public void setCouponUsed(Boolean couponUsed) {
        this.couponUsed = couponUsed;
    }

    public String getCouponText() {
        return couponText;
    }

    public void setCouponText(String couponText) {
        this.couponText = couponText;
    }

    public void insert() throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO coupons (customer_id, coupon_text, coupon_valid_until, coupon_price) VALUES (?,?,cast(? as date),?)", Statement.RETURN_GENERATED_KEYS) ){
            s.setInt(1, customerId);
            s.setString(2,couponText);
            s.setString(3, validUntil);
            s.setInt(4, couponValue);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()){
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void updateUse() throws SQLException{
        if(id == null) {
            throw new IllegalStateException("id not set");
        }
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE coupons SET coupon_used = true WHERE coupon_id=?")){
            s.setInt(1, id);
            s.executeUpdate();
        }
    }

    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM coupons WHERE coupon_id = ?")) {
            s.setInt(1, id);
            s.executeUpdate();
        }
    }

}
