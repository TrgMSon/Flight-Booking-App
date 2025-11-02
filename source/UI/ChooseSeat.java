package UI;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;

import java.util.ArrayList;

import manage.BookingDetailBusiness;
import manage.FlightBusiness;
import manage.FlightTicketBusiness;
import entity.Flight;
import entity.Booking;
import entity.Customer;
import entity.BookingDetail;
import UI.Mode.Mode;


public class ChooseSeat extends Application {
    private Mode mode;
    private Stage chooseSeatStage, chooseSeatStage2, confirmFlightStage, formCustomerStage;
    private int passengers;
    private int count;
    private Flight flight;
    private int[][] used;
    private CheckBox[][] chairs;
    private Booking booking;
    private Customer customer;
    private ArrayList<BookingDetail> bookingDetails;
    private BookingDetail bookingDetail;
    private Label seatChoosen;
    private GridPane gridPane;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setFormCustomerStage(Stage formCustomerStage) {
        this.formCustomerStage = formCustomerStage;
    }

    public ChooseSeat(Booking booking, Customer customer, ArrayList<BookingDetail> bookingDetails) {
        this.booking = booking;
        this.customer = customer;
        this.bookingDetails = bookingDetails;
        bookingDetail = bookingDetails.get(0);
        flight = FlightBusiness.showFlight(bookingDetails.get(0).getFlightId());
        passengers = bookingDetails.get(0).getPassengers();
    }

    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        VBox plane = new VBox(20);
        plane.setAlignment(Pos.CENTER);

        Label postion = new Label("Mặt trước của máy bay");
        chairs = new CheckBox[6][21];
        used = new int[6][21];
        seatChoosen = new Label();
        loadData();

        HBox rowSeat = createRowSeat(seatChoosen);
        HBox buttonBox = createbuttonBox(stage);

        plane.getChildren().addAll(postion, gridPane, rowSeat, buttonBox);

        root.setCenter(plane);

