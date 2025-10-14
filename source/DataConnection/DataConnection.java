
package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnection {
    private static final String url = "";
    private static final String user = "";
    private static final String password = "";

    public static Connection setConnect() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch(SQLException e) {
            System.out.println("Error connect");
        }

        return conn;
    }
}
