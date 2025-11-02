package UI.controllers;

import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import manage.BookingBusiness;
import manage.FlightBusiness;
import entity.FlightInfo;
import entity.FlightSearch;
import entity.Flight;
import UI.FormCustomer;
import UI.Mode.Mode;
import Connection.DataConnection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class ChooseFlightController {

    @FXML private TableView<FlightInfo> flightTable;
    @FXML private TableColumn<FlightInfo, String> colDeparture;
    @FXML private TableColumn<FlightInfo, String> colDestination;
    @FXML private TableColumn<FlightInfo, String> colDepartureTime;
    @FXML private TableColumn<FlightInfo, String> colArrivalTime;
    @FXML private TableColumn<FlightInfo, String> colDuration;
    @FXML private TableColumn<FlightInfo, String> colPrice;
    @FXML private TableColumn<FlightInfo, String> colSeats;
    @FXML private TableColumn<FlightInfo, Void> colAction;
    @FXML private Button btnBack;
    private Mode mode;
    private Stage chooseFlightStage, chooseFlightStage2;
    private Scene chooseFLightScene;
    private ArrayList<Flight> flights;
    private ObservableList<FlightInfo> flightData = FXCollections.observableArrayList();
    private FlightSearch flightSearch;

    public void setCFStage(Stage chooseFlightStage) {
        this.chooseFlightStage = chooseFlightStage;
    }

    public void setCFScene(Scene scene) {
        chooseFLightScene = scene;
        btnBack.getStylesheets().add(getClass().getResource("/effect/style.css").toExternalForm());
        btnBack.getStyleClass().add("btnBack");
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setFlightSearch(FlightSearch flightSearch) {
        String dpt = FlightBusiness.getAirportId(flightSearch.getDepartureCity());
        flightSearch.setDepartureCity(dpt);
        String arv = FlightBusiness.getAirportId(flightSearch.getDestinationCity());
        flightSearch.setDestinationCity(arv);
        this.flightSearch = flightSearch;
    }

    @FXML
    public void initData() {
        flights = new ArrayList<>();

        colDeparture.setCellValueFactory(new PropertyValueFactory<>("departure"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
        colDepartureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        colArrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
        colSeats.setCellValueFactory(new PropertyValueFactory<>("seats"));

        addButtonToTable();
        loadFlightData();

        if (flightData.isEmpty()) {
            Label message = new Label("Không có chuyến bay nào phù hợp");
            message.setStyle("-fx-font-size: 16px");
            flightTable.setPlaceholder(message);
        }

        if (mode == Mode.ROUNDTRIP) {
            mode = Mode.ROUNDTRIP2;
            loadFlightData();
            mode = Mode.ROUNDTRIP;

            if (flightData.isEmpty()) {
                Label message = new Label("Không có chuyến bay nào phù hợp");
                message.setStyle("-fx-font-size: 16px");
                flightTable.setPlaceholder(message);
            }
            else {
                loadFlightData();
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return DataConnection.setConnect();
    }

    public void loadFlightData() {
        flightData.clear();

        String query = """
                           SELECT
                                   dep.city AS departure_city,
                                   arr.city AS arrival_city,
                                   f.flight_id,
                                   f.departure_time,
                                   f.arrival_time,
                                   t.price,
                                   t.seats_available
                               FROM flight f
                               JOIN airport dep ON f.departure_airport_id = dep.airport_id
                               JOIN airport arr ON f.arrival_airport_id = arr.airport_id
                               JOIN flightticket t ON f.flight_id = t.flight_id
                WHERE dep.airport_id = ?
                AND arr.airport_id = ?
                AND t.seats_available >= ?
                AND f.departure_time LIKE ?
                               AND IF(CAST(f.departure_time AS DATE) = CAST(NOW() AS DATE), CAST(f.departure_time AS TIME) >= CAST(NOW() AS TIME), TRUE)
                               ORDER BY f.departure_time;
                           """;

        if (mode == Mode.ROUNDTRIP2) {
            query = """
                        SELECT
                            dep.city AS departure_city,
                            arr.city AS arrival_city,
                            f.flight_id,
                            f.departure_time,
                            f.arrival_time,
                            t.price,
                            t.seats_available
                        FROM flight f
                        JOIN airport dep ON f.departure_airport_id = dep.airport_id
                        JOIN airport arr ON f.arrival_airport_id = arr.airport_id
                        JOIN flightticket t ON f.flight_id = t.flight_id
                        WHERE dep.airport_id = ?
                        AND arr.airport_id = ?
                        AND t.seats_available >= ?
                        AND f.departure_time LIKE ?
                        AND CAST(f.departure_time AS TIME) > CAST(? AS TIME)
                        ORDER BY f.departure_time
                    """;
        }

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, flightSearch.getDepartureCity());
            stmt.setString(2, flightSearch.getDestinationCity());
            stmt.setInt(3, flightSearch.getPassenger());
            stmt.setString(4, "%" + flightSearch.getDepartureDate() + "%");
            if (mode == Mode.ROUNDTRIP2) {
                stmt.setString(1, flightSearch.getDestinationCity());
                stmt.setString(2, flightSearch.getDepartureCity());
                stmt.setString(4, "%" + flightSearch.getReturnDate() + "%");
                stmt.setString(5, flightSearch.getReturnDate());
            }
            ResultSet rs = stmt.executeQuery();

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                String flight_id = rs.getString("flight_id");
                String departure = rs.getString("departure_city");
                String destination = rs.getString("arrival_city");
                String depTime = rs.getString("departure_time");
                String arrTime = rs.getString("arrival_time");
                BigDecimal price = rs.getBigDecimal("price");
                int seats = rs.getInt("seats_available");

                LocalDateTime dep = LocalDateTime.parse(depTime, fmt);
                LocalDateTime arr = LocalDateTime.parse(arrTime, fmt);
                long minutes = Duration.between(dep, arr).toMinutes();
                String duration = (minutes / 60) + " giờ " + (minutes % 60) + " phút";

                FlightInfo tmp = new FlightInfo(flight_id, departure, destination, depTime, arrTime, duration, price,
                        seats);
                flightData.add(tmp);
            }

            flightTable.setItems(flightData);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Lỗi tải dữ liệu: " + e.getMessage()).showAndWait();
        }
    }

    private void addButtonToTable() {
        Callback<TableColumn<FlightInfo, Void>, TableCell<FlightInfo, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<FlightInfo, Void> call(final TableColumn<FlightInfo, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Chọn");

                    {
                        btn.getStylesheets().add(getClass().getResource("/effect/style.css").toExternalForm());
                        btn.setStyle("-fx-font-size: 14px; -fx-pref-height: 20px; -fx-pref-width: 80px");
                        btn.setOnAction(event -> {
                            FlightInfo data = getTableView().getItems().get(getIndex());

                            if (mode == Mode.ONEWAY) {
                                if (flights.size() == 1) {
                                    flights.set(0, FlightBusiness.showFlight(data.getFlightId()));
                                } else {
                                    flights.add(FlightBusiness.showFlight(data.getFlightId()));
                                }
                            } else {
                                if (flights.size() == 2) {
                                    if (mode == Mode.ROUNDTRIP) {
                                        flights.set(0, FlightBusiness.showFlight(data.getFlightId()));
                                    } else {
                                        flights.set(1, FlightBusiness.showFlight(data.getFlightId()));
                                    }
                                } else {
                                    flights.add(FlightBusiness.showFlight(data.getFlightId()));
                                }
                            }

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.initOwner(chooseFlightStage2);
                            alert.initModality(Modality.APPLICATION_MODAL);
                            alert.setTitle("Chọn chuyến bay");
                            alert.setHeaderText(null);
                            alert.setContentText("Bạn có muốn chọn chuyến bay:\n" +
                                            data.getDeparture() + " → " + data.getDestination() + "\n" +
                                            "Giờ đi: " + data.getDepartureTime() + "\n" +
                                            "Giờ đến: " + data.getArrivalTime() + "\n" +
                                            "Giá vé: " + BookingBusiness.normalizeTotal(String.valueOf(data.getPrice()))
                                            + " VND\n" +
                                            "Ghế trống: " + data.getSeats());

                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.getStylesheets().add(getClass().getResource("/effect/style.css").toExternalForm());

                            ButtonType acptBt = new ButtonType("Xác nhận");
                            ButtonType cancelBt = new ButtonType("Hủy");
                            alert.getButtonTypes().clear();
                            alert.getButtonTypes().addAll(cancelBt, acptBt);

                            alert.getDialogPane().lookupButton(acptBt).setFocusTraversable(false);
                            alert.getDialogPane().lookupButton(acptBt).setStyle("-fx-pref-width: 120px; -fx-pref-height: 36px");
                            alert.getDialogPane().lookupButton(cancelBt).setFocusTraversable(false);
                            alert.getDialogPane().lookupButton(cancelBt).setStyle("-fx-pref-width: 120px; -fx-pref-height: 36px");

                            Optional<ButtonType> option = alert.showAndWait();
                            if (option.get() == acptBt) {
                                if (mode == Mode.ROUNDTRIP) {
                                    chooseFlightStage.hide();
                                    mode = Mode.ROUNDTRIP2;
                                    chooseFlightStage2 = new Stage();
                                    chooseFlightStage2 = chooseFlightStage;
                                    chooseFlightStage2.setScene(chooseFLightScene);
                                    loadFlightData();
                                    chooseFlightStage2.show();
                                }

                                else {
                                    FormCustomer formCustomer = new FormCustomer();
                                    if (mode == Mode.ROUNDTRIP2) {
                                        formCustomer.setMode(Mode.ROUNDTRIP);
                                    } else {
                                        formCustomer.setMode(Mode.ONEWAY);
                                    }
                                    formCustomer.setFlights(flights);
                                    formCustomer.setPassengers(flightSearch.getPassenger());

                                    Stage formCustomerStage = new Stage();
                                    formCustomerStage.initOwner(chooseFlightStage);
                                    formCustomerStage.initModality(Modality.APPLICATION_MODAL);
                                    formCustomer.start(formCustomerStage);

                                }
                            }

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                };
            }
        };

        colAction.setCellFactory(cellFactory);
    }

    @FXML
    private void handleBack() {
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (mode == Mode.ROUNDTRIP2) {
                    chooseFlightStage2.hide();
                    mode = Mode.ROUNDTRIP;
                    loadFlightData();
                    chooseFlightStage.show();
                } else {
                    chooseFlightStage.close();
                }
            }
        });
    }
}
