package UI.controllers;

import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
// import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.util.ArrayList;
import javafx.stage.Stage;
import entity.FlightSearch;
import Connection.DataConnection;
import UI.FormCustomer;
import UI.Mode.Mode;

public class FlightSearchController {

    public Button searchButton;
    public Button recentSearchButton;
    @FXML private ComboBox<String> departurePicker;
    @FXML private ComboBox<String> destinationPicker;
    @FXML private DatePicker departureDate;
    @FXML private DatePicker returnDate;
    private ObservableList<FlightSearch> recentSearches = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        ObservableList<String> locations = FXCollections.observableArrayList();
        String query = "SELECT city FROM airport";

        try (Connection conn = DataConnection.setConnect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                locations.add(rs.getString("city"));
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
    private void handleSearch() {
        String departure = departurePicker.getValue();
        String destination = destinationPicker.getValue();
        String depDate = (departureDate.getValue() != null) ? departureDate.getValue().toString() : "N/A";
        String retDate = (returnDate.getValue() != null) ? returnDate.getValue().toString() : "N/A";
        FlightSearch flightSearch = new FlightSearch(departure, destination, depDate, retDate);
        recentSearches.add(flightSearch);

        System.out.println("Searching flights from " + departure + " to " + destination +
                " departing on " + depDate + " and returning on " + retDate);
    }

    public void handleRecentSearch() {
        FormCustomer formCustomer = new FormCustomer();
        Stage stage = new Stage();
        formCustomer.setMode(Mode.SHOW);
        formCustomer.start(stage);
    }
}
