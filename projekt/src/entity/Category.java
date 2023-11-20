package entity;
import app.DBContext;
import com.sun.tools.javac.Main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Category {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void insert() throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO categories (category_name) VALUES (?)", Statement.RETURN_GENERATED_KEYS) ){
            s.setString(1, name);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()){
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void insertMovCat(int filmId) throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("INSERT INTO movie_categories (category_id, movie_id) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setInt(1, id);
            s.setInt(2, filmId);
            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()){
                r.next();
                id = r.getInt(1);
            }
        }
    }

}
