package entity;

import app.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CouponGetter {

    private static final CouponGetter INSTANCE = new CouponGetter();

    public static CouponGetter getInstance() {
        return INSTANCE;
    }

    private CouponGetter(){}

    public List<Coupon> getCustomerCoupons(int id) throws SQLException{
        List<Coupon> coupons = new ArrayList<>();
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM customers JOIN coupons c on customers.customer_id = c.customer_id WHERE c.customer_id = ?")){
            s.setInt(1,id);
            try (ResultSet r = s.executeQuery()){
                while (r.next()) {
                    Coupon c = new Coupon();
                    c.setId(r.getInt("coupon_id"));
                    c.setCustomerId(id);
                    c.setCouponText(r.getString("coupon_text"));
                    c.setValidUntil(r.getString("coupon_valid_until"));
                    c.setCouponValue(r.getInt("coupon_price"));
                    c.setCouponUsed(r.getBoolean("coupon_used"));
                    coupons.add(c);
                }
                return coupons;
            }
        }
    }

    public Coupon getCouponByTextAndCustomer(int customerId, String text) throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM coupons WHERE coupon_text = ? AND customer_id = ? AND coupon_used = false")){
            s.setString(1,text);
            s.setInt(2,customerId);
            try (ResultSet r = s.executeQuery()){
                if(r.next()) {
                    Coupon c = new Coupon();
                    c.setId(r.getInt("coupon_id"));
                    c.setCustomerId(customerId);
                    c.setCouponText(r.getString("coupon_text"));
                    c.setValidUntil(r.getString("coupon_valid_until"));
                    c.setCouponValue(r.getInt("coupon_price"));
                    c.setCouponUsed(r.getBoolean("coupon_used"));
                    return c;
                }
                return null;
            }
        }
    }
}
