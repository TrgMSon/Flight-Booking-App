package UI;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import entity.*;
import manage.*;

public class ConfirmFlight extends Application {

    @Override
    public void start(Stage primaryStage) {
        String customerId = "CUST001";

        
        Customer customer = CustomerBusiness.showInfor(customerId);

        
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER); // Center everything
        root.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");

        
        Label header = new Label("Đặt chỗ đã được xác nhận");
        header.setFont(new Font("Arial", 24));
        header.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        root.getChildren().add(header);

        
        Label customerLabel = new Label("Thông tin khách hàng: " + customer.getName());
        customerLabel.setStyle("-fx-font-size: 16px;");
        root.getChildren().add(customerLabel);

        
        ArrayList<Booking> bookings = BookingBusiness.showListBooking(customerId);
        for (Booking booking : bookings) {
            Label bookingLabel = new Label("Mã đặt vé: " + booking.getBookingId());
            bookingLabel.setStyle("-fx-font-size: 14px;");
            root.getChildren().add(bookingLabel);

            
            ArrayList<BookingDetail> details = BookingDetailBusiness.showListBookingDetail(booking.getBookingId());
            for (BookingDetail detail : details) {
                Flight flight = FlightBusiness.showFlight(detail.getFlightId());
                Label flightLabel = new Label("Chuyến bay: " + flight.getFlightId() + " | " + flight.getDepartureTime() + " → " + flight.getArrivalTime());
                flightLabel.setStyle("-fx-font-size: 14px;");
                root.getChildren().add(flightLabel);
            }

            
            Label total = new Label("Tổng số tiền: " + BookingBusiness.normalizeTotal(String.valueOf(booking.getTotalAmount())) + " VND");
            total.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            root.getChildren().add(total);
        }

       
        Label bookingCode = new Label("Mã đặt chỗ của Quý khách là 6KJQZZ");
        bookingCode.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        root.getChildren().add(bookingCode);

        
        Button sendTicketButton = new Button("Gửi phiếu thu vé điện tử (E-Ticket)");
        sendTicketButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px;");
        root.getChildren().add(sendTicketButton);

       
        Button sendEmailButton = new Button("Gửi email đặt chỗ của Quý khách");
        sendEmailButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px;");
        root.getChildren().add(sendEmailButton);

       
        Label totalPriceLabel = new Label("Tổng số tiền: 3.847.000 VND");
        totalPriceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: green;");
        root.getChildren().add(totalPriceLabel);

       
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Confirm Flight");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
