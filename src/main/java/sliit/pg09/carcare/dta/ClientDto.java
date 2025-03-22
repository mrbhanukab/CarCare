package sliit.pg09.carcare.dta;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ClientDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String nic;
    private LocalDate created;
    private LocalDate updated;
}
