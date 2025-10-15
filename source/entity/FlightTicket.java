package entity;
import java.math.BigDecimal;

public class FlightTicket {
    private String flight_id;
    private BigDecimal price;
    private int seats_available;

    public FlightTicket() {

    }

    public FlightTicket(String flight_id, BigDecimal price, int seats_available) {
        this.flight_id = flight_id;
        this.price = price;
        this.seats_available = seats_available;
    }

    public String getFlightId() {
        return flight_id;
    }

    public void setFlightId(String flight_id) {
        this.flight_id = flight_id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getSeatAvailable() {
        return seats_available;
    }

    public void setSeatAvailable(int seats_available) {
        this.seats_available = seats_available;
    }
}