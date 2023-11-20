package entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieStatistic {

    private String category;
    private List<String> films = new ArrayList<>();

    @Override
    public String toString() {
        return  "category: " + getCategory() +
                "\nfilms:\n\t" + Arrays.toString(getF().toArray());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getF() {
        return films;
    }

    public void setF(ArrayList<String> f) {
        this.films.addAll(f);
    }
}
