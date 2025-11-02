package manage;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Connection.DataConnection;

public class FlightTicketBusiness {
    public static String getPrice(String flight_id) {
        String price = "";

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT price FROM flightticket WHERE flight_id = '" + flight_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                price = String.valueOf(rs.getBigDecimal("price"));
            }
        } catch (SQLException e) {
            Logger.getLogger(FlightTicketBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return price;
    }

    public static boolean decreaseSeatAvailable(String flight_id, int passengers, boolean allowed) {
        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql1 = "SELECT seats_available FROM flightticket WHERE flight_id = ?";
            PreparedStatement psm1 = conn.prepareStatement(sql1);
            psm1.setString(1, flight_id);
            ResultSet rs = psm1.executeQuery();
            if (rs.next()) {
                int seats_available = rs.getInt("seats_available");
                if (seats_available < passengers)
                    return false;
            }

            if (allowed == true) {
                String sql = "UPDATE flightticket SET seats_available = seats_available - ? WHERE flight_id = ?";
                PreparedStatement psm = conn.prepareStatement(sql);
                psm.setInt(1, passengers);
                psm.setString(2, flight_id);

                return psm.executeUpdate() > 0;
            }
            return true;
        } catch (SQLException e) {
            Logger.getLogger(FlightTicketBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }
}