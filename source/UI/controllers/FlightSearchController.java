package UI.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import entity.FlightSearch;
import Connection.DataConnection;
import UI.ChooseFlight;
import UI.FormCustomer;
import UI.Mode.Mode;

public class FlightSearchController {
    public Button searchButton;
    public Button recentSearchButton;
    public Button bookingHistoryButton;
    public AnchorPane flightSearchWindow;
    private int count = 1;
    @FXML private ComboBox<String> departurePicker;
    @FXML private ComboBox<String> destinationPicker;
    @FXML private DatePicker departureDate;
    @FXML private DatePicker returnDate;
    @FXML private Label passengers;
    static private ObservableList<FlightSearch> recentSearches = FXCollections.observableArrayList();

    @FXML
    private void initializeForLocation(){

        ObservableList<String> locations = FXCollections.observableArrayList();
        String query = "SELECT city, airport_id, country FROM airport";

        try (Connection conn = DataConnection.setConnect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String city = rs.getString("city");
                String airportId = rs.getString("airport_id");
                String country = rs.getString("country");
                String location = city + " (" + airportId + "), " + country;
                locations.add(location);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        departurePicker.setItems(locations);
        destinationPicker.setItems(locations);

        if (locations.size() >= 2) {
            departurePicker.setValue(locations.get(0));
            destinationPicker.setValue(locations.get(1));
        }
    }

    @FXML
    public void initialize() {
        initializeForLocation();
        departureDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                }
            }
        });

        returnDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (departureDate.getValue() != null && date.isBefore(departureDate.getValue())) {
                    setDisable(true);
                }
            }
        });

        // --- Departure Date Picker ---
        departureDate.setValue(LocalDate.now());
        departureDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;");
                }
            }
        });

        // --- Return Date Picker ---
        returnDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (departureDate.getValue() != null && date.isBefore(departureDate.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3;");
                }
            }
        });
    }

    @FXML
    private void handleSearch() {
        String departure = departurePicker.getValue();
        String destination = destinationPicker.getValue();
        String depDate = (departureDate.getValue() != null) ? departureDate.getValue().toString() : "N/A";
        String retDate = (returnDate.getValue() != null) ? returnDate.getValue().toString() : "N/A";

        if (depDate.equals("N/A")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn ngày khởi hành.");
            alert.showAndWait();
            return;
        }

        if (retDate.equals("N/A")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn ngày về.");
            alert.showAndWait();
            return;
        }

        if (depDate.compareTo(retDate) > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Ngày về phải sau ngày khởi hành.");
            alert.showAndWait();
            return;
        }


        if (departure.equals(destination)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Thành phố khởi hành và điểm đến không thể giống nhau.");
            alert.showAndWait();
            return;
        }

        int passenger = Integer.parseInt(passengers.getText());

        FlightSearch flightSearch = new FlightSearch(departure, destination, depDate, retDate, passenger);
        if (!recentSearches.contains(flightSearch)) {
            recentSearches.add(flightSearch);
        }

        ChooseFlight chooseFlight = new ChooseFlight();
        chooseFlight.setFlightSearch(flightSearch);
        Stage stage = new Stage();
        chooseFlight.start(stage);

        System.out.println("Searching flights from " + departure + " to " + destination +
                " departing on " + depDate + " and returning on " + retDate + " passengers " + passenger);

    }

    public void handleRecentSearch() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UI/views/recent-search-history-view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Lịch sử tìm kiếm gần đây");
            stage.setScene(new javafx.scene.Scene(fxmlLoader.load()));
            RecentSearchHistoryController controller = fxmlLoader.getController();
            controller.setHistoryTable(recentSearches);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleBookingHistory() {
        FormCustomer formCustomer = new FormCustomer();
        Stage stage = new Stage();
        formCustomer.setMode(Mode.SHOW);
        formCustomer.start(stage);
    }

    @FXML
    private void handlePlus() {
        if (count < 9) {
            count++;
        }
        passengers.setText(String.valueOf(count));
    }

    @FXML
    private void handleMinus() {
        if (count > 1) {
            count--;
        }
        passengers.setText(String.valueOf(count));
    }
}
