package manage;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Connection.DataConnection;

public class CustomerBusiness {
    public static Customer showInfor(String customer_id) {
        Customer customer = new Customer();

        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM customer WHERE customer_id = '" + customer_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next()) {
                String name = rs.getString("name");
                String dob = rs.getString("dob");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                customer = new Customer(customer_id, name, dob, phone, email);
            }
        } catch(SQLException e) {
            Logger.getLogger(CustomerBusiness.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException e) {
                System.out.println("Cannot close connection");
            }
        }

        return customer;
    }

    public static boolean addCustomer(Customer customer) {
        Connection conn = null;
        try {
            conn = DataConnection.setConnect();
            String sql = "INSERT INTO customer(customer_id, name, phone, email, dob) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement psm = conn.prepareStatement(sql);
            psm.setString(1, customer.getCustomerId());
            psm.setString(2, customer.getName());
            psm.setString(3, customer.getPhone());
            psm.setString(4, customer.getEmail());
            psm.setString(5, customer.getDob());

            return psm.executeUpdate() > 0;
        } catch(SQLException e) {
            Logger.getLogger(CustomerBusiness.class.getName()).log(Level.SEVERE, null, e);
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

    public static boolean isExist(String customer_id) {
        Connection conn = null;

        try {
            conn = DataConnection.setConnect();
            String sql = "SELECT * FROM customer WHERE customer_id = '" + customer_id + "'";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if (rs.next())
                return true;
        } catch (SQLException e) {
            Logger.getLogger(CustomerBusiness.class.getName()).log(Level.SEVERE, null, e);
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
