package UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;

import manage.AirPortBusiness;
import manage.BookingBusiness;
import manage.BookingDetailBusiness;
import manage.CustomerBusiness;
import manage.FlightBusiness;
import manage.FlightTicketBusiness;
import entity.Airport;
import entity.Booking;
import entity.BookingDetail;
import entity.Customer;
import entity.Flight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Bill extends Application {
    private String customer_id;
    private int qty_bookings;

    public void setCustomerId(String customer_id) {
        this.customer_id = customer_id;
    }

    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20, 10, 10, 10));

        VBox customerInfor = initInforCustomer(customer_id);
        VBox bill = initBill(customer_id);
        HBox buttonBox = new HBox();

        if (qty_bookings > 0) {
            StackPane wrapper = new StackPane(bill);
            wrapper.setAlignment(Pos.CENTER);

            ScrollPane scrollPane = new ScrollPane(wrapper);
            scrollPane.setPadding(new Insets(5, 5, 20, 5));
            scrollPane.setFitToWidth(true);
            scrollPane.setPannable(true);

            
            Button close = new Button("Đóng");
            close.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    stage.close();
                    ((Stage) stage.getOwner()).close();
                }
            });
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.getChildren().add(close);

            root.setTop(customerInfor);
            root.setCenter(scrollPane);
            root.setBottom(buttonBox);
        }

        else {
            Label message = new Label("Hiện tại bạn chưa thực hiện lần đặt vé nào");
            message.setStyle("-fx-font-size: 16px");
            root.setTop(customerInfor);
            root.setCenter(message);
            root.setBottom(buttonBox);
        }

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight() - 20);
        scene.getStylesheets().add(getClass().getResource("/effect/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Lịch sử giao dịch");
        stage.show();
        
    }

    public VBox initInforCustomer(String customer_id) {
        VBox customerInfor = new VBox(20);
        customerInfor.setAlignment(Pos.CENTER_LEFT);
        Customer customer = CustomerBusiness.showInfor(customer_id);

        HBox nameRow = new HBox(20);
        Label txtName = new Label("Tên khách hàng: ");
        Label name = new Label(customer.getName());
        nameRow.getChildren().addAll(txtName, name);

        HBox emailRow = new HBox(20);
        Label txtEmail = new Label("Email: ");
        Label email = new Label(customer.getEmail());
        emailRow.getChildren().addAll(txtEmail, email);

        customerInfor.getChildren().addAll(nameRow, emailRow);
        return customerInfor;
    }

    public VBox initBill(String customer_id) {
        VBox bill = new VBox(20);
        bill.setAlignment(Pos.CENTER);

        ArrayList<Booking> bookings = BookingBusiness.showListBooking(customer_id);
        Collections.sort(bookings, new Comparator<Booking>() {
            public int compare(Booking a, Booking b) {
                if (a.getYear() == b.getYear()) {
                    if (a.getMonth() == b.getMonth()) {
                        if (a.getDay() == b.getDay()) {
                            if (a.getHour() == b.getHour()) {
                                return Integer.compare(a.getMinute(), b.getMinute());
                            }
                            return Integer.compare(a.getHour(), b.getHour());
                        }
                        return Integer.compare(a.getDay(), b.getDay());
                    }
                    return Integer.compare(a.getMonth(), b.getMonth());
                }
                return Integer.compare(a.getYear(), b.getYear());
            }
        });

        qty_bookings = bookings.size();
        for (Booking booking : bookings) {
            VBox bookingRow = initBookingRow(booking);
            bookingRow.getStyleClass().add("vbox-bookingRow");
            bookingRow.setAlignment(Pos.CENTER);

            String total_amount = String.valueOf(booking.getTotalAmount());
            ArrayList<BookingDetail> bookingDetails = BookingDetailBusiness
                    .showListBookingDetail(booking.getBookingId());
            for (int i=bookingDetails.size()-1; i>=0; i--) {
                HBox addressInfor = addingAddress(FlightBusiness.showFlight(bookingDetails.get(i).getFlightId()));
                addressInfor.getStyleClass().add("hbox-address");
                HBox flightRow = createRow(FlightBusiness.showFlight(bookingDetails.get(i).getFlightId()),
                        bookingDetails.get(i).getPassengers());
                bookingRow.getChildren().addAll(addressInfor, flightRow);
            }


            HBox totalBox = new HBox(20);
            totalBox.setAlignment(Pos.CENTER);
            Label totalLabel = new Label();
            totalLabel.setMinWidth(500);
            Label total = new Label("Tổng tiền: " + BookingBusiness.normalizeTotal(total_amount) + " VND");
            total.getStyleClass().add("label-total");
            totalBox.getChildren().addAll(totalLabel, total);

            bookingRow.getChildren().add(totalBox);
            bill.getChildren().add(bookingRow);
        }

        return bill;
    }

    public VBox initBookingRow(Booking booking) {
        VBox bookingRow = new VBox(20);
        Label flightDate = new Label(FlightBusiness.getDate(booking.getBookingDate()));
        Label flightTime = new Label(FlightBusiness.formatTime(FlightBusiness.getTime(booking.getBookingDate())));
        bookingRow.getChildren().addAll(flightDate, flightTime);
        return bookingRow;
    }

    public HBox addingAddress(Flight flight) {
        HBox addressBox = new HBox(20);
        addressBox.setAlignment(Pos.CENTER_LEFT);

        Label joiner = new Label();
        joiner.setMinWidth(280);

        Airport airport = AirPortBusiness.showInfor(flight.getDepartureAirportId());
        Label dpt_address = new Label(airport.getCity());

        airport = AirPortBusiness.showInfor(flight.getArrivalAirportId());
        Label arv_address = new Label(airport.getCity());

        Label tmp = new Label("đến");
        String tmp1 = FlightBusiness.getDate(flight.getDepartureTime());
        Label dpt_date = new Label("(Khởi hành vào " + tmp1.substring(0, 1).toLowerCase() + tmp1.substring(1) + ")");
        addressBox.getChildren().addAll(joiner, dpt_address, tmp, arv_address, dpt_date);
        return addressBox;
    }

    public HBox createRow(Flight flight, int passengers) {
        HBox flightRow = new HBox(40);
        flightRow.setPadding(new Insets(20, 40, 20, 40));
        flightRow.setAlignment(Pos.TOP_CENTER);

        VBox dpt_infor = new VBox(20);
        dpt_infor.setAlignment(Pos.CENTER);
        Label dpt_time = new Label(FlightBusiness.getTime(flight.getDepartureTime()));
        Label dpt_ap = new Label(flight.getDepartureAirportId());
        dpt_infor.getChildren().addAll(dpt_time, dpt_ap);

        VBox joinerBox = new VBox(20);
        joinerBox.setAlignment(Pos.CENTER);
        Label joiner = new Label("-------------------->");
        joinerBox.getChildren().add(joiner);

        VBox arv_infor = new VBox(20);
        arv_infor.setAlignment(Pos.CENTER);
        Label arv_time = new Label(FlightBusiness.getTime(flight.getArrivalTime()));
        Label arv_ap = new Label(flight.getArrivalAirportId());
        arv_infor.getChildren().addAll(arv_time, arv_ap);

        VBox flightDurationBox = new VBox(20);
        Label flightDurationLabel = new Label("Thời gian bay " + FlightBusiness.flightDuration(flight));
        flightDurationLabel.setMinWidth(300);
        Label flightInfor = new Label(flight.getFlightNumber() + " được khai thác bởi CAST Airline");
        flightInfor.setMinWidth(300);
        flightDurationBox.setAlignment(Pos.CENTER);
        flightDurationBox.getChildren().addAll(flightDurationLabel, flightInfor);

        VBox costBox = new VBox(20);
        Label cost = new Label("Giá vé: " + BookingBusiness.normalizeTotal(FlightTicketBusiness.getPrice(flight.getFlightId()))
                + " VND (mỗi hành khách)");
        cost.setMinWidth(300);
        Label qty_passenger = new Label("Số lượng hành khách: " + passengers);
        qty_passenger.setMinWidth(300);
        costBox.setAlignment(Pos.CENTER);
        costBox.getChildren().addAll(cost, qty_passenger);

        flightRow.getChildren().addAll(dpt_infor, joinerBox, arv_infor, flightDurationBox, costBox);
        return flightRow;
    }

    public static void main(String[] args) {
        launch(args);
    }
}