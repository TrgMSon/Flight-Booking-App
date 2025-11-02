package entity;
import java.math.BigDecimal;

public class Booking {
    private String booking_id, customer_id, booking_date;
    private BigDecimal total_amount;
    private int day, month, year;
    private int hour, minute;

    public Booking() {

    }

    public Booking(String booking_id, String customer_id, String booking_date, BigDecimal total_amount) {
        this.booking_id = booking_id;
        this.customer_id = customer_id;
        this.booking_date = booking_date;
        this.total_amount = total_amount;

        String date = booking_date.split(" ")[0];
        day = Integer.parseInt(date.split("-")[2]);
        month = Integer.parseInt(date.split("-")[1]);
        year = Integer.parseInt(date.split("-")[0]);

        String time = booking_date.split(" ")[1];
        hour = Integer.parseInt(time.split(":")[0]);
        minute = Integer.parseInt(time.split(":")[1]);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
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
