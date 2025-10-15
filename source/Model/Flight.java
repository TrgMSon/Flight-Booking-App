package entity;

public class Flight {
    private String flight_id, flight_number, departure_airport_id, arrival_airport_id, departure_time, arrival_time;

    public Flight() {

    }

    public Flight(String flight_id, String flight_number, String departure_airport_id, String arrival_airport_id,
            String departure_time, String arrival_time) {
                this.flight_id = flight_id;
                this.flight_number = flight_number;
                this.departure_airport_id = departure_airport_id;
                this.arrival_airport_id = arrival_airport_id;
                this.departure_time = departure_time;
                this.arrival_time = arrival_time;
    }

    public String getFlightId() {
        return flight_id;
    }

    public void setFlightId(String flight_id) {
        this.flight_id = flight_id;
    }

        public String getFlightNumber() {
        return flight_number;
    }

    public void setFlightNumber(String flight_number) {
        this.flight_number = flight_number;
    }

    public String getDepartureAirportId() {
        return departure_airport_id;
    }

    public void setDepartureAirportId(String departure_airport_id) {
        this.departure_airport_id = departure_airport_id;
    }

    public String getArrivalAirportId() {
        return arrival_airport_id;
    }

    public void setArrivalAirportId(String arrival_airport_id) {
        this.arrival_airport_id = arrival_airport_id;
    }

    public String getDepartureTime() {
        return departure_time;
    }

    public void setDepartureTime(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getArrivalTime() {
        return arrival_time;
    }

    public void setArrivalTime(String arrival_time) {
        this.arrival_time = arrival_time;
    }

} 
