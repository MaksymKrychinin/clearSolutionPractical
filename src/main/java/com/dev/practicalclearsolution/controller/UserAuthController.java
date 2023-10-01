package com.dev.practicalclearsolution.controller;

import com.dev.practicalclearsolution.entity.User;
import com.dev.practicalclearsolution.service.UserService;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserAuthController {
    private final UserService userServiceImpl;

    //create
    @PostMapping
    public ResponseEntity<User> saveUser(@Validated User user) {
        return userServiceImpl.save(user);
    }

    //update one or some
    //update all
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Validated User user) {
        return userServiceImpl.update(id, user);
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userServiceImpl.delete(id);

    }

    //Search for users by birthdate range
    @GetMapping("/date1={date1}&date2={date2}")
    public ResponseEntity<List<User>> findUserByDate(
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @Past(message = "Date should be at past time")
            @Validated LocalDate date1,
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @Past(message = "Date should be at past time")
            @Validated LocalDate date2) {
        return userServiceImpl.findAllUsersByBirthdateInRange(date1, date2);
    }
}