        Scene scene = new Scene(root, 500, 700);
        scene.getStylesheets().add(getClass().getResource("/effect/style.css").toExternalForm());
        stage.setTitle("Đặt chỗ");
        stage.setScene(scene);
        stage.setResizable(false);
        chooseSeatStage = stage;
        stage.show();
    }

    public void loadData() {
        count = 0;
        chairs = new CheckBox[6][21];
        used = new int[6][21];
        seatChoosen.setText(count + "/" + passengers + " ghế đã chọn");
        gridPane = createGrid(seatChoosen);
        preChoosenSeat();
    }

    public HBox createbuttonBox(Stage stage) {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER);

        Button back = new Button("Quay lại");
        back.setFocusTraversable(false);
        initActionBack(back, stage);

        Button acpt = new Button("Xác nhận");
        acpt.setFocusTraversable(false);
        initActionAcpt(acpt, stage, booking.getBookingId(), flight.getFlightId());

        box.getChildren().addAll(back, acpt);
        return box;
    }

    public Alert initAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(message);
        return alert;
    }

    public void initActionBack(Button back, Stage stage) {
        back.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                if (mode == Mode.ROUNDTRIP2) {
                    mode = Mode.ROUNDTRIP;
                    bookingDetail = bookingDetails.get(0);
                    flight = FlightBusiness.showFlight(bookingDetails.get(0).getFlightId());
                    stage.hide();
                    start(chooseSeatStage);
                }
                else {
                    stage.close();
                }
            }
        });
    }

    public void initActionAcpt(Button acpt, Stage stage, String booking_id, String flight_id) {
        acpt.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                if (FlightTicketBusiness.decreaseSeatAvailable(flight.getFlightId(), passengers, false) == false) {
                    Alert alert = initAlert("Số lượng ghế trống không đủ");
                    alert.showAndWait();
                    return;
                }

                if (count < passengers) {
                    Alert alert = initAlert("Vui lòng chọn đủ số ghế");
                    alert.showAndWait();
                    return;
                }

                bookingDetail.setPosition(getPos());

                if (mode == Mode.ROUNDTRIP) {
                    bookingDetail = bookingDetails.get(1);
                    flight = FlightBusiness.showFlight(bookingDetails.get(1).getFlightId());

                    mode = Mode.ROUNDTRIP2;
                    chooseSeatStage.hide();
                    chooseSeatStage2 = new Stage();
                    chooseSeatStage2.initOwner(formCustomerStage);
                    chooseSeatStage2.initModality(Modality.APPLICATION_MODAL);
                    start(chooseSeatStage2);
                }

                else {
                    ConfirmFlight confirmFlight = new ConfirmFlight(customer, booking, bookingDetails);
                    confirmFlightStage = new Stage();
                    confirmFlightStage.initOwner(formCustomerStage);
                    confirmFlightStage.initModality(Modality.APPLICATION_MODAL);
                    confirmFlight.start(confirmFlightStage);
                }

            }
        });
    }

    public String getPos() {
        String pos = "";
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 20; j++) {
                if (used[i][j] == 1) {
                    char row = (char) ('A' + j - 1);
                    int column = i;
                    String tmp = "" + row + column;
                    pos = pos + tmp + " ";
                }
            }
        }
        return pos.trim();
    }

    public HBox createRowSeat(Label seatChoosen) {
        Button deleteBt = new Button("Xóa lựa chọn");
        deleteBt.getStyleClass().add("delete-button");
        deleteBt.setFocusTraversable(false);
        initActionDel(deleteBt, seatChoosen);

        HBox rowSeat = new HBox(100);
        rowSeat.setAlignment(Pos.CENTER);
        rowSeat.getChildren().addAll(seatChoosen, deleteBt);
        return rowSeat;
    }

    public void initActionDel(Button deleteBt, Label seatChoosen) {
        deleteBt.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                for (int i = 1; i <= 5; i++) {
                    for (int j = 1; j <= 20; j++) {
                        if (used[i][j] == 1) {
                            chairs[i][j].setSelected(false);
                            used[i][j] = 0;
                            if (count > 0) {
                                count -= 1;
                            }
                        }
                    }
                }
                seatChoosen.setText(count + "/" + passengers + " ghế đã chọn");
            }
        });
    }

    public void preChoosenSeat() {
        String positions = BookingDetailBusiness.showChoosenChair(flight.getFlightId());
        if (positions.equals(""))
            return;
        String[] position = positions.split("\\s+");
        for (String pos : position) {
            int column = pos.charAt(0) - 'A' + 1;
            int row = pos.charAt(1) - '0';
            chairs[row][column].setSelected(true);
            chairs[row][column].setMouseTransparent(true);
            used[row][column] = 2;
        }
    }

    public void initCheckBox(CheckBox chair, Label seatChoosen, int row, int column) {
        chair.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                if (chair.isSelected()) {
                    if (count < passengers) {
                        count += 1;
                        used[row][column] = 1;
                    } else {
                        chair.setSelected(false);
                    }
                } else {
                    if (count > 0) {
                        count -= 1;
                        used[row][column] = 0;
                    }
                }
                seatChoosen.setText(count + "/" + passengers + " ghế đã chọn");
            }
        });
    }

    public GridPane createGrid(Label seatChoosen) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        for (int i = 0; i <= 5; i++) {
            if (i == 0) {
                for (int h = 1; h <= 20; h++) {
                    String indexRow = "" + (char) ('A' + h - 1);
                    gridPane.add(new Label(indexRow), i, h);
                }

                for (int h = 1; h <= 5; h++) {
                    String indexColumn = " " + h;
                    gridPane.add(new Label(indexColumn), h, 0);
                }
            }

            else {
                for (int j = 1; j <= 20; j++) {
                    CheckBox chair = new CheckBox();
                    chair.setFocusTraversable(false);
                    initCheckBox(chair, seatChoosen, i, j);
                    chairs[i][j] = chair;
                    gridPane.add(chair, i, j);
                }
            }
        }
        return gridPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}