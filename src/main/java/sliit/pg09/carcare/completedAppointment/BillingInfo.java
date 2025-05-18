package sliit.pg09.carcare.completedAppointment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class BillingInfo {
    private double labour;
    private double parts;
    private double additional;
    private double discount;

    @JsonIgnore
    public double getTotal() {
        return labour + parts + additional - discount;
    }
}