package com.dima.entity;

import com.dima.enumPack.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;

    private String lastname;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String password;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<DeliveryAddress> deliveryAddresses = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<PizzaToOrder> pizzaToOrders = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstname, user.firstname)
               && Objects.equals(lastname, user.lastname)
               && Objects.equals(phoneNumber, user.phoneNumber)
               && Objects.equals(email, user.email)
               && role == user.role
               && Objects.equals(birthDate, user.birthDate)
               && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, phoneNumber, email, role, birthDate, password);
    }

    @Override
    public String toString() {
        return "User{" +
               "firstname='" + firstname + '\'' +
               ", lastname='" + lastname + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", email='" + email + '\'' +
               ", role=" + role +
               ", birthDate=" + birthDate +
               ", password='" + password + '\'' +
               '}';
    }
}
