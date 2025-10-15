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
import entity.Booking;

import java.math.BigDecimal;

public class BookingBusiness {
    public static ArrayList<Booking> showListBooking(String customer_id) {
        ArrayList<Booking> list = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM booking WHERE customer_id = '" + customer_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                String booking_date = rs.getString("booking_date");
                String booking_id = rs.getString("booking_id");
                BigDecimal total_amount = rs.getBigDecimal("total_amount");
                Booking obj = new Booking(booking_id, customer_id, booking_date, total_amount);
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

    public static String normalizeTotal(String total_amount) {
        String total = "";
        int cnt = 1;
        for (int i=total_amount.length() - 1; i>=0; i--) {
            total = total_amount.charAt(i) + total;
            if (cnt % 3 == 0 && i != 0) total =  ',' + total; 
            cnt += 1;
        }
        return total;
    }
}