package entity;

import app.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HallSeatGetter {

    private static final HallSeatGetter INSTANCE = new HallSeatGetter();

    public static HallSeatGetter getInstance() {return INSTANCE;}

    private HallSeatGetter(){}

    public HallSeat getFreeSeat() throws SQLException{
        try (PreparedStatement s = DBContext.getConnection().prepareStatement("SELECT * FROM hall_seats WHERE hall_seat_sold = false")) {

            try (ResultSet r = s.executeQuery()){
                if(r.next()){
                    HallSeat h = new HallSeat();
                    h.setId(r.getInt("hall_seat_id"));
                    h.setCinemaHallId(r.getInt("cinema_hall_id"));
                    h.setHallSeatNumber(r.getInt("hall_seat_number"));
                    h.setSold(r.getBoolean("hall_seat_sold"));
                    h.setRow(r.getInt("hall_seat_row"));

                    return h;
                }
            }
            return null;
        }
    }


}
