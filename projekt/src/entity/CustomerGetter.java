package entity;

import app.DBContext;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerGetter {

    private static final CustomerGetter INSTANCE = new CustomerGetter();

    public static CustomerGetter getInstance() {
        return INSTANCE;
    }

    private CustomerGetter() {
    }

    public List<Customer> findAll() throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM customers")) {
            try (ResultSet r = s.executeQuery()) {

                List<Customer> elements = new ArrayList<>();

                while (r.next()) {
                    Customer c = new Customer();

                    c.setId(r.getInt("customer_id"));
                    c.setfName(r.getString("customer_first_name"));
                    c.setlName(r.getString("customer_last_name"));
                    c.setMail(r.getString("customer_mail"));
                    c.setAge(r.getInt("customer_age"));
                    c.setDateOfBirth(r.getString("customer_date_of_birth"));

                    elements.add(c);
                }

                return elements;
            }
        }
    }

    public Customer getById(int id) throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM customers WHERE customer_id = ?")){
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()){
                if (r.next()) {
                    Customer c = new Customer();
                    c.setId(r.getInt("customer_id"));
                    c.setfName(r.getString("customer_first_name"));
                    c.setlName(r.getString("customer_last_name"));
                    c.setMail(r.getString("customer_mail"));
                    c.setAge(r.getInt("customer_age"));
                    c.setDateOfBirth(r.getString("customer_date_of_birth"));

                    if (r.next()) {
                        throw new RuntimeException("More than one row was returned");
                    }

                    return c;
                }
                return null;
            }
        }

    }
}
