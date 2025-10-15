package manage;

import java.sql.Statement;
// import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Connection.DataConnection;

public class BookingDetailBusiness {
    public static ArrayList<BookingDetail> showListBookingDetail(String booking_id) {
        ArrayList<BookingDetail> list = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM bookingdetail WHERE booking_id = '" + booking_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                String flight_id = rs.getString("flight_id");
                String customer_id = rs.getString("customer_id");
                String flight_number = rs.getString("flight_number");
                int passengers = rs.getInt("passengers");
                BookingDetail obj = new BookingDetail(flight_id, booking_id, customer_id, flight_number, passengers);
                list.add(obj);
            }
            
        } catch(SQLException e) {
            Logger.getLogger(BookingDetailBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return list;
    }

}
