package UI.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import entity.FlightSearch;


public class RecentSearchHistoryController {

    @FXML private TableView<FlightSearch> historyTable;
    @FXML private TableColumn<FlightSearch, String> departureCityColumn;
    @FXML private TableColumn<FlightSearch, String> destinationCityColumn;
    @FXML private TableColumn<FlightSearch, String> departureDateColumn;
    @FXML private TableColumn<FlightSearch, String> returnDateColumn;

    public void centerAlignColumns() {
        departureCityColumn.setStyle("-fx-alignment: CENTER;");
        destinationCityColumn.setStyle("-fx-alignment: CENTER;");
        departureDateColumn.setStyle("-fx-alignment: CENTER;");
        returnDateColumn.setStyle("-fx-alignment: CENTER;");
    }

    @FXML
    public void initialize() {
        centerAlignColumns();
        departureCityColumn.setCellValueFactory(new PropertyValueFactory<>("departureCity"));
        destinationCityColumn.setCellValueFactory(new PropertyValueFactory<>("destinationCity"));
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }

    public void setHistoryTable(ObservableList<FlightSearch> recentSearches) {
        historyTable.setItems(recentSearches);
    }
}
