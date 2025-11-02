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
import entity.BookingDetail;

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
                String position = rs.getString("seat_position");
                BookingDetail obj = new BookingDetail(flight_id, booking_id, customer_id, flight_number, passengers, position);
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

    public static String showChoosenChair(String flight_id) {
        String p = "";

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT seat_position FROM bookingdetail WHERE flight_id = '" + flight_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                String position = rs.getString("seat_position");
                if (position == null) return p;
                p = p + " " + position + " ";
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

        return p.trim();
    }

    public static boolean add(BookingDetail bookingDetail) {
        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "INSERT INTO bookingdetail(flight_id, booking_id, customer_id, flight_number, passengers, seat_position) VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, bookingDetail.getFlightId());
            psm.setString(2, bookingDetail.getBookingId());
            psm.setString(3, bookingDetail.getCustomerId());
            psm.setString(4, bookingDetail.getFlightNumber());
            psm.setInt(5, bookingDetail.getPassengers());
            psm.setString(6, bookingDetail.getPosition());

            return psm.executeUpdate() > 0;
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

        return false;
    }

    public static boolean updatePosition(String booking_id, String flight_id, String position) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "UPDATE bookingdetail SET seat_position = ? WHERE booking_id = ? AND flight_id = ?";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, position);
            psm.setString(2, booking_id);
            psm.setString(3, flight_id);
            return psm.executeUpdate() > 0;
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

        return false;
    }

    public static boolean deleteBookingDetail(String flight_id, String booking_id) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql1 = "UPDATE booking AS b" + 
                          " JOIN bookingdetail AS bd ON b.booking_id = bd.booking_id" +
                          " JOIN flightticket AS ft ON ft.flight_id = bd.flight_id" +
                          " SET b.total_amount = b.total_amount - ft.price * bd.passengers" +
                          " WHERE b.booking_id = ? AND ft.flight_id = ?"; 
            PreparedStatement psm = conn.prepareStatement(sql1);
            psm.setString(1, booking_id);
            psm.setString(2, flight_id);

            String sql2 = "DELETE FROM bookingdetail WHERE flight_id = '" + flight_id + "'";
            Statement stm2 = conn.createStatement();

            return psm.executeUpdate() > 0 && stm2.executeUpdate(sql2) > 0;
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

        return false;
    }
}