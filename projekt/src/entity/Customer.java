package entity;
import app.DBContext;
import com.sun.tools.javac.Main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer {

    private Integer id;
    private String fName;
    private String lName;
    private Integer age;
    private String dateOfBirth;
    private String mail;

    @Override
    public String toString() {
        return  "id: " + getId() +
                "\nMeno: " + getfName() + ' ' + getlName() +
                "\nVek: " + getAge() +
                "\nDátum narodenia: " + getDateOfBirth() +
                "\nE-mailová adresa: " + getMail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void insert() throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO customers (customer_first_name, customer_last_name, customer_mail, customer_age, customer_date_of_birth) VALUES (?,?,?,?,cast (? as date) )", Statement.RETURN_GENERATED_KEYS) ){
            s.setString(1, fName);
            s.setString(2, lName);
            s.setString(3, mail);
            s.setInt(4, age);
            s.setString(5, dateOfBirth);
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

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("UPDATE customers SET customer_first_name = ?, customer_last_name = ?, customer_mail = ?, customer_age = ?, customer_date_of_birth = cast(? as date) WHERE customer_id = ?")) {
            s.setString(1, fName);
            s.setString(2, lName);
            s.setString(3, mail);
            s.setInt(4, age);
            s.setString(5, dateOfBirth);

            s.setInt(6, id);

            s.executeUpdate();
        }

    }

    public void delete() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id not set");
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement("DELETE FROM customers WHERE customer_id = ?")) {
            s.setInt(1, id);
//            try (PreparedStatement a = DBContext.getConnection().prepareStatement("UPDATE bookings set customer_id = null WHERE customer_id = ?")) {
//                a.setInt(1, id);
//                a.executeUpdate();
//            }
//            try (PreparedStatement a = DBContext.getConnection().prepareStatement("UPDATE coupons set customer_id = null WHERE customer_id = ?")) {
//                a.setInt(1, id);
//                a.executeUpdate();
//            }
            s.executeUpdate();
        }
    }

}
