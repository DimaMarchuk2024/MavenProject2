package com.dima.entity;

import com.dima.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"deliveryAddresses", "pizzaToOrders"})
@Builder
@Entity
@Table(name = "users")
public class User {

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
}
