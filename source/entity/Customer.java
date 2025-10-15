package entity;
public class Customer {
    private String customer_id, name, phone, email, dob;

    public Customer() {

    }

    public Customer(String customer_id, String name, String dob, String phone, String email) {
        this.customer_id = customer_id;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}