package UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;

import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.ArrayList;

import entity.Customer;
import entity.Booking;
import UI.Mode.Mode;
import entity.Flight;
import entity.BookingDetail;
import manage.BookingBusiness;
import manage.CustomerBusiness;
import manage.FlightTicketBusiness;

public class FormCustomer extends Application {
    private Stage formCustomerStage, stageBill;
    private Mode mode;
    private ArrayList<Flight> flights;
    private int passengers;
    private Booking booking;
    private Customer customer;
    private ArrayList<BookingDetail> bookingDetails;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = new ArrayList<>(flights);
    }

    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("Thông tin khách hàng");
        title.setStyle("-fx-font-size: 30px");

        BorderPane titlePane = new BorderPane();
        titlePane.setPadding(new Insets(50, 10, 10, 10));
        titlePane.setCenter(title);
        BorderPane.setAlignment(titlePane, Pos.CENTER);

        VBox form = new VBox(20);
        form.setPadding(new Insets(20, 40, 20, 40));
        form.setAlignment(Pos.CENTER);

        double labelWidth = 100;

        HBox nameRow = createFormRow("Tên đầy đủ", new TextField(""), labelWidth);
        ((TextField) nameRow.getChildren().get(1)).setPromptText("như trên CCCD/hộ chiếu");

        HBox dobRow = createFormRow("Ngày sinh", new DatePicker(), labelWidth);
        ((DatePicker) dobRow.getChildren().get(1)).setPromptText("ngày / tháng / năm");

        HBox phoneRow = createFormRow("Số điện thoại", new TextField(""), labelWidth);
        ((TextField) phoneRow.getChildren().get(1)).setPromptText("đủ 10 số");

        HBox mailRow = createFormRow("Email", new TextField(""), labelWidth);

        form.getChildren().addAll(nameRow, dobRow, phoneRow, mailRow);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(50, 40, 20, 40));

        Button accept = new Button("Xác nhận");
        initActionAccept(accept, stage, form, mode);
        Button close = new Button("Quay lại");
        initActionClose(close, stage, mode);

        buttonBox.getChildren().addAll(close, accept);
        form.getChildren().add(buttonBox);

        root.setTop(titlePane);
        root.setCenter(form);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight() - 20);
        scene.getStylesheets().add(getClass().getResource("/effect/style.css").toExternalForm());
        stage.setTitle("Nhập thông tin khách hàng");
        stage.setScene(scene);
        stage.setResizable(false);
        formCustomerStage = stage;
        stage.show();
    }

    public Alert initAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(message);
        return alert;
    }

    public void initActionAccept(Button accept, Stage stage, VBox form, Mode mode) {
        accept.setFocusTraversable(false);
        accept.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                HBox box = (HBox) form.getChildren().get(0);
                String name = ((TextField) box.getChildren().get(1)).getText();

                box = (HBox) form.getChildren().get(1);
                LocalDate localDate = ((DatePicker) box.getChildren().get(1)).getValue();
                String dob = "";
                if (localDate != null) {
                    dob = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }

                box = (HBox) form.getChildren().get(2);
                String phone = ((TextField) box.getChildren().get(1)).getText();

                box = (HBox) form.getChildren().get(3);
                String mail = ((TextField) box.getChildren().get(1)).getText();

                String customer_id = CustomerBusiness.showCustomerId(mail);
                customer = new Customer(customer_id, name, dob, phone, mail);
                if (CustomerBusiness.isValid(customer) == false) {
                    Alert alert = initAlert("Vui lòng nhập đủ thông tin khách hàng");
                    alert.showAndWait();
                    return;
                }

                if (customer_id.equals("")) {
                    Random rand = new Random();
                    customer_id = "C" + String.format("%04d", rand.nextInt(10000));
                    while (CustomerBusiness.isExist(customer_id)) {
                        customer_id = "C" + String.format("%04d", rand.nextInt(10000));
                    }
                    customer.setCustomerId(customer_id);
                }

                if (mode == Mode.SHOW) {
                    if (CustomerBusiness.isExist(customer) == false) {
                        Alert alert = initAlert("Thông tin khách hàng không tồn tại");
                        alert.showAndWait();
                        return;
                    }

                    Bill bill = new Bill();
                    bill.setCustomerId(customer_id);

                    stageBill = new Stage();
                    stageBill.initOwner(formCustomerStage);
                    stageBill.initModality(Modality.WINDOW_MODAL);
                    bill.start(stageBill);

                }

                else {
                    if (CustomerBusiness.isExist(customer_id) == false) {
                        CustomerBusiness.addCustomer(customer);
                    }

                    Random rand = new Random();
                    String booking_id = "B" + String.format("%04d", rand.nextInt(10000));
                    while (BookingBusiness.isExist(booking_id)) {
                        booking_id = "B" + String.format("%04d", rand.nextInt(10000));
                    }
                    BigDecimal total = new BigDecimal("0");

                    bookingDetails = new ArrayList<>();
                    for (Flight flight : flights) {
                        BookingDetail bookingDetail = new BookingDetail(flight.getFlightId(), booking_id, customer_id,
                                flight.getFlightNumber(), passengers);
                        bookingDetails.add(bookingDetail);
                        BigDecimal price = new BigDecimal(FlightTicketBusiness.getPrice(flight.getFlightId()));
                        BigDecimal qty = new BigDecimal(passengers);
                        total = total.add(price.multiply(qty));
                    }

                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formatted = now.format(fmt);
                    booking = new Booking(booking_id, customer_id, formatted, total);

                    ChooseSeat chooseSeat = new ChooseSeat(booking, customer, bookingDetails);
                    chooseSeat.setMode(mode);
                    chooseSeat.setFormCustomerStage(formCustomerStage);

                    Stage chooseSeatStage = new Stage();
                    chooseSeatStage.initOwner(formCustomerStage);
                    chooseSeatStage.initModality(Modality.APPLICATION_MODAL);
                    chooseSeat.start(chooseSeatStage);

                }

            }
        });
    }

    public void initActionClose(Button close, Stage stage, Mode mode) {
        close.setFocusTraversable(false);
        close.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                if (mode == Mode.SHOW) {
                    stage.close();
                } else {
                    stage.hide();
                }
            }
        });
    }

    public HBox createFormRow(String labelText, Control input, double labelWidth) {
        Label label = new Label(labelText);
        label.setMinWidth(labelWidth);
        label.setAlignment(Pos.BASELINE_LEFT);

        if (input instanceof TextField) {
            ((TextField) input).setFocusTraversable(false);
            ((TextField) input).setPrefSize(500, 40);
        }

        else if (input instanceof DatePicker) {
            ((DatePicker) input).setFocusTraversable(false);
            ((DatePicker) input).setPrefSize(500, 40);
        }

        HBox row = new HBox(20);
        row.setAlignment(Pos.BASELINE_CENTER);
        row.getChildren().addAll(label, input);

        return row;
    }

    public static void main(String[] args) {
        launch(args);
    }
}