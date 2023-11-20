package entity;
import app.DBContext;
import com.sun.tools.javac.Main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HallSeat {

    private Integer id;
    private Integer cinemaHallId;
    private Integer hallSeatNumber;
    private Boolean sold;
    private Integer row;

    @Override
    public String toString() {
        return  "id: " + getId() +
                "\nhall: " + getCinemaHallId() +
                "\nnumber: " + getHallSeatNumber() +
                "\nrow: " + getRow() +
                "\nsold: " + getSold();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCinemaHallId() {
        return cinemaHallId;
    }

    public void setCinemaHallId(Integer cinemaHallId) {
        this.cinemaHallId = cinemaHallId;
    }

    public Integer getHallSeatNumber() {
        return hallSeatNumber;
    }

    public void setHallSeatNumber(Integer hallSeatNumber) {
        this.hallSeatNumber = hallSeatNumber;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

}
