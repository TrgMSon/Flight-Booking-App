import java.sql.Statement;
// import java.sql.PreparedStatement;
import java.sql.Connection;
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
        } catch(SQLException e) {
            Logger.getLogger(FlightTicketBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return price;
    }
}