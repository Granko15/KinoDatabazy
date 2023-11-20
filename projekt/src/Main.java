import UI.Menu;
import app.DBContext;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;



public class Main {

    public static void main(String[] args) throws SQLException, IOException {

        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        dataSource.setServerNames(new String[]{"db.dai.fmph.uniba.sk"});
        dataSource.setPortNumbers(new int[]{5432});
        dataSource.setDatabaseName("playground");
        dataSource.setUser("granec12@uniba.sk");
//        dataSource.setPassword("Granko151");
//        dataSource.setServerNames(new String[]{"localhost"});
//        dataSource.setPortNumbers(new int[]{5432});
//        dataSource.setDatabaseName("kinodb");
//        dataSource.setUser("postgres");
//        dataSource.setPassword("granko");

        try (Connection connection = dataSource.getConnection()) {
            DBContext.setConnection(connection);

            Menu mainMenu = new Menu();
            mainMenu.run();

        } finally {
            DBContext.clear();
        }
    }
}
