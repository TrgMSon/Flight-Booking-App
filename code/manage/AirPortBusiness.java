package manage;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Connection.DataConnection;
import entity.Airport;

public class AirPortBusiness {
    public static Airport showInfor(String airport_id) {
        Airport airport = new Airport();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM airport WHERE airport_id = '" + airport_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                String name = rs.getString("name");
                String city = rs.getString("city");
                String country = rs.getString("country");
                airport = new Airport(airport_id, name, city, country);
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

        return airport;
    }

    public static String getCity(String airport_id) {
        String name = "";

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM airport WHERE airport_id = '" + airport_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                name = rs.getString("city");
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