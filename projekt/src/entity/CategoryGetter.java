package entity;

import app.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryGetter {
    private static final CategoryGetter INSTANCE = new CategoryGetter();

    public static CategoryGetter getInstance() {
        return INSTANCE;
    }

    private CategoryGetter(){}

    public Category getById(int id) throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM categories WHERE category_id = ?")) {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Category c = new Category();
                    c.setId(id);
                    c.setName(r.getString("category_name"));

                    if (r.next()) {
                        throw new RuntimeException("More than one row was returned");
                    }

                    return c;
                }
            }
            return null;
        }
    }

    public List<Category> getCategories() throws SQLException{
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM categories")){
            try (ResultSet r = s.executeQuery()){
                while (r.next()){
                    Category c = new Category();
                    c.setId(r.getInt("category_id"));
                    c.setName(r.getString("category_name"));
                    categories.add(c);
                }
                return categories;
            }
        }
    }
}
