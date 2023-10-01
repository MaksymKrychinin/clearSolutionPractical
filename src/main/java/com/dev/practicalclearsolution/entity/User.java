package com.dev.practicalclearsolution.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    @Email(message = "Email should be valid", regexp = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$")
    @NotNull(message = "Email should be not null")
    private String email;
    @Column(name = "name", nullable = false)
    @NotNull(message = "Name should be not null")
    private String name;
    @Column(name = "surname", nullable = false)
    @NotNull(message = "Surname should be not null")
    private String surname;
    @Past(message = "Date should be at past time")
    @NotNull(message = "Date should be not null")
    private LocalDate date;
    @Column(name = "address")
    private String address;
    @Column(name = "phoneNumber")
    private String phoneNumber;


}
