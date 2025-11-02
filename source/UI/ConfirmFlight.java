package UI;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import entity.Booking;
import entity.Customer;
import entity.BookingDetail;
import entity.Flight;
import manage.FlightBusiness;
import manage.FlightTicketBusiness;
import manage.BookingBusiness;
import manage.BookingDetailBusiness;
import manage.AirPortBusiness;


public class ConfirmFlight extends Application {

    private Booking booking;
    private Customer customer;
    private ArrayList<BookingDetail> bookingDetails;

    public ConfirmFlight(Customer customer, Booking booking, ArrayList<BookingDetail> bookingDetails) {
        this.customer = customer;
        this.booking = booking;
        this.bookingDetails = new ArrayList<>(bookingDetails);
    }

    public ConfirmFlight() {
    }

    @Override
    public void start(Stage stage) {
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Customer is null!");
            alert.setContentText("Vui lòng truyền Customer từ FormCustomer.");
            alert.showAndWait();
            stage.close();
            return;
        }

        stage.setTitle("Xác nhận đặt vé");
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("XÁC NHẬN ĐẶT VÉ");
        title.setFont(new Font("Arial", 22));

        Label customerLabel = new Label("Thông tin khách hàng:");
        Label name = new Label("Tên: " + customer.getName());
        Label phone = new Label("Số điện thoại: " + customer.getPhone());
        Label email = new Label("Email: " + customer.getEmail());

        VBox customerBox = new VBox(5, customerLabel, name, phone, email);
        customerBox.setPadding(new Insets(10));
        customerBox.setStyle("-fx-border-color: gray; -fx-background-color: #f7f7f7; -fx-background-radius: 10; -fx-font-size: 15px");

        VBox flightBox = new VBox(10);
        flightBox.setPadding(new Insets(10));

        AtomicInteger totalPassengers = new AtomicInteger(0);
        AtomicInteger totalPrice = new AtomicInteger(0);
        Label totalLabel = new Label();

        for (BookingDetail bookingDetail : bookingDetails) {
            Flight flight = FlightBusiness.showFlight(bookingDetail.getFlightId());
            String priceStr = FlightTicketBusiness.getPrice(bookingDetail.getFlightId());
            int ticketPrice = Integer.parseInt(priceStr);
            int passengers = bookingDetail.getPassengers();
            String seat = bookingDetail.getPosition();

            totalPassengers.addAndGet(passengers);
            totalPrice.addAndGet(ticketPrice * passengers);

            VBox infoBox = new VBox(5);
            infoBox.setPadding(new Insets(10));
            infoBox.setStyle("-fx-border-color: #d3d3d3; -fx-background-color: #ffffff; -fx-background-radius: 8; -fx-font-size: 15px");

            Label fNum = new Label("Số hiệu chuyến bay: " + flight.getFlightNumber());
            Label route = new Label(AirPortBusiness.getCity(flight.getDepartureAirportId()) + " → " + AirPortBusiness.getCity(flight.getArrivalAirportId()));
            String dpt_date_time = FlightBusiness.getDate(flight.getDepartureTime()) + " lúc "
                    + FlightBusiness.getTime(flight.getDepartureTime());
            String arv_date_time = FlightBusiness.getDate(flight.getArrivalTime()) + " lúc "
                    + FlightBusiness.getTime(flight.getArrivalTime());
            Label time = new Label("Giờ khởi hành: " + dpt_date_time + " | Giờ đến: " + arv_date_time);
            Label pass = new Label("Số hành khách: " + passengers);
            Label seats = new Label("Vị trí ghế: " + seat);
            Label price = new Label("Giá vé: " + BookingBusiness.normalizeTotal(String.valueOf(ticketPrice)) + " VND");

            Button cancelFlightBtn = new Button("Hủy chuyến này");
            cancelFlightBtn.setFocusTraversable(false);
            cancelFlightBtn.getStylesheets().add(getClass().getResource("/effect/style.css").toExternalForm());
            cancelFlightBtn.setStyle("-fx-pref-width: 160px; -fx-pref-height: 36px");
            cancelFlightBtn.setOnAction(e -> {
                bookingDetails.remove(bookingDetail);
                flightBox.getChildren().remove(infoBox);
                totalPassengers.addAndGet(-passengers);
                totalPrice.addAndGet(-(ticketPrice * passengers));
                totalLabel.setText("Tổng số hành khách: " + totalPassengers.get() + " | Tổng số tiền: "
                        + BookingBusiness.normalizeTotal(String.valueOf(totalPrice.get())) + " VND");
            });

            infoBox.getChildren().addAll(fNum, route, time, pass, seats, price, cancelFlightBtn);
            flightBox.getChildren().add(infoBox);
        }

        totalLabel.setText(
                "Tổng số hành khách: " + totalPassengers.get() + " | Tổng số tiền: " + BookingBusiness.normalizeTotal(String.valueOf(totalPrice.get())) + " VND");
        totalLabel.setFont(new Font("Arial", 17));

        ScrollPane scrollPane = new ScrollPane(flightBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);

        Button confirmBtn = new Button("Xác nhận đặt vé");
        Button cancelAllBtn = new Button("Hủy tất cả");
        Button closeBtn = new Button("Quay lại");

        confirmBtn.setFocusTraversable(false);
        initActionConfirm(confirmBtn, bookingDetails, totalPrice, stage);

        cancelAllBtn.setFocusTraversable(false);
        cancelAllBtn.setOnAction(e -> {
            bookingDetails.clear();
            flightBox.getChildren().clear();
            totalPassengers.set(0);
            totalPrice.set(0);
            totalLabel.setText("Tổng hành khách: 0 | Tổng tiền: 0 VND");
        });

        closeBtn.setFocusTraversable(false);
        closeBtn.setOnAction(e -> stage.close());

        HBox btnBox = new HBox(15, confirmBtn, cancelAllBtn, closeBtn);
        btnBox.getStylesheets().add(getClass().getResource("/effect/style.css").toExternalForm());
        btnBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, customerBox, scrollPane, totalLabel, btnBox);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void initActionConfirm(Button confirmBtn, ArrayList<BookingDetail> bookingDetails, AtomicInteger totalPrice, Stage stage) {
        confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                if (totalPrice.get() != 0) {
                    alert.setHeaderText("Đặt vé thành công!");
                    alert.setContentText("Tổng tiền: " + BookingBusiness.normalizeTotal(String.valueOf(totalPrice.get())) + " VND");

                    if (bookingDetails.size() > 0) {
                        BookingBusiness.add(booking);
                        for (BookingDetail bookingDetail : bookingDetails) {
                            BookingDetailBusiness.add(bookingDetail);
                            FlightTicketBusiness.decreaseSeatAvailable(bookingDetail.getFlightId(),
                                    bookingDetail.getPassengers(), true);
                        }
                    }
                } else {
                    alert.setHeaderText("Quý khách đã hủy toàn bộ vé");
                    alert.setContentText("Vui lòng chọn chuyến bay trước khi xác nhận");
                }
                alert.showAndWait();

                ((Stage) ((Stage) stage.getOwner()).getOwner()).close();
                stage.close();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}