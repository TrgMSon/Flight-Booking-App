package manage;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
            Logger.getLogger(BookingBusiness.class.getName()).log(Level.SEVERE, null, e);
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
            if (cnt % 3 == 0 && i != 0) total =  '.' + total; 
            cnt += 1;
        }
        return total;
    }

    public static boolean isExist(String booking_id) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM booking WHERE booking_id = '" + booking_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) return true;
        } catch(SQLException e) {
            Logger.getLogger(BookingBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }

    public static boolean add(Booking booking) {
        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "INSERT INTO booking(booking_id, customer_id, booking_date, total_amount) VALUES(?, ?, ?, ?)";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, booking.getBookingId());
            psm.setString(2, booking.getCustomerId());
            psm.setString(3, booking.getBookingDate());
            psm.setBigDecimal(4, booking.getTotalAmount());

            return psm.executeUpdate() > 0;
        } catch(SQLException e) {
            Logger.getLogger(BookingBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }

    public static Booking showInfor(String booking_id) {
        Booking booking = new Booking();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM booking WHERE booking_id = '" + booking_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                booking.setBookingId(booking_id);
                booking.setCustomerId(rs.getString("customer_id"));
                booking.setBookingDate(rs.getString("booking_date"));
                booking.setTotalAmount(rs.getBigDecimal("total_amount"));
            }
        } catch(SQLException e) {
            Logger.getLogger(BookingBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return booking;
    }

    public static boolean delete(String booking_id) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "DELETE FROM booking WHERE booking_id = '" + booking_id + "'";
            Statement stm = conn.createStatement();

            return stm.executeUpdate(sql) > 0;
        } catch(SQLException e) {
            Logger.getLogger(BookingBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return false;
    }
}