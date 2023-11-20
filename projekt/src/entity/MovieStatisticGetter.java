package entity;

import app.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MovieStatisticGetter {

    private static final MovieStatisticGetter INSTANCE = new MovieStatisticGetter();

    public static MovieStatisticGetter getInstance() {
        return INSTANCE;
    }

    private MovieStatisticGetter() {
    }

    public void findAll() throws SQLException {
        try (PreparedStatement s = DBContext.getConnection().prepareStatement(
                "SELECT categories.category_name, movie_name, date_trunc('month', movie_showing_date) as datum ,SUM(t.ticket_price) as sucet\n" +
                        "FROM categories\n" +
                        "    JOIN movie_categories mc on categories.category_id = mc.category_id\n" +
                        "    JOIN movies m on m.movie_id = mc.movie_id\n" +
                        "    JOIN tickets t on m.movie_id = t.movie_id\n" +
                        "    JOIN movie_showings ms on m.movie_id = ms.movie_id WHERE movie_showing_date > (current_date - '6 month'::interval)\n" +
                        "    GROUP BY categories.category_name, movie_name, datum ORDER BY SUM(t.ticket_price) DESC")
        ){
            try (ResultSet r = s.executeQuery()) {
                int counter = 0;
                String catName = "";
                String mesiac = "";
                String film = "";
                while (r.next()) {
                    if (catName.equals(r.getString("category_name"))){
                        counter++;
                        if(counter > 3) continue;
                    }
                    catName = r.getString("category_name");
                    film = r.getString("movie_name");
                    mesiac = r.getString("datum");
                    System.out.println("Month: "+ mesiac);
                    System.out.println("\tGenre: "+ catName);
                    System.out.println("\tFilm: "+ film + '\n');
                }
            }
        }
    }

    public static void getLastMonth() throws SQLException{
        Map<String,Integer> prvy = new HashMap<>();
        Map<String,Integer> druhy = new HashMap<>();
        try (PreparedStatement s = DBContext.getConnection().prepareStatement(
                "SELECT category_name, sum(t.ticket_price) as sucet, date_trunc('month', movie_showing_date) as datum FROM categories\n" +
                        "        JOIN movie_categories mc on categories.category_id = mc.category_id\n" +
                        "        JOIN movies m on m.movie_id = mc.movie_id\n" +
                        "        JOIN tickets t on m.movie_id = t.movie_id\n" +
                        "        JOIN movie_showings ms on m.movie_id = ms.movie_id WHERE movie_showing_date >= (current_date - '1 month'::interval)\n" +
                        "        GROUP BY category_name, datum ORDER BY SUM(t.ticket_price) DESC")
        ){
            try (ResultSet r = s.executeQuery()){
                while (r.next()){
                    if (!prvy.containsKey(r.getString("category_name"))){
                        prvy.put(r.getString("category_name"), r.getInt("sucet"));
                    }
                    prvy.put(r.getString("category_name"), prvy.get(r.getString("category_name")) + r.getInt("sucet"));
                }
            }
        }

        try (PreparedStatement s = DBContext.getConnection().prepareStatement(
                "SELECT category_name, sum(t.ticket_price) as sucet, date_trunc('month', movie_showing_date) as datum FROM categories\n" +
                        "      JOIN movie_categories mc on categories.category_id = mc.category_id\n" +
                        "      JOIN movies m on m.movie_id = mc.movie_id\n" +
                        "      JOIN tickets t on m.movie_id = t.movie_id\n" +
                        "      JOIN movie_showings ms on m.movie_id = ms.movie_id\n" +
                        "        WHERE movie_showing_date >= (current_date - '2 month'::interval)\n" +
                        "          AND movie_showing_date < (current_date - '1 month'::interval)\n" +
                        "            GROUP BY category_name, datum ORDER BY SUM(t.ticket_price) DESC;")
        ){
            try (ResultSet r = s.executeQuery()){
                while (r.next()){
                    if (!druhy.containsKey(r.getString("category_name"))){
                        druhy.put(r.getString("category_name"), r.getInt("sucet"));
                    }
                    druhy.put(r.getString("category_name"), druhy.get(r.getString("category_name")) + r.getInt("sucet"));
                }
            }
        }

        List<Integer> diff = new ArrayList<>();

        System.out.println("This month grossing:");
        for (String k : prvy.keySet()){
            diff.add(prvy.get(k));
            System.out.println(k + ": "+prvy.get(k));
        }
        int counter = 0;
        for (String k : druhy.keySet()){
            diff.set(counter,diff.get(counter) - druhy.get(k));
            counter++;
        }

        System.out.println("Difference from last month:");
        for (Integer i : diff){
            System.out.println("diff: "+i);
        }


    }

}
