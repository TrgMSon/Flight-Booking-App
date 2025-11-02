package entity;

import java.math.BigDecimal;
import manage.BookingBusiness;

public class FlightInfo {
        private String departure, destination, departureTime, arrivalTime, duration, flight_id;
        private BigDecimal price;
        private String formattedPrice;
        private int seats;

        public FlightInfo(String flight_id, String departure, String destination, String departureTime,
                String arrivalTime, String duration, BigDecimal price, int seats) {
            this.flight_id = flight_id;
            this.departure = departure;
            this.destination = destination;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.duration = duration;
            this.price = price;
            this.seats = seats;
            formattedPrice = BookingBusiness.normalizeTotal(String.valueOf(price));
        }

        public String getFormattedPrice() {
            return formattedPrice;
        }

        public String getFlightId() {
            return flight_id;
        }

        public String getDeparture() {
            return departure;
        }

        public String getDestination() {
            return destination;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public String getDuration() {
            return duration;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public int getSeats() {
            return seats;
        }
    }