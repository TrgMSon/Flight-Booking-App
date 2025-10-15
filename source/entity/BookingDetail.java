package entity;
public class BookingDetail {
    private String flight_id, booking_id, customer_id, flight_number;
    private int passengers;

    public BookingDetail() {

    }

    public BookingDetail(String flight_id, String booking_id, String customer_id, String flight_number,
            int passengers) {
        this.flight_id = flight_id;
        this.booking_id = booking_id;
        this.customer_id = customer_id;
        this.flight_number = flight_number;
        this.passengers = passengers;
    }

    public String getFlightId() {
        return flight_id;
    }

    public void setFlightId(String flight_id) {
        this.flight_id = flight_id;
    }

    public String getBookingId() {
        return booking_id;
    }

    public void setBookingId(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getFlightNumber() {
        return flight_number;
    }

    public void setFlightNumber(String flight_number) {
        this.flight_number = flight_number;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
}