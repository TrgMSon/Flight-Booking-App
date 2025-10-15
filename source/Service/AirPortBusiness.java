package manage;

import java.sql.Statement;
// import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Connection.DataConnection;

public class AirPortBusiness {
    public static String getName(String airport_id) {
        String name = "";

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT name FROM airport WHERE airport_id = '" + airport_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                name = rs.getString("name");
            }
        } catch(SQLException e) {
            Logger.getLogger(AirPortBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return name;
    }

}
