package sliit.pg09.carcare.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Admin {
    @Id
    private String email;
    private String password;
    private String name;

    public Admin(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}