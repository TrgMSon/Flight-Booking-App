import java.math.BigDecimal;

public class Booking {
    private String booking_id, customer_id, booking_date;
    private BigDecimal total_amount;

    public Booking() {

    }

    public Booking(String booking_id, String customer_id, String booking_date, BigDecimal total_amount) {
        this.booking_id = booking_id;
        this.customer_id = customer_id;
        this.booking_date = booking_date;
        this.total_amount = total_amount;
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

    public String getBookingDate() {
        return booking_date;
    }

    public void setBookingDate(String booking_date) {
        this.booking_date = booking_date;
    }

    public BigDecimal getTotalAmount() {
        return total_amount;
    }

    public void setTotalAmount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }
}
