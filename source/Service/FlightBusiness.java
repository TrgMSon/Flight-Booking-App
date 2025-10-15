package manage;

import java.sql.Statement;
// import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Connection.DataConnection;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FlightBusiness {
    public static Flight showFlight(String flight_id) {
        Flight flight = new Flight();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM flight WHERE flight_id = '" + flight_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                String flight_number = rs.getString("flight_number");
                String dpt_ap_id = rs.getString("departure_airport_id");
                String arv_ap_id = rs.getString("arrival_airport_id");
                String dpt_time = rs.getString("departure_time");
                String arv_time = rs.getString("arrival_time");
                flight = new Flight(flight_id, flight_number, dpt_ap_id, arv_ap_id, dpt_time, arv_time);
            }
            
        } catch(SQLException e) {
            Logger.getLogger(FlightBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return flight;
    }

    public static String getTime(String time) {
        String ans = time.split("\\s+")[1].substring(0, 5);
        return ans;
    }

    public static String getDate(String date) {
        String[] tmp = date.split("\\s+")[0].split("-");
        String ans = "ngày " + tmp[2] + " tháng " + tmp[1] + " năm " + tmp[0];
        return ans; 
    }

    public static String flightDuration(Flight flight) {
        String[] tmp = flight.getDepartureTime().split("\\s+")[0].split("-");
        LocalDate dpt_date = LocalDate.of(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
        tmp = flight.getArrivalTime().split("\\s+")[0].split("-");
        LocalDate arv_date = LocalDate.of(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Integer.parseInt(tmp[2]));
        long duration_date = ChronoUnit.DAYS.between(dpt_date, arv_date);
        
        String dpt_time = flight.getDepartureTime().split("\\s+")[1].substring(0, 5);
        String arv_time = flight.getArrivalTime().split("\\s+")[1].substring(0, 5);
        long time = duration_date * 24 * 60 + 60 * (Long.parseLong(arv_time.substring(0, 2)) - Long.parseLong(dpt_time.substring(0, 2))) +
                        (Long.parseLong(arv_time.substring(3)) - Long.parseLong(dpt_time.substring(3)));
        
        return time/60 + " giờ " + time%60 + " phút ";
    }

}
